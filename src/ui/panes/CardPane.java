/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.panes;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Pair;
import uses.Usefull;

/**
 *
 * @author maillot
 */
public class CardPane extends Pane {

    private final BorderPane mainPane = new BorderPane();
    private final FlowPane north = new FlowPane();
    private final RadioButton here, mamie, tv;
    private final StackPane center = new StackPane();
    private final List<Pair<Button, Pane>> list = new LinkedList<>();
    private Group root;

    public CardPane() {
        mainPane.setTop(north);
        mainPane.setCenter(center);
        super.getChildren().add(mainPane);
        north.setStyle(/*"-fx-background-color: linear-gradient(to bottom, #f2f2f2, #d4d4d4);"
                + */" -fx-border: 12px solid; -fx-border-color: black; -fx-background-radius: 0.0;"
                + " -fx-border-radius: 0.0");
        BorderPane.setMargin(center, new Insets(8, 0, 0, 0));

        here = new RadioButton("Ici");
        mamie = new RadioButton("Mamie");
        tv = new RadioButton("Derrière la télé");
        ToggleGroup tg = new ToggleGroup();

        EventHandler<ActionEvent> eh = (ActionEvent event) -> {
            try {
                if (here.isSelected()) {
                    Usefull.changeTo("gerobum");
                } else if (mamie.isSelected()) {
                    Usefull.changeTo("mamie");
                } else {
                    Usefull.changeTo("yvan");
                }
            } catch (FileNotFoundException e) {
                Alert alert = new Alert(AlertType.ERROR, "Ce fichier n'existe pas " + e);
                alert.showAndWait();
            }
        };

        mamie.setToggleGroup(tg);
        mamie.setOnAction(eh);
        tv.setToggleGroup(tg);
        tv.setOnAction(eh);
        here.setToggleGroup(tg);
        here.setOnAction(eh);
        here.setSelected(true);
        try {
            Usefull.changeTo("gerobum");
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(AlertType.ERROR, "Ce fichier n'existe pas " + e);
            alert.showAndWait();
        }

        FlowPane south = new FlowPane(mamie, tv, here);
        south.setHgap(20);
        mainPane.setBottom(south);
    }

    public void add(String title, Pane pane) {
        Pair<Button, Pane> pair = new Pair(new Button(title), pane);
        north.getChildren().add(pair.getKey());
        center.getChildren().add(pane);
        list.add(pair);
        pair.getKey().setOnAction(value -> {
            toFront(pair);
        });

        toFront(pair);
    }

    private void toFront(Pair<Button, Pane> pair) {
        pair.getValue().toFront();
        for (Pair<Button, Pane> p : list) {
            p.getValue().setVisible(false);
            p.getKey().setDisable(false);

        }
        pair.getValue().setVisible(true);
        pair.getKey().setDisable(true);
        if (root != null) {
            root.requestFocus();
        }
    }

    public void setRoot(Group root) {
        this.root = root;
    }

}
