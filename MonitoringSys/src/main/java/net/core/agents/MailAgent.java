package net.core.agents;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by ANTON on 30.01.2016.
 */
public final class MailAgent {
    private String username;
    private String password;
    private Properties properties;
    public MailAgent(){

    }
    private MailAgent(String username, String password) {
        this.username = username;
        this.password = password;

        properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");
    }
    private void send(String subject, String text, String fromEmail, String toEmail){
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            //от кого
            message.setFrom(new InternetAddress(username));
            //кому
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            //Заголовок письма
            message.setSubject(subject);
            //Содержимое
            message.setText(text);

            //Отправляем сообщение
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    public void standartSender(String text, String mailTo){
        mailTo="anton130794@ya.ru";
        MailAgent sslSender = new MailAgent("testmailncracker@gmail.com", "12341234q");
        sslSender.send("Сбой в системе. MonitoringSystem", text, "testmailncracker@gmail.com",mailTo);
    }
}
