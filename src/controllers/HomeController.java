package controllers;

import client.Client;
import database.DBHelper;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class HomeController implements Initializable {


    Socket socket = null;
    public static Client client;

    private ArrayList<String> allUsers = new ArrayList<>();

    private Set<String> eachUserMessages = new HashSet<>();

    @FXML
    private AnchorPane homePage;

    @FXML
    private Label loggedUserName;

    @FXML
    private VBox layoutOfAllUsers;

    @FXML
    private VBox eachUserContactsLayout;
    @FXML
    private VBox layoutOfAllMessageRequests;

    @FXML
    private VBox allMessagesLayout;

    @FXML
    private TextArea messageTF;

    public static String userToChatWith;

    @FXML
    void onSendBroadcastMessage(ActionEvent event) {
        readMessage(SignInController.loggedInUserName + ": " + messageTF.getText());
        client.sendMessage(messageTF.getText());
        messageTF.setText("");
    }


    @FXML
    void refreshLayoutOfMessages(MouseEvent event) {
        System.out.println("Reload: " + client.messageToShow);
        if (!eachUserMessages.contains(client.messageToShow)){
            eachUserMessages.add(client.messageToShow);
            readMessage(client.messageToShow);
        }
    }

    void readMessage(String m) {
        Label messageLabel = new Label(m);
        messageLabel.setFont(new Font(18));
        messageLabel.setPrefWidth(300);
        messageLabel.setWrapText(true);
        allMessagesLayout.getChildren().add(messageLabel);
    }

    private void addNewUser(String name, VBox parentLayout,
                            Image userImage, Image iconImage) {
        // create the image views
        ImageView leftImage = new ImageView(userImage);
        leftImage.setFitWidth(50);
        leftImage.setFitHeight(50);
        ImageView rightImage = new ImageView(iconImage);
        rightImage.setFitHeight(40);
        rightImage.setFitWidth(40);

        Label nameLabel = new Label(name);
        nameLabel.setFont(new Font(24));
        nameLabel.setPrefWidth(500);


        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setStyle("-fx-background-color: #B8E7E1; -fx-background-insets: 3; -fx-background-radius: 10;");
        hbox.setPadding(new Insets(8));
        hbox.setPrefWidth(600);
        hbox.setPrefHeight(77);
        hbox.getChildren().addAll(leftImage, nameLabel, rightImage);
        hbox.setSpacing(20);

        parentLayout.setPadding(new Insets(8, 8, 8, 8));
        parentLayout.getChildren().add(hbox);


        rightImage.setCursor(Cursor.HAND);
        rightImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                client.sendMessage("add " + nameLabel.getText());
            }
        });
    }


    private void addNewUserToContacts(String name, VBox parentLayout,
                                      Image userImage, Image iconImage) {
        // create the image views
        ImageView leftImage = new ImageView(userImage);
        leftImage.setFitWidth(50);
        leftImage.setFitHeight(50);
        ImageView rightImage = new ImageView(iconImage);
        rightImage.setFitHeight(40);
        rightImage.setFitWidth(40);

        Label nameLabel = new Label(name);
        nameLabel.setFont(new Font(24));
        nameLabel.setPrefWidth(500);


        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setStyle("-fx-background-color: #B8E7E1; -fx-background-insets: 3; -fx-background-radius: 10;");
        hbox.setPadding(new Insets(8));
        hbox.setPrefWidth(600);
        hbox.setPrefHeight(77);
        hbox.getChildren().addAll(leftImage, nameLabel, rightImage);
        hbox.setSpacing(20);

        parentLayout.setPadding(new Insets(8, 8, 8, 8));
        parentLayout.getChildren().add(hbox);


        rightImage.setCursor(Cursor.HAND);
        rightImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                client.sendMessage("accept " + nameLabel.getText());
                System.out.println("accept " + nameLabel.getText());
            }
        });
    }

    private void saveInContacts(String name, VBox parentLayout,
                                Image userImage) {
        // create the image views
        ImageView leftImage = new ImageView(userImage);
        leftImage.setFitWidth(50);
        leftImage.setFitHeight(50);


        Label nameLabel = new Label(name);
        nameLabel.setFont(new Font(24));
        nameLabel.setPrefWidth(500);


        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setStyle("-fx-background-color: #B8E7E1; -fx-background-insets: 3; -fx-background-radius: 10;");
        hbox.setPadding(new Insets(8));
        hbox.setPrefWidth(600);
        hbox.setPrefHeight(77);
        hbox.getChildren().addAll(leftImage, nameLabel);
        hbox.setSpacing(20);

        parentLayout.setPadding(new Insets(8, 8, 8, 8));
        parentLayout.getChildren().add(hbox);

        hbox.setCursor(Cursor.HAND);
        hbox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                userToChatWith = nameLabel.getText();
                homePage.getScene().getWindow().hide();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/frontend/single-chat.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = new Stage();
                stage.setTitle("Chat!");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        try {
            socket = new Socket("127.0.0.1", 8888);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (socket != null) {
            client = new Client(socket, SignInController.loggedInUserName);
            try {
                client.openNewClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        loggedUserName.setText(SignInController.loggedInName);

        allUsers = DBHelper.getAllUsers(SignInController.loggedInName);

        ArrayList<String> execludedContacts = DBHelper.getSpecificUserContacts(SignInController.loggedInUserName);

        allUsers.removeAll(execludedContacts);

        if (!allUsers.isEmpty() && !allUsers.contains("No users yet")) {
            for (String user : allUsers) {
                addNewUser(user, layoutOfAllUsers,
                        new Image("/imgs/userPic.png"), new Image("/imgs/addUser.png"));
            }
        }

        ArrayList<String> myRequests = DBHelper.getSpecificUserRequests(SignInController.loggedInUserName);
        System.out.println(myRequests);
        if (!myRequests.isEmpty() && !myRequests.contains("No requests")) {
            for (String req : myRequests) {
                addNewUserToContacts(req, layoutOfAllMessageRequests, new Image("/imgs/userPic.png"), new Image("/imgs/accept.png"));
            }
        }

        ArrayList<String> myContacts = DBHelper.getSpecificUserContacts(SignInController.loggedInUserName);
        System.out.println(myContacts);
        if (!myContacts.isEmpty() && !myContacts.contains("No contacts")) {
            for (String contact : myContacts) {
                saveInContacts(contact, eachUserContactsLayout, new Image("/imgs/userPic.png"));
            }
        }


//        ArrayList<String>names = new ArrayList<>();
//        names.add("7egz");
//        names.add("m");
//        Server.usersRequests.put("ferr",names);

//        String key = SignInController.loggedInUserName.trim();
//        ArrayList<String> currentUserRequests = Server.getUsersRequests().get(key);

    }
}
