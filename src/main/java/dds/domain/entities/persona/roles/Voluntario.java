package dds.domain.entities.persona.roles;

import dds.domain.entities.persona.transaccion.*;
import dds.domain.entities.persona.transaccion.RechazarPublicacion;
import dds.domain.entities.persona.transaccion.ValidarPublicacion;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("V")
public class Voluntario extends RolPersona {

    private static Voluntario rolVoluntario = new Voluntario() ;

    public static Voluntario getVoluntario() {return rolVoluntario;}

    public Voluntario() {
        this.id = 3;
        this.nombre = "Voluntario";
        this.permisos.add(new ValidarPublicacion());
        this.permisos.add(new RechazarPublicacion());
        //this.permisos.add(new GestionarPreguntasAdopcion()); TODO
    }
}