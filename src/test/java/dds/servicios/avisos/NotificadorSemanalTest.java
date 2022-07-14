package dds.servicios.avisos;

import dds.db.EntityManagerHelper;
import dds.domain.entities.asociacion.Asociacion;
import dds.domain.entities.mascota.Mascota;
import dds.domain.entities.mascota.Sexo;
import dds.domain.entities.mascota.TipoMascota;
import dds.domain.entities.persona.Persona;
import dds.domain.entities.persona.TipoDocumento;
import dds.domain.entities.persona.roles.Adoptante;
import dds.domain.entities.persona.roles.Duenio;
import dds.domain.entities.persona.transaccion.DarEnAdopcion;
import dds.domain.entities.persona.transaccion.QuieroAdoptar;
import dds.domain.entities.seguridad.usuario.Standard;
import dds.servicios.apiHogares.Ubicacion;
import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Thread.sleep;


public class NotificadorSemanalTest {
    Asociacion asoc;
    HashMap<String, String> preguntas,preguntasAdoptante;
    Persona adoptador,duenio;
    Standard standard,standardAdoptante;


    @Before
    public void setUp() throws NoSuchAlgorithmException {
//        RepositorioAsociaciones.getRepositorio().getAsociaciones().clear();
//        RepositorioPersonas.getRepositorio().getPersonas().clear();
//        RepositorioUsuarios.getRepositorio().getUsuarios().clear();
        //ALTA ASOCIACION
        asoc = new Asociacion("Rescate de Patitas",new Ubicacion("Alberdi 3368",-60.5948,-43.24186));


        //ALTA PERSONA DUENIO

        Email email = new Email();
        List<FormaNotificacion> formasDeNoti = new ArrayList<>();
        formasDeNoti.add(email);
        duenio = new Persona("Matias", "Lanneponders", TipoDocumento.DNI,
                39000401,LocalDate.of(1995, 7, 7),
                "dir","1155892198", "mlyonadi@gmail.com", formasDeNoti);
        duenio.agregarRol(Duenio.getDuenio());
        standard = new Standard("matilanne","Password1234+",duenio);
        standard.setAsociacion(asoc);
        Mascota perro = new Mascota(TipoMascota.PERRO,"nombrePerro","apodoPerro", LocalDate.now().minusYears(5),"Pelo largo",new ArrayList<>(),new HashMap<>(), Sexo.MACHO);
        duenio.agregarMascota(perro);

        preguntas = new HashMap<String, String>();


        //ALTA PERSONA ADOPTANTE
        Email email4 = new Email();
        List<FormaNotificacion> formasDeNoti4 = new ArrayList<>();
        formasDeNoti4.add(email4);
        adoptador= new Persona("Agustin", "Orlando",TipoDocumento.DNI,
                4303123,LocalDate.of(2000, 11, 3),
                "dir","1157383400", "orlandoagustin00@gmail.com", formasDeNoti4);
        adoptador.agregarRol(Adoptante.getAdoptante());
        standardAdoptante = new Standard("UsuarioAdoptante","Password1234+",adoptador);
        standardAdoptante.setAsociacion(asoc);

        preguntasAdoptante = new HashMap<String, String>();

        List<String> keys = asoc.getConfigurador().getPreguntas();
        for (int i = 0; i < keys.size(); i++) {
            preguntas.put(keys.get(i), "Respuesta"+i);
            preguntasAdoptante.put(keys.get(i),"Respuesta"+i);
        }


        EntityManagerHelper.beginTransaction();
        //EntityManagerHelper.entityManager().persist(asoc);
        EntityManagerHelper.entityManager().persist(standard);
        EntityManagerHelper.entityManager().persist(standardAdoptante);
        EntityManagerHelper.commit();

        duenio.ejecutarTransaccion(new DarEnAdopcion(perro.getIdMascota(), duenio.getIdPersona(), preguntas));

        adoptador.ejecutarTransaccion(new QuieroAdoptar(adoptador.getIdPersona(),preguntasAdoptante));
        for(int l = 0;l<asoc.getPublicador().getPublicacionesQuieroAdoptar().size();l++){
            asoc.getPublicador().getPublicacionesQuieroAdoptar().get(l).setIdPublicacion("PubliAdoptanteNro:"+l);
        }

        for(int m = 0;m<asoc.getPublicador().getEnAdopcion().size();m++){
            asoc.getPublicador().getEnAdopcion().get(m).setIdPublicacion("PubliBuscoDuenioNro::"+m);
        }
    }



    @Test
    public void pruebaEnvioNotificacionSemanal() throws InterruptedException {
        NotificadorSemanal.getNotificadorSemanalHelper().notificar();
        //notificadorSemanal = new NotificadorSemanal();
        //notificadorSemanal.notificar();
        //notificadorSemanal.notificarPublicacionesConCoincidenciaSegun(1,asoc.getIdAsociacion());
        sleep(10000);
        System.out.println(preguntasAdoptante);
        System.out.println(preguntas);
        System.out.println("fin notificacion....");
        //notificadorSemanal.notificarPublicacionesConCoincidenciaSegun(2,asoc.getIdAsociacion());

    }






}
