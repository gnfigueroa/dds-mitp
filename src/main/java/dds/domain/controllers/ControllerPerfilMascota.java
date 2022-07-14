package dds.domain.controllers;
import dds.db.RepositorioMascotas;
import dds.db.RepositorioPersonas;
import dds.domain.entities.mascota.Mascota;
import dds.domain.entities.mascota.Sexo;
import dds.domain.entities.mascota.TipoMascota;
import dds.domain.entities.persona.Persona;
import dds.domain.entities.seguridad.usuario.Usuario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ControllerPerfilMascota {
    public ControllerPerfilMascota() {
    }
    public ModelAndView mostrarPerfilMascota(Request req, Response rep){
        String idMascota = req.params("id");
        Mascota mascotaEncontrada= RepositorioMascotas.getRepositorio().getMascota(idMascota);
        Map<String,Object> parametros = new HashMap<>();
        Usuario usuario = req.session().attribute("usuario");
        String idMascota2 = usuario.getAsociacion().getPublicador().getEnAdopcion().stream().filter(p -> p.getIdMascota().equals(idMascota)).findFirst().orElse(null).getIdMascota();
        if(usuario != null){parametros.put("persona", usuario.getPersona());}
        try {
            if (usuario.getPersona().getMascota(idMascota).getIdMascota().equals(idMascota) || mascotaEncontrada.getEstaPerdida() || mascotaEncontrada.getIdMascota().equals(idMascota2)) {

                //duenio = RepositorioPersonas.getRepositorio().getPersona(RepositorioPersonas.getRepositorio().getIdPersonaXidMascota(idMascota));
                parametros.put("mascota",mascotaEncontrada);
                return new ModelAndView(parametros,"perfilMascota.hbs");
            }
        }
        catch (Exception e){
            System.out.println("La mascota no le pertenece o no existe");
            rep.redirect("//#faltaLogin");
        }

        rep.redirect("/");
        return new ModelAndView(parametros,"index.hbs");}
}