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
import javafx.scene.layout.FlowPane;
import mail.GMail;

/**
 *
 * @author maillot
 */
public class FontSizePane extends FlowPane {
    private final Button send;

    public FontSizePane() {
        this.send = new Button("Changer la taille des fontes");
        this.send.setOnAction(event -> {
            try {
                GMail.send("ALL FONT", "8");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FontSizePane.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        this.getChildren().add(send);
    }
    
    
}
