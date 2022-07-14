package dds.domain.controllers;


import dds.db.RepositorioAdopcion;
import dds.db.RepositorioMascotas;
import dds.domain.entities.mascota.Mascota;
import dds.domain.entities.seguridad.usuario.Usuario;
import dds.servicios.avisos.Email;
import dds.servicios.avisos.FormaNotificacion;
import dds.servicios.avisos.SMS;
import dds.servicios.avisos.WhatsApp;
import dds.servicios.publicaciones.PublicacionAdopcion;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ControllerAgregarContacto {
    public ControllerAgregarContacto() {
    }

    //String nombre, String apellido, String telefono, String email, List<FormaNotificacion> formasDeNoti
    public Response agregarContacto(Request request, Response response) throws NoSuchAlgorithmException {

        Usuario usuario = request.session().attribute("usuario");
        String nombre = (request.queryParams("nombre") != null) ? request.queryParams("nombre") : "";
        String apellido = (request.queryParams("apellido") != null) ? request.queryParams("apellido") : "";
        String telefono = (request.queryParams("telefono") != null) ? request.queryParams("telefono") : "";
        String email = (request.queryParams("email") != null) ? request.queryParams("email") : "";

        ArrayList<FormaNotificacion> formasDeNoti = new ArrayList<>();
        String formaEmail = (request.queryParams("formaEmail") != null) ? request.queryParams("formaEmail") : "";

        if (formaEmail != null) {
            formasDeNoti.add(new Email());
        }
        String formaWhatsapp = (request.queryParams("formaWhatsapp") != null) ? request.queryParams("formaWhatsapp") : "";
        if (formaWhatsapp != null) {
            formasDeNoti.add(new WhatsApp());
        }
        String formaSMS = (request.queryParams("formaSMS") != null) ? request.queryParams("formaSMS") : "";
        if (formaSMS != null) {
            formasDeNoti.add(new SMS());
        }

        usuario.getPersona().getNotificador().agendarContacto(nombre, apellido, telefono, email, formasDeNoti);

        Map<String, Object> parametros = new HashMap<>();

        response.redirect("/panel#agregarContacto");  //hay que ver como era el redirect
        return response;
    }


    public ModelAndView mostrarVerContactos(Request req, Response rep){
        Usuario usuario = req.session().attribute("usuario");


        Map<String,Object> parametros = new HashMap<>();
        if(usuario!=null) {
            parametros.put("persona", usuario.getPersona());
            parametros.put("listaContactos", usuario.getPersona().getNotificador().getContactos());
        }
        else{
            rep.redirect("/");
        }

        return new ModelAndView(parametros,"verContactos.hbs");
    }
}
