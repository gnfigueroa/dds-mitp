package dds.domain.controllers;
import dds.db.EntityManagerHelper;
import dds.db.RepositorioMascotas;
import dds.domain.entities.mascota.Mascota;
import dds.domain.entities.mascota.Sexo;
import dds.domain.entities.mascota.TipoMascota;
import dds.domain.entities.seguridad.usuario.Usuario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ControllerNoTengoAlma {
    public ControllerNoTengoAlma() {
    }
    public ModelAndView mostrarNoTengoAlma(Request req, Response rep){

        Usuario usuario = req.session().attribute("usuario");
        Map<String,Object> parametros = new HashMap<>();
        if(usuario!=null) {
            parametros.put("persona", usuario.getPersona());
            parametros.put("roles", usuario.getPersona().getListaRoles());
            parametros.put("mascotas",usuario.getPersona().getMascotas());
            parametros.put("claves",usuario.getAsociacion().getConfigurador().getClaves());
            List<String> enumSexo = Stream.of(Sexo.values()).map(Enum::name).collect(Collectors.toList());
            parametros.put("sexos",enumSexo);
            List<String> enumTipo = Stream.of(TipoMascota.values()).map(Enum::name).collect(Collectors.toList());
            parametros.put("tiposMascota",enumTipo);
        }
        else{
            rep.redirect("/");
        }



        return new ModelAndView(parametros,"noTengoAlma.hbs");
    }
    public Response mascotaPerdidaEncontrada(Request req,Response res){
        String idMascota = req.params("idMascota");
        Mascota mascotaEncontrada= RepositorioMascotas.getRepositorio().getMascota(idMascota);
        if (mascotaEncontrada.getEstaPerdida()){
            mascotaEncontrada.setEstaPerdida(false);
        }else{
            mascotaEncontrada.setEstaPerdida(true);
        }
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.entityManager().merge(mascotaEncontrada);
        EntityManagerHelper.commit();
        res.redirect("/");
        return res;
    }
}
