package dds.servicios.apiHogares;

import com.google.gson.annotations.SerializedName;

import javax.persistence.*;

@Entity
@Table
public class Ubicacion {

    @Id
    @GeneratedValue
    private int id;

    @Column
    private String direccion;
    @Column
    private double lat;
    @Column
    @SerializedName("long")
    private double longitud;

    public Ubicacion() {
    }

    public Ubicacion(String direccion, double lat, double longitud) {
        this.direccion = direccion;
        this.lat = lat;
        this.longitud = longitud;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}
