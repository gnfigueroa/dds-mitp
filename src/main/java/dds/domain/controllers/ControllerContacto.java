package dds.domain.controllers;
import dds.domain.entities.seguridad.usuario.Usuario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class ControllerContacto {
    public ControllerContacto() {
    }
    public ModelAndView mostrarContacto(Request req, Response rep){

        Usuario usuario = req.session().attribute("usuario");
        Map<String,Object> parametros = new HashMap<>();
        if(usuario!=null) {
            parametros.put("persona", usuario.getPersona());
            parametros.put("roles", usuario.getPersona().getListaRoles());
        }

        return new ModelAndView(parametros,"___contacto.hbs");
    }
}
