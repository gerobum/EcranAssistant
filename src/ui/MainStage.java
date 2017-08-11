/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.io.FileNotFoundException;
import ui.panes.SendTextPane;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.mail.MessagingException;
import ui.panes.CardPane;
import ui.panes.CommandPane;
import ui.panes.ListPane;
import ui.panes.ScrotPane;

/**
 *
 * @author maillot
 */
public class MainStage extends Application {
    
    private final CardPane card = new CardPane();
    

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Ecran distant");                

        Group root = new Group();
        Scene scene = new Scene(root/*, 300, 250*/);
        
        addPanesToStack();
        card.setRoot(root);
                
        root.getChildren().add(card);
        
        primaryStage.setScene(scene);
        //primaryStage.setVisible(true);  
        primaryStage.sizeToScene();
        primaryStage.show();

    }

    public static void main(String[] args) {
        Application.launch(MainStage.class, args);
    }

    private void addPanesToStack() throws MessagingException, FileNotFoundException {
        card.add("Copie d'écran", new ScrotPane());
        card.add("Envoi de messages", new SendTextPane());
        card.add("Liste des messages", new ListPane());
        card.add("Commande", new CommandPane());
        
        //card.add("FONT", new FontSizePane());
    }
}
