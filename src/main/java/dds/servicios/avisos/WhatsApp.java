package dds.servicios.avisos;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("W")
public class WhatsApp extends FormaNotificacion {
    public WhatsApp() {
        this.adapter = new AdapterWhatsApp();
    }
}
