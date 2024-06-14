package org.example.onlineexchange;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailSender {

    private final Session session;
    private final String senderUsername;
    private final String senderPassword;

    public EmailSender(String senderUsername, String senderPassword){
        // اطلاعات جیمیل
        this.senderUsername = senderUsername;
        this.senderPassword = senderPassword;

        // تنظیمات SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // ایجاد نشست
        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderUsername, senderPassword);
                    }
                });
    }


    public void send(String subject, String text, String recipientEmail) throws MessagingException {
        // ایجاد پیام
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderUsername));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject(subject);
        message.setText(text);

        // ارسال پیام
        Transport.send(message);

    }

    public Session getSession() {
        return session;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getSenderPassword() {
        return senderPassword;
    }
}