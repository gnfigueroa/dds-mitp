package dds.servicios.publicaciones;

import dds.db.RepositorioMascotas;
import dds.db.RepositorioPersonas;
import dds.db.repositorioException.LogicRepoException;
import dds.domain.entities.mascota.Mascota;
import dds.domain.entities.persona.Persona;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table (name = "publicacion_mascota")
public class PublicacionMascota {
    @Id
    private String idPublicacion;

    public PublicacionMascota() {
    }

    @Column
    private String idMascota;

    @Column
    private double latitud;

    @Column
    private double longitud;

    @Lob
    @ElementCollection(fetch=FetchType.EAGER)
    @Column(name="lista_foto_publicacion_mascota", columnDefinition="longblob")
    //@ElementCollection(fetch = FetchType.LAZY)
    //@CollectionTable(name = "lista_foto_mascota")
    private List<String> pathFoto = new ArrayList<>();

    @Column
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private TipoPublicacion tipoPublicacion;

    @Column
    private String idHogaresDeTransito;

    @Column
    private String idRescatista;

/*
    @ManyToOne
    @JoinColumn(name = "FK_PUBLICADOR", updatable = false)
    private Publicador publicador;
    //PRUEBA
    public PublicacionMascota(Publicador publicador,String idMascota, double latitud, double longitud, List<String> listaFotos, String descripcion,String idRescatista) {
        this.idPublicacion = UUID.randomUUID().toString().replace("-", "");
        this.idMascota = idMascota;
        this.latitud = latitud;
        this.longitud = longitud;
        this.listaFotos = listaFotos;
        this.descripcion = descripcion;
        this.idRescatista = idRescatista;
        this.publicador = publicador;
    }
    public Publicador getPublicador() {
        return publicador;
    }

    public void setPublicador(Publicador publicador) {
        this.publicador = publicador;
    }*/

    public PublicacionMascota(String idMascota, double latitud, double longitud, List<String> listaFotos, String descripcion,String idRescatista,TipoPublicacion tipo) {
        this.idPublicacion = UUID.randomUUID().toString().replace("-", "");
        this.idMascota = idMascota;
        this.latitud = latitud;
        this.longitud = longitud;
        this.pathFoto = listaFotos;
        this.descripcion = descripcion;
        this.idRescatista = idRescatista;
        this.tipoPublicacion = tipo;
    }


    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public PublicacionMascota(double latitud, double longitud, List<String> listaFotos, String descripcion, String idRescatista, TipoPublicacion tipo) {
        this.idPublicacion = UUID.randomUUID().toString().replace("-", "");
        this.latitud = latitud;
        this.longitud = longitud;
        this.pathFoto = listaFotos;
        this.descripcion = descripcion;
        this.idRescatista= idRescatista;
        this.tipoPublicacion = tipo;
    }

    public void setIdPublicacion(String idPubli) {
        this.idPublicacion = idPubli;
    }

    public String getIdPublicacion() {
        return idPublicacion;
    }

    public String getIdRescatista() {
        return idRescatista;
    }

    public void setIdHogaresDeTransito(String idHogaresDeTransito) {
        this.idHogaresDeTransito = idHogaresDeTransito;
    }

    public void setIdRescatista(String idRescatista) {
        this.idRescatista = idRescatista;
    }

    public TipoPublicacion getTipoPublicacion() {
        return tipoPublicacion;
    }

    public void setTipoPublicacion(TipoPublicacion tipoPublicacion) {
        this.tipoPublicacion = tipoPublicacion;
    }

    public List<String> getPathFoto() {
        return pathFoto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getRescatista() {
        try {
            return RepositorioPersonas.getRepositorio().getPersona(this.idRescatista).getNombre();
        } catch (LogicRepoException e) {
            //ESTO PASA CUANDO EL ID ES INEXISTENTE
            return "No se encontró el nombre del rescatista";
        }
    }

    public String getMascota() {
        try {
            return RepositorioMascotas.getRepositorio().getMascota(this.idMascota).getNombre();
        } catch (LogicRepoException e) {
            //ESTO PASA CUANDO EL ID ES INEXISTENTE
            return "No se encontró el nombre de la mascota";
        }
    }

    public String getPrimeraFoto() {
        try {
            return this.pathFoto.get(0);
        } catch (Exception e) {
            return "/images/perroadop.jpg";
        }
    }

    public List<String> getFotosSinPrimera() {
        try {
            List<String> fotosSinPrimera = new ArrayList<>();
            fotosSinPrimera = this.getPathFoto();
            fotosSinPrimera.remove(0);
            return fotosSinPrimera;
        } catch (Exception e) {
            return null;
        }

    }

    public String getIdMascota() {
        return idMascota;
    }
}
