package com.learntodroid.simplealarmclock.mail;

import android.content.Context;
import android.os.AsyncTask;

import java.util.Arrays;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailAPI extends AsyncTask<Void, Void, Void> {

    private Context context;


    public static final  String EMAIL = "dungpv10001@gmail.com";
    public static final String PASSWORD = "Dung1012001@";

    private Session session;
    private String email, subject, message;

    public JavaMailAPI(Context context, String email, String subject, String message) {
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.zoho.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL, PASSWORD);
            }
        });

        String[] to = email.split(",");

        for (String toMail : to) {
            MimeMessage mimeMessage = new MimeMessage(session);
            try {
                mimeMessage.setFrom(new InternetAddress("mosan_cms@zohomail.com"));
                mimeMessage.setRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(toMail.trim())));
                mimeMessage.setSubject(subject);
                mimeMessage.setText(message);
                Transport.send(mimeMessage);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

        return null;

    }
}
