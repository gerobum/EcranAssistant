/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.panes;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
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
    private final Button send;
    private final TextArea text;

    public SendTextPane() {
        date = new DateCtrl("Date : ");
        begin = new HoursCtrl("Début : ", "00", "00");
        end = new HoursCtrl("Fin :   ", "23", "59");
        day = new DayCtrl("Le jour : ");
        text = new TextArea();
        send = new Button("Envoyer");
        int i = 0;
        add(date, 0, i++);
        add(begin, 0, i++);
        add(end, 0, i++);
        add(day, 0, i++);
        add(text, 0, i++);
        add(send, 0, i++);

        send.setDisable(true);

        send.setOnAction(value -> {
            MamieMail.send("MSG", String.format("%s§%s§%s§%s§%s", date.getText(), begin.getText(),
                    end.getText(), day.getText(), text.getText()));
            /*System.out.println(String.format("%s§%s§%s§%s§%s", date.getText(), begin.getText(),
                    end.getText(), day.getText(), text.getText()));*/
        });

        text.setOnKeyTyped(value -> {
            send.setDisable(text.getText().trim().isEmpty());
        });
    }

}
