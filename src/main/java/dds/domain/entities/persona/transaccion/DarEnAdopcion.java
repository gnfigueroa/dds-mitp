package dds.domain.entities.persona.transaccion;

import dds.db.RepositorioAsociaciones;
import dds.db.RepositorioUsuarios;
import dds.domain.entities.asociacion.Asociacion;
import dds.servicios.publicaciones.PublicacionAdopcion;

import javax.persistence.*;
import java.util.HashMap;

@Entity
@DiscriminatorValue("dar_adopcion")
public class DarEnAdopcion extends Transaccion {
    @Transient
    String idMascota;
    @Transient
    String idDuenio;
    @Transient
    private HashMap<String, String> respuestas = new HashMap <String, String> ();


    public DarEnAdopcion() {
        this.idTransaccion = 3;
    }

    public DarEnAdopcion(String idMascota, String idDuenio, HashMap<String, String> respuestas) {
        this.idTransaccion = 3;
        this.idMascota = idMascota;
        this.idDuenio = idDuenio;
        this.respuestas = respuestas;
    }

    @Override
    public void ejecutar(){
        int idAsoc = RepositorioUsuarios.getRepositorio().getIDAsocXIdMascota(idMascota);
        Asociacion asoc = RepositorioAsociaciones.getRepositorio().getAsociacion(idAsoc);
        PublicacionAdopcion publi = new PublicacionAdopcion(idMascota,idDuenio,respuestas);
        asoc.getPublicador().agregarPublicacionMascotaEnAdopcion(publi);


    }

    @Override
    public int getIdTransaccion() {
        return idTransaccion;
    }
}
