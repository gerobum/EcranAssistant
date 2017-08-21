/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.ctrls;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

/**
 *
 * @author maillot
 */
public class HoursCtrl extends GridPane {

    private final TextField H;
    private final TextField M;
    private final CheckBox C;
    private final String HD;
    private final String MD;

    public HoursCtrl() {
        this(null, "00", "00");
    }

    public HoursCtrl(String msg, String hd, String md) {
        HD = hd;
        MD = md;
        C = new CheckBox();
        H = new TextField();
        H.setText(HD);
        H.setPrefColumnCount(2);
        M = new TextField();
        M.setText(MD);
        M.setPrefColumnCount(2);
        int c = 0;

        if (msg != null) {
            add(new Label(msg), c++, 0);
            getColumnConstraints().add(new ColumnConstraints(65));
        }

        FlowPane fl = new FlowPane();
        fl.getChildren().add(C);
        fl.getChildren().add(H);
        fl.getChildren().add(new Label(" : "));
        fl.getChildren().add(M);

        add(fl, c++, 0);

        H.setDisable(!C.isSelected());
        M.setDisable(!C.isSelected());

        C.setOnAction(value -> {
            H.setDisable(!C.isSelected());
            M.setDisable(!C.isSelected());
            if (!C.isSelected()) {
                H.setText(HD);
                M.setText(MD);
            }
        });
    }
   
    public void init() {
        C.setSelected(false);
        H.setDisable(true);
        H.setText(HD);
        M.setDisable(true);
        M.setText(MD);
    }
    
    public String getText() {

        return H.getText() + ":" + M.getText();

    }
}
