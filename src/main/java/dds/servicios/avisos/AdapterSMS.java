package dds.servicios.avisos;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class AdapterSMS implements AdapterFormaNotificacion{



    @Override
    public void notificar(String mensaje,Contacto contacto) {
        // Find your Account Sid and Token at twilio.com/user/account
        String ACCOUNT_SID = "AC0e930b6c51a527c0a83a7bbe5fe4ee80";
        String AUTH_TOKEN = "07cf1b839c8bfda71bedb2e458da9ed6";


        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(new PhoneNumber("+54"+contacto.getTelefono()),
                new PhoneNumber("+14156826570"),
                mensaje).create();

        //System.out.println(message.getSid());

    }



}
