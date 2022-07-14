package dds.domain.entities.asociacion;
import dds.db.EntityManagerHelper;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Configurador {

    @Id
    @GeneratedValue
    private int id;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "pregunta_obligatoria")
    private List<String> preguntasObligatorias= new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "pregunta_opcional")
    private List<String> preguntasOpcionales = new ArrayList<>();

    @Column
    private Integer anchoFoto;

    @Column
    private Integer altoFoto;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "clave")
    private List<String> claves = new ArrayList<>();



    public Configurador() {
        this.anchoFoto=720;
        this.altoFoto=480;
        this.claves = new ArrayList<>(); //se usa para el hashmap de mascotas
        this.preguntasOpcionales = new ArrayList<>();
        preguntasObligatorias.add("Tamaño");
        preguntasObligatorias.add("Temperamento");
        preguntasObligatorias.add("Esta Vacunado?");
    }

    public void setAnchoFoto(Integer anchoFoto) {
        this.anchoFoto = anchoFoto;
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.entityManager().merge(this);
        EntityManagerHelper.commit();
    }

    public void setAltoFoto(Integer altoFoto) {
        this.altoFoto = altoFoto;
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.entityManager().merge(this);
        EntityManagerHelper.commit();
    }

    public void cambiarTamanio(String archivoFoto, String rutaGuardado) {
        Imagen foto = new Imagen();
        try {
            ImageIO.write(foto.redimensionar(archivoFoto, anchoFoto, altoFoto),"jpg",new File(rutaGuardado));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ////MASCOTAS

    public void agregarCaracteristicaMascota(String caracteristicaNueva){
        this.claves.add(caracteristicaNueva);
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.entityManager().merge(this);
        EntityManagerHelper.commit();
    }
    public void agregarCaracteristicas(List<String> caracs){
        this.claves = caracs;
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.entityManager().merge(this);
        EntityManagerHelper.commit();
    }
    public void eliminarCaracteristicas(String caracABorrar){
        this.claves.remove(caracABorrar);
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.entityManager().merge(this);
        EntityManagerHelper.commit();
    }
    public List<String> getClaves() {
        return claves;
    }

    ///PREGUNTAS
    public void agregarPreguntaNueva(String preg){
        if (this.preguntasOpcionales.contains(preg)){}else{
        this.preguntasOpcionales.add(preg);}
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.entityManager().merge(this);
        EntityManagerHelper.commit();
    }
    public void agregarPreguntas(List<String> pregs){
        this.preguntasOpcionales = pregs;
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.entityManager().merge(this);
        EntityManagerHelper.commit();
    }
    public void eliminarPregunta(String preg){
        if (this.preguntasOpcionales.contains(preg)){
        this.preguntasOpcionales.remove(preg);}
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.entityManager().merge(this);
        EntityManagerHelper.commit();
    }
    public List<String> getPreguntas() {

        List<String> listaTot;

        (listaTot= new ArrayList<String>(this.preguntasObligatorias)).addAll(this.preguntasOpcionales);

        return listaTot;
    }

    public int getId() {
        return id;
    }

    public List<String> getPreguntasObligatorias() {
        return preguntasObligatorias;
    }

    public List<String> getPreguntasOpcionales() {
        return preguntasOpcionales;
    }

    public Integer getAnchoFoto() {
        return anchoFoto;
    }

    public Integer getAltoFoto() {
        return altoFoto;
    }
}