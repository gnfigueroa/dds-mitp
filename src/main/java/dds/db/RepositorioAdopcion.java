package dds.db;
import dds.db.repositorioException.LogicRepoException;
import dds.domain.entities.asociacion.Asociacion;
import dds.servicios.publicaciones.PublicacionAdopcion;

import java.util.List;

public class RepositorioAdopcion {

    private static RepositorioAdopcion repositorioAdopcion = new RepositorioAdopcion() ;

    public static RepositorioAdopcion getRepositorio() {return repositorioAdopcion;}

    public List<PublicacionAdopcion> getPublicacionesAdopcion() {
        return (List<PublicacionAdopcion>) EntityManagerHelper.getEntityManager().createQuery("from PublicacionAdopcion").getResultList();

    }

}
