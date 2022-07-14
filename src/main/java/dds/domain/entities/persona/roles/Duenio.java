package dds.domain.entities.persona.roles;


import dds.domain.entities.persona.transaccion.*;
import dds.domain.entities.persona.transaccion.DarEnAdopcion;
import dds.domain.entities.persona.transaccion.EncontreMiMascota;
import dds.domain.entities.persona.transaccion.RegistrarMascota;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("D")
public class Duenio extends RolPersona {

    private static Duenio rolDuenio = new Duenio() ;

    public static Duenio getDuenio() {return rolDuenio;}

    public Duenio() {
        this.id = 2;
        this.nombre = "Duenio";
        this.permisos.add(new RegistrarMascota());
        this.permisos.add(new EncontreMiMascota());
        this.permisos.add(new DarEnAdopcion());
    }
}