package dds.domain.entities.seguridad.validador;

import dds.db.EntityManagerHelper;
import dds.db.RepositorioUsuarios;
import dds.domain.entities.seguridad.usuario.Usuario;
import dds.domain.entities.seguridad.usuario.usuarioException.WrongLoginException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ValidadorUsuarioTest {


        Usuario usuarioCreado;

        @Before
        public void setup() throws NoSuchAlgorithmException {

            usuarioCreado = new Usuario("usuarioTest","Password123+");

            if (EntityManagerHelper.getEntityManager().find(Usuario.class, usuarioCreado.getUserName()) == null){
                EntityManagerHelper.beginTransaction();
                EntityManagerHelper.entityManager().persist(usuarioCreado);
                EntityManagerHelper.commit();
            } else
            {
                usuarioCreado = (Usuario) EntityManagerHelper.getEntityManager().createQuery("from Usuario").getResultList().get(0);
            }
        }

        @Test
        public void A_testValidarIdentidad() throws NoSuchAlgorithmException {

            String username = usuarioCreado.getUserName();
            Assert.assertTrue(ValidadorUsuario.getValidadorUsuario().validarIdentidad(username,"Password123+"));
            //En validarIdentidad la contraseña que recibe dicho método es la que ingresa el usuario
            //y la que se persiste en el repositorio se encuentra hasheada, por lo tanto la recibe
            //en una cadena.
        }
        @Test (expected = WrongLoginException.class)
        public void B_testValidarIdentidadErrorContraseniaNoCoincide() throws NoSuchAlgorithmException{
            String username = usuarioCreado.getUserName();
            ValidadorUsuario.getValidadorUsuario().validarIdentidad(username,"sarasa");
        }
        @Test (expected = WrongLoginException.class)
        public void C_testValidarIdentidadUsuarioInexistente() throws NoSuchAlgorithmException{
            ValidadorUsuario.getValidadorUsuario().validarIdentidad("pepito","");
        }

        @Test (expected = WrongLoginException.class)
        public void D_testValidarContraseniaVencida() throws NoSuchAlgorithmException {
            RepositorioUsuarios.getRepositorio().getUsuario("usuarioTest").setLastPasswordDT(LocalDateTime.now(ZoneOffset.UTC).minusDays(31));
            ValidadorUsuario.getValidadorUsuario().validarIdentidad(usuarioCreado.getUserName(),"Password123+");
        }
        @Test (expected = WrongLoginException.class)
        public void E_testValidarIdentidadUsuarioBloqueado() throws NoSuchAlgorithmException{
        usuarioCreado.bloquear();
        ValidadorUsuario.getValidadorUsuario().validarIdentidad(usuarioCreado.getUserName(),"Password123+");

    }


}