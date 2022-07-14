package dds.domain.entities.persona;

import dds.db.*;
import dds.domain.entities.asociacion.Asociacion;
import dds.domain.entities.mascota.Mascota;
import dds.domain.entities.mascota.Sexo;
import dds.domain.entities.mascota.TipoMascota;
import dds.domain.entities.persona.personaException.TransactionException;
import dds.domain.entities.persona.roles.*;
import dds.domain.entities.persona.transaccion.*;
import dds.domain.entities.persona.roles.Adoptante;
import dds.domain.entities.persona.roles.Duenio;
import dds.domain.entities.persona.roles.Rescatista;
import dds.domain.entities.persona.roles.Voluntario;
import dds.domain.entities.persona.transaccion.*;
import dds.domain.entities.seguridad.usuario.Standard;
import dds.domain.entities.seguridad.usuario.Usuario;
import dds.servicios.apiHogares.Ubicacion;
import dds.servicios.avisos.*;
import dds.servicios.publicaciones.PublicacionMascota;
import dds.servicios.publicaciones.TipoPublicacion;
import dds.servicios.publicaciones.publicacionesException.ErrorPubliException;
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
public class PersonaTest {
    Usuario usuAdoptante,usuRescatista,usuVoluntario,usuDuenio;
    Persona personaRescat,personaVoluntario,personaDuenio, personaAdoptante;
    List<Mascota> mascotas = new ArrayList<>();
    Mascota perro,gato,firulais;
    Asociacion asoc;

    @Before
    public void setUp() throws NoSuchAlgorithmException {

        //CREO ASOC
        asoc = new Asociacion("Rescate de Patitas",new Ubicacion("Independencia 200",63.002114,62.090841));


        //CREO MASCOTAS
        firulais = new Mascota(TipoMascota.PERRO,"Firulais","Firu",LocalDate.now().minusYears(2),"Pelo largo",new ArrayList<>(),new HashMap<>(), Sexo.MACHO);
        firulais.setEstaPerdida(true);
        perro = new Mascota(TipoMascota.PERRO,"Tobi","Tobi",LocalDate.now().minusYears(5),"Pelo Corto",new ArrayList<>(),new HashMap<>(), Sexo.MACHO);
        gato = new Mascota(TipoMascota.GATO,"India","Indi",LocalDate.now().minusYears(8),"Siames",new ArrayList<>(),new HashMap<>(),Sexo.HEMBRA);


        //CREO RESCATISTA
        Email email2 = new Email();
        List<FormaNotificacion> formasDeNoti2 = new ArrayList<>();
        formasDeNoti2.add(email2);
        personaRescat = new Persona("Gabriel", "Figueroa",TipoDocumento.DNI,37048624,LocalDate.of(1992, 10, 9),
                "lomas","1140435092", "gafigueroa@gmail.com", formasDeNoti2);
        personaRescat.agregarRol(Rescatista.getRescatista());
        usuRescatista = new Standard("UsuarioRescatista","Password1234+",personaRescat);
        usuRescatista.setAsociacion(asoc);


        //CREO VOLUNTARIO
        Email email3 = new Email();
        List<FormaNotificacion> formasDeNoti3 = new ArrayList<>();
        formasDeNoti3.add(email3);
        personaVoluntario = new Persona("Pedro", "Dorr",TipoDocumento.DNI,390048516,LocalDate.of(1994, 12, 20),
                "dir","1140435092", "dorrpei@gmail.com", formasDeNoti3);
        personaVoluntario.agregarRol(Voluntario.getVoluntario());

        usuVoluntario = new Standard("dorrsito","Password1234+",personaVoluntario);
        usuVoluntario.setAsociacion(asoc);


        //CREO DUENIO
        Email email = new Email();
        List<FormaNotificacion> formasDeNoti = new ArrayList<>();
        formasDeNoti.add(email);
        personaDuenio = new Persona("Matias", "Lanneponders",TipoDocumento.DNI,
                                            39000401,LocalDate.of(1995, 7, 7),
                                            "dir","1155892198", "mlyonadi@gmail.com", formasDeNoti);

        usuDuenio = new Standard("matilanne","Password1234+",personaDuenio);
        usuDuenio.setAsociacion(asoc);
        personaDuenio.agregarRol(Duenio.getDuenio());
        personaDuenio.agregarMascota(perro);
        personaDuenio.agregarMascota(gato);
        personaDuenio.agregarMascota(firulais);


        // CREO ADOPTANTE
        Email email4 = new Email();
        List<FormaNotificacion> formasDeNoti4 = new ArrayList<>();
        formasDeNoti4.add(email4);
        personaAdoptante = new Persona("Agustin", "Orlando",TipoDocumento.DNI,
                4303123,LocalDate.of(2000, 11, 3),
                "dir","1157383400", "orlandoagustin00@gmail.com", formasDeNoti4);
        usuAdoptante = new Standard("UsuarioAdoptante","Password1234+",personaAdoptante);
        usuAdoptante.setAsociacion(asoc);
        personaAdoptante.agregarRol(Adoptante.getAdoptante());


        //Guardo estaticamente los objetos persistidos para el testeo
        if (EntityManagerHelper.getEntityManager().find(Asociacion.class, 1) != null) {
            asoc = (Asociacion) EntityManagerHelper.getEntityManager().createQuery("from Asociacion ").getResultList().get(0);
            List <Persona> listaPersonas = (List <Persona>) EntityManagerHelper.getEntityManager().createQuery("from Persona").getResultList();
            personaDuenio = listaPersonas.stream().filter(persona -> persona.getNroDoc() ==39000401).findFirst().orElse(null);
            personaVoluntario = listaPersonas.stream().filter(persona -> persona.getNroDoc() ==390048516).findFirst().orElse(null);
            personaAdoptante = listaPersonas.stream().filter(persona -> persona.getNroDoc() ==4303123).findFirst().orElse(null);
            personaRescat = listaPersonas.stream().filter(persona -> persona.getNroDoc() ==37048624).findFirst().orElse(null);

            List <Mascota> listaMascotas = (List <Mascota>) EntityManagerHelper.getEntityManager().createQuery("from Mascota ").getResultList();
            perro = listaMascotas.stream().filter(mascota -> mascota.getNombre().equals("Tobi")).findFirst().orElse(null);
            gato = listaMascotas.stream().filter(mascota -> mascota.getNombre().equals("India")).findFirst().orElse(null);
            firulais = listaMascotas.stream().filter(mascota -> mascota.getNombre().equals("Firulais")).findFirst().orElse(null);
        }
        }

