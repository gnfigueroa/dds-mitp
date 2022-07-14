package dds.domain.controllers;

import dds.db.RepositorioHogaresDeTransito;
import dds.domain.entities.mascota.Sexo;
import dds.domain.entities.mascota.TipoMascota;
import dds.domain.entities.persona.Persona;
import dds.domain.entities.persona.roles.Rescatista;
import dds.domain.entities.persona.transaccion.BuscarHogarDeTransito;
import dds.domain.entities.persona.transaccion.EncontreMascotaPerdidaSinChapita;
import dds.domain.entities.seguridad.usuario.Usuario;
import dds.servicios.apiHogares.HogarDeTransito;
import dds.servicios.helpers.PhotoUploaderHelper;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ControllerEncontreMascotaSinChapita {
    public ControllerEncontreMascotaSinChapita() {
    }

    public ModelAndView mostrarEncontreMascotaSinChapita(Request request, Response response) {

        Usuario usuario = request.session().attribute("usuario");
        Map<String, Object> parametros = new HashMap<>();

        if (usuario != null) {
            if (usuario.soyAdmin()) {
                parametros.put("Admin", 1);
                parametros.put("asociacion", usuario.getAsociacion());
            } else {
                if (!usuario.getPersona().getListaRoles().stream().anyMatch(p -> (p.getNombre().equals("Rescatista")))) {
                    usuario.getPersona().agregarRol(Rescatista.getRescatista());
                }
                parametros.put("standard", 1);
                parametros.put("persona", usuario.getPersona());
                parametros.put("roles", usuario.getPersona().getListaRoles());
                if (usuario.getPersona().getListaRoles().stream().anyMatch(p -> (p.getNombre().equals("Duenio")))) {
                    parametros.put("Duenio", 1);
                }
                if (usuario.getPersona().getListaRoles().stream().anyMatch(p -> (p.getNombre().equals("Adoptante")))) {
                    parametros.put("Adoptante", 1);
                }
                if (usuario.getPersona().getListaRoles().stream().anyMatch(p -> (p.getNombre().equals("Rescatista")))) {
                    parametros.put("Rescatista", 1);
                }
                if (usuario.getPersona().getListaRoles().stream().anyMatch(p -> (p.getNombre().equals("Voluntario")))) {
                    parametros.put("Voluntario", 1);
                }
            }
        } else {
            response.redirect("/#faltaLogin");
        }
        return new ModelAndView(parametros, "encontreMascotaSinChapita.hbs");
    }
    /*public ModelAndView mostrarHogaresDeTransito(Request request, Response response) {

    }*/

    //personaRescat.ejecutarTransaccion(new EncontreMascotaPerdidaSinChapita((float)-34.605807,(float)-58.438423,new ArrayList<>(),"Perfecto estado",personaRescat.getIdPersona()));
    public ModelAndView crearMascotaPerdidaSinChapita(Request request, Response response) {
        Usuario usuario = request.session().attribute("usuario");
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("usuario",usuario);
        String radio = null;
        String latitud = null;
        String longitud = null;
        String descripcion = null;
        String foto = null;
        String accion = null;
        request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
        try {
            latitud =  PhotoUploaderHelper.getHelper().convertInputStreamToString(request.raw().getPart("lat").getInputStream());
            longitud = PhotoUploaderHelper.getHelper().convertInputStreamToString(request.raw().getPart("long").getInputStream());
            descripcion = PhotoUploaderHelper.getHelper().convertInputStreamToString(request.raw().getPart("descripcion").getInputStream());
            foto = PhotoUploaderHelper.getHelper().uploadPhoto(request.raw().getPart("foto").getInputStream());
            accion = PhotoUploaderHelper.getHelper().convertInputStreamToString(request.raw().getPart("accion").getInputStream()); //
            radio = PhotoUploaderHelper.getHelper().convertInputStreamToString(request.raw().getPart("radio").getInputStream()); // hogar o transito

        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
        if (accion.equals("hogar")){
            usuario.getPersona().ejecutarTransaccion(new BuscarHogarDeTransito(Double.valueOf(latitud),Double.valueOf(longitud),Double.valueOf(radio)));
            parametros.put("hogares",RepositorioHogaresDeTransito.getRepositorio().getPosiblesHogares());

            return new ModelAndView(parametros,"enviarAHogarDeTransito.hbs");
        }
        if (accion.equals("transito")){
        ArrayList<String> listaFotos = new ArrayList<>();
        listaFotos.add(foto);
        Persona rescatista = usuario.getPersona();
        Float fLatitud = Float.parseFloat(latitud);
        Float fLongitud = Float.parseFloat(longitud);
        rescatista.ejecutarTransaccion(new EncontreMascotaPerdidaSinChapita(fLatitud, fLongitud, listaFotos, descripcion, rescatista.getIdPersona()));

        return new ModelAndView(parametros,"/panel#registroMascotaConExito");}
        return new ModelAndView(parametros,"/panel#error");
    }

}
