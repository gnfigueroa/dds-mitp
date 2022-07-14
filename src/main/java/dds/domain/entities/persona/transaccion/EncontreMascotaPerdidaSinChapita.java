package dds.domain.entities.persona.transaccion;

import dds.db.RepositorioAsociaciones;
import dds.domain.entities.asociacion.Asociacion;
import dds.servicios.publicaciones.PublicacionMascota;
import dds.servicios.publicaciones.TipoPublicacion;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@DiscriminatorValue("mascota_perdida_sin")
public class EncontreMascotaPerdidaSinChapita extends Transaccion{
    @Transient
    float latitud;
    @Transient
    float longitud;
    @Transient
    String idRescatista;
    @Transient
    ArrayList<String> listaFotos;
    @Transient
    String descripcion;

    //CONSTRUCTOR PARA LISTA DE PERMISOS
    public EncontreMascotaPerdidaSinChapita(){
        this.idTransaccion = 9;
    }

    //CONSTRUCTOR PARA REALIZAR TRANSACCION
    public EncontreMascotaPerdidaSinChapita(float latitud, float longitud, ArrayList<String> listaFotos, String descripcion,String idRescatista) {
        this.idTransaccion = 9;
        this.latitud = latitud;
        this.longitud = longitud;
        this.listaFotos = listaFotos;
        this.descripcion = descripcion;
        this.idRescatista =idRescatista;
    }

    @Override
    public void ejecutar()  {
        PublicacionMascota publi = new PublicacionMascota(latitud,longitud,listaFotos,descripcion,idRescatista, TipoPublicacion.PENDIENTE);
        Asociacion asoc = RepositorioAsociaciones.getRepositorio().getAsociacionMasCercana(latitud,longitud);
        asoc.getPublicador().agregarPublicacion(publi);
    }

    @Override
    public  int getIdTransaccion() {
        return idTransaccion;
    }
}
