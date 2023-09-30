package nl.thelastages.website.service;

import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import nl.thelastages.website.configuration.EmailConfiguration;
import nl.thelastages.website.model.dto.NewUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Properties;

@Service
public class UserService implements IUserService{
    EmailConfiguration emailConfiguration;

    @Value("${spring.mail.username}")
    private String userName;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.receiver}")
    private String receiver;


    @Autowired
    JavaMailSender sender;



    public UserService(EmailConfiguration emailConfiguration) {
        this.emailConfiguration = emailConfiguration;
    }

    private static final String MESSAGE = "<html><head></head><body><p>Beste professional, <br><br>\n" +
            "    Dankjewel voor je aanmelding bij WageBuddy.\n" +
            "    Een van onze collega's probeert je vandaag te bellen. Wil je liever een voorkeurstijd doorgeven?\n" +
            "    Stuur gewoon een mailtje naar info@wagebuddy.nl.<br><br>\n" +
            "    Vriendelijke groet,<br><br>\n" +
            "    Team WageBuddy\n" +
            "</p></body></html>";

    private static final String ENCODE = "text/html; charset=UTF-8";

    public Boolean addEmail(NewUserDTO dto) {
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp");
            props.setProperty("mail.host", "smtp.hostnet.nl");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.starttls.enabled", true);

            Session session = Session.getDefaultInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication(){
                    return new PasswordAuthentication(userName, password);
                }
            });
            try{
                MimeMessage msg = new MimeMessage(session);
                msg.setFrom("info@thelastages.com");
                msg.setRecipients(Message.RecipientType.TO,
                        dto.getEmailAddress());
                msg.setSubject("Bedankt voor je aanmelding bij WageBuddy!");
                msg.setSentDate(new Date());
                msg.setText(MESSAGE);
                msg.setHeader("Content-Type", ENCODE);
                Transport.send(msg);
                sendMail(session, dto);
                return true;
            }catch (MessagingException mex){
                System.out.println("send failed, exception: " + mex);
                return false;
            }
    }

    private void sendMail(Session session, NewUserDTO dto) throws MessagingException {
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom("info@thelastages.com");
        msg.setRecipients(Message.RecipientType.TO, receiver);
        msg.setSubject("Nieuwe aanmelding voor WageBuddy");
        msg.setSentDate(new Date());
        msg.setText(dto.getName() +" " + dto.getEmailAddress() +  " " + dto.getPhone());
        msg.setHeader("Content-Type", ENCODE);
        Transport.send(msg);
    }

}
