package ui.panes;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javax.mail.MessagingException;
import mail.GMail;
import threads.SendACommandeAndWaitForResultTask;

/**
 *
 * @author maillot
 */
public class CommandPane extends BorderPane {

    private final Button send;
    private final TextArea text;
    private SendACommandeAndWaitForResultTask wfam;
    private final ProgressBar pbmn;
    private final ProgressBar pbsec;
    private boolean again = true;
    private final TextField command;
    private final CardPane parent;

    public CommandPane(CardPane parent) throws MessagingException, FileNotFoundException {
        
        command = new TextField();
        send = new Button("Go");
        send.setDisable(true);
        text = new TextArea();
        
        FlowPane top = new FlowPane(command, send);
        command.setPrefWidth(560);
        pbmn = new ProgressBar(0);
        pbsec = new ProgressBar(0);
        pbmn.setPrefWidth(Double.MAX_VALUE);
        pbsec.setPrefWidth(Double.MAX_VALUE);
        
        this.parent = parent;
                        
        command.setOnKeyTyped(value -> {
            send.setDisable(command.getText().trim().isEmpty());
            System.out.println("send.setDisable("+command.getText().trim().isEmpty()+");");
        });

        this.send.setOnAction(event -> {
            //send.setText("En attente de la réponse du serveur...");
            send.setDisable(true);
            text.setText("");
            try {
                wfam = new SendACommandeAndWaitForResultTask(this.parent.getGMail(), new Date(), "COMMANDE", command.getText(), command.getText());
                wfam.setOnSucceeded(value -> {
                    text.setText(wfam.getMessage());
                    //send.setText("Relancer une recherche");
                    send.setDisable(false);
                    System.out.println("Commande passée");
                    again = false;
                });
                launchSec();
                pbmn.progressProperty().bind(wfam.progressProperty());
                new Thread(wfam).start();
            } catch (FileNotFoundException | MessagingException ex) {
                Logger.getLogger(CommandPane.class.getName()).log(Level.SEVERE, null, ex);
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
