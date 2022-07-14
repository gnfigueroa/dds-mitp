package dds.domain.controllers;

import dds.domain.entities.persona.transaccion.RechazarPublicacion;
import dds.domain.entities.persona.transaccion.ValidarPublicacion;
import dds.domain.entities.seguridad.usuario.Usuario;
import dds.servicios.publicaciones.PublicacionMascota;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControllerEvaluarPublicacion {
    public ModelAndView mostrarPublicaciones(Request req, Response res) {
        Usuario usuario = req.session().attribute("usuario");

        Map<String, Object> parametros = new HashMap<>();
        if (usuario != null) {
            if (usuario.getPersona().getListaRoles().stream().anyMatch(p -> (p.getNombre().equals("Voluntario")))) {
                parametros.put("usuario", usuario);

                List<PublicacionMascota> publicacionesPendientes = usuario.getAsociacion().getPublicador().getPublicacionesPendientes();
                parametros.put("pendientes", publicacionesPendientes);
            } else {
                res.redirect("/");
            }
        } else {
            res.redirect("/");
        }

        return new ModelAndView(parametros, "evaluarPublicaciones.hbs");
    }


    public Response aprobarRechazarPublicacion(Request request, Response response) {
        Usuario usuario = request.session().attribute("usuario");
        String idPublicacion = (request.queryParams("idPubli") != null) ? request.queryParams("idPubli") : "";
        String accion = (request.queryParams("accion") != null) ? request.queryParams("accion") : "";
        if (accion.equals("aprobar")) {
            usuario.getPersona().ejecutarTransaccion(new ValidarPublicacion(idPublicacion));
            response.redirect("/panel#aprobacionConExito");
            return response;
        }
        if (accion.equals("rechazar")) {
            usuario.getPersona().ejecutarTransaccion(new RechazarPublicacion(idPublicacion));
            response.redirect("/panel#rechazoConExito");
        }

        response.redirect("/panel#error");
        return response;
    }

}
