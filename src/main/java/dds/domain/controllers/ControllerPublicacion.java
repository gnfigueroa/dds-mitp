package dds.domain.controllers;


import dds.db.RepositorioAdopcion;
import dds.db.RepositorioMascotas;
import dds.db.RepositorioPersonas;
import dds.domain.entities.mascota.Mascota;
import dds.domain.entities.persona.Persona;
import dds.domain.entities.seguridad.usuario.Usuario;
import dds.servicios.avisos.Contacto;
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


public class ControllerPublicacion {

    public ControllerPublicacion(){
    }


    public ModelAndView mostrarPublicacionPrivada(Request request,Response response){
        Usuario usuario = request.session().attribute("usuario");
        String idPublicacion = request.params("idPublicacion");
        Map<String,Object> parametros = new HashMap<>();

        if(usuario!=null) {
            PublicacionMascota publicacionMascota = usuario.getAsociacion().getPublicador().getPrivadaXId(idPublicacion);
            Persona rescatista = RepositorioPersonas.getRepositorio().getPersona(publicacionMascota.getIdRescatista());
            Contacto crescatista = rescatista.getNotificador().getContactos().get(0);
            Mascota mascota =  RepositorioMascotas.getRepositorio().getMascota(publicacionMascota.getIdMascota());

            parametros.put("persona", usuario.getPersona());
            parametros.put("roles", usuario.getPersona().getListaRoles());
            parametros.put("publicacion",publicacionMascota);
            parametros.put("mascota",mascota);
            parametros.put("rescatista",crescatista);

            if (!usuario.getPersona().getIdPersona().equals(RepositorioPersonas.getRepositorio().getIdPersonaXidMascota(mascota.getIdMascota()))){
                response.redirect("/panel#noEsTuMascota");
            }
        }else
        {
            response.redirect("/#faltaLogin");
        }

        return new ModelAndView(parametros,"publicacionPrivada.hbs");
    }

}
