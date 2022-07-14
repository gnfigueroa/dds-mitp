package dds.domain.entities.mascota;


import dds.db.EntityManagerHelper;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;


@Entity
@Table (name = "mascota")
public class Mascota {

    @Id
    @Column (name = "id")
    private String idMascota;

    @Enumerated(EnumType.STRING)
    private TipoMascota tipo;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @Column
    private String nombre;

    @Column
    private String apodo;

    @Column (columnDefinition = "DATE")
    private Date fechaNac;

    @Column
    private String descripcion;



    @Lob
    @ElementCollection(fetch=FetchType.EAGER)
    @Column(name="Path", columnDefinition="longblob")
    //@ElementCollection(fetch = FetchType.LAZY)
    //@CollectionTable(name = "lista_foto_mascota")
    private List<String> pathFoto = new ArrayList<>();

    //private List<ConfigCaracMascota> caracteristica;

    public Mascota() {
    }

    @ElementCollection
    @MapKeyColumn(name="caracteristica")
    @Column(name="clave")
    @CollectionTable(name="caracteristica", joinColumns=@JoinColumn(name="mascota_id"))
    private Map <String, String> caracteristica = new HashMap <String, String> ();

    @Column
    private Boolean estaPerdida = false;

    public Mascota(TipoMascota tipo, String nombre, String apodo, LocalDate fechaNac, String descripcion, List<String> listaFotos, HashMap <String, String> caracteristica,Sexo sexo) {
        this.idMascota= UUID.randomUUID().toString().replace("-", "");
        this.tipo = tipo;
        this.nombre = nombre;
        this.apodo = apodo;
        this.sexo = sexo;
        this.fechaNac = Date.from(fechaNac.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());//TODO Modificar el constructor y todos los test que lo usen para recibir una fecha de nacimiento.
        this.descripcion = descripcion;
        this.pathFoto = listaFotos;
        this.caracteristica = caracteristica;
    }
    public void agregarfoto(String a){
        this.pathFoto.add(a);
    }

    public TipoMascota getTipo() {
        return tipo;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public String getApodo() {
        return apodo;
    }

    public Date getFechaNac() {
        return fechaNac;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public List<String> getPathFoto() {
        return pathFoto;
    }

    public Boolean getEstaPerdida() {
        return estaPerdida;
    }

    public Map<String, String> getCaracteristica() {
        return caracteristica;
    }

    public void agregarCaracteristica(String key, String value){
        caracteristica.put(key,value);

    }  // POR FRONT NADA MAS SE VA A MOSTRAR LAS KEYS QUE AGREGO ASOCIACION PARA QUE EL DUEÑO PUEDA AGREGARLAS
    public void eliminarCaracteristica(String key){

        caracteristica.remove(key);

    }

    public void setIdMascota(String idMascota) {
        this.idMascota = idMascota;
    }

    public void setEstaPerdida(Boolean estaPerdida) {
        this.estaPerdida = estaPerdida;
    }

    public String getIdMascota() {
        return idMascota;
    }

    public Mascota(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNac.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }




}
