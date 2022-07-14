package dds.servicios.publicaciones;

import dds.db.EntityManagerHelper;
import dds.domain.entities.mascota.Mascota;
import dds.domain.entities.persona.Persona;
import dds.servicios.publicaciones.publicacionesException.ErrorPubliException;

import javax.management.DescriptorKey;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table (name = "publicador")
public class Publicador {
    @Id
    @GeneratedValue
    private int id;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<PublicacionMascota> publicacionesMascotas= new ArrayList<>();

    @OneToMany(cascade = {CascadeType.ALL})
    private List<PublicacionQuieroAdoptar> publicacionesQuieroAdoptar= new ArrayList<>();

    @OneToMany(cascade = {CascadeType.ALL})
    private List<PublicacionAdopcion> enAdopcion= new ArrayList<>();


    public Publicador(){}

    public void agregarPublicacionQuieroAdoptar(PublicacionQuieroAdoptar publi){
        publicacionesQuieroAdoptar.add(publi);

    }

    public void agregarPublicacionMascotaEnAdopcion(PublicacionAdopcion publi){
        enAdopcion.add(publi);
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.entityManager().persist(publi);
        EntityManagerHelper.commit();

    }

    public List<PublicacionAdopcion> getEnAdopcion() {
        return enAdopcion;
    }

    public List<PublicacionQuieroAdoptar> getPublicacionesQuieroAdoptar() {
        return publicacionesQuieroAdoptar;
    }

    public void eliminarPublicacionQuieroAdoptar(String idPublicacionQuieroAdoptar){
        //TODO
    }

    public void aprobarPublicacion (PublicacionMascota publi) {  //aprueba publi pendiente y la pasa a aprobada
        if (publi.getTipoPublicacion().equals(TipoPublicacion.PENDIENTE)){
            publi.setTipoPublicacion(TipoPublicacion.APROBADA);
            EntityManagerHelper.beginTransaction();
            EntityManagerHelper.entityManager().merge(publi);
            EntityManagerHelper.commit();
        }else{
            throw new ErrorPubliException("Dicha publicacion ya fue aprobada");
        }
    }

    public void rechazarPublicacion (PublicacionMascota publi) {  //aprueba publi pendiente y la pasa a aprobada
        if (publi.getTipoPublicacion().equals(TipoPublicacion.PENDIENTE)){
            eliminarPublicacion(publi);
        }else{
            throw new ErrorPubliException("Dicha publicacion no se encuentra en la lista de pendientes");
        }
    }

    public void agregarPublicacion (PublicacionMascota publi){
        this.publicacionesMascotas.add(publi);
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.entityManager().persist(publi);
        EntityManagerHelper.commit();
    }
    public void eliminarPublicacion (PublicacionMascota publi){
        this.publicacionesMascotas.remove(publi);
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.entityManager().remove(publi);
        EntityManagerHelper.commit();
    }


    public boolean tienePublicacionPendiente(String idPublicacion) {
        return this.getPublicacionesPendientes().stream().anyMatch(p -> p.getIdPublicacion().equals(idPublicacion)) ;
    }
    public boolean tienePublicacionAprobada(String idPublicacion) {
        return this.getPublicacionesAprobadas().stream().anyMatch(p -> p.getIdPublicacion().equals(idPublicacion));
    }

    public List<PublicacionMascota> getPublicacionesMascotas() {
        return (List<PublicacionMascota>) EntityManagerHelper.getEntityManager().createQuery(" from PublicacionMascota").getResultList() ;
    }

    public List<PublicacionMascota> getPublicacionesAprobadas() {
        return getPublicacionesMascotas().stream().filter(p-> p.getTipoPublicacion().equals(TipoPublicacion.APROBADA)).collect(Collectors.toList());
    }

    public List<PublicacionMascota> getPublicacionesPendientes() {
        return getPublicacionesMascotas().stream().filter(p-> p.getTipoPublicacion().equals(TipoPublicacion.PENDIENTE)).collect(Collectors.toList());
    }

    public List<PublicacionMascota> getPublicacionesPrivadas() {
        return getPublicacionesMascotas().stream().filter(p-> p.getTipoPublicacion().equals(TipoPublicacion.PRIVADA)).collect(Collectors.toList());
    }


    public PublicacionMascota getPendienteXId(String id){
        PublicacionMascota publi = this.getPublicacionesPendientes().stream().filter(p-> p.getIdPublicacion().equals(id)).findFirst().orElse(null) ;
        if(publi== null){
            throw new ErrorPubliException("Dicha publicacion no se encuentra en la lista de pendientes");
        }
        return  publi;
    }
    public PublicacionMascota getPrivadaXId(String id){
        PublicacionMascota publi = this.getPublicacionesPrivadas().stream().filter(p-> p.getIdPublicacion().equals(id)).findFirst().orElse(null) ;
        if(publi== null){
            throw new ErrorPubliException("Dicha publicacion no se encuentra en la lista de privadas");
        }
        return  publi;
    }
    public PublicacionMascota getAprobadaXId(String id){
        return this.getPublicacionesAprobadas().stream().filter(p-> p.getIdPublicacion().equals(id)).findFirst().orElse(null);
    }

    public PublicacionAdopcion getEnAdopcionXId(String id){
        return this.enAdopcion.stream().filter(p-> p.getIdPublicacion().equals(id)).findFirst().orElse(null);
    }


}
