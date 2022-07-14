package dds.domain.entities.persona.roles;

import dds.domain.entities.persona.personaException.TransactionException;
import dds.domain.entities.persona.transaccion.Transaccion;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rol")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipoRol")
public abstract class RolPersona {

    @Id
    protected int id;

    @Column
    protected String nombre;

    @OneToMany(cascade = {CascadeType.ALL})
    List<Transaccion> permisos = new ArrayList<>();

     public void ejecutarTransaccion(Transaccion transaccion)  {
        if(this.permisos.stream().anyMatch(p -> p.getIdTransaccion() ==(transaccion.getIdTransaccion()))){
            transaccion.ejecutar();
        }else{
            throw new TransactionException("Permiso denegado");
        }
    }

    public String getNombre() {
        return nombre;
    }
}
