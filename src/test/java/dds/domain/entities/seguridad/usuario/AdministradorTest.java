package dds.domain.entities.seguridad.usuario;

import dds.db.EntityManagerHelper;
import dds.db.RepositorioAsociaciones;
import dds.domain.entities.asociacion.Asociacion;
import dds.servicios.apiHogares.Ubicacion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.security.NoSuchAlgorithmException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdministradorTest {

    Administrador admin;
    Asociacion asoc;

    @Before
    public void setUp() throws NoSuchAlgorithmException {

        //CREO ADMIN
        admin = new Administrador("admin","Password123+");
        //CREO ASOC
        asoc = new Asociacion("Asco",new Ubicacion("Rescate de Patitas",-34.559974,-58.4838289));
        admin.setAsociacion(asoc);

        if (EntityManagerHelper.getEntityManager().find(Asociacion.class, 1) != null) {
            asoc = (Asociacion) EntityManagerHelper.getEntityManager().createQuery("from Asociacion ").getResultList().get(0);
            admin  = (Administrador) EntityManagerHelper.getEntityManager().createQuery("from Administrador ").getResultList().get(0);
        }
    }
    @Test
    public void A_persistenciaTest(){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.entityManager().persist(asoc);
        EntityManagerHelper.entityManager().persist(admin);
        EntityManagerHelper.commit();
    }

    @Test
    public void B_testAgregarCaracteristica() {
        admin.agregarCaracteristica("Color de ojos");
        Assert.assertEquals("Color de ojos",RepositorioAsociaciones.getRepositorio().getAsociacion(admin.getAsociacion().getIdAsociacion()).getConfigurador().getClaves().stream().filter(s -> s.equals("Color de ojos")).findFirst().orElse(null));
    }

    @Test
    public void C_testEliminarCaracteristica() {
        int cantCaracteristicas = RepositorioAsociaciones.getRepositorio().getAsociaciones().get(0).getConfigurador().getClaves().size();
        admin.agregarCaracteristica("Tamanio");
        Assert.assertEquals(cantCaracteristicas+1,RepositorioAsociaciones.getRepositorio().getAsociacion(admin.getAsociacion().getIdAsociacion()).getConfigurador().getClaves().size());
        admin.eliminarCaracteristica("Tamanio");
        Assert.assertEquals(cantCaracteristicas,RepositorioAsociaciones.getRepositorio().getAsociacion(admin.getAsociacion().getIdAsociacion()).getConfigurador().getClaves().size());
    }

    // Para los siguientes tests se usó una foto de 1600x900 píxeles [rda = 1.7777]
    @Test
    public void D_testModificarTamanioFotosA_192000x480() {
        admin.modificarTamanioFotos(192000, 480);
        asoc.getConfigurador().cambiarTamanio("imgprueba.jpg","recorte2.jpg");
        // No se obtiene una foto de 192000x480 [rda = 400.0000]...
        // ... sino una de 853x480 [rda = 1.7770], manteniendo la relación de aspecto original [1.7777].
    }
    @Test
    public void E_testModificarTamanioFotosA_64x480() {
        admin.modificarTamanioFotos(64, 480);
        asoc.getConfigurador().cambiarTamanio("imgprueba.jpg","recorte2.jpg");
        // No se obtiene una foto de 64x480 [rda = 0.1333]...
        // ... sino una de 64x36 [rda = 1.7777], manteniendo la relación de aspecto original [1.7777].
    }
    @Test
    public void F_testModificarTamanioFotosA_480x192000() { //
        admin.modificarTamanioFotos(480, 192000);
        asoc.getConfigurador().cambiarTamanio("imgprueba.jpg","recorte2.jpg");
        // No se obtiene una foto de 480x192000 [rda = 0.0025]...
        // ... sino una de 480x270 [rda = 1.7777], manteniendo la relación de aspecto original [1.7777].
    }
    @Test
    public void G_testModificarTamanioFotosA_480x64() { //
        admin.modificarTamanioFotos(480, 64);
        asoc.getConfigurador().cambiarTamanio("imgprueba.jpg","recorte2.jpg");
        // No se obtiene una foto de 480x64 [rda = 7.5]...
        // ... sino una de 113x64 [rda = 1.7656], manteniendo la relación de aspecto original [1.7777].
    }
    @Test
    public void H_testModificarTamanioFotosA_1280x1280() { //
        admin.modificarTamanioFotos(1280, 1280);
        asoc.getConfigurador().cambiarTamanio("imgprueba.jpg","recorte2.jpg");
        // No se obtiene una foto de 1280x1280 [rda = 1]...
        // ... sino una de 1280x720 [rda = 1.7777], manteniendo la relación de aspecto original [1.7777].
    }

}