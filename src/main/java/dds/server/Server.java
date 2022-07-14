package dds.server;


import dds.servicios.avisos.NotificadorSemanal;
import spark.Spark;


public class Server {
    public static void main(String[] args) {

        Spark.port(getHerokuAssignedPort());
        Router.init();
        NotificadorSemanal.getNotificadorSemanalHelper().notificar();
        //DebugScreen.enableDebugScreen(); //solo se usa en ambiente de trabajo a la hora de presentar vuela
    }
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 9000; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}