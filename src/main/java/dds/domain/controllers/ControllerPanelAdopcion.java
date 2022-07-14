package dds.domain.controllers;

import dds.db.RepositorioMascotas;
import dds.domain.entities.asociacion.Asociacion;
import dds.domain.entities.mascota.Mascota;
import dds.domain.entities.persona.transaccion.DarEnAdopcion;
import dds.domain.entities.seguridad.usuario.Usuario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class ControllerPanelAdopcion {
    public ControllerPanelAdopcion() {
    }

    public ModelAndView mostrarPanel(Request request, Response response){
        Map<String, Object> parametros = new HashMap<>();
        String id = request.params("idMascota");
        Usuario usuario = request.session().attribute("usuario");
        Mascota mascota = RepositorioMascotas.getRepositorio().getMascota(id);
        parametros.put("mascota",mascota);
        parametros.put("id",id);
        parametros.put("asoc",usuario.getAsociacion());
        parametros.put("clave",usuario.getAsociacion().getConfigurador().getClaves());
        parametros.put("usuario",usuario);
        parametros.put("nombre",usuario);

        return new ModelAndView(parametros,"ponerEnAdopcion.hbs");
    }

    public Response ponerEnAdopcion(Request request, Response response){
        HashMap <String,String> claves = new HashMap<>();
        String id = request.params("idMascota");
        Usuario usuario = request.session().attribute("usuario");
        for(int i=0;i<usuario.getAsociacion().getConfigurador().getClaves().size();i++){
            String clave = (request.queryParams(usuario.getAsociacion().getConfigurador().getClaves().get(i)) != null) ? request.queryParams(usuario.getAsociacion().getConfigurador().getClaves().get(i)) : "";
            claves.put(usuario.getAsociacion().getConfigurador().getClaves().get(i),clave);
        }
        usuario.getPersona().ejecutarTransaccion(new DarEnAdopcion(id,usuario.getPersona().getIdPersona(),claves));

        response.redirect("/");
        return response;
    }



}
