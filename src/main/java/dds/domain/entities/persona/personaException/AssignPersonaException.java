package dds.domain.entities.persona.personaException;

public class AssignPersonaException extends RuntimeException{

    public AssignPersonaException(String mensajeDeError) {
        System.out.println(mensajeDeError);
    }

}
