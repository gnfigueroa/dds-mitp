package dds.domain.entities.persona.transaccion;

import dds.db.RepositorioAsociaciones;
import dds.domain.entities.asociacion.Asociacion;
import dds.servicios.publicaciones.PublicacionMascota;
import dds.servicios.publicaciones.Publicador;

import javax.persistence.*;

@Entity
@DiscriminatorValue("validar_publicacion")
public class ValidarPublicacion extends Transaccion {
    @Transient
    String idPublicacion;

    public ValidarPublicacion(){
        this.idTransaccion = 8;
    }

    public ValidarPublicacion(String idPublicacion) {
        this.idTransaccion = 8;
        this.idPublicacion = idPublicacion;
    }

    @Override
    public void ejecutar()  {
        Asociacion asoc = RepositorioAsociaciones.getRepositorio().getAsociacion(RepositorioAsociaciones.getRepositorio().getIDAsocXIdPublicacion(idPublicacion));
        Publicador publicador= asoc.getPublicador();
        PublicacionMascota publicacion = publicador.getPendienteXId(idPublicacion);
        publicador.aprobarPublicacion(publicacion);

    }

    @Override
    public int getIdTransaccion() {
        return idTransaccion;
    }
}
