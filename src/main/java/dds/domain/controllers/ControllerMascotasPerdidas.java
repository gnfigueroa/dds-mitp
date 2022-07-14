package dds.domain.controllers;

import dds.db.RepositorioAdopcion;
import dds.db.RepositorioMascotas;
import dds.domain.entities.mascota.Mascota;

import dds.domain.entities.seguridad.usuario.Usuario;
import dds.servicios.publicaciones.PublicacionAdopcion;
import dds.servicios.publicaciones.PublicacionMascota;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
public class ControllerMascotasPerdidas {

    public ControllerMascotasPerdidas(){

    }

    public ModelAndView mostrarMascotasPerdidas(Request req,Response rep){
        Usuario usuario = req.session().attribute("usuario");


        Map<String,Object> parametros = new HashMap<>();
        if(usuario!=null) {
            parametros.put("persona", usuario.getPersona());
            parametros.put("roles", usuario.getPersona().getListaRoles());
        }
        List<PublicacionAdopcion> publicaciones = RepositorioAdopcion.getRepositorio().getPublicacionesAdopcion();

        List<Mascota> mascotas = RepositorioMascotas.getRepositorio().getMascotas().stream().filter(p->p.getEstaPerdida() == true).collect(Collectors.toList());

        parametros.put("mascotas",mascotas);

        return new ModelAndView(parametros,"mascotasPerdidas.hbs");
    }
    public ModelAndView mostrarPagina(Request req,Response rep){
        Usuario usuario = req.session().attribute("usuario");
        Map<String,Object> parametros = new HashMap<>();
        String pagina = req.params("page");
        int i = Integer.valueOf(pagina);
        String proxPag = String.valueOf(i+1);
        if (i>0){
            String antPag = String.valueOf(i-1);
            parametros.put("ant",antPag);}
        i = i*10;
        int cont = 0;

        if(usuario!=null) {
            parametros.put("persona", usuario.getPersona());
            parametros.put("roles", usuario.getPersona().getListaRoles());
        }
        List<PublicacionAdopcion> publicaciones = RepositorioAdopcion.getRepositorio().getPublicacionesAdopcion();
        List<Mascota> mascotaspaginado = new ArrayList<>();
        List<Mascota> mascotas = RepositorioMascotas.getRepositorio().getMascotas().stream().filter(p->p.getEstaPerdida() == true).collect(Collectors.toList());

        if(i<mascotas.size()){
            for(int v=i; v<mascotas.size();v++){
                cont ++;
                if (cont <11) {
                    mascotaspaginado.add(mascotas.get(v));}

            }}
        if(((i*10)+10)<mascotas.size()){
            parametros.put("prox",proxPag);}

        parametros.put("mascotas", mascotaspaginado);
        return new ModelAndView(parametros,"mascotasPerdidas.hbs");
    }

}
