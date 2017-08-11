package ui.panes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import mail.MamieMail;
import threads.SendACommandeAndWaitForResultTask;

/**
 *
 * @author maillot
 */
public class ScreenCopyPane extends BorderPane {

    private final Button send;
    private final ImageView image;
    private SendACommandeAndWaitForResultTask wfam;
    private final ProgressBar pbmn;
    private final ProgressBar pbsec;
    private final MamieMail mamieMail;
    private boolean again = true;

    public ScreenCopyPane() throws MessagingException, FileNotFoundException {

        send = new Button("Go");
        image = new ImageView();

        pbmn = new ProgressBar(0);
        pbsec = new ProgressBar(0);
        pbmn.setPrefWidth(600);
        pbsec.setPrefWidth(600);
        mamieMail = new MamieMail();

        this.send.setOnAction(event -> {
            //send.setText("En attente de la réponse du serveur...");
            System.out.println("SCROT");
            send.setDisable(true);
            try {
                wfam = new SendACommandeAndWaitForResultTask(mamieMail, new Date(), "SCROT", "Copie d'écran");
                System.out.println("demande de copie d'écran envoyée");
                wfam.setOnSucceeded(value -> {
                    System.out.println("Copie d'écran reçue : ");
                    MimeMessage message = (MimeMessage) wfam.getValue();

                    try {
                        //if (message.getContent().getClass() == MimeMultipart.class) {
                            MimeMultipart content = (MimeMultipart) message.getContent();
                            System.out.println("BodyPart : =" + content.getBodyPart(0));
                            BodyPart bp = content.getBodyPart(0);
                            System.out.println("getFileName : " + bp.getFileName());                         
                            
                            image.setImage(new Image(bp.getInputStream()));
                            image.setPreserveRatio(true);
                            image.setFitHeight(this.getHeight());
                            
                        //}
                    } catch (IOException | MessagingException e) {

                    }
                    System.out.println(message);

                    try {
                        System.out.println("type : " + message.getContent().getClass());
                    } catch (IOException | MessagingException e) {
                        System.out.println("getFileName provoque une erreur");
                    }
                    try {
                        System.out.println("getFileName() : " + message.getFileName());
                    } catch (MessagingException e) {
                        System.out.println("getFileName provoque une erreur");
                    }
                    try {
                        System.out.println("getEncoding() : " + message.getEncoding());
                    } catch (MessagingException e) {
                        System.out.println("getEncoding provoque une erreur");
                    }
                    try {
                        System.out.println("getContentType() : " + message.getContentType());
                    } catch (MessagingException e) {
                        System.out.println("getContentType provoque une erreur");
                    }
                    try {
                        System.out.println("getContentID() : " + message.getContentID());
                    } catch (MessagingException e) {
                        System.out.println("getContentType provoque une erreur");
                    }
                    //System.out.println("getContentID() : " + message.);                    

                    //image.setText(message.toString());

                    send.setDisable(false);
                    again = false;
                });
                launchSec();
                pbmn.progressProperty().bind(wfam.progressProperty());
                new Thread(wfam).start();
            } catch (FileNotFoundException | MessagingException ex) {

            }
        });

        this.setTop(send);
        this.setCenter(image);
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
                while (again) {
                    try {
                        pbsec.setProgress(i / 599.0);
                        Thread.sleep(100);
                        i = (i + 1) % 600;
                    } catch (InterruptedException ex) {
                        again = false;
                    }
                }
            }
        }.start();
    }

}
