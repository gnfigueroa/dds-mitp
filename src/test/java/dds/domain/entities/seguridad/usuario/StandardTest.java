package dds.domain.entities.seguridad.usuario;

import dds.db.EntityManagerHelper;
import dds.db.RepositorioUsuarios;
import dds.domain.entities.persona.Persona;
import dds.domain.entities.persona.TipoDocumento;
import dds.domain.entities.persona.personaException.AssignPersonaException;
import dds.servicios.avisos.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StandardTest{
    Persona persona;

    @Before
    public void setUp() throws Exception {
        List<FormaNotificacion> formasDeNoti = new ArrayList<>();
        Email email = new Email();
        formasDeNoti.add(email);
        Notificador noti= new Notificador();
        noti.agendarContacto("Matias", "Lanneponders", "1155892198", "mlyonadi@gmail.com", formasDeNoti);

        //Creo persona para probar en tests
        persona = new Persona("Matias", "Lanneponders", TipoDocumento.DNI,
                39000401, LocalDate.of(1995, 7, 7),
                "dir","1155892198", "mlyonadi@gmail.com", formasDeNoti);
    }

    @Test
    public void agregarPersonaDespuesDeCrearUsuarioTest() throws NoSuchAlgorithmException {

        Standard usuarioTest = new Standard("usuarioTest","Password123+");
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.entityManager().persist(usuarioTest);
        EntityManagerHelper.commit();
        usuarioTest.agregarPersona(persona);
        Assert.assertEquals(RepositorioUsuarios.getRepositorio().getUsuario("usuarioTest").getPersona().getNombre(),"Matias");

    }
    @Test
    public void agregarPersonaEnConstructorTest() throws NoSuchAlgorithmException {
        Standard usuarioTest = new Standard("usuarioTest","Password123+",persona);
        Assert.assertEquals(usuarioTest.getPersona().getNombre(),"Matias");
    }
    @Test (expected = AssignPersonaException.class)
    public void agregarPersonaAUsuarioConPersonaExistenteErrorTest() throws NoSuchAlgorithmException {
        Standard usuarioTest = new Standard("usuarioTest","Password123+",persona);
        usuarioTest.agregarPersona(persona);
    }
}