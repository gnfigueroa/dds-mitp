package dds.domain.entities.seguridad.validador;

import dds.domain.entities.seguridad.usuario.Usuario;
import dds.domain.entities.seguridad.usuario.usuarioException.EasyPasswordException;
import dds.servicios.helpers.HashHelper;
import org.junit.Assert;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

public class ValidadorPasswordTest {

    Usuario usuario;

    @Test
    public void validarPasswordOK() throws NoSuchAlgorithmException {
        usuario = new Usuario("usuarioTest","Password123+");
        Assert.assertEquals(usuario.getUserName(),"usuarioTest");
    }

    @Test (expected = EasyPasswordException.class)
    public void validarPasswordErrorEasy10K() throws NoSuchAlgorithmException {
        usuario = new Usuario("usuarioTest","12345678");
    }

    @Test (expected = EasyPasswordException.class)
    public void validarPasswordErrorEasyRegex() throws NoSuchAlgorithmException {
        usuario = new Usuario("usuarioTest","Hjdsod54cd");
    }

    @Test
    public void validarLast5Passwords() throws NoSuchAlgorithmException {
        usuario = new Usuario("usuarioTest","Password123+");
        usuario.changePassword("Password1234+");
        usuario.changePassword("Password12345+");
        usuario.changePassword("Password12346+");
        usuario.changePassword("Password12347+");
        Assert.assertEquals(usuario.getPassword(), HashHelper.getHashHelper().passwordAMD5("Password12347+"));
    }

    @Test (expected = EasyPasswordException.class)
    public void validarLast5PasswordsError() throws NoSuchAlgorithmException {
        usuario = new Usuario("usuarioTest","Password123+");
        usuario.changePassword("Password123+");
    }
}