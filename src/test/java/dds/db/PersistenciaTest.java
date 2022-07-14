package dds.db;

import dds.domain.entities.asociacion.Asociacion;
import dds.domain.entities.mascota.Mascota;
import dds.domain.entities.mascota.Sexo;
import dds.domain.entities.mascota.TipoMascota;
import dds.domain.entities.persona.Persona;
import dds.domain.entities.persona.TipoDocumento;
import dds.domain.entities.persona.roles.Duenio;
import dds.domain.entities.persona.roles.Rescatista;
import dds.domain.entities.persona.roles.Voluntario;
import dds.domain.entities.persona.transaccion.DarEnAdopcion;
import dds.domain.entities.seguridad.usuario.Administrador;
import dds.domain.entities.seguridad.usuario.Standard;
import dds.servicios.apiHogares.Ubicacion;
import dds.servicios.avisos.Email;
import dds.servicios.avisos.FormaNotificacion;
import dds.servicios.publicaciones.PublicacionMascota;
import dds.servicios.publicaciones.TipoPublicacion;
import org.junit.After;
import org.junit.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PersistenciaTest extends AbstractPersistenceTest implements WithGlobalEntityManager {


    @After
    public void rollBack() {
        rollbackTransaction();
    }


    @Test
    public void PullInicialDeDatos() throws NoSuchAlgorithmException {

        Asociacion asoc = new Asociacion("Rescate de Patitas CABA",new Ubicacion("Rivadavia 9450",-34.63722034233585, -58.49715981178081));
        asoc.getConfigurador().agregarCaracteristicaMascota("Color de Pelo");
        asoc.getConfigurador().agregarCaracteristicaMascota("Tamaño");

        Administrador usuarioTest = new Administrador("usuarioTest","Password123+");
        usuarioTest.setAsociacion(asoc);



        Mascota perro = new Mascota(TipoMascota.PERRO,"nombrePerro","apodoPerro",LocalDate.now().minusYears(5),"Pelo largo",new ArrayList<>(),new HashMap<>(), Sexo.MACHO);
        Mascota gato = new Mascota(TipoMascota.GATO,"nombreGato","apodoGato",LocalDate.now().minusYears(8),"Siames",new ArrayList<>(),new HashMap<>(),Sexo.MACHO);
        perro.agregarCaracteristica("Color De Pelo","Negro y Marron");
        perro.agregarCaracteristica("Tamaño","Grande");
        perro.setEstaPerdida(true);
        Email email = new Email();
        List<FormaNotificacion> formasDeNoti = new ArrayList<>();
        formasDeNoti.add(email);

        Email email2 = new Email();
        List<FormaNotificacion> formasDeNoti2 = new ArrayList<>();
        formasDeNoti2.add(email2);

        Persona persona = new Persona("npersona","apersona",TipoDocumento.DNI,39000401,LocalDate.of(1995,07,07),"dire","1165485425","mlyonadi@gmail.com",formasDeNoti);
        persona.getNotificador().agendarContacto("Pedro", "Dorr", "1140435092", "dorrpei@gmail.com", formasDeNoti);
        persona.agregarMascota(perro);
        persona.agregarMascota(gato);
        HashMap<String, String> preguntas = new HashMap<String, String>();
        preguntas.put(asoc.getConfigurador().getPreguntas().get(0),"Negro");
        preguntas.put(asoc.getConfigurador().getPreguntas().get(1),"Grande");

        Standard usuarioStandard = new Standard("usuarioStandard","Password123+2",persona,asoc);



        Persona persona3 = new Persona("Vpersona","apersona",TipoDocumento.DNI,39000401,LocalDate.of(1995,07,07),"dire","1165485425","makinsonf.christian@gmail.com",formasDeNoti);
        persona3.getListaRoles().add(Voluntario.getVoluntario());
        Standard usuarioStandard2 = new Standard("usuarioVoluntario","Password123+2",persona3,asoc);



        EntityManagerHelper.beginTransaction();

        EntityManagerHelper.getEntityManager().persist(usuarioStandard);

        EntityManagerHelper.getEntityManager().persist(usuarioStandard2);

        EntityManagerHelper.getEntityManager().persist(usuarioTest);


        EntityManagerHelper.commit();
        //Prueba de ejecucion de tansaccion Dar en Adopcion por un usuario standard dueño.
        persona.ejecutarTransaccion(new DarEnAdopcion(perro.getIdMascota(),persona.getIdPersona(),preguntas));


    }
}