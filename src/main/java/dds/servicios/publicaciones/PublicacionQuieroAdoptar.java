package dds.servicios.publicaciones;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "publicacion_quiero_adoptar")
public class PublicacionQuieroAdoptar {
    @Id
    private String idPublicacion;

    public PublicacionQuieroAdoptar() {
    }

    @Column
    private String idAdoptante;

    @ElementCollection
    @MapKeyColumn(name="respuesta")
    @Column(name="pregunta")
    @CollectionTable(name="pregunta_publicacion_quiero_adoptar", joinColumns=@JoinColumn(name="publicacion_id"))
    private Map <String, String> preguntas = new HashMap <String, String> ();

    public PublicacionQuieroAdoptar(String idAdoptante, HashMap<String, String> preguntas) {
        this.idPublicacion = UUID.randomUUID().toString().replace("-", "");
        this.idAdoptante = idAdoptante;
        this.preguntas = preguntas;
    }



    public Map<String, String> getPreguntas() {
        return preguntas;
    }
    public void responderPregunta(String key, String value){ //no se si es necesario ya que las preguntas se pasan en el constr
        preguntas.put(key,value);
    }

    public void setIdPublicacion(String idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    public void setPreguntas(HashMap<String, String> preguntas) {
        this.preguntas = preguntas;
    }

    public String getIdPublicacion() {
        return idPublicacion;
    }

    public String getIdAdoptante() {
        return idAdoptante;
    }
}
