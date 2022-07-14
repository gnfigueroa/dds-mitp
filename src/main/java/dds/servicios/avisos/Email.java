package dds.servicios.avisos;

import dds.domain.entities.persona.transaccion.QuieroAdoptar;
import dds.domain.entities.persona.transaccion.SolicitarAdopcion;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("E")
public class Email extends FormaNotificacion {
    public Email() {
        this.adapter = new AdapterEmail();
    }
}
