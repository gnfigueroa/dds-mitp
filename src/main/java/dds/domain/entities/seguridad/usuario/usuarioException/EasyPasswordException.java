package dds.domain.entities.seguridad.usuario.usuarioException;

public class EasyPasswordException extends RuntimeException{

    public EasyPasswordException(String mensajeDeError) {
        System.out.println(mensajeDeError);
    }

}
