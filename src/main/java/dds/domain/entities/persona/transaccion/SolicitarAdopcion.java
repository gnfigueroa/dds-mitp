package dds.domain.entities.persona.transaccion;

import dds.db.RepositorioAsociaciones;
import dds.db.RepositorioPersonas;
import dds.domain.entities.asociacion.Asociacion;
import dds.domain.entities.persona.Persona;
import dds.servicios.publicaciones.PublicacionAdopcion;

import javax.persistence.*;

@Entity
@DiscriminatorValue("solicitar_adopcion")
public class SolicitarAdopcion extends Transaccion {
    @Transient
    String idPublicacion;
    @Transient
    int idAsociacion;
    @Transient
    String idAdoptante;

    //CONSTRUCTOR PARA LISTA DE PERMISOS
    public SolicitarAdopcion() {
        this.idTransaccion = 10;
    }
    //CONSTRUCTOR PARA REALIZAR TRANSACCION
    public SolicitarAdopcion(String idPublicacion,int idAsociacion,String idAdoptante) {
        this.idTransaccion = 10;
        this.idPublicacion = idPublicacion;
        this.idAsociacion = idAsociacion;
        this.idAdoptante = idAdoptante;
    }

    @Override
    public void ejecutar()  {
        Asociacion asoc =  RepositorioAsociaciones.getRepositorio().getAsociacion(idAsociacion);
        PublicacionAdopcion publi =  asoc.getPublicador().getEnAdopcionXId(idPublicacion);
        Persona duenio = RepositorioPersonas.getRepositorio().getPersona(publi.getIdDueño());
        Persona adoptante = RepositorioPersonas.getRepositorio().getPersona(idAdoptante);
        String datosAdoptante = "Nombre: " + adoptante.getNombre()+"\n"+
                "Apellido: "+ adoptante.getApellido() +"\n"+
                "Tipo y Nro Doc: "+ adoptante.getTipoDoc() +" - "+adoptante.getNroDoc()+"\n"+
                "Fecha de Nacimiento: "+ adoptante.getFechaNac().toString() +"\n"+
                "Direccion: "+ adoptante.getDireccion()+"\n"+
                "Telefono: "+ adoptante.getTelefono()+"\n"+
                "Mail: "+ adoptante.getEmail();
        String link = "www.link.com"; //TODO Link a la publicacion
        String mensaje = "Encontramos un posible adoptante para su mascota de la publicación: " + link + "\n" +
                "Los datos del posible adoptante son: " + "\n" +
                datosAdoptante;
        duenio.getNotificador().notificarPersona(mensaje);
    }

    @Override
    public int getIdTransaccion() {
        return idTransaccion;
    }
}
