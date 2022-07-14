package dds.db;



import dds.db.repositorioException.LogicRepoException;
import dds.domain.entities.persona.Persona;


import java.util.List;

public class RepositorioPersonas {


    private static RepositorioPersonas repositorioPersonas = new RepositorioPersonas() ;

    public static RepositorioPersonas getRepositorio() {return repositorioPersonas;}


    public List<Persona> getPersonas() {
        return (List<Persona>) EntityManagerHelper.getEntityManager().createQuery("from Persona").getResultList();
    }

    public Persona getPersona(String idPersona) {

        if(esIDValido(idPersona)){
            return EntityManagerHelper.getEntityManager().find(Persona.class, idPersona) ;
        }else{
            throw new LogicRepoException("Id Persona Inexistente");
        }
    }

    public String getIdPersonaXidMascota(String idMascota){
        if(RepositorioMascotas.getRepositorio().esIDValido(idMascota)){
            String jql = "Select p from Persona p, Mascota m where m.idMascota = :idMascota";
            Persona persona = (Persona) EntityManagerHelper.getEntityManager().createQuery(jql).
                    setParameter("idMascota",idMascota).getResultList().get(0);
            return  persona.getIdPersona();

        }else {
            throw new LogicRepoException("IdMascota inexistente");
        }
    }


        public boolean esIDValido(String ID) {
        return (EntityManagerHelper.getEntityManager().find(Persona.class, ID) != null) ;
    }



}
