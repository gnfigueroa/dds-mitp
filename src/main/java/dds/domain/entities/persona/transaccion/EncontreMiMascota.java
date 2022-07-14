package dds.domain.entities.persona.transaccion;


import dds.db.RepositorioAsociaciones;
import dds.db.RepositorioPersonas;
import dds.domain.entities.asociacion.Asociacion;
import dds.domain.entities.persona.Persona;
import dds.servicios.publicaciones.PublicacionMascota;

import javax.persistence.*;

@Entity
@DiscriminatorValue("encontre_mi_mascota")
public class EncontreMiMascota extends Transaccion {
    @Transient
    String idPublicacion;
    @Transient
    int idAsociacion;
    @Transient
    String idDuenio;

    //CONSTRUCTOR PARA LISTA DE PERMISOS
    public EncontreMiMascota() {
        this.idTransaccion = 5;
    }
    //CONSTRUCTOR PARA REALIZAR TRANSACCION
    public EncontreMiMascota(String idPublicacion,int idAsociacion,String idDuenio) {
        this.idTransaccion = 5;
        this.idPublicacion = idPublicacion;
        this.idAsociacion = idAsociacion;
        this.idDuenio = idDuenio;
    }

    @Override
    public void ejecutar(){
        Asociacion asoc =  RepositorioAsociaciones.getRepositorio().getAsociacion(idAsociacion);
        PublicacionMascota publi =  asoc.getPublicador().getAprobadaXId(idPublicacion);
        Persona rescatista = RepositorioPersonas.getRepositorio().getPersona(publi.getIdRescatista());
        Persona duenio = RepositorioPersonas.getRepositorio().getPersona(idDuenio);
        String datosDuenio = "Nombre: " + duenio.getNombre()+"\n"+
                             "Apellido: "+ duenio.getApellido() +"\n"+
                             "Tipo y Nro Doc: "+ duenio.getTipoDoc() +" - "+duenio.getNroDoc()+"\n"+
                             "Fecha de Nacimiento: "+ duenio.getFechaNac().toString() +"\n"+
                             "Direccion: "+ duenio.getDireccion()+"\n"+
                             "Telefono: "+ duenio.getTelefono()+"\n"+
                             "Mail: "+ duenio.getEmail();
        String link = "www.link.com"; //TODO Link a la publicacion
        String mensaje = "Encontramos al dueño de la publicacion: " + link + "\n" +
                         "Los datos del dueño son: " + "\n" +
                          datosDuenio;
        rescatista.getNotificador().notificarPersona(mensaje);
    }

    @Override
    public int getIdTransaccion() {
        return idTransaccion;
    }
}
