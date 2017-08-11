/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import java.util.Date;
import javax.mail.Message;
import javax.mail.MessagingException;
import mail.MamieMail;

/**
 *
 * @author maillot
 */
public class SendACommandeAndWaitForResult extends Thread {

    private final Date date;
    private final String commande;
    private boolean finished = false;
    private Message message = null;
    private final MamieMail mamieMail;
    private int numberOfTries = 0;
    public final int MAX_TRIES = 10;

    public boolean isFinished() {
        return finished;
    }

    private final long DURATION = 60000; // 1 minute    

    public SendACommandeAndWaitForResult(Date date, String commande) throws MessagingException {
        this.date = date;
        this.commande = commande;
        mamieMail = new MamieMail();
        MamieMail.send("COMMANDE", commande);
    }

    public void run() {
        stopTiming();             
        while (!finished) {
            try {
                Thread.sleep(DURATION);
                ++numberOfTries;
                message = mamieMail.getLastMessagesAfter(commande, date);
                if (message != null) {
                    finished = true;
                }
            } catch (InterruptedException ex) {
                message = mamieMail.getLastMessagesAfter(commande, date);
                if (message != null) {
                    finished = true;
                }
            }
        }
        message = mamieMail.getLastMessagesAfter(commande, date);
        finished = true;
    }

    public int getNumberOfTries() {
        return numberOfTries;
    }

    private void stopTiming() {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(MAX_TRIES * DURATION);
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

    public Message getMessage() {
        return message;
    }

}
