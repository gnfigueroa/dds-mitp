package dds.servicios.avisos;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("S")
public class SMS extends FormaNotificacion {
    public SMS() {
        this.adapter = new AdapterSMS();
    }
}
