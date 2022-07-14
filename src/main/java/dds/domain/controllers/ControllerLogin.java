package dds.domain.controllers;


import dds.db.RepositorioUsuarios;
import dds.domain.entities.seguridad.usuario.Usuario;
import dds.domain.entities.seguridad.validador.ValidadorUsuario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class ControllerLogin {

    public ControllerLogin() {
    }



    public ModelAndView login (Request request, Response response) throws NoSuchAlgorithmException {

        //INPUT
        String user = (request.queryParams("usuario") != null) ? request.queryParams("usuario") : "";
        String pass = (request.queryParams("password") != null) ? request.queryParams("password") : "";
        Usuario usuario = null;

        Boolean datosOk = false;
        if (user.isEmpty()) {
            response.status(401);
            response.redirect("/");
        } else {
            try {
                datosOk = ValidadorUsuario.getValidadorUsuario().validarIdentidad (user, pass);
                usuario = RepositorioUsuarios.getRepositorio().getUsuario(user);
            } catch (Exception e) {
                datosOk = false;
            }


            if (datosOk) {
                request.session(true);
                request.session().attribute("user", user);
                request.session().attribute("usuario", usuario);
                request.session().maxInactiveInterval(3600);
                response.redirect("/panel");
            } else {
                response.status(401);
                response.redirect("/#loginError");
            }
        }
        //OUTPUT
        Map<String, Object> map = new HashMap<>();
        map.put("loginError", 1);
        map.put("persona", usuario.getPersona());
        map.put("roles", usuario.getPersona().getListaRoles());
        return new ModelAndView(map, "panel.hbs");
    }
    public ModelAndView logOut(Request request, Response response)
    {
        request.session().removeAttribute("user");
        request.session().removeAttribute("usuario");
        response.redirect("/");
        return null;
    }

}
