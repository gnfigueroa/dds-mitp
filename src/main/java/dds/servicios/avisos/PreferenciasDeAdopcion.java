package dds.servicios.avisos;

import dds.db.RepositorioAsociaciones;
import dds.db.RepositorioPersonas;
import dds.domain.entities.asociacion.Asociacion;
import dds.servicios.publicaciones.PublicacionAdopcion;
import dds.servicios.publicaciones.PublicacionQuieroAdoptar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PreferenciasDeAdopcion {
    RepositorioPersonas repositorioPersonas;
    RepositorioAsociaciones repositorioAsociaciones;


    public List<PublicacionQuieroAdoptar> obtenerPublicacionesAdoptantesSegunAsociacion(int idAsoc) {
        Asociacion asociacion;
        asociacion = repositorioAsociaciones.getRepositorio().getAsociacion(idAsoc);

        return asociacion.getPublicador().getPublicacionesQuieroAdoptar();
    }

    public List<PublicacionAdopcion> obtenerPublicacionesEnAdopcionSegunAsociacion(int idAsoc) {
        Asociacion asociacion;
        asociacion = repositorioAsociaciones.getRepositorio().getAsociacion(idAsoc);

        return asociacion.getPublicador().getEnAdopcion();
    }

    public List<String> obtenerPreguntasSegunAsociacion(int idAsoc) {
        Asociacion asociacion;
        asociacion = repositorioAsociaciones.getRepositorio().getAsociacion(idAsoc);

        return asociacion.getConfigurador().getPreguntas();
    }

    public List<PublicacionAdopcion> obtenerPublicacionesConCoincidenciaSegunAdoptante(int coincidenciasMinima, PublicacionQuieroAdoptar publicacionQuieroAdoptarAux, List<PublicacionAdopcion> publicacionAdopcionesAux) {
        int cantidad = 0;
        String valor = "";
        Map<String, String> preguntasPosibleAdoptante = new HashMap<String, String>();
        preguntasPosibleAdoptante = publicacionQuieroAdoptarAux.getPreguntas();
        List<PublicacionAdopcion> auxPubli = new ArrayList<>();

        for (String key : preguntasPosibleAdoptante.keySet()) {
            valor = (String) preguntasPosibleAdoptante.get(key);
            if (cantidad <= coincidenciasMinima) {
                String finalValor = valor;
                auxPubli = publicacionAdopcionesAux.stream().filter(p -> p.getPreguntas().containsKey(key)).collect(Collectors.toList()).stream().filter(pe -> pe.getPreguntas().containsValue(finalValor)).collect(Collectors.toList());
                cantidad++;
            } else {
                return auxPubli;
            }
            publicacionAdopcionesAux.clear();
            //cargo las nuevas publicaciones para filtrar nuevamente
            publicacionAdopcionesAux.addAll(auxPubli);
        }
        return auxPubli;
    }
}