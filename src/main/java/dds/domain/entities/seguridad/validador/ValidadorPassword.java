package dds.domain.entities.seguridad.validador;

import dds.domain.entities.seguridad.usuario.Usuario;
import dds.domain.entities.seguridad.usuario.usuarioException.EasyPasswordException;
import dds.servicios.helpers.HashHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidadorPassword {
    private final String yourDesktopPath = System.getProperty("user.dir")+"/";
    private static ValidadorPassword validadorPassword = new ValidadorPassword();

    public static ValidadorPassword getValidadorPassword() {
        return validadorPassword;
    }

    public void validarPassword(String password, Usuario usuario) throws NoSuchAlgorithmException {
        validarPassword10k(password);
        validarCredenciales(password,usuario);
        validarPasswordRegex(password);
        validarLast5Passwords(password,usuario);
    }

    public void validarPasswordRegex(String password){
        Pattern pattern;
        Matcher matcher;
        // Regex: Longitud entre 8 y 16
        //        Al menos: 1 Mayuscula, 1 Minuscula, 1 Simbolo y 1 Numero
        final String PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[@#$%^&+=!,_?-])(?=.*[A-Z])(?=.*[a-z])\\S{8,16}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new EasyPasswordException("La contraseña no cumple los requsitos");
        }
    }

    // este seria el metodo que validaria contra las 10k de contraseñas, faltaria agregar los otros requerimientos de la OWASP
    public void validarPassword10k(String password)  {

        Scanner inputFile = null;
        try {
            inputFile = new Scanner(new File(yourDesktopPath + "10k-worst-password.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Objects.requireNonNull(inputFile).useDelimiter("[\\r\\n]");
        while (inputFile.hasNext()) {
            String wrongPassword = inputFile.next();
            //System.out.print(wrongPassword);
            if (password.equals(wrongPassword))              {
                throw new EasyPasswordException("La contraseña esta entre las 10k mas faciles");
            }
        }
        inputFile.close();
    }

    public void validarCredenciales(String password, Usuario usuario) {
        if (password.equals(usuario.getUserName())) {
            throw new EasyPasswordException("La contraseña tiene problemas de credenciales (no debe ser la misma que su nombre de usuario)");
        }

    }


    public void validarLast5Passwords(String password, Usuario usuario) throws NoSuchAlgorithmException {
        String passwordHasheada = HashHelper.getHashHelper().passwordAMD5(password);
        if(usuario.lastPasswords().contains(passwordHasheada)){
            throw new EasyPasswordException("La contraseña ingresada es igual a una previa");
        }

    }
}
