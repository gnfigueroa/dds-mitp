package dds.server;


import dds.domain.controllers.*;

import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;
import dds.spark.utils.BooleanHelper;
import dds.spark.utils.HandlebarsTemplateEngineBuilder;

import javax.naming.ldap.Control;
import java.security.NoSuchAlgorithmException;

public class Router {
    private static HandlebarsTemplateEngine engine;

    private static void initEngine() { //construimos instancia de hbs
        Router.engine = HandlebarsTemplateEngineBuilder
                .create()
                .withDefaultHelpers()
                .withHelper("isTrue", BooleanHelper.isTrue)
                .build();
    }

    public static void init() {
        Router.initEngine();
        Spark.staticFileLocation("/public");
        Router.configure();
    }

    private static void configure() {
        //Spark.get("/",(req,res)->"hola");  //+ req.queryParams("nombre") + " " +req.queryParams("apellido")
        // localhost:9000/?nombre=sarasa&apellido=sarasa2

        //Spark.get("/usuario/:id", (req,res)->req.params("id"));  //req.splat()[0] devuelve array de asteriscos
        ControllerContacto controllerContacto = new ControllerContacto();
        ControllerConfigurarCaracteristicasAsociacion controllerConfigurarCaracteristicasAsociacion = new ControllerConfigurarCaracteristicasAsociacion();
        ControllerEncontreMascotaConChapita controllerEncontreMascotaConChapita = new ControllerEncontreMascotaConChapita();
        ControllerEncontreMascotaSinChapita controllerEncontreMascotaSinChapita = new ControllerEncontreMascotaSinChapita();
        ControllerFAQ controllerFAQ = new ControllerFAQ();
        ControllerNosotros controllerNosotros = new ControllerNosotros();
        ControllerNoTengoAlma controllerNoTengoAlma = new ControllerNoTengoAlma();
        ControllerRegistroUsuario controllerRegistroUsuario = new ControllerRegistroUsuario();
        ControllerPanel controllerPanel = new ControllerPanel();
        AdopcionController adopcionController = new AdopcionController();
        IndexController indexController = new IndexController();
        ControllerLogin controllerLogin = new ControllerLogin();
        ControllerRegistroMascota controllerRegistroMascota = new ControllerRegistroMascota();
        ControllerAgregarContacto controllerAgregarContacto = new ControllerAgregarContacto();
        ControllerEvaluarPublicacion controllerEvaluarPublicacion = new ControllerEvaluarPublicacion();
        ControllerPanelAdopcion controllerPanelAdopcion = new ControllerPanelAdopcion();
        ControllerContactar controllerContactar = new ControllerContactar();
        ControllerPublicacion controllerPublicacion = new ControllerPublicacion();
        ControllerMascotasPerdidas controllerMascotasPerdidas = new ControllerMascotasPerdidas();
        ControllerPerfilMascota controllerPerfilMascota = new ControllerPerfilMascota();
        //SPARK GETS
        //Spark.get("/saludar",usuarioController::mostarComandera, Router.engine); //usa :: para invocar al metodo pero desde la ruta y no desde el router

        Spark.get("/adopcion", adopcionController::mostrarMascotas, Router.engine);
        Spark.get("/adopcion/:page", adopcionController::mostrarPagina, Router.engine);
        Spark.get("/", indexController::mostrarIndex, Router.engine);
        Spark.get("/nosotros", controllerNosotros::mostrarNosotros, Router.engine);
        Spark.get("/FAQs", controllerFAQ::mostrarFAQ, Router.engine);
        Spark.get("/contacto", controllerContacto::mostrarContacto, Router.engine);
        Spark.get("/login", controllerLogin::login, Router.engine);
        Spark.get("/encontreMascotaConChapita", controllerEncontreMascotaConChapita::mostrarMascotaConChapita, Router.engine);
        Spark.get("/encontreMascotaSinChapita", controllerEncontreMascotaSinChapita::mostrarEncontreMascotaSinChapita, Router.engine);
        Spark.get("/noTengoAlma", controllerNoTengoAlma::mostrarNoTengoAlma, Router.engine);
        Spark.get("/noTengoAlma/:idMascota", controllerNoTengoAlma::mascotaPerdidaEncontrada);
        Spark.get("/configurarCaracteristicasAsociacion", controllerConfigurarCaracteristicasAsociacion::mostrarConfigurarCaracteristicasAsociacion, Router.engine);
        Spark.get("/crearCuenta", controllerRegistroUsuario::mostrarRegistroUsuario, Router.engine);
        Spark.get("/panel", controllerPanel::mostrarPanel, Router.engine);
        Spark.get("/logout", controllerLogin::logOut, Router.engine);
        Spark.get("/evaluarPublicacion", controllerEvaluarPublicacion::mostrarPublicaciones, Router.engine);
        Spark.get("/panelAdopcion/:idMascota", controllerPanelAdopcion::mostrarPanel, Router.engine);
        Spark.get("/contactarPersona/:id",controllerContactar::contactarPersona,Router.engine);
        Spark.get("/publicacionPrivada/:idPublicacion", controllerPublicacion::mostrarPublicacionPrivada, Router.engine);
        //Spark.get("/enviarAHogarDeTransito", controllerEncontreMascotaSinChapita::mostrarHogaresDeTransito, Router.engine) ;
        Spark.get("/mascotasPerdidas", controllerMascotasPerdidas::mostrarMascotasPerdidas, Router.engine);
        Spark.get("/mascotasPerdidas/:page", controllerMascotasPerdidas::mostrarPagina, Router.engine);
        Spark.get("/perfilMascota/:id", controllerPerfilMascota::mostrarPerfilMascota, Router.engine);
        Spark.post("/login", (request, response) -> {
            try {
                return controllerLogin.login(request, response);
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return request;
        });

        Spark.post("/crearCuenta", controllerRegistroUsuario::crearUsuario, Router.engine);
        Spark.post("/registroMascota", controllerRegistroMascota::registrarMascota);
        Spark.post("/agregarFoto/:id", controllerRegistroMascota::agregarFoto);
        Spark.post("/agregarContacto", controllerAgregarContacto::agregarContacto);
        Spark.get("/verContactos", controllerAgregarContacto::mostrarVerContactos, Router.engine);
        Spark.post("/configurarCaracteristicasAsociacion", controllerConfigurarCaracteristicasAsociacion::modificarCaracteristicas, Router.engine);
        Spark.post("/panelAdopcion/:idMascota", controllerPanelAdopcion::ponerEnAdopcion);
        Spark.post("/crearMascotaPerdidaSinChapita",controllerEncontreMascotaSinChapita::crearMascotaPerdidaSinChapita, Router.engine);
        Spark.post("/crearPublicacionMascotaPerdidaConChapita",controllerEncontreMascotaConChapita::crearPublicacionMascotaPerdidaConChapita);
        Spark.post("/aprobarRechazarPublicacion",controllerEvaluarPublicacion::aprobarRechazarPublicacion);
    }

}