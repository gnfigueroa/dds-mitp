package dds.servicios.avisos;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class AdapterEmail implements AdapterFormaNotificacion{

    @Override
    public void notificar(String mensaje,Contacto contacto) {
        mensaje = mensaje.replaceAll("\\n","<br/>");
        sendAsHtml(contacto.getEmail(),
                "Rescate de patitas",
                "<h2>Rescate de Patitas</h2><p>"+mensaje+"</p>");
    }

    private static final String senderEmail = "dds2021.grupo15@gmail.com";//change with your sender email
    private static final String senderPassword = "frbautneduar2021";//change with your sender password

    public static void sendAsHtml(String to, String title, String html) {
        //System.out.println("Sending email to " + to);

        Session session = createSession();

        //create message using session
        MimeMessage message = new MimeMessage(session);
        try {
            prepareEmailMessage(message, to, title, html);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        //sending message
        try {
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        //System.out.println("Done");
    }

    private static void prepareEmailMessage(MimeMessage message, String to, String title, String html)
            throws MessagingException {
        message.setContent(html, "text/html; charset=utf-8");
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(title);
    }

    private static Session createSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");//Outgoing server requires authentication
        props.put("mail.smtp.starttls.enable", "true");//TLS must be activated
        props.put("mail.smtp.host", "smtp.gmail.com"); //Outgoing server (SMTP) - change it to your SMTP server
        props.put("mail.smtp.port", "587");//Outgoing port

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
        return session;
    }

}
