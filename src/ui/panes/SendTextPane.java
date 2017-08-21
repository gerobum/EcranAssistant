/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.panes;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import mail.MamieMail;
import ui.ctrls.DateCtrl;
import ui.ctrls.DayCtrl;
import ui.ctrls.HoursCtrl;

/**
 *
 * @author maillot
 */
public class SendTextPane extends GridPane {

    private final DateCtrl date;
    private final HoursCtrl begin, end;
    private final DayCtrl day;
    private final Button send, annuler;
    private final TextArea text;

    public SendTextPane() {
        date = new DateCtrl("Date : ");
        begin = new HoursCtrl("Début : ", "00", "00");
        end = new HoursCtrl("Fin :   ", "23", "59");
        day = new DayCtrl("Le jour : ");
        text = new TextArea();
        text.setWrapText(true);
        send = new Button("Envoyer");
        annuler = new Button("Annuler");
        int i = 0;
        add(date, 0, i++);
        add(begin, 0, i++);
        add(end, 0, i++);
        add(day, 0, i++);
        add(text, 0, i++);
        FlowPane boutons = new FlowPane(send, annuler);
        add(boutons, 0, i++);

        send.setDisable(true);
        annuler.setDisable(true);

        send.setOnAction(value -> {
            try {
                MamieMail.send("MSG", String.format("%s§%s§%s§%s§%s", date.getText(), begin.getText(),
                        end.getText(), day.getText(), text.getText()));
                initAll();
                /*System.out.println(String.format("%s§%s§%s§%s§%s", date.getText(), begin.getText(),
                end.getText(), day.getText(), text.getText()));*/
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SendTextPane.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        text.setOnKeyTyped(value -> {
            send.setDisable(text.getText().trim().isEmpty());
            annuler.setDisable(text.getText().trim().isEmpty());
        });
        
        annuler.setOnAction(value -> {
            initAll();
        });
    }

    private void initAll() {
        date.init();
        begin.init();
        end.init();
        day.init();
        text.setText("");
        send.setDisable(true);
        annuler.setDisable(true);
    }
}
