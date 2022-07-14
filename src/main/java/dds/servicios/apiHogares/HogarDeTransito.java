package dds.servicios.apiHogares;


import java.util.ArrayList;
import java.util.List;

public class HogarDeTransito {
    private Ubicacion ubicacion;
    private String telefono;
    private Admision admisiones;
    private String id;
    private String nombre;
    private int capacidad;
    private int lugares_disponibles;
    private Boolean patio;
    private List<String> caracteristicas = new ArrayList<>();


    public HogarDeTransito(String id,String nombre, Ubicacion ubicacion, String telefono, Admision admisiones, int capacidad, int lugares_disponibles, Boolean patio, List<String> caracteristicas) {
        this.ubicacion = ubicacion;
        this.telefono = telefono;
        this.admisiones = admisiones;
        this.id = id;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.lugares_disponibles = lugares_disponibles;
        this.patio = patio;
        this.caracteristicas = caracteristicas;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }
    public String getDireccion(){

        return this.getUbicacion().getDireccion();
    }
    public Double getLatitud(){

        return this.getUbicacion().getLat();
    }
    public Double getLongitud(){

        return this.getUbicacion().getLongitud();
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Admision getAdmision() {
        return admisiones;
    }

    public void setAdmision(Admision admisiones) {
        this.admisiones = admisiones;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getLugares_disponibles() {
        return lugares_disponibles;
    }

    public void setLugares_disponibles(int lugares_disponibles) {
        this.lugares_disponibles = lugares_disponibles;
    }

    public Boolean getPatio() {
        return patio;
    }

    public void setPatio(Boolean patio) {
        this.patio = patio;
    }

    public List<String> getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(List<String> caracteristicas) {
        this.caracteristicas = caracteristicas;
    }
}
