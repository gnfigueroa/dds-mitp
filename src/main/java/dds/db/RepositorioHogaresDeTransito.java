package dds.db;

import dds.domain.entities.mascota.TipoMascota;
import dds.servicios.apiHogares.HogarDeTransito;
import dds.servicios.helpers.CalcDistanciaHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioHogaresDeTransito {
    static List<HogarDeTransito> hogares = new ArrayList<>();
    static List<HogarDeTransito> posiblesHogares = new ArrayList<>();

    public List<HogarDeTransito> getPosiblesHogares() {
        return posiblesHogares;
    }

    private static RepositorioHogaresDeTransito repositorioHogaresDeTransito = new RepositorioHogaresDeTransito() ;

    public static RepositorioHogaresDeTransito getRepositorio(){
        return repositorioHogaresDeTransito;
    }
    public List<HogarDeTransito> getHogares() {
         return hogares;
    }

    public void setRepositorio(List<HogarDeTransito> hogares){
        this.hogares = hogares;
    }

    //filtrar aceptar perros/gatos o ambos
    public List<HogarDeTransito> filtrarPorTipoDeAnimal(TipoMascota tipoMascota){
        if (tipoMascota == TipoMascota.GATO) {
            return filtrarPorGato();
        }
        else {
            return filtrarPorPerro();
        }
    }

    public List<HogarDeTransito> filtrarPorGato(){
        return this.hogares.stream().filter(p -> p.getAdmision().getGatos()).collect(Collectors.toList());
    }

    public List<HogarDeTransito> filtrarPorPerro(){
        return this.hogares.stream().filter(p -> p.getAdmision().getPerros()).collect(Collectors.toList());
    }

    //filtrar por ambos.
    public List<HogarDeTransito> filtrarPorAmbosTipoDeAnimal(){
        return this.hogares.stream().filter(p -> (p.getAdmision().getGatos()) && (p.getAdmision().getPerros())).collect(Collectors.toList());
    }

    //filtrar por patio
    public List<HogarDeTransito> filtrarPorPatio(){
        return this.hogares.stream().filter(p -> p.getPatio()).collect(Collectors.toList());
    }

    //filtrar por lugares disponibles
    public List<HogarDeTransito> filtrarPorDisponibilidad(){
        return this.hogares.stream().filter(p -> p.getLugares_disponibles() > 0).collect(Collectors.toList());
    }

    //filtrar por características
    public List<HogarDeTransito> filtrarPorCaracteristica(List<String> caracteristicas){
        if (caracteristicas.isEmpty()){
            return this.hogares.stream().filter(p -> p.getCaracteristicas().isEmpty()).collect(Collectors.toList());
        } else{
            return this.hogares.stream().filter(p -> p.getCaracteristicas().containsAll(caracteristicas)).collect(Collectors.toList());
        }
    }
    //filtrar por rango de distancia entre Rescatista y hogares
    public List<HogarDeTransito> filtrarPorDistancia(double latitudComparar, double longitudComparar, double radiocercania){
        return this.hogares.
                stream().
                filter(p -> (CalcDistanciaHelper.getHelper().distanciaCoord(p.getUbicacion().getLat(), p.getUbicacion().getLongitud(), latitudComparar, longitudComparar) <= radiocercania)).
                collect(Collectors.toList());
    }




}



