package dds.servicios.apiHogares;

import com.google.gson.Gson;
import dds.db.RepositorioHogaresDeTransito;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;


public class ServicioHogarDeTransito {


    private static ServicioHogarDeTransito servicioHogarDeTransito = new ServicioHogarDeTransito() ;

    public static ServicioHogarDeTransito getInstance() {return servicioHogarDeTransito;}

    private final String TOKEN = "vZ1FyLA96SztFwBa0EyApB9qS5EGqfcsyQDzaNxPi8OZJXA1GqqixFx3XRYM";


    public String RegistrarEmail(String mail) {
        //Esta variable res la usaremos únicamente para dar un respuesta final
        String token;
        String URL = "https://api.refugiosdds.com.ar/api/";
        String email = mail;

        try {
            //Creamos el cliente de conexión al API Restful
            Client client = ClientBuilder.newClient();

            //Creamos el target lo cuál es nuestra URL junto con el nombre del método a llamar
            WebTarget target = client.target(URL + "usuarios");

            //Creamos nuestra solicitud que realizará el request
            Invocation.Builder solicitud = target.request().header("accept","application/json");

            //Creamos y llenamos nuestro objeto BaseReq con los datos que solicita el API
            ReqBase req = new ReqBase(email);

            //Convertimos el objeto req a un json
            Gson gson = new Gson();
            String jsonString = gson.toJson(req);
            System.out.println(jsonString);

            //Enviamos nuestro json vía post al API Restful
            Response post = solicitud.post(Entity.json(jsonString));


            //Recibimos la respuesta y la leemos en una clase de tipo String, en caso de que el json sea tipo json y no string, debemos usar la clase de tipo JsonObject.class en lugar de String.class
            String responseJson = post.readEntity(String.class);

            //Imprimimos el status de la solicitud
            System.out.println("Estatus: " + post.getStatus());

            switch (post.getStatus()) {
                case 200:
                    BearerToken bearer = gson.fromJson(responseJson, BearerToken.class);
                    token = bearer.getBearer_token();
                    break;
                case 409:
                    token = "UsuarioYaIngresado";
                    break;
                case 422:
                    token = "MailInvalido";
                    break;
                default:
                    token = "Error";
                    break;
            }

        } catch (Exception e) {
            //En caso de un error en la solicitud, llenaremos res con la exceptión para verificar que sucedió
            token = e.toString();
        }
        //Imprimimos la respuesta del API Restful
        System.out.println(token);
        return token;
    }

    public String obtenerHogares(int offset, String token) {
        //Esta variable res la usaremos únicamente para dar un respuesta final
        String res;
        String URL = "https://api.refugiosdds.com.ar/api/";
        Mensaje msj;
        RespuestaApiHogares respuestaApiHogares;

        try {
            //Creamos el cliente de conexión al API Restful
            Client client = ClientBuilder.newClient();

            //Creamos el target lo cuál es nuestra URL junto con el nombre del método a llamar
            WebTarget target = client.target(URL + "hogares?offset="+offset);

            //Creamos nuestra solicitud que realizará el request
            Invocation.Builder solicitud = target.request().header("accept","aplication/json").header("Authorization","Bearer "+token);

            //Creamos objeto gson para las respuestas JSON.
            Gson gson = new Gson();

            //Enviamos nuestro json vía post al API Restful
            Response get = solicitud.get();


            //Recibimos la respuesta y la leemos en una clase de tipo String, en caso de que el json sea tipo json y no string, debemos usar la clase de tipo JsonObject.class en lugar de String.class
            String responseJson = get.readEntity(String.class);

            //Imprimimos el status de la solicitud
            System.out.println("Estatus: " + get.getStatus());

            switch (get.getStatus()) {
                case 200:
                    respuestaApiHogares = gson.fromJson(responseJson, RespuestaApiHogares.class);
                    RepositorioHogaresDeTransito.getRepositorio().getHogares().addAll(respuestaApiHogares.getHogares());

                    res=" ";
                    break;
                case 400:
                    res = "Ha superado el limite de paginas";
                    break;
                case 401:
                    //Message message = gson.fromJson(jsonString, Message.class);
                    msj = gson.fromJson(responseJson, Mensaje.class);
                    res= msj.getMessage();
                    break;
                default:
                    res = "Error";
                    break;
            }

        } catch (Exception e) {
            //En caso de un error en la solicitud, llenaremos res con la exceptión para verificar que sucedió
            res = e.toString();
        }
        //Imprimimos la respuesta del API Restful
        System.out.println(res);
        return res;
    }

    public void actualizarRepositorioHogaresDeTransito(){
        RepositorioHogaresDeTransito.getRepositorio().getHogares().clear();
        int i = 1;
        String res = this.obtenerHogares(i,TOKEN);

        while(!res.equals("Ha superado el limite de paginas")){
            i++;
            res = this.obtenerHogares(i,TOKEN);
        }
    }

}
