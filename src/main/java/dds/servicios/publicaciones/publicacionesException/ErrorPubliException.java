package dds.servicios.publicaciones.publicacionesException;

public class ErrorPubliException extends RuntimeException{

    public ErrorPubliException(String mensajeDeError) {
        System.out.println(mensajeDeError);
    }

}
