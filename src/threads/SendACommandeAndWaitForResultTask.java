/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import java.io.FileNotFoundException;
import java.util.Date;
import javafx.concurrent.Task;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import mail.MamieMail;
import static uses.Usefull.conversionEncoding;

/**
 *
 * @author maillot
 */
public class SendACommandeAndWaitForResultTask extends Task<Message> {

    private final Date date;
    //private final String command;
    private final String attempting;
    private boolean finished = false;
    private final MamieMail mamieMail;
    public final int MAX_TRIES = 10;
    private final long DURATION = 60000; // 1 minute    

    public boolean isFinished() {
        return finished;
    }

    /**
     * Envoi une commande et attend son résultat. 
     * @param mamieMail
     * @param date
     * @param subject
     * @param body
     * @param attempting
     * @throws MessagingException
     * @throws FileNotFoundException 
     */
    public SendACommandeAndWaitForResultTask(
            MamieMail mamieMail, Date date, String subject, String body, String attempting) throws MessagingException, FileNotFoundException {
        this.date = date;
        //this.command = subject;
        this.attempting = attempting;
        this.mamieMail = mamieMail;
        new Thread() {
            @Override
            public void run() {
                try {
                    System.out.println("Envoi de la commande");
                    MamieMail.send(subject, body);
                    System.out.println("Commande envoyée");
                } catch (FileNotFoundException ex) {
                    System.out.println("Erreur de commande");
                    finished = true;
                }
            }
        }.start();
        
    }

    
    public SendACommandeAndWaitForResultTask(MamieMail mamieMail, Date date, String subject, String attempting) throws MessagingException, FileNotFoundException {
        this(mamieMail, date, subject, "", attempting);
    } 

    @Override
    protected Message call() throws Exception {
        System.out.println("Recherche dans la boîte mail...");
        stopTiming();          
        updateMessage("Recherche dans la boîte mail...");
        int nbTries = 0;
        updateProgress(nbTries, MAX_TRIES);
        while (!finished) {
            try {
                System.out.println("Dans la boucle");
                Thread.sleep(DURATION);
                ++nbTries;
                updateProgress(nbTries, MAX_TRIES);
                System.out.println("Essai °" + nbTries);
                Message message = mamieMail.getLastMessagesAfter(attempting, date);
                if (message != null) {
                    finished = true;
                }
            } catch (InterruptedException ex) {
                Message message = mamieMail.getLastMessagesAfter(attempting, date);
                if (message != null) {
                    finished = true;
                }
            }
        }
        updateProgress(MAX_TRIES, MAX_TRIES);
        Message message = mamieMail.getLastMessagesAfter(attempting, date);
        if (message == null) {
                    updateMessage("Problème avec le serveur !");
                } else {
                    System.out.println(message.getSubject());

                    Multipart mp = (Multipart) message.getContent();

                    String result = mp.getBodyPart(0).getContent().toString();
                    String convertedResult = conversionEncoding(result, "ISO-8859-1", "UTF-8");          

                    if (convertedResult.equals("cat: lmes: Aucun fichier ou dossier de ce type")) {
                        convertedResult = "Ecran noir";
                    } else if (convertedResult.trim().isEmpty()) {
                        convertedResult = "Ecran vide";
                    }
                    System.out.println(convertedResult);

                    updateMessage(convertedResult);
                    updateValue(message);
                }
        finished = true;
        return message;
    }


    private void stopTiming() {
        new Thread() {
            @Override
            public void run() {
                try {
                    System.out.println("Début du timing");
                    sleep(MAX_TRIES * DURATION);
                    System.out.println("Fin du timing");
                    finished = false;
                } catch (InterruptedException ex) {
                }
            }
        }.start();
    }

    public void close() {
        if (mamieMail != null) {
            mamieMail.close();
        }
    }

}
