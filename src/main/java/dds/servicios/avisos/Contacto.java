package dds.servicios.avisos;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Contacto {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    public int getId(){
        return id;
    }

    @Column
    private String nombre;

    @Column
    private String apellido;

    @Column
    private String telefono;

    @Column
    private String email;

    @ManyToMany(cascade = {CascadeType.ALL})
    private List<FormaNotificacion> formasNotificacion = new ArrayList<>();

    public Contacto() {}

    public Contacto(String nombre, String apellido, String telefono, String email, List<FormaNotificacion> formasNoti) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono; //SOLO INGRESAR 11 xxxx xxxx
        this.email = email;
        this.formasNotificacion = formasNoti;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }
    public String getEmail() {
        return email;
    }

    public String getApellido() {
        return apellido;
    }

    public List<FormaNotificacion> getFormasNotificacion() {
        return formasNotificacion;
    }

    public void setId(int id) {
        this.id = id;
    }
}
