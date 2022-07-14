package dds.domain.entities.mascota.mascotaException;

public class LogicMascotaException extends RuntimeException{

    public LogicMascotaException(String mensajeDeError) {
        System.out.println(mensajeDeError);
    }

}
