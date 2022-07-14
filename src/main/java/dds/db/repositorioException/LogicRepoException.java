package dds.db.repositorioException;

public class LogicRepoException extends RuntimeException{

    public LogicRepoException(String mensajeDeError) {
        System.out.println(mensajeDeError);
    }
}
