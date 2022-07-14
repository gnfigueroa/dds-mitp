package dds.domain.entities;
import dds.db.EntityManagerHelper;
import dds.db.RepositorioAsociaciones;
import dds.domain.entities.asociacion.Asociacion;
import dds.domain.entities.mascota.Mascota;
import dds.domain.entities.mascota.Sexo;
import dds.domain.entities.mascota.TipoMascota;
import dds.domain.entities.persona.Persona;
import dds.domain.entities.persona.TipoDocumento;
import dds.domain.entities.persona.roles.Duenio;
import dds.domain.entities.seguridad.usuario.Standard;
import dds.domain.entities.seguridad.usuario.Usuario;
import dds.servicios.apiHogares.Ubicacion;
import dds.servicios.avisos.Email;
import dds.servicios.avisos.FormaNotificacion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConfiguradorTest {
    Usuario usuDuenio;
    String fotoAModif = "imgprueba.jpg";
    String fotoModif = "recorte.jpg";
    Asociacion asoc;
    Mascota mascota;
    Persona personaDuenio;

    @Before
    public void setUp() throws NoSuchAlgorithmException {

        if (EntityManagerHelper.getEntityManager().createQuery("from Asociacion").getResultList().isEmpty()){
            //CREO ASOC
            asoc = new Asociacion("Asco",new Ubicacion("DIR",0,0));
            mascota = new Mascota(TipoMascota.PERRO,"lola beatriz","lola", LocalDate.now().minusYears(6),"bruta beba",new ArrayList<>(),new HashMap<>(), Sexo.MACHO);
            //CREO DUENIO
            Email email = new Email();
            List<FormaNotificacion> formasDeNoti = new ArrayList<>();
            formasDeNoti.add(email);
            personaDuenio = new Persona("Matias", "Lanneponders", TipoDocumento.DNI,
                    39000401,LocalDate.of(1995, 7, 7),
                    "dir","1155892198", "mlyonadi@gmail.com", formasDeNoti);

            usuDuenio = new Standard("matilanne","Password1234+",personaDuenio);
            usuDuenio.setAsociacion(asoc);
            personaDuenio.agregarRol(Duenio.getDuenio());
            personaDuenio.agregarMascota(mascota);

            EntityManagerHelper.beginTransaction();
            EntityManagerHelper.entityManager().persist(asoc);
            EntityManagerHelper.entityManager().persist(usuDuenio);
            EntityManagerHelper.commit();
            asoc.getConfigurador().agregarCaracteristicaMascota("Color de Pelo");
            asoc.getConfigurador().agregarCaracteristicaMascota("Tamaño");
        } else
        {
            asoc = (Asociacion) EntityManagerHelper.getEntityManager().createQuery("from Asociacion ").getResultList().get(0);
            mascota  = (Mascota) EntityManagerHelper.getEntityManager().createQuery("from Mascota ").getResultList().get(0);
        }
    }

    @Test
    public void testFoto() {

        asoc.getConfigurador().cambiarTamanio(fotoAModif,fotoModif);
    }
    @Test
    public void testAgregarCaracteristica() {
        Assert.assertEquals(2,RepositorioAsociaciones.getRepositorio().getAsociacion(asoc.getIdAsociacion()).getConfigurador().getClaves().size());
    }
    @Test
    public void testEliminarCaracteristica() {
        RepositorioAsociaciones.getRepositorio().getAsociacion(asoc.getIdAsociacion()).getConfigurador().eliminarCaracteristicas("Color de Pelo");
        Assert.assertEquals(1,RepositorioAsociaciones.getRepositorio().getAsociacion(asoc.getIdAsociacion()).getConfigurador().getClaves().size());
    }
    @Test
    public void testAgregarCaracteristicaAMascota() {
        mascota.agregarCaracteristica("Color De Pelo","Negro y Marron");
        mascota.agregarCaracteristica("Tamaño","Grande");
        //System.out.println (mascota.getCaracteristica().keySet ()); //trae solo keys y el de abajo solo values
        //System.out.println (mascota.getCaracteristica().values ());
        System.out.println (mascota.getCaracteristica());
    }
    @Test
    public void testEliminarCaracteristicaAMascota() {
        mascota.agregarCaracteristica("Color De Pelo","Negro y Marron");
        mascota.agregarCaracteristica("Tamaño","Grande");
        System.out.println (mascota.getCaracteristica());
        mascota.eliminarCaracteristica("Color De Pelo");
        System.out.println ("Se borra caracteristica Color De Pelo");
        System.out.println (mascota.getCaracteristica());
    }
}