/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.mail.Message;
import javax.mail.MessagingException;
import mail.MamieMail;

/**
 *
 * @author maillot
 */
public class WaitForAMail extends Thread {

    private final Date date;
    private final String subject;
    private boolean finished = false;

    public boolean isFinished() {
        return finished;
    }
    private Message message = null;
    private final MamieMail mamieMail;
    private int numberOfTries = 0;
    public final int MAX_TRIES = 10;

    private final long DURATION = 60000; // 1 minute    

    public WaitForAMail(Date date, String subject) throws MessagingException {
        this.date = date;
        this.subject = subject;
        mamieMail = new MamieMail();
    }

    @Override
    public void run() {
        stopTiming();
     
        
        while (!finished) {
            try {
                sleep(DURATION);
                ++numberOfTries;
                message = mamieMail.getLastMessagesAfter(subject, date);
                if (message != null) {
                    finished = true;
                }
            } catch (InterruptedException ex) {
                message = mamieMail.getLastMessagesAfter(subject, date);
                if (message != null) {
                    finished = true;
                }
            }
        }
        message = mamieMail.getLastMessagesAfter(subject, date);
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
