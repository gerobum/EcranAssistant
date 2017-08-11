/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.panes;

import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import mail.MamieMail;

/**
 *
 * @author maillot
 */
public class FontSizePane extends FlowPane {
    private final Button send;

    public FontSizePane() {
        this.send = new Button("Changer la taille des fontes");
        this.send.setOnAction(event -> {
            MamieMail.send("ALL FONT", "8");
        });
        this.getChildren().add(send);
    }
    
    
}
