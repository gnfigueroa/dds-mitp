package dds.domain.controllers;
import dds.domain.entities.asociacion.Asociacion;
import dds.domain.entities.asociacion.Configurador;
import dds.domain.entities.seguridad.usuario.Usuario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class ControllerConfigurarCaracteristicasAsociacion {
    public ControllerConfigurarCaracteristicasAsociacion() {
    }
    public ModelAndView mostrarConfigurarCaracteristicasAsociacion(Request req, Response rep){

        Usuario usuario = req.session().attribute("usuario");
        Asociacion asoc;
        Configurador config ;
        List<String>preguntas;
        List<String>pregMascotas;
        Map<String,Object> parametros = new HashMap<>();
        if(usuario.soyAdmin()) {
            asoc = usuario.getAsociacion();
            config = asoc.getConfigurador();
            preguntas = config.getPreguntasOpcionales();
            pregMascotas = config.getClaves();
            parametros.put("config",config);
            parametros.put("Admin",1);
            parametros.put("asociacion",asoc);
            parametros.put("preguntas",preguntas);
            parametros.put("preguntasMascotas",pregMascotas);
        }else{
            rep.redirect("/");
        }




        return new ModelAndView(parametros,"configurarCaracteristicasAsociacion.hbs");
    }
    public ModelAndView modificarCaracteristicas(Request request, Response response){
        Usuario usuario = request.session().attribute("usuario");
        Asociacion asoc;
        Configurador config ;
        List<String>preguntas;
        List<String>pregMascotas;
        Map<String,Object> parametros = new HashMap<>();
        if(usuario.soyAdmin()) {
            asoc = usuario.getAsociacion();
            config = asoc.getConfigurador();
            preguntas = config.getPreguntasOpcionales();
            pregMascotas = config.getClaves();
            parametros.put("config",config);
            parametros.put("Admin",1);
            parametros.put("asociacion",asoc);
            List<String> listaPregs = new ArrayList<>();
            List<String> listaCaracs = new ArrayList<>();

            String key1 = (request.queryParams("key1") != null) ? request.queryParams("key1") : "";
            String key2 = (request.queryParams("key2") != null) ? request.queryParams("key2") : "";
            String[] preg = key1.split("&&");
            String[] pregMasc = key2.split("&&");
            for (int i = 0; i < preg.length; ++i) {
                listaPregs.add(preg[i]);

            }
            for (int i = 0; i < pregMasc.length; ++i) {
                listaCaracs.add(pregMasc[i]);
            }
            config.agregarCaracteristicas(listaCaracs);
            config.agregarPreguntas(listaPregs);
            preguntas = config.getPreguntasOpcionales();
            pregMascotas = config.getClaves();
            parametros.put("preguntas",preguntas);
            parametros.put("preguntasMascotas",pregMascotas);
        }else{
            response.redirect("/");
        }






        return new ModelAndView(parametros,"configurarCaracteristicasAsociacion.hbs");
    }

}
