package dds.servicios.publicaciones;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "publicacion_en_adopcion")
public class PublicacionAdopcion {
    @Id
    private String idPublicacion;

    public PublicacionAdopcion() {
    }

    @Column
    private String idMascota;

    @Column
    private String idDueño;

    @ElementCollection
    @MapKeyColumn(name="respuesta")
    @Column(name="pregunta")
    @CollectionTable(name="pregunta_publicacion_adopcion", joinColumns=@JoinColumn(name="publicacion_id"))
    private Map<String, String> preguntas = new HashMap <String, String> ();

    public PublicacionAdopcion(String idMascota, String idDueño, HashMap<String, String> preguntas) { //esti va a responderse desde el front con getpreguntas
        this.idPublicacion = UUID.randomUUID().toString().replace("-", "");
        this.idMascota = idMascota;
        this.idDueño = idDueño;
        this.preguntas = preguntas; //se obtiene de la asociacion con el metodo getPreguntas
    }

    public void responderPregunta(String key, String value){ //no se si es necesario ya que las preguntas se pasan en el constr
        preguntas.put(key,value);
    }
    public Map<String, String> getPreguntas() {
        return preguntas;
    }

    public String getIdPublicacion() {
        return idPublicacion;
    }

    public String getIdMascota() {
        return idMascota;
    }

    public void setIdPublicacion(String idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    public String getIdDueño() {
        return idDueño;
    }
}
