package dds.servicios.avisos;

import dds.db.RepositorioAsociaciones;
import dds.db.RepositorioPersonas;
import dds.domain.entities.mascota.Mascota;
import dds.domain.entities.persona.Persona;
import dds.servicios.publicaciones.PublicacionAdopcion;
import dds.servicios.publicaciones.PublicacionQuieroAdoptar;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class NotificadorSemanal{

    private static NotificadorSemanal notificadorSemanalHelper = new NotificadorSemanal();

    public static NotificadorSemanal getNotificadorSemanalHelper() { return notificadorSemanalHelper; }

    PreferenciasDeAdopcion preferencias;
    RepositorioPersonas repositorioPersonas;
    private List<Contacto> suscriptores = new ArrayList<>();
    private FormaNotificacion formaNotificacion;
    int cantMinima = 1; //esto tiene que configurarse
    RepositorioAsociaciones repositorioAsociaciones;
    private final ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);

    public void notificar(){

        //Runnable enviarRecomendacion = () -> System.out.println("Notificando...");
        final Runnable enviarRecomendacion = new Runnable() {
            @Override
            public void run() {
                notificarPublicaciones();
            }
        };


        //dejamos en 5 segundos para realizar pruebas
        ScheduledFuture<?> result = ses.scheduleAtFixedRate(enviarRecomendacion,0,20,TimeUnit.SECONDS);
        //ScheduledFuture<?> result = ses.scheduleAtFixedRate(task2,0,7,TimeUnit.DAYS);
        //ses.shutdown(); //tener en cuenta que se utiliza para finalizar el periodo...
    }

    public void notificarPublicaciones(){
        for (int i=0;i<RepositorioAsociaciones.getRepositorio().getAsociaciones().size();i++){
            notificarPublicacionesConCoincidenciaSegun(cantMinima,RepositorioAsociaciones.getRepositorio().getAsociaciones().get(i).getIdAsociacion());
        }
    }

    public void notificarPublicacionesConCoincidenciaSegun(int coincidenciasMinima, int idAsociacion) {
        preferencias = new PreferenciasDeAdopcion();
        List<PublicacionQuieroAdoptar> publicacionQuieroAdoptar= new ArrayList<>();
        List<PublicacionAdopcion> publicacionAdopcion = new ArrayList<>();
        List<PublicacionAdopcion> listaAEnviaraPosibleAdoptante = new ArrayList<>();

        publicacionQuieroAdoptar = preferencias.obtenerPublicacionesAdoptantesSegunAsociacion(idAsociacion);
        publicacionAdopcion = preferencias.obtenerPublicacionesEnAdopcionSegunAsociacion(idAsociacion);

        for (int i=0;i<publicacionQuieroAdoptar.size();i++){
            listaAEnviaraPosibleAdoptante = preferencias.obtenerPublicacionesConCoincidenciaSegunAdoptante(coincidenciasMinima,publicacionQuieroAdoptar.get(i),publicacionAdopcion);
            if (!listaAEnviaraPosibleAdoptante.isEmpty()){
                notificar(publicacionQuieroAdoptar.get(i).getIdAdoptante(),listaAEnviaraPosibleAdoptante);
            }
            ;
        }
    }

    public void notificar(String idPersona,List<PublicacionAdopcion> listaAEnviaraPosibleAdoptante)  {
        Persona adoptante =  RepositorioPersonas.getRepositorio().getPersona(idPersona);
        Persona duenio;
        Mascota mascota;
        String link = "www.patitas.com/IdPublicacion=";//TODO Crear formula en un singleton servicio que genere el link que te lleve a la publicacion de la mascota encontrada.

        String mensaje;
        for(int k=0;k<listaAEnviaraPosibleAdoptante.size();k++){
            //Recupero el duenio
            duenio = RepositorioPersonas.getRepositorio().getPersona(idPersona);
            //Recupero la mascota
            mascota = duenio.getMascota(listaAEnviaraPosibleAdoptante.get(k).getIdMascota());
            mensaje = "Encontramos esta pulicaciones de Mascota: "+ mascota.getNombre() + " para mas informacion ingresa al siguiente link de publicacion!: "+link+listaAEnviaraPosibleAdoptante.get(k).getIdPublicacion();

            for (int i = 0; i<adoptante.getNotificador().getContactos().size(); i++){
                List<FormaNotificacion> formas = adoptante.getNotificador().getContactos().get(i).getFormasNotificacion();
                for (int j=0;j<formas.size();j++) {
                    formas.get(j).notificar(mensaje,adoptante.getNotificador().getContactos().get(i)); //aca paso el suscriptor
                }
            }
        }
    }


}

