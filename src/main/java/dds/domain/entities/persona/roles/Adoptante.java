package dds.domain.entities.persona.roles;

import dds.domain.entities.persona.transaccion.QuieroAdoptar;
import dds.domain.entities.persona.transaccion.SolicitarAdopcion;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A")
public class Adoptante extends RolPersona {

    private static Adoptante rolAdoptante = new Adoptante() ;

    public static Adoptante getAdoptante() {return rolAdoptante;}

    public Adoptante() {
        this.id = 4;
        this.nombre = "Adoptante";
        //this.permisos.add(new DarEnAdopcion());
        this.permisos.add(new QuieroAdoptar());
        this.permisos.add(new SolicitarAdopcion());
    }
}