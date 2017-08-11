/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.panes;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.util.Pair;

/**
 *
 * @author maillot
 */
public class CardPane extends Pane {
    private final BorderPane mainPane = new BorderPane();
    private final FlowPane north = new FlowPane();
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
        for (Pair<Button,Pane> p : list) {
            p.getValue().setVisible(false);
            p.getKey().setDisable(false);
            
        }
        pair.getValue().setVisible(true);
        pair.getKey().setDisable(true);
        if (root != null)
             root.requestFocus();
    }

    public void setRoot(Group root) {
        this.root = root;}
    
}
