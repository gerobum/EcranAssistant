/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.ctrls;

import enumerations.Day;
import static enumerations.Day.*;
import interfaces.Identifiable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

/**
 *
 * @author maillot
 */
public class DayCtrl extends GridPane implements Identifiable {

    private final ComboBox<Day> D;
    private final CheckBox C;

    public DayCtrl() {
        this(null);
    }

    public DayCtrl(String msg) {
        C = new CheckBox();
        D = new ComboBox();

        int c = 0;

        if (msg != null) {
            add(new Label(msg), c++, 0);
            getColumnConstraints().add(new ColumnConstraints(65));
        }

        FlowPane fl = new FlowPane();
        fl.getChildren().add(C);
        fl.getChildren().add(D);

        D.getItems().addAll(CHAQUE_JOUR, LUNDI, MARDI, MERCREDI, JEUDI, VENDREDI, SAMEDI, DIMANCHE);
        D.getSelectionModel().select(0);
        add(fl, c++, 0);

        D.setDisable(!C.isSelected());        

        C.setOnAction(value -> {
            if (C.isSelected()) {
                D.setDisable(false);
            } else {
                D.setDisable(true);
                D.getSelectionModel().select(0);
            }
        });
    }
    
    public void addAction(EventHandler<ActionEvent> event) {
        D.addEventHandler(ActionEvent.ACTION, event);
    }

    public void init() {
        C.setSelected(false);
        D.setDisable(true);
        D.getSelectionModel().select(0);
    }

    public String getText() {
        return D.getSelectionModel().getSelectedItem().abv();
    }

    @Override
    public Control getSource() {
        return D;
    }
}
