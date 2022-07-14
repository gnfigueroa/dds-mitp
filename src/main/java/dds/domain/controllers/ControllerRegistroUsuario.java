package dds.domain.controllers;

import com.twilio.rest.api.v2010.account.incomingphonenumber.Local;
import dds.db.RepositorioAsociaciones;
import dds.db.RepositorioUsuarios;
import dds.domain.entities.asociacion.Asociacion;
import dds.domain.entities.mascota.Mascota;
import dds.domain.entities.persona.Persona;
import dds.domain.entities.persona.TipoDocumento;
import dds.domain.entities.persona.roles.*;
import dds.domain.entities.seguridad.usuario.Standard;
import dds.domain.entities.seguridad.usuario.Usuario;
import dds.domain.entities.seguridad.validador.ValidadorPassword;
import dds.domain.entities.seguridad.validador.ValidadorUsuario;
import dds.servicios.avisos.*;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ControllerRegistroUsuario {
    public ControllerRegistroUsuario() {
    }

    public ModelAndView mostrarRegistroUsuario(Request req, Response rep) {

        Usuario usuario = req.session().attribute("usuario");
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("clavesAsociacion", RepositorioAsociaciones.getRepositorio().getAsociaciones());
        if (usuario != null) {
            rep.redirect("/");
        }

        List<String> enumNames = Stream.of(TipoDocumento.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        parametros.put("tipoDoc", enumNames);
        return new ModelAndView(parametros, "registroUsuario.hbs");
    }

    public ModelAndView crearUsuario(Request request, Response response) throws NoSuchAlgorithmException, ParseException {

        Map<String, Object> parametros = new HashMap<>();
        String user = (request.queryParams("usuario") != null) ? request.queryParams("usuario") : "";
        String documento = (request.queryParams("documento") != null) ? request.queryParams("documento") : "";
        String telefono = (request.queryParams("telefono") != null) ? request.queryParams("telefono") : "";
        String fecha = (request.queryParams("fecha") != null) ? request.queryParams("fecha") : "";
        String direccion = (request.queryParams("direccion") != null) ? request.queryParams("direccion") : "";
        String tipoDoc = (request.queryParams("tipoDoc") != null) ? request.queryParams("tipoDoc") : "";
        String pass = (request.queryParams("password") != null) ? request.queryParams("password") : "";
        String nombre = (request.queryParams("nombre") != null) ? request.queryParams("nombre") : "";
        String apellido = (request.queryParams("apellido") != null) ? request.queryParams("apellido") : "";
        String email = (request.queryParams("email") != null) ? request.queryParams("email") : "";
        String idAsociacion = (request.queryParams("asociacion") != null) ? request.queryParams("asociacion") : "";
        Boolean usuarioOk = true;
        if (RepositorioUsuarios.getRepositorio().esIDValido(user)) {
            usuarioOk = false;
            parametros.put("usuarioDuplicado", 1);
        }
        Boolean passwordOk = true;
        try {
            ValidadorPassword.getValidadorPassword().validarPasswordRegex(pass);
        } catch (Exception e) {
            passwordOk = false;
            parametros.put("passwordDebil", 1);
        }

        if (passwordOk && usuarioOk) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dt = LocalDate.parse(fecha, dtf);
            ArrayList<FormaNotificacion> formasDeNoti = new ArrayList<>();
            String formaEmail = (request.queryParams("formaEmail") != null) ? request.queryParams("formaEmail") : "";
            if (!formaEmail.equals("")) {
                formasDeNoti.add(new Email());
            }
            String formaWhatsapp = (request.queryParams("formaWhatsapp") != null) ? request.queryParams("formaWhatsapp") : "";
            if (!formaWhatsapp.equals("")) {
                formasDeNoti.add(new WhatsApp());
            }
            String formaSMS = (request.queryParams("formaSMS") != null) ? request.queryParams("formaSMS") : "";
            if (!formaSMS.equals("")) {
                formasDeNoti.add(new SMS());
            }

            TipoDocumento tipoDocumento = TipoDocumento.valueOf(tipoDoc);
            String[] asoc = idAsociacion.split("-");
            Asociacion asociacionIni = RepositorioAsociaciones.getRepositorio().getAsociacion(Integer.valueOf(asoc[0]));

            Persona persona = new Persona(nombre, apellido, tipoDocumento, Integer.valueOf(documento), dt, direccion, telefono, email, formasDeNoti);

            Standard usuario = new Standard(user, pass, persona, asociacionIni);
            RepositorioUsuarios.getRepositorio().agregarUsuario(usuario);

            request.session(true);
            request.session().attribute("user", user);
            request.session().attribute("usuario", usuario);
            request.session().maxInactiveInterval(3600);
            response.redirect("/panel");
        } else {
            parametros.put("nombreDuplicado", nombre);
            parametros.put("apellidoDuplicado", apellido);
            parametros.put("telefonoDuplicado", telefono);
            parametros.put("fechaDuplicado", fecha);
            parametros.put("direccionDuplicado", direccion);
            parametros.put("tipoDocDuplicado", tipoDoc);
            parametros.put("emailDuplicado", email);
            parametros.put("documentoDuplicado", documento);
            List<String> enumNames = Stream.of(TipoDocumento.values())
                    .map(Enum::name)
                    .collect(Collectors.toList());
            parametros.put("tipoDoc", enumNames);
            parametros.put("clavesAsociacion", RepositorioAsociaciones.getRepositorio().getAsociaciones());

            return new ModelAndView(parametros, "/registroUsuario.hbs");
        }

        return new ModelAndView(parametros, "index.hbs");
    }


}
