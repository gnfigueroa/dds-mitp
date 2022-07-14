package dds.servicios.avisos;

import dds.db.EntityManagerHelper;
import dds.domain.entities.mascota.Mascota;
import dds.domain.entities.mascota.Sexo;
import dds.domain.entities.mascota.TipoMascota;
import dds.domain.entities.persona.Persona;
import dds.domain.entities.persona.TipoDocumento;
import dds.domain.entities.persona.roles.Duenio;
import dds.domain.entities.seguridad.usuario.Standard;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class NotificadorTest extends TestCase {
    Persona duenio;
    Standard standard;
    Mascota perro;

    @Before
    public void setUp() throws NoSuchAlgorithmException {

        //ALTA PERSONA DUENIO
        Email email = new Email();
        List<FormaNotificacion> formasDeNoti = new ArrayList<>();
        formasDeNoti.add(email);
        duenio = new Persona("Matias", "Lanneponders", TipoDocumento.DNI,
                39000401,LocalDate.of(1995, 7, 7),
                "dir","1155892198", "mlyonadi@gmail.com", formasDeNoti);
        duenio.getNotificador().agendarContacto("Pedro", "Dorr", "1140435092", "dorrpei@gmail.com", formasDeNoti);
        duenio.agregarRol(Duenio.getDuenio());
        standard = new Standard("matilanne","Password1234+",duenio);

        perro = new Mascota(TipoMascota.PERRO,"nombrePerro","apodoPerro", LocalDate.now().minusYears(5),"Pelo largo",new ArrayList<>(),new HashMap<>(), Sexo.MACHO);
        perro.setEstaPerdida(true);
        duenio.agregarMascota(perro);

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(standard);
        EntityManagerHelper.commit();
    }

//COMENTO PARA NO COMER CREDITO EN TWILO
/*
    @Test
    public void testNotificarSMS() throws MessagingException {
        formasDeNoti.add(testeoSMS);
        noti.agendarContacto("Matias", "Lanneponders", "1155892198", "mlyonadi@gmail.com", formasDeNoti);
        noti.agendarContacto("Pedro", "Dorr", "1140435092", "dorrpei@gmail.com", formasDeNoti);
        noti.notificar("perro1");
    }
    @Test
    public void testNotificarWPP() throws MessagingException {
        formasDeNoti.add(testeoWPP);
        noti.agendarContacto("Matias", "Lanneponders", "1155892198", "mlyonadi@gmail.com", formasDeNoti);

        noti.notificar("perro1");
    }
*/

    @Test
    public void testNotificarEmail(){
        duenio.getNotificador().notificar(perro.getIdMascota());
    }
}