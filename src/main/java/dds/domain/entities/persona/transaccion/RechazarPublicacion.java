package dds.domain.entities.persona.transaccion;

import dds.db.RepositorioAsociaciones;
import dds.domain.entities.asociacion.Asociacion;
import dds.servicios.publicaciones.PublicacionMascota;
import dds.servicios.publicaciones.Publicador;

import javax.persistence.*;

@Entity
@DiscriminatorValue("rechazar_publicacion")
public class RechazarPublicacion extends Transaccion {
    @Transient
    String idPublicacion;

    public RechazarPublicacion(){
        this.idTransaccion = 6;
    }

    public RechazarPublicacion(String idPublicacion) {
        this.idTransaccion = 6;
        this.idPublicacion = idPublicacion;
    }

    @Override
    public void ejecutar()  {
        Asociacion asoc = RepositorioAsociaciones.getRepositorio().getAsociacion(RepositorioAsociaciones.getRepositorio().getIDAsocXIdPublicacion(idPublicacion));
        Publicador publicador= asoc.getPublicador();
        PublicacionMascota publicacion = publicador.getPendienteXId(idPublicacion);
        publicador.rechazarPublicacion(publicacion);
    }

    @Override
    public int getIdTransaccion() {
        return idTransaccion;
    }
}
