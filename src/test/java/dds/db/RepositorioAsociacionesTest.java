package dds.db;

import dds.db.repositorioException.LogicRepoException;
import dds.domain.entities.asociacion.Asociacion;
import dds.servicios.apiHogares.Ubicacion;
import org.junit.*;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RepositorioAsociacionesTest {

    Asociacion asoc;

    @Before
    public void setUp(){

        asoc = new Asociacion("Asociacion",new Ubicacion("Rivadavia 3350",63.584865,63.25186));

        //Guardo estaticamente los objetos persistidos para el testeo
        if (EntityManagerHelper.getEntityManager().find(Asociacion.class,1) != null) {
            asoc = (Asociacion) EntityManagerHelper.entityManager().createQuery("FROM Asociacion ").getResultList().get(0);
        }
    }

    @Test
    public void A_persistenciaTest(){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(asoc);
        EntityManagerHelper.commit();
    }

    @Test
    public void B_testGetAsociacion() {


        Assert.assertEquals(asoc.getIdAsociacion(),RepositorioAsociaciones.getRepositorio().getAsociacion(asoc.getIdAsociacion()).getIdAsociacion());
    }

    @Test (expected = LogicRepoException.class)
    public void C_testGetAsociacionError() {
        Assert.assertEquals(asoc,RepositorioAsociaciones.getRepositorio().getAsociacion(1568).getIdAsociacion());
    }

    @Test
    public void D_testEliminaAsociacion() {
        int cantActualAsoc = EntityManagerHelper.getEntityManager().createQuery("from Asociacion").getResultList().size();
        RepositorioAsociaciones.getRepositorio().eliminarAsociacion(asoc);
        Assert.assertEquals(cantActualAsoc-1,RepositorioAsociaciones.getRepositorio().getAsociaciones().size());
    }
    @Test
    public void B_testGetAsociaciones() {


        Assert.assertEquals(2,RepositorioAsociaciones.getRepositorio().getAsociaciones().size());
    }


}