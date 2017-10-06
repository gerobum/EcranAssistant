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
import ui.panes.FileEditPane;
import ui.panes.ListPane;
import ui.panes.ScreenCopyPane;
import ui.panes.SupPane;

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
        Scene scene = new Scene(root);       
        
        addPanesToStack();
        card.setRoot(root);
                
        root.getChildren().add(card);
        
        primaryStage.setScene(scene);
        //primaryStage.setVisible(true);  
        primaryStage.sizeToScene();
        primaryStage.widthProperty().addListener(p -> {
            primaryStage.sizeToScene();
            System.out.println("resizing width");
        });
        primaryStage.heightProperty().addListener(p -> {
            primaryStage.sizeToScene();
            System.out.println("resizing height");
        });
        primaryStage.show();

    }

    public static void main(String[] args) {
        Application.launch(MainStage.class, args);
    }

    private void addPanesToStack() throws MessagingException, FileNotFoundException {
        card.add("Copie d'écran", new ScreenCopyPane(card));
        card.add("Envoi de messages", new SendTextPane());
        card.add("Liste des messages", new ListPane(card));
        card.add("Commande", new CommandPane(card));
        card.add("Suppression", new SupPane());
        card.add("Edition de fichier", new FileEditPane(card));
        
        //card.add("FONT", new FontSizePane());
    }
}
