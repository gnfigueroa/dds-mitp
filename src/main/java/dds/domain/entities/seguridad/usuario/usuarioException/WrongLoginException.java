package dds.domain.entities.seguridad.usuario.usuarioException;

public class WrongLoginException extends RuntimeException{

    public WrongLoginException(String mensajeDeError) {
        System.out.println(mensajeDeError);
    }
}
