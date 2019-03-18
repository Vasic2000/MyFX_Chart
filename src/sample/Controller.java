package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Controller {
    DateFormat df = new SimpleDateFormat("hh:mm:ss");

    @FXML
    TextArea textArea_ID;

    @FXML
    TextField text_ID;

    public void sendMessage() {
        if(!text_ID.getText().isEmpty()) {
            textArea_ID.appendText(df.format(new Date()) + " : " + text_ID.getText() + "\n");
            text_ID.clear();
            text_ID.requestFocus();
        }
    }
}
