/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.panes;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javax.mail.MessagingException;
import mail.GMail;
import threads.SendACommandeAndWaitForResultTask;

/**
 *
 * @author maillot
 */
public class ListPane extends BorderPane {

    private final Button send;
    private final TextArea text;
    private SendACommandeAndWaitForResultTask wfam;
    private final ProgressBar pbmn;
    private final ProgressBar pbsec;
    private boolean again = true;
    private final CardPane parent;

    public ListPane(CardPane parent) throws MessagingException, FileNotFoundException {
        send = new Button("Lancer la recherche");
        text = new TextArea();
        pbmn = new ProgressBar(0);
        pbsec = new ProgressBar(0);
        pbmn.setPrefWidth(Double.MAX_VALUE);
        pbsec.setPrefWidth(Double.MAX_VALUE);
        this.parent = parent;

        this.send.setOnAction(event -> {
            send.setText("En attente de la réponse du serveur...");
            send.setDisable(true);
            text.setText("");
            try {
                System.out.println("Debut task");
                wfam = new SendACommandeAndWaitForResultTask(this.parent.getGMail(), new Date(), "CAT LMES", "Contenu de lmes");
                wfam.setOnSucceeded(value -> {
                    text.setText(wfam.getMessage());
                    send.setText("Relancer une recherche");
                    send.setDisable(false);
                    again = false;
                });
                launchSec();
                pbmn.progressProperty().bind(wfam.progressProperty());
                new Thread(wfam).start();
            } catch (FileNotFoundException | MessagingException ex) {
                Logger.getLogger(ListPane.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        this.setTop(send);
        this.setCenter(text);
        GridPane south = new GridPane();
        south.addRow(0, pbsec);
        south.addRow(1, pbmn);
        this.setBottom(south);
    }

    private void launchSec() {
        new Thread() {
            @Override
            public void run() {
                int i = 0;
                while(again) {
                    try {
                        pbsec.setProgress(i/599.0);
                        Thread.sleep(100);
                        i = (i + 1)%600;
                    } catch (InterruptedException ex) {
                        again = false;
                    }
                }
            }
        }.start();
    }

}
