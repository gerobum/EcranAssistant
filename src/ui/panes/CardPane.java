/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.panes;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.mail.MessagingException;
import mail.GMail;
import uses.Usefull;

/**
 *
 * @author maillot
 */
public class CardPane extends BorderPane {

    private final FlowPane north = new FlowPane();
    private final RadioButton gerobum, mamie_rasp, yvan_rasp;
    private final StackPane center = new StackPane();
    private final List<Pair<Button, Pane>> list = new LinkedList<>();
    private GMail gerobumMail, mamieMail, yvanMail, gmail;
    private Pane root;

    public CardPane() {
        //setPrefWidth(Double.MAX_VALUE);
        setTop(north);
        setCenter(center);
        north.setPrefWidth(Double.MAX_VALUE);
        //center.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        
        /*north.setStyle("-fx-background-color: linear-gradient(to bottom, #f2f2f2, #d4d4d4);"
                + 
                " -fx-border: 12px solid; -fx-border-color: black; -fx-background-radius: 0.0;"
                + " -fx-border-radius: 0.0");*/
        //north.setPrefWidth(Double.MAX_VALUE);
        BorderPane.setMargin(center, new Insets(8, 0, 0, 0));

        gerobum = new RadioButton("gerobum@gmail.com");
        mamie_rasp = new RadioButton("mamie.rasp@gmail.com");
        yvan_rasp = new RadioButton("yvan.rasp@gmail.com");

        try {
            Usefull.changeTo("gerobum");
            gerobumMail = new GMail();
            Usefull.changeTo("mamie");
            mamieMail = new GMail();
            Usefull.changeTo("yvan");
            yvanMail = new GMail();
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(AlertType.ERROR, "Ce fichier n'existe pas " + e.getMessage());
            alert.showAndWait();
        } catch (MessagingException ex) {
            Logger.getLogger(CardPane.class.getName()).log(Level.SEVERE, null, ex);
        }

        ToggleGroup tg = new ToggleGroup();

        EventHandler<ActionEvent> eh = (ActionEvent event) -> {
            try {
                if (gerobum.isSelected()) {
                    Usefull.changeTo("gerobum");
                    gmail = gerobumMail;
                } else if (mamie_rasp.isSelected()) {
                    Usefull.changeTo("mamie");
                    gmail = mamieMail;
                } else {
                    Usefull.changeTo("yvan");
                    gmail = yvanMail;
                }
            } catch (FileNotFoundException e) {
                Alert alert = new Alert(AlertType.ERROR, "Ce fichier n'existe pas " + e);
                alert.showAndWait();
            }
        };

        mamie_rasp.setToggleGroup(tg);
        mamie_rasp.setOnAction(eh);
        yvan_rasp.setToggleGroup(tg);
        yvan_rasp.setOnAction(eh);
        gerobum.setToggleGroup(tg);
        gerobum.setOnAction(eh);
        gerobum.setSelected(true);
        try {
            Usefull.changeTo("gerobum");
            gmail = gerobumMail;
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(AlertType.ERROR, "Ce fichier n'existe pas " + e.getMessage());
            alert.showAndWait();
        }

        FlowPane south = new FlowPane(mamie_rasp, yvan_rasp, gerobum);
        south.setPrefWidth(Double.MAX_VALUE);
        south.setHgap(20);
        setBottom(south);
    }

    public void add(String title, Pane pane) {
        pane.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
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

    public void setRoot(Pane root) {
        this.root = root;
    }
    
    public GMail getGMail() {
        return gmail;
    }

}
