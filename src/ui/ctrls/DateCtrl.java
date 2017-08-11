/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.ctrls;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

/**
 *
 * @author maillot
 */
public class DateCtrl extends GridPane {

    private final DatePicker D;
    private final CheckBox C;

    public DateCtrl() {
        this(null);
    }

    public DateCtrl(String msg) {
        C = new CheckBox();
        D = new DatePicker(LocalDate.now());

        int c = 0;

        if (msg != null) {
            add(new Label(msg), c++, 0);
            getColumnConstraints().add(new ColumnConstraints(65));
        }

        FlowPane fl = new FlowPane();
        fl.getChildren().add(C);
        fl.getChildren().add(D);

        add(fl, c++, 0);

        D.setDisable(!C.isSelected());

        C.setOnAction(value -> {
            if (C.isSelected()) {
                D.setDisable(false);
                System.out.println(D.getValue());
            } else {
                D.setValue(LocalDate.now());
                D.setDisable(true);
            }
        });
    }

    public void init() {
        C.setSelected(false);
        D.setDisable(true);
        D.setValue(LocalDate.now());
    }

    public String getText() {
        LocalDate ld = D.getValue();
        return String.format("%s/%s/%s", ld.getDayOfMonth(), ld.getMonthValue(), ld.getYear());
    }
}
