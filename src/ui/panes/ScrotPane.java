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
            try {
                MamieMail.send("SCROT");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ScrotPane.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
        this.setTop(send);
    }
    
    
}
