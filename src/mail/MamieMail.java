/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mail;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author maillot
 */
public class MamieMail {

    private final Folder inbox;
    private final Store store;
    
    private static String getAdresse() throws FileNotFoundException {
        try (Scanner in = new Scanner(new File(".adresse"))) {
            return in.next();
        }
    }
    private static String getMdp() throws FileNotFoundException {
        try (Scanner in = new Scanner(new File(".passwd"))) {
            return in.next();
        }
    }

    public MamieMail() throws MessagingException, FileNotFoundException {
        Properties props = new Properties();

        //props.load(new FileInputStream(new File("C:\\smtp.properties")));
        Session session = Session.getDefaultInstance(props, null);

        store = session.getStore("imaps");        

        store.connect("smtp.gmail.com", getAdresse(), getMdp());

        inbox = store.getFolder("inbox");
        inbox.open(Folder.READ_ONLY);

    }

    /**
     *
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        try {
            inbox.close(true);
            store.close();
        } finally {
            super.finalize();
        }
    }

    public void close() {
        try {
            inbox.close(true);
            store.close();
        } catch (MessagingException ex) {
        }
    }

    public Message getLastMessagesAfter(String subject, Date date) {
        try {
            Message[] message = inbox.getMessages();
            int i = message.length - 1;
            while (i >= 0 && message[i].getReceivedDate().after(date)) {
                if (message[i].getSubject().equals(subject)) {
                    return message[i];
                }
                --i;
            }
            return null;
        } catch (MessagingException ex) {
            throw new IllegalStateException();
        }
    }

    public Message getLastMessages(String subject) {
        try {
            Message[] message = inbox.getMessages();
            for (int i = message.length - 1; i >= 0; --i) {
                if (message[i].getSubject().equals(subject)) {
                    return message[i];
                }
            }
            return null;
        } catch (MessagingException ex) {
            throw new IllegalStateException();
        }
    }

    public Message getLastMessages() {
        try {
            return inbox.getMessages()[inbox.getMessageCount() - 1];
        } catch (MessagingException ex) {
            throw new IllegalStateException();
        }
    }

    public Message[] getMessages() {
        try {
            return inbox.getMessages();
        } catch (MessagingException ex) {
            throw new IllegalStateException();
        }
    }

    public static void send(String subject) throws FileNotFoundException {
        send(subject, "");
    }

    public static void send(String subject, String text) throws FileNotFoundException {
        final String username = getAdresse();
        final String password = getMdp();

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(getAdresse()));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(getAdresse()));
            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
