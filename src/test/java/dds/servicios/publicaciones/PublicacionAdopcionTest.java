package dds.servicios.publicaciones;
import dds.db.RepositorioAsociaciones;
import dds.db.RepositorioPersonas;
import dds.db.RepositorioUsuarios;
import dds.domain.entities.asociacion.Asociacion;
import dds.domain.entities.mascota.Mascota;
import dds.domain.entities.mascota.Sexo;
import dds.domain.entities.mascota.TipoMascota;
import dds.domain.entities.persona.Persona;
import dds.domain.entities.persona.roles.Adoptante;
import dds.domain.entities.persona.roles.RolPersona;
import dds.domain.entities.persona.transaccion.DarEnAdopcion;
import dds.domain.entities.seguridad.usuario.Standard;
import dds.servicios.apiHogares.Ubicacion;
import dds.servicios.avisos.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PublicacionAdopcionTest {
    Asociacion asoc;
    HashMap<String, String> preguntas;
    Persona duenio;
    List<Mascota> mascotas = new ArrayList<>();

    @Before
    public void setUp() throws NoSuchAlgorithmException {
        RepositorioAsociaciones.getRepositorio().getAsociaciones().clear();
        RepositorioPersonas.getRepositorio().getPersonas().clear();
        RepositorioUsuarios.getRepositorio().getUsuarios().clear();
        Notificador noti= new Notificador();
        List<RolPersona> listaRoles = new ArrayList<>();
        listaRoles.add(Adoptante.getAdoptante());
        Email email = new Email();
        List<FormaNotificacion> formasDeNoti = new ArrayList<>();
        formasDeNoti.add(email);
        noti.agendarContacto("Matias", "Lanneponders", "1155892198", "mlyonadi@gmail.com", formasDeNoti);

        Mascota perro = new Mascota(TipoMascota.PERRO,"nombrePerro","apodoPerro", LocalDate.now().minusYears(5),"Pelo largo",new ArrayList<>(),new HashMap<>(), Sexo.MACHO);
        perro.setIdMascota("perro1");
        mascotas.add(perro);
        duenio = new Persona("npersona","apersona",mascotas,listaRoles,noti);
        duenio.setIdPersona("persona1");

        asoc = new Asociacion("asoc1",new Ubicacion("DIR",0,0));
        asoc.setIdAsociacion(1);
        Standard standard = new Standard("UsuarioTest","Password1234+",duenio);
        standard.setAsociacion(asoc);

        preguntas = new HashMap<String, String>();
        RepositorioAsociaciones.getRepositorio().agregarAsociacion(asoc);

        RepositorioUsuarios.getRepositorio().agregarUsuario(standard);
        RepositorioPersonas.getRepositorio().getPersonas().add(duenio);


    }


    @Test
    public void testeoPonerEnAdopcionAMiMascotaPorqueNoTengoCorazon() {
        List<String> keys = asoc.getConfigurador().getPreguntas();
        for (int i = 0; i < keys.size(); i++) {
            preguntas.put(keys.get(i), "Respuesta x");
        }


        duenio.ejecutarTransaccion(new DarEnAdopcion("perro1", "persona1", preguntas));
        Assert.assertEquals(1, asoc.getPublicador().getEnAdopcion().size());

    }
}
