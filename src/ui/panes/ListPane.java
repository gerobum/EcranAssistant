/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.panes;

import java.io.IOException;
import java.util.Date;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import threads.SendACommandeAndWaitForResult;
import static uses.Usefull.*;

/**
 *
 * @author maillot
 */
public class ListPane extends BorderPane {

    private final Button send;
    private final TextArea text;
    private SendACommandeAndWaitForResult wfam;
    private final ProgressBar pb;

    public ListPane() {
        this.send = new Button("Lancer la recherche");
        this.text = new TextArea();
        this.pb = new ProgressBar(0);
        pb.setPrefWidth(600);

        this.send.setOnAction(event -> {
            send.setText("En attente de la réponse du serveur...");
            send.setDisable(true);
            text.setText("");
            try {
                wfam = new SendACommandeAndWaitForResult(new Date(), "cat lmes");
                wfam.start();
                // Le thread suivant attend son aboutissement.
                waitingResponse();
            } catch (MessagingException ex) {
                System.err.println(ex);
            }

        });
        this.setTop(send);
        this.setCenter(text);
        this.setBottom(pb);

    }

    private void updatingProgressBar() {
        Platform.runLater(() -> {
            while (!wfam.isFinished()) {
                try {
                    pb.setProgress(wfam.getNumberOfTries() / wfam.MAX_TRIES);
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                }
            }
        });
    }

    private void waitingResponse() {
        Platform.runLater(() -> {
            try {
                System.out.println("ATTENTE");
                updatingProgressBar();
                wfam.join();
                if (wfam.getMessage() == null) {
                    System.out.println("Pas de message");
                    text.setText("Problème avec le serveur !");
                } else {
                    System.out.println(wfam.getMessage().getSubject());

                    Multipart mp = (Multipart) wfam.getMessage().getContent();

                    String result = mp.getBodyPart(0).getContent().toString();
                    String convertedResult = conversionEncoding(result, "ISO-8859-1", "UTF-8");

                    if (convertedResult.equals("cat: lmes: Aucun fichier ou dossier de ce type")) {
                        convertedResult = "Ecran noir";
                    }
                    
                    if (convertedResult.trim().isEmpty()) {
                        convertedResult = "Ecran vide";
                    }
                    System.out.println(convertedResult);

                    text.setText(convertedResult);
                }
                System.out.println("FINI");

            } catch (InterruptedException | MessagingException | IOException ex) {
                System.err.println(ex);
            } finally {
                wfam.close();
                send.setDisable(false);
                send.setText("Lancer une nouvelle recherche");
                pb.setProgress(0);
            }
        });
    }
}
