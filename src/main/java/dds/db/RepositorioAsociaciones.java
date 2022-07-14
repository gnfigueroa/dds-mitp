package dds.db;

import dds.db.repositorioException.LogicRepoException;
import dds.domain.entities.asociacion.Asociacion;
import dds.servicios.helpers.CalcDistanciaHelper;
import dds.servicios.publicaciones.PublicacionMascota;

import java.util.Comparator;
import java.util.List;


public class RepositorioAsociaciones {



    private static RepositorioAsociaciones repositorioAsociaciones = new RepositorioAsociaciones() ;

    public static RepositorioAsociaciones getRepositorio() {
        return repositorioAsociaciones;
    }

    public void getAsociacionXMascota(String idMascota){
        //TODO la asociacion esta encargada de esto?
    }

    public List<Asociacion> getAsociaciones() {
        return (List<Asociacion>) EntityManagerHelper.getEntityManager().createQuery("from Asociacion").getResultList();
    }


    public Asociacion getAsociacion(int idAsoc) {
        if(esIDValido(idAsoc)){
            return EntityManagerHelper.getEntityManager().find(Asociacion.class, idAsoc) ;
        }else{
            throw new LogicRepoException("Id asociacion Inexistente");
        }
    }

    public int getIDAsocXIdPublicacion(String idPublicacion) {
        if (EntityManagerHelper.getEntityManager().find(PublicacionMascota.class, idPublicacion) != null) {
            String jql = "Select a.idAsociacion from Asociacion a, Publicador publi, PublicacionMascota p where p.idPublicacion = :idPublicacion";
            int idAsoc = (int) EntityManagerHelper.getEntityManager().createQuery(jql).
                    setParameter("idPublicacion", idPublicacion).getResultList().get(0);
            return idAsoc;
        } else {
            throw new LogicRepoException("Id Publicacion Incorrecta");
        }
    }
    public Asociacion getAsociacionMasCercana(double latitud,double longitud){
        List<Asociacion> asociaciones = (List<Asociacion>)EntityManagerHelper.getEntityManager().createQuery("FROM Asociacion ").getResultList();

        Asociacion asoc = asociaciones.stream()
                .min(Comparator.comparingDouble(a->CalcDistanciaHelper.getHelper().distanciaCoord(a.getUbicacion().getLat(), a.getUbicacion().getLongitud(), latitud, longitud))).orElse(null);
        return asoc;
    }

    public void agregarAsociacion(Asociacion asoc){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.entityManager().persist(asoc);
        EntityManagerHelper.commit();
    }
    public void eliminarAsociacion(Asociacion asoc){
        if(this.esIDValido(asoc.getIdAsociacion())){
            EntityManagerHelper.beginTransaction();
            EntityManagerHelper.entityManager().remove(asoc);
            EntityManagerHelper.commit();
        }else{
            throw new LogicRepoException("Id asociacion Inexistente");
        }

    }

    public boolean esIDValido(int ID) {
        return (EntityManagerHelper.getEntityManager().find(Asociacion.class, ID) != null) ;
    }

}
