package dds.domain.entities.persona.roles;

import dds.domain.entities.persona.transaccion.BuscarHogarDeTransito;
import dds.domain.entities.persona.transaccion.EncontreMascotaPerdidaConChapita;
import dds.domain.entities.persona.transaccion.EncontreMascotaPerdidaSinChapita;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("R")
public class Rescatista extends RolPersona {
    private static Rescatista rolRescatista = new Rescatista() ;

    public static Rescatista getRescatista() {return rolRescatista;}

    public Rescatista() {
        this.id =1;
        this.nombre = "Rescatista";
        this.permisos.add(new EncontreMascotaPerdidaConChapita());
        this.permisos.add(new EncontreMascotaPerdidaSinChapita());
        this.permisos.add(new BuscarHogarDeTransito());
    }
}
