package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class SingleChatController implements Initializable {


    @FXML
    private VBox userChatMessagesLayout;

    @FXML
    private TextArea messageField;

    @FXML
    private Label chatUserName;

    @FXML
    private AnchorPane singleMessageLayout;

    private Set<String> eachUserMessages = new HashSet<>();

    void readMessage(String m) {
        Label messageLabel = new Label(m);
        messageLabel.setFont(new Font(18));
        messageLabel.setPrefWidth(300);
        messageLabel.setWrapText(true);
        userChatMessagesLayout.getChildren().add(messageLabel);
    }

    @FXML
    void onWritingNewMessage(MouseEvent event) {
        System.out.println("Reload: " + HomeController.client.messageToShow);
        if (!eachUserMessages.contains(HomeController.client.messageToShow)){
            eachUserMessages.add(HomeController.client.messageToShow);
            readMessage(HomeController.client.messageToShow);
        }
    }

    @FXML
    void onSendSingleMessage(ActionEvent event) {
        readMessage(SignInController.loggedInUserName + ": " + messageField.getText());
        HomeController.client.sendMessageToSingleUser(messageField.getText() , chatUserName.getText());
        messageField.setText("");
    }

    @FXML
    void onBackClick(MouseEvent event) {

        singleMessageLayout.getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/frontend/home.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setTitle("Home!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chatUserName.setText(HomeController.userToChatWith);
    }
}
