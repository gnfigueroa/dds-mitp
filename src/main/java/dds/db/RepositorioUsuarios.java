package dds.db;

import dds.db.repositorioException.LogicRepoException;
import dds.domain.entities.persona.Persona;
import dds.domain.entities.seguridad.usuario.Usuario;

import java.util.List;

public class RepositorioUsuarios {

    private static RepositorioUsuarios repositorioUsuarios = new RepositorioUsuarios() ;

    public static RepositorioUsuarios getRepositorio() {return repositorioUsuarios;}

    public void agregarUsuario(Usuario usuario) {
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.entityManager().merge(usuario);
        EntityManagerHelper.commit();
    }

    public List<Usuario> getUsuarios() {
        return (List<Usuario>) EntityManagerHelper.getEntityManager().createQuery("from Usuario").getResultList();
    }


    public Usuario getUsuario(String username) {
        if(esIDValido(username)){
            return EntityManagerHelper.getEntityManager().find(Usuario.class, username) ;
        }else{
            throw new LogicRepoException("Id Usuario Inexistente");
        }
    }

    public String getUserNameXIdPersona(String idPersona) {
        if(RepositorioPersonas.getRepositorio().esIDValido(idPersona)){
            String jql = "Select u from Usuario u, Persona p where p.idPersona = :idPersona";
            Usuario usuario = (Usuario) EntityManagerHelper.getEntityManager().createQuery(jql).
                    setParameter("idPersona",idPersona).getResultList().get(0);
            return  usuario.getUserName();
        }else{
            throw new LogicRepoException("idPersona Incorrecto");
        }
    }

    public int getIDAsocXIdPersona(String idPersona) {
        String userName = getUserNameXIdPersona(idPersona);
        Usuario usuario1 = getUsuario(userName);
        if (usuario1 == null) {
            throw new LogicRepoException("idUsuario Incorrecto");
        }
        return usuario1.getAsociacion().getIdAsociacion();

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
    public int getIDAsocXIdMascota(String idMascota) {


        String idPersona = RepositorioPersonas.getRepositorio().getIdPersonaXidMascota(idMascota);
        String userName = this.getUserNameXIdPersona(idPersona);
        Usuario usuario1 = getUsuario(userName);
        if (usuario1 == null) {
            throw new LogicRepoException("userName Incorrecto");
        }
        return usuario1.getAsociacion().getIdAsociacion();


    }
    public boolean esIDValido(String ID) {
        return (EntityManagerHelper.getEntityManager().find(Usuario.class, ID) != null) ;
    }


}
