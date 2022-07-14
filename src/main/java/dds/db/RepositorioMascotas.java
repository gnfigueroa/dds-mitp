package dds.db;


import dds.db.repositorioException.LogicRepoException;
import dds.domain.entities.mascota.Mascota;
import dds.domain.entities.persona.Persona;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioMascotas {

    private static RepositorioMascotas repositorioMascotas = new RepositorioMascotas() ;

    public static RepositorioMascotas getRepositorio() {return repositorioMascotas;}

    public List<Mascota> getMascotas() {
        return  (List<Mascota>) EntityManagerHelper.getEntityManager().createQuery("from Mascota").getResultList();
    }

    public List<Mascota> getMascotasXPersonaId(String idPersona){
        if(RepositorioPersonas.getRepositorio().esIDValido(idPersona)){
            String jql = "Select m from Persona p, Mascota m where p.idPersona = :idPersona";
            List<Mascota> mascotas  =  (List<Mascota>) EntityManagerHelper.getEntityManager().createQuery(jql).
                    setParameter("idPersona",idPersona).getResultList();
            return  mascotas;

        }else {
            throw new LogicRepoException("IdPersona inexistente");
        }
    }

    public Mascota getMascota(String id) {
        if(esIDValido(id)){
            return EntityManagerHelper.getEntityManager().find(Mascota.class, id) ;
        }else{
            throw new LogicRepoException("Id Mascota Inexistente");
        }
    }
    public List<Mascota> getMascotasPorListaId(List<String>ids) {
        List<Mascota> mascotas = new ArrayList<>();
        for (String id:ids){
            mascotas.add(this.getMascota(id));
        }
        return mascotas;
    }

    public boolean esIDValido(String ID) {
        return (EntityManagerHelper.getEntityManager().find(Mascota.class, ID) != null) ;
    }



}