    @Test
    public void A_persistenciaTest(){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.entityManager().persist(asoc);
        EntityManagerHelper.entityManager().persist(usuRescatista);
        EntityManagerHelper.entityManager().persist(usuVoluntario);
        EntityManagerHelper.entityManager().persist(usuAdoptante);
        EntityManagerHelper.entityManager().persist(usuDuenio);
        //
        EntityManagerHelper.commit();

    }
    @Test
    public void B_testRegistrarMascota(){
        int size = personaDuenio.getMascotas().size();
        personaDuenio.ejecutarTransaccion(new RegistrarMascota(personaDuenio,TipoMascota.PERRO,"nuevoPerro","nuevoPerro",LocalDate.now().minusYears(8),"Pelo corto",new ArrayList<>(),new HashMap<>(),Sexo.MACHO));
        Assert.assertEquals(size+1, personaDuenio.getMascotas().size());
    }

    @Test
    public void C_testEncontreMascotaPerdidaConChapita(){
        personaRescat.ejecutarTransaccion(new EncontreMascotaPerdidaConChapita(perro.getIdMascota(),(float)-34.605807,(float)-58.438423,new ArrayList<>(),"Perfecto estado",personaRescat.getIdPersona()));

    }
    @Test
    public void D_testEncontreMascotaPerdidaSinChapita(){
        personaRescat.ejecutarTransaccion(new EncontreMascotaPerdidaSinChapita((float)-34.605807,(float)-58.438423,new ArrayList<>(),"Perfecto estado",personaRescat.getIdPersona()));
        Assert.assertEquals(1,asoc.getPublicador().getPublicacionesPendientes().size());
    }

    @Test
    public void E_testEncontreMiMascota(){
        personaRescat.ejecutarTransaccion(new EncontreMascotaPerdidaSinChapita((float)-34.605807,(float)-58.438423,new ArrayList<>(),"Brand new",personaRescat.getIdPersona()));
        String jql = "Select p.id from PublicacionMascota p where p.idRescatista = :idRescatista and p.descripcion = :desc";
        String idPublicacion = (String) EntityManagerHelper.getEntityManager().createQuery(jql).
                setParameter("idRescatista",personaRescat.getIdPersona()).setParameter("desc","Brand new").getResultList().get(0);
        personaVoluntario.ejecutarTransaccion(new ValidarPublicacion(idPublicacion));
        Assert.assertEquals(TipoPublicacion.APROBADA,EntityManagerHelper.getEntityManager().find(PublicacionMascota.class,idPublicacion).getTipoPublicacion()); //Valido que esté aprobada
        personaDuenio.ejecutarTransaccion(new EncontreMiMascota(idPublicacion,asoc.getIdAsociacion(),personaDuenio.getIdPersona()));

    }

