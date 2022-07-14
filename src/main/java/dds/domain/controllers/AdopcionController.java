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


public class AdopcionController {

    public AdopcionController(){
    }

//asd
    public ModelAndView mostrarMascotas(Request req,Response rep){
        Usuario usuario = req.session().attribute("usuario");


        Map<String,Object> parametros = new HashMap<>();
        if(usuario!=null) {
            parametros.put("persona", usuario.getPersona());
            parametros.put("roles", usuario.getPersona().getListaRoles());
        }
        List<PublicacionAdopcion> publicaciones = RepositorioAdopcion.getRepositorio().getPublicacionesAdopcion();

        List<Mascota> mascotas = RepositorioMascotas.getRepositorio().getMascotasPorListaId(publicaciones.stream().map(p->p.getIdMascota()).collect(Collectors.toList()));

        parametros.put("mascotas",mascotas);

        return new ModelAndView(parametros,"adopcion.hbs");
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
        List<Mascota> mascotas = RepositorioMascotas.getRepositorio().getMascotasPorListaId(publicaciones.stream().map(p->p.getIdMascota()).collect(Collectors.toList()));
        if(i<mascotas.size()){
        for(int v=i; v<mascotas.size();v++){
            cont ++;
        if (cont <11) {
            mascotaspaginado.add(mascotas.get(v));}

        }}
        if(((i*10)+10)<mascotas.size()){
        parametros.put("prox",proxPag);}

        parametros.put("mascotas", mascotaspaginado);
        return new ModelAndView(parametros,"adopcion.hbs");
    }

   /* public ModelAndView mascotaId(Request req,Response rep){
        //validar que exista el id
        Mascota mascota = this.repositorio.getMascota(new String(req.params("id")));//o lo que sea el id
        Map<String,Object> parametros = new HashMap<>();
        parametros.put("mascotas",mascota);
        return new ModelAndView(parametros,"mascota.hbs"); //TODO: crear ventana que muestre 1 sola mascota por id con datos de contacto
        // {{#if mascota}} value = "{{mascota.apodo}}" {{/if}}  esto muestras el apodo de la mascota en el .hbs
    }*/
/* ESTO VA EN MASCOTA CONTROLLER y va en un spark.post
    public Response mascotaId(Request req,Response rep){
        //validar que exista el id
        Mascota mascota = this.repositorio.getMascota(new String(req.params("id")));//o lo que sea el id
        String nuevoNombre = request.queryParams("nombre"); //meter try catch y lo que va en el queryparams tiene que coincidir con el input del html
        mascota.setNombre(nuevoNombre);
    }*/
}
