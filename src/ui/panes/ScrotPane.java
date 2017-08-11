/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.panes;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import mail.MamieMail;

/**
 *
 * @author maillot
 */
public class ScrotPane extends BorderPane {
    private final Button send;

    public ScrotPane() {
        this.send = new Button("Copie d'écran");
        this.send.setOnAction(event -> {
            MamieMail.send("SCROT");
            
        });
        this.getChildren().add(send);
    }
    
    
}