    @Test
    public void F_testSolicitarAdopcion(){
        personaDuenio.ejecutarTransaccion(new DarEnAdopcion(perro.getIdMascota(),personaDuenio.getIdPersona(),new HashMap <String, String> ()));
        String jql = "Select p.id from PublicacionAdopcion p where p.idDueño = :idDueño";
        String idPublicacion = (String) EntityManagerHelper.getEntityManager().createQuery(jql).
                setParameter("idDueño",personaDuenio.getIdPersona()).getResultList().get(0);
        personaAdoptante.ejecutarTransaccion(new SolicitarAdopcion(idPublicacion,1,personaAdoptante.getIdPersona()));
    }

    @Test
    public void G_testValidarPublicacion(){
        personaRescat.ejecutarTransaccion(new EncontreMascotaPerdidaSinChapita((float)-34.605807,(float)-58.438423,new ArrayList<>(),"Perfecto estado",personaRescat.getIdPersona()));
        String jql = "Select p.id from PublicacionMascota p where p.idRescatista = :idRescatista and p.tipoPublicacion = :tipo";
        String idPublicacion = (String) EntityManagerHelper.getEntityManager().createQuery(jql).
                setParameter("idRescatista",personaRescat.getIdPersona()).setParameter("tipo",TipoPublicacion.PENDIENTE).getResultList().get(0);
        personaVoluntario.ejecutarTransaccion(new ValidarPublicacion(idPublicacion));
        Assert.assertEquals(TipoPublicacion.APROBADA,EntityManagerHelper.getEntityManager().find(PublicacionMascota.class,idPublicacion).getTipoPublicacion());
    }


    //Intento validar publicacion con rescatista
    @Test (expected = TransactionException.class)
    public void H_testValidarPublicacionErrorPermisos(){
        PublicacionMascota publicacionMascota = new PublicacionMascota(perro.getIdMascota(),(float)-34.605807,(float)-58.438423,new ArrayList<>(),"Perfecto estado",personaRescat.getIdPersona(), TipoPublicacion.PENDIENTE);
        asoc.getPublicador().agregarPublicacion(publicacionMascota);
        personaRescat.ejecutarTransaccion(new ValidarPublicacion(publicacionMascota.getIdPublicacion()));
    }

    @Test
    public void I_testRechazarPublicacion(){
        PublicacionMascota publicacionMascota = new PublicacionMascota(perro.getIdMascota(),(float)-34.605807,(float)-58.438423,new ArrayList<>(),"Perfecto estado",personaRescat.getIdPersona(),TipoPublicacion.PENDIENTE);
        asoc.getPublicador().agregarPublicacion(publicacionMascota);
        personaVoluntario.ejecutarTransaccion(new RechazarPublicacion(publicacionMascota.getIdPublicacion()));
        Assert.assertFalse(asoc.getPublicador().tienePublicacionPendiente(publicacionMascota.getIdPublicacion()));
    }

    //RECHAZAR PUBLICACION QUE NO ESTA PENDIENTE
    @Test (expected = ErrorPubliException.class)
    public void J_testRechazarPublicacionError(){
        String jql = "Select p.id from PublicacionMascota p where p.idRescatista = :idRescatista and p.tipoPublicacion = :tipoPublicacion";
        String idPublicacion = (String) EntityManagerHelper.getEntityManager().createQuery(jql).
                setParameter("idRescatista",personaRescat.getIdPersona()).setParameter("tipoPublicacion",TipoPublicacion.APROBADA).getResultList().get(0);
        personaVoluntario.ejecutarTransaccion(new RechazarPublicacion(idPublicacion));
    }

    @Test
    public void K_testBuscarHogarDeTransito(){
        BuscarHogarDeTransito transaccion = new BuscarHogarDeTransito(-51.622855315759274,-69.21685055962318,500);
        personaRescat.ejecutarTransaccion(transaccion);
        Assert.assertEquals(1,RepositorioHogaresDeTransito.getRepositorio().getPosiblesHogares().size());
    }
}
