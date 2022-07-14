package dds.db;

import dds.db.repositorioException.LogicRepoException;
import dds.domain.entities.asociacion.Asociacion;
import dds.domain.entities.mascota.Mascota;
import dds.domain.entities.mascota.Sexo;
import dds.domain.entities.mascota.TipoMascota;
import dds.domain.entities.persona.Persona;
import dds.domain.entities.persona.TipoDocumento;
import dds.domain.entities.persona.roles.Duenio;
import dds.domain.entities.seguridad.usuario.Standard;
import dds.servicios.apiHogares.Ubicacion;
import dds.servicios.avisos.Email;
import dds.servicios.avisos.FormaNotificacion;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RepositorioUsuariosTest  {
    Persona personaDuenio;
    Standard usuDuenio;
    Asociacion asoc;
    Mascota perro;


    @Before
    public void setUp() throws Exception {
        //CREO ASOC
        asoc = new Asociacion("asoc",new Ubicacion("DIR",0,0));

        //CREO DUENIO
        List<FormaNotificacion> formasDeNoti = new ArrayList<>();
        Email email = new Email();
        formasDeNoti.add(email);
        personaDuenio = new Persona("Matias", "Lanneponders", TipoDocumento.DNI,
                39000401, LocalDate.of(1995, 7, 7),
                "Byron 35","1155892198", "mlyonadi@gmail.com", formasDeNoti);
        usuDuenio = new Standard("matilanne","Password1234+",personaDuenio);
        usuDuenio.setAsociacion(asoc);
        perro = new Mascota(TipoMascota.PERRO,"nombrePerro","apodoPerro",LocalDate.now().minusYears(5),"Pelo largo",new ArrayList<>(),new HashMap<>(), Sexo.MACHO);
        personaDuenio.agregarMascota(perro);
        personaDuenio.agregarRol(Duenio.getDuenio());

        //Guardo estaticamente los objetos persistidos para el testeo
        if (EntityManagerHelper.getEntityManager().find(Asociacion.class, 1) != null) {
            personaDuenio = (Persona) EntityManagerHelper.getEntityManager().createQuery("from Persona").getResultList().get(0);
            asoc = (Asociacion) EntityManagerHelper.getEntityManager().createQuery("from Asociacion ").getResultList().get(0);
            perro = (Mascota) EntityManagerHelper.getEntityManager().createQuery("from Mascota ").getResultList().get(0);
        }
    }

    @Test
    public void A_persistenciaTest(){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(usuDuenio);
        EntityManagerHelper.commit();

    }

    @Test
    public void B_testGetIdUsuarioXPersona(){
        Assert.assertEquals("matilanne",RepositorioUsuarios.getRepositorio().getUserNameXIdPersona(personaDuenio.getIdPersona()));
    }

    @Test (expected = LogicRepoException.class)
    public void C_testGetIdUsuarioXPersonaError() {
        String userName = RepositorioUsuarios.getRepositorio().getUserNameXIdPersona("1sasdaw0");

    }

    @Test
    public void D_testGetIDAsocXIdPersona() {
        Assert.assertEquals(asoc.getIdAsociacion(),RepositorioUsuarios.getRepositorio().getIDAsocXIdPersona(personaDuenio.getIdPersona()));

    }
    @Test (expected = LogicRepoException.class)
    public void E_testGetIDAsocXIdPersonaError(){

        Assert.assertEquals(asoc.getIdAsociacion(),RepositorioUsuarios.getRepositorio().getIDAsocXIdPersona("10asdasdw"));

    }

    @Test
    public void F_testGetIDAsocXIdMascota() {
        Assert.assertEquals(asoc.getIdAsociacion(),RepositorioUsuarios.getRepositorio().getIDAsocXIdMascota(perro.getIdMascota()));

    }
    @Test (expected = LogicRepoException.class)
    public void G_testGetIDAsocXIdMascotaError() {
        Assert.assertEquals(asoc.getIdAsociacion(),RepositorioUsuarios.getRepositorio().getIDAsocXIdMascota("12349ds"));

    }

}