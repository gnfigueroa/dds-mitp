package dds.servicios.avisos;

import javax.persistence.*;

@Entity
@Table(name = "forma_notificacion")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipoNotificacion")
public abstract class FormaNotificacion {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    protected int id;

    @Transient
    protected AdapterFormaNotificacion adapter;

    void notificar(String mensaje, Contacto contacto) {
        adapter.notificar(mensaje, contacto);
    }

    public int getId() {
        return id;
    }

}
