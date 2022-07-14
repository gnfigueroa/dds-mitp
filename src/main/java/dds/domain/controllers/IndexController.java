package dds.domain.controllers;


import dds.domain.entities.asociacion.Asociacion;
import dds.domain.entities.asociacion.Configurador;
import dds.domain.entities.seguridad.usuario.Usuario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexController {

    public IndexController() {
    }

    public ModelAndView mostrarIndex(Request req, Response rep) {
        Usuario usuario = req.session().attribute("usuario");
        Asociacion asoc;
        Configurador config;
        List<String> preguntas;
        List<String> pregMascotas;
        Map<String, Object> parametros = new HashMap<>();
        if (usuario != null) {
            if (usuario.soyAdmin()) {
                asoc = usuario.getAsociacion();
                parametros.put("Admin", 1);
                parametros.put("asociacion", asoc);
            } else {

                parametros.put("persona", usuario.getPersona());
                parametros.put("roles", usuario.getPersona().getListaRoles());

            }
        }
        return new ModelAndView(parametros, "index.hbs");
    }

}
