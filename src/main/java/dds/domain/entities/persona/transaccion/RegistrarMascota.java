package dds.domain.entities.persona.transaccion;

import dds.db.EntityManagerHelper;
import dds.domain.entities.mascota.Mascota;
import dds.domain.entities.mascota.Sexo;
import dds.domain.entities.mascota.TipoMascota;
import dds.domain.entities.persona.Persona;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Entity
@DiscriminatorValue("registrar_mascota")
public class RegistrarMascota extends Transaccion {
    @Transient
    private Persona duenio;
    @Transient
    private TipoMascota tipo;
    @Transient
    private String nombre;
    @Transient
    private String apodo;
    @Transient
    private LocalDate fechaNac;
    @Transient
    private String descripcion;
    @Transient
    private Sexo sexo;
    @Transient
    private List<String> listaFotos;
    @Transient
    private HashMap <String, String> caracteristica = new HashMap <String, String> ();

    public RegistrarMascota() {
        this.idTransaccion = 7;
    }

    public RegistrarMascota(Persona dueño, TipoMascota tipo, String nombre, String apodo, LocalDate fnac, String descripcion, List<String> listaFotos, HashMap<String, String> caracteristica, Sexo sexo) {
        this.idTransaccion = 7;
        this.duenio = dueño;
        this.tipo = tipo;
        this.nombre = nombre;
        this.apodo = apodo;
        this.fechaNac = fnac;
        this.descripcion = descripcion;
        this.listaFotos = listaFotos;
        this.caracteristica = caracteristica;
        this.sexo = sexo;
    }

    //REGISTRAR MASCOTA
    @Override
    public void ejecutar(){
        Mascota nueva = new Mascota(tipo,nombre,apodo,fechaNac,descripcion,listaFotos,caracteristica,sexo);
        duenio.agregarMascota(nueva);
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().merge(duenio);
        EntityManagerHelper.commit();
    }

    @Override
    public int getIdTransaccion() {
        return idTransaccion;
    }


}
