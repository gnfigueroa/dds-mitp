package dds.domain.entities.asociacion;



import dds.servicios.apiHogares.Ubicacion;
import dds.servicios.publicaciones.Publicador;

import javax.persistence.*;

@Entity
@Table (name = "asociacion")
public class Asociacion {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int idAsociacion;

    @Column
    private String nombre;

    @OneToOne (cascade = {CascadeType.ALL})
    @JoinColumn(name = "direccion_id")
    private Ubicacion ubicacion;

    @OneToOne (cascade = {CascadeType.ALL})
    private Publicador publicador;

    @OneToOne (cascade = {CascadeType.ALL})
    private Configurador configurador;

    public Asociacion(String nombre, Ubicacion ubicacion) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.publicador = new Publicador();
        this.configurador = new Configurador();
    }
    public Asociacion(){}

    public Configurador getConfigurador() {
        return configurador;
    }

    public void setIdAsociacion(int idAsociacion) {
        this.idAsociacion = idAsociacion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public int getIdAsociacion() {
        return idAsociacion;
    }

    public Publicador getPublicador() {
        return publicador;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

}
