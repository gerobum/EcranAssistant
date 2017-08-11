/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.panes;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import mail.GMail;

/**
 *
 * @author maillot
 */
public class SupPane extends BorderPane {


    private final Button send;
    private final ComboBox<Integer> lineNumber;

    public SupPane() {
        send = new Button("Supprimer");
        lineNumber = new ComboBox<>();
        lineNumber.getItems().addAll(Arrays.asList(0,1,2,3,4,5,6,7,8,9));
        FlowPane fp = new FlowPane(send, lineNumber);
        getChildren().add(fp);
        
        send.setOnAction(value -> {
            try {
                GMail.send("SUP " + lineNumber.getSelectionModel().getSelectedItem());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SupPane.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }
}
