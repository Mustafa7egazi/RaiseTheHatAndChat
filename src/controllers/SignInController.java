package controllers;

import com.sun.tools.javac.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SignInController {
    @FXML
    private Button loginBtn;

    @FXML
    private PasswordField passwordTF;

    @FXML
    private Label registerLabel;

    @FXML
    private AnchorPane signInPage;

    @FXML
    private TextField usernameTF;

    @FXML
    void onLoginClick(ActionEvent event) {

    }

    @FXML
    void onRegisterClick(MouseEvent event) throws IOException {
        signInPage.getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(SignUpController.class.getResource("/frontend/signup-layout.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Sign Up");
        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        stage.show();

    }

}
