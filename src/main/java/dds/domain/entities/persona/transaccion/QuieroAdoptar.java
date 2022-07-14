package dds.domain.entities.persona.transaccion;

import dds.db.RepositorioAsociaciones;
import dds.db.RepositorioUsuarios;
import dds.domain.entities.asociacion.Asociacion;
import dds.servicios.publicaciones.PublicacionQuieroAdoptar;

import javax.persistence.*;
import java.util.HashMap;

@Entity
@DiscriminatorValue("quiero_adoptar")
public class QuieroAdoptar extends Transaccion {
    @Transient
    String idPersona;
    @Transient
    private HashMap<String, String> respuestas = new HashMap <String, String> ();

    public QuieroAdoptar(String idPersona, HashMap<String, String> respuestas) {
        this.idTransaccion = 1;
        this.idPersona = idPersona;
        this.respuestas = respuestas;
    }

    //CONSTRUCTOR PARA LISTA DE PERMISOS
    public QuieroAdoptar() {
        this.idTransaccion = 1;

    }

    @Override
    public void ejecutar()  {
        PublicacionQuieroAdoptar publi = new PublicacionQuieroAdoptar(idPersona,respuestas);
        System.out.print("AAA");
        Asociacion asoc = RepositorioAsociaciones.getRepositorio().getAsociacion(RepositorioUsuarios.getRepositorio().getIDAsocXIdPersona(idPersona));
        asoc.getPublicador().agregarPublicacionQuieroAdoptar(publi);
    }

    @Override
    public int getIdTransaccion() {
        return idTransaccion;
    }
}
