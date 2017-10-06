package ui.panes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javax.mail.MessagingException;
import mail.GMail;
import threads.SendACommandeAndWaitForResultTask;

/**
 *
 * @author maillot
 */
public class FileEditPane extends BorderPane {

    private final Button send, cancel;
    private final TextArea text;
    private SendACommandeAndWaitForResultTask wfam;
    private final ProgressBar pbmn;
    private final ProgressBar pbsec;
    private boolean again = true;
    private final TextField fileName;
    private final CardPane parent;

    public FileEditPane(CardPane parent) throws MessagingException, FileNotFoundException {

        fileName = new TextField();
        send = new Button("Get file");
        send.setDisable(true);
        cancel = new Button("Cancel");
        cancel.setVisible(false);
        text = new TextArea();
        text.setEditable(false);
        text.setBackground(new Background(new BackgroundFill(Color.BISQUE, CornerRadii.EMPTY, Insets.EMPTY)));

        FlowPane top = new FlowPane(fileName, send, cancel);
        fileName.setPrefWidth(560);
        pbmn = new ProgressBar(0);
        pbsec = new ProgressBar(0);
        pbmn.setPrefWidth(Double.MAX_VALUE);
        pbsec.setPrefWidth(Double.MAX_VALUE);
        text.setMaxSize(800, 600);
        text.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);

        this.parent = parent;

        this.cancel.setOnAction(value -> {
            cancel.setVisible(false);
            send.setText("Get file");
            text.setText("");
            send.setDisable(false);
        });

        fileName.setOnKeyTyped(value -> {
            send.setDisable(fileName.getText().trim().isEmpty());
            System.out.println("send.setDisable(" + fileName.getText().trim().isEmpty() + ");");
        });

        this.send.setOnAction(event -> {
            if ("Get file".equals(send.getText())) {
                //send.setText("En attente de la réponse du serveur...");
                send.setDisable(true);
                text.setDisable(true);
                try {
                    wfam = new SendACommandeAndWaitForResultTask(this.parent.getGMail(), new Date(),
                            "COMMANDE",
                            "cat " + fileName.getText(),
                            "cat " + fileName.getText());
                    wfam.setOnSucceeded(value -> {
                        text.setText(wfam.getMessage());
                        send.setText("Send after editing");
                        cancel.setVisible(true);
                        fileName.setDisable(true);
                        send.setDisable(false);
                        System.out.println("Commande passée");
                        text.setDisable(false);
                        text.setEditable(true);
                        again = false;
                    });
                    launchSec();
                    pbmn.progressProperty().bind(wfam.progressProperty());
                    new Thread(wfam).start();
                } catch (FileNotFoundException | MessagingException ex) {
                    Logger.getLogger(FileEditPane.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                StringBuilder sb = new StringBuilder();
                try (Scanner read = new Scanner(text.getText());
                        PrintStream out = new PrintStream("tmp/lperm")) {

                    while (read.hasNextLine()) {
                        out.println(read.nextLine());
                    }
                    try {
                        GMail.send("FICHIER", new File("tmp/lperm"));
                    } catch (FileNotFoundException ex) {
                    }
                } catch (FileNotFoundException ex) {

                }

                send.setText("Get file");
                text.setText("");
                text.setEditable(false);
                send.setDisable(false);
                fileName.setDisable(false);
                cancel.setVisible(false);
            }
        });

        this.setTop(top);
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
