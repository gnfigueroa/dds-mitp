package dds.servicios.publicaciones;
import dds.db.EntityManagerHelper;

import dds.domain.entities.asociacion.Asociacion;
import dds.domain.entities.persona.Persona;
import dds.domain.entities.persona.TipoDocumento;
import dds.domain.entities.persona.roles.Adoptante;
import dds.domain.entities.persona.transaccion.QuieroAdoptar;
import dds.domain.entities.seguridad.usuario.Administrador;
import dds.domain.entities.seguridad.usuario.Standard;
import dds.domain.entities.seguridad.usuario.Usuario;
import dds.servicios.apiHogares.Ubicacion;
import dds.servicios.avisos.Email;
import dds.servicios.avisos.FormaNotificacion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PublicacionQuieroAdoptarTest  {
    Asociacion asoc;
    PublicacionQuieroAdoptar publi;
    HashMap <String, String> preguntas;
    Persona adoptador;
    Usuario standard;
    Administrador admin;

    @Before
    public void setUp() throws NoSuchAlgorithmException {

        asoc = new Asociacion("Rescate de Patitas",new Ubicacion("Jose Maria Moreno 256",-62.015153,-30.524153));
        admin = new Administrador("matilanne","Passwrod1234+");
        admin.setAsociacion(asoc);

        // CREO ADOPTANTE
        Email email4 = new Email();
        List<FormaNotificacion> formasDeNoti4 = new ArrayList<>();
        formasDeNoti4.add(email4);
        adoptador = new Persona("Agustin", "Orlando", TipoDocumento.DNI,
                4303123, LocalDate.of(2000, 11, 3),
                "dir","1157383400", "orlandoagustin00@gmail.com", formasDeNoti4);
        standard = new Standard("UsuarioAdoptante","Password1234+",adoptador);
        standard.setAsociacion(asoc);
        adoptador.agregarRol(Adoptante.getAdoptante());
        preguntas = new HashMap<String, String>();

        if (EntityManagerHelper.getEntityManager().find(Usuario.class, standard.getUserName()) == null){
            EntityManagerHelper.beginTransaction();
            EntityManagerHelper.entityManager().persist(standard);
            EntityManagerHelper.entityManager().persist(admin);
            EntityManagerHelper.commit();
        } else
        {
            admin = (Administrador) EntityManagerHelper.getEntityManager().createQuery("from Administrador ").getResultList().get(0);
            standard = (Usuario) EntityManagerHelper.getEntityManager().createQuery("from Standard").getResultList().get(0);
        }


    }

    @Test
    public void testeoLasPreguntasBase(){
        Assert.assertEquals(3, asoc.getConfigurador().getPreguntas().size());
    }
    @Test
    public void testeoAgregadoDePreguntas(){
        int cantPreg = asoc.getConfigurador().getPreguntas().size();
        admin.agregarPregunta("Tiene genitales?");
        Assert.assertEquals(cantPreg+1, asoc.getConfigurador().getPreguntas().size());
    }
    @Test
    public void testeoEliminadoDePreguntas(){
        int cantPreg = admin.getAsociacion().getConfigurador().getPreguntas().size();
        admin.agregarPregunta("Tiene genitales?");
        List<String > preguntas = admin.getAsociacion().getConfigurador().getPreguntas();
        int result = preguntas.size();
        Assert.assertEquals(cantPreg+1, result);
        admin.eliminarPregunta("Tiene genitales?");
        Assert.assertEquals(cantPreg, admin.getAsociacion().getConfigurador().getPreguntas().size());
    }
    @Test
    public void testeoPublicacion(){
        List <String> keys = asoc.getConfigurador().getPreguntas();
        for (int i=0;i<keys.size();i++) {
            preguntas.put(keys.get(i),"Respuesta x");
        }
        publi= new PublicacionQuieroAdoptar(standard.getPersona().getIdPersona(),preguntas);
        Assert.assertEquals(keys.size(),publi.getPreguntas().size());
    }
    @Test
    public void testeoDeseoAdoptar(){
        List <String> keys =  admin.getAsociacion().getConfigurador().getPreguntas();
        for (int i=0;i<keys.size();i++) {
            preguntas.put(keys.get(i),"Respuesta x");
        }
        adoptador.ejecutarTransaccion(new QuieroAdoptar(standard.getPersona().getIdPersona(),preguntas));

    }
}