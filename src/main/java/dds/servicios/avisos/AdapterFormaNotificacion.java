package dds.servicios.avisos;

import javax.mail.MessagingException;

public interface AdapterFormaNotificacion {


    void notificar(String mensaje, Contacto contacto);


}
