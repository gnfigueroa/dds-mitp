package dds.servicios.avisos;


import dds.db.EntityManagerHelper;
import dds.db.RepositorioPersonas;
import dds.domain.entities.mascota.Mascota;
import dds.domain.entities.persona.Persona;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Notificador {

    @Id
    @GeneratedValue
    private int id;

    public int getId() {
        return id;
    }

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Contacto> contactos = new ArrayList<>();


    public List<Contacto> getContactos() {
        return contactos;
    }

    //agendar
    public void agendarContacto(String nombre, String apellido, String telefono, String email, List<FormaNotificacion> formasDeNoti) {
        Contacto contactoNuevo = new Contacto(nombre, apellido, telefono, email, formasDeNoti);
        contactos.add(contactoNuevo);
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.entityManager().merge(this);
        EntityManagerHelper.commit();

    }

    // modificar()
    public void modificarContacto(Contacto buscado, String nombre, String apellido, String telefono, String email) throws Exception {
        if (buscarContacto(buscado) == -1) {
            throw new Exception("No existe dicho usuario");
        } else {
            contactos.get(buscarContacto(buscado)).setNombre(nombre);
            contactos.get(buscarContacto(buscado)).setApellido(apellido);
            contactos.get(buscarContacto(buscado)).setEmail(email);
            contactos.get(buscarContacto(buscado)).setTelefono(telefono);
            EntityManagerHelper.beginTransaction();
            EntityManagerHelper.entityManager().merge(this);
            EntityManagerHelper.commit();
        }
    }

    public int buscarContacto(Contacto buscado) {
        for (int i = 0; i < contactos.size(); i++) {
            if (buscado.getNombre() == contactos.get(i).getNombre()) {
                return i;
            }
        }
        return -1;
    }

    // eliminar()
    public void eliminarContacto(Contacto eliminar) {
        contactos.remove(buscarContacto(eliminar));
    }

    public void notificar(String idMascota) {
        Persona duenio = RepositorioPersonas.getRepositorio().getPersona(RepositorioPersonas.getRepositorio().getIdPersonaXidMascota(idMascota));
        Mascota mascota = duenio.getMascota(idMascota);
        String path = "https://tpdds15.herokuapp.com/";
        String link = path+"perfilMascota/"+idMascota;//TODO Crear formula en un singleton servicio que genere el link que te lleve a la publicacion de la mascota encontrada.
        String mensaje = "Encontramos a " + mascota.getNombre() + " para mas informacion ingresa al siguiente link!: " + link;
        for (int i = 0; i < contactos.size(); i++) {
            List<FormaNotificacion> formas = contactos.get(i).getFormasNotificacion();
            for (int j = 0; j < formas.size(); j++) {
                formas.get(j).notificar(mensaje, contactos.get(i)); //aca paso el suscriptor
            }
        }
    }


    public void notificarPersona(String mensaje) {
        List<FormaNotificacion> formas = contactos.get(0).getFormasNotificacion();
        for (int j = 0; j < formas.size(); j++) {
            formas.get(j).notificar(mensaje, contactos.get(0)); //aca paso el suscriptor
        }
    }
}
