package dds.domain.entities.seguridad.validador;

import dds.db.EntityManagerHelper;
import dds.db.RepositorioUsuarios;
import dds.domain.entities.seguridad.usuario.Usuario;
import dds.domain.entities.seguridad.usuario.usuarioException.WrongLoginException;
import dds.servicios.helpers.HashHelper;

import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;

public class ValidadorUsuario {
    private static ValidadorUsuario validadorUsuario = new ValidadorUsuario();

    public static ValidadorUsuario getValidadorUsuario() { return  validadorUsuario;}

    public Boolean validarIdentidad(String userName, String password) throws NoSuchAlgorithmException {

        String passwordHasheada = HashHelper.getHashHelper().passwordAMD5(password);

        usuarioInexistente(userName);
        Usuario usuario = RepositorioUsuarios.getRepositorio().getUsuarios().stream().filter(u -> u.getUserName().equals(userName)).findFirst().orElse(null);
        usuarioBloqueado(usuario);
        passwordVencida(usuario);
        passwordErronea(usuario,passwordHasheada);
        usuario.setIntentosFallidos(0);
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.entityManager().merge(usuario);
        EntityManagerHelper.commit();
        return true;

    }
    public void usuarioInexistente(String userName){
        if(!RepositorioUsuarios.getRepositorio().getUsuarios().stream().map(Usuario::getUserName).collect(Collectors.toList()).contains(userName)) {
            throw new WrongLoginException("El usuario ingresado no existe");
        }
    }

    public void usuarioBloqueado(Usuario usuario) {
        if(usuario.estaBloqueado()){
            throw new WrongLoginException("El usuario esta bloqueado");
        }
    }

    public void passwordVencida(Usuario usuario) {
        if(usuario.passwordVencida()){
            throw new WrongLoginException("La contraseña esta vencida.");
        }
    }

    public void passwordErronea(Usuario usuario, String passwordHasheada) {
        if (!RepositorioUsuarios.getRepositorio().getUsuarios().stream().map(Usuario::getPassword).collect(Collectors.toList()).contains(passwordHasheada)) {
            usuario.sumaIntentoFallido();
            usuario.verificarIntentosFallidos();
            throw new WrongLoginException("La contraseña ingresada no coincide");
        }
    }


}
