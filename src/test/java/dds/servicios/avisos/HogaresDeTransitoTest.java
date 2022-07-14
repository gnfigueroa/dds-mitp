package dds.servicios.avisos;

import dds.db.RepositorioHogaresDeTransito;
import dds.servicios.apiHogares.ServicioHogarDeTransito;
import dds.servicios.apiHogares.HogarDeTransito;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class HogaresDeTransitoTest {
        String mail = "dorrpei@gmail.com";
        String token= "vZ1FyLA96SztFwBa0EyApB9qS5EGqfcsyQDzaNxPi8OZJXA1GqqixFx3XRYM";

        RepositorioHogaresDeTransito repositorioHogaresDeTransito;
        ServicioHogarDeTransito servicioHogarDeTransito = new ServicioHogarDeTransito();


        @Test
        public void usuarioYaIngresadoTest(){
            String res;
            res = servicioHogarDeTransito.RegistrarEmail(mail);
            Assert.assertEquals("UsuarioYaIngresado",res);
        }


        @Test
        public void mailMalIngresadoTest(){
            String res;
            String mail = "gabi@example.com";
            res = servicioHogarDeTransito.RegistrarEmail(mail);
            Assert.assertEquals("MailInvalido",res);
        }

        @Test
        public void usuarioNoAutorizadoTest(){
            String res;
            res = servicioHogarDeTransito.obtenerHogares(2,"token");
            Assert.assertEquals("Unauthenticated.",res);
        }

        @Test
        public void obtenerHogaresTest(){
            String res;
            String nombreHogar;

            res = servicioHogarDeTransito.obtenerHogares(1,token);
            nombreHogar = repositorioHogaresDeTransito.getRepositorio().getHogares().get(0).getNombre();
            Assert.assertEquals("Pensionado de mascotas \"Como en casa\"",nombreHogar);
        }

        @Test
        public void calcularDistanciaTest(){
            String res;
            List<HogarDeTransito> hogarDeTransitos = new ArrayList<>();
            res = servicioHogarDeTransito.obtenerHogares(1,token);
            hogarDeTransitos =  repositorioHogaresDeTransito.getRepositorio().filtrarPorDistancia(-34.42061525423029,-58.572775488348505,10);

        }
}
