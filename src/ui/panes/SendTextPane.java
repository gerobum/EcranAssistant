/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.panes;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import mail.GMail;
import ui.ctrls.DateCtrl;
import ui.ctrls.DayCtrl;
import ui.ctrls.HoursCtrl;

/**
 *
 * @author maillot
 */
public class SendTextPane extends GridPane implements EventHandler<ActionEvent> {

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

        day.addAction(this);
        date.addAction(this);

        send.setOnAction(value -> {
            try {
                String textToSend;
                if (!day.getText().equals("*")) {
                    textToSend = String.format("%s§%s§%s§%s", begin.getText(),
                            end.getText(), day.getText(), text.getText());
                } else {
                    textToSend = String.format("%s§%s§%s§%s§%s", date.getText(), begin.getText(),
                            end.getText(), day.getText(), text.getText());
                }
                GMail.send("MSG", textToSend);
                System.out.println(textToSend);
                initAll();
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

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == day.getSource()) {
            if (!day.getText().equals("*")) {
                date.init();
            }
        } else {
            CheckBox C = (CheckBox) event.getSource();
            if (C.isSelected()) {
                day.init();
            }
        }
    }
}
