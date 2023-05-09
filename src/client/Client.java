
package client;
import database.DBHelper;


import java.io.*;
import java.net.Socket;
import java.util.*;

public class Client {
    private List<String> contactList = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public String messageToShow;


    public Client(Socket socket, String username) {
        try {
            messageToShow = "";
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = username;
            contactList = DBHelper.getSpecificUserContacts(username);
        } catch (IOException e) {
            closeAll(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage(String message) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                        bufferedWriter.write(username + " : " + contactList + " : " + message);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();

                } catch (IOException e) {
                    closeAll(socket, bufferedReader, bufferedWriter);
                }
            }
        }).start();
    }

    public void sendMessageToSingleUser(String message,String userToSend) {

        new Thread(new Runnable(){
            @Override
            public void run() {

                try {
                    bufferedWriter.write(username + " : " + userToSend + " : " + message);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();

                } catch (IOException e) {
                    closeAll(socket, bufferedReader, bufferedWriter);
                }
            }
        }).start();
    }

    public void sendUsernameAsMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bufferedWriter.write(username);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                } catch (IOException e) {
                    closeAll(socket, bufferedReader, bufferedWriter);
                }
            }
        }).start();
    }


    public void listenForMessages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("listening");

                String messageFromGroupChat;
                while (socket.isConnected()) {
                    try {
                        messageFromGroupChat = bufferedReader.readLine();

                        messageToShow = messageFromGroupChat;
                        //System.out.println(messageToShow);

                    } catch (IOException e) {
                        closeAll(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }


    public void closeAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {

        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openNewClient() throws IOException {
        System.out.println("If you want send message to one user => to (userName) (message) ");
        System.out.println("If you want send message to all user => to all (message) ");
        System.out.println("If you want to add one user => add (userName) ");
        System.out.println(" client connected");
        sendUsernameAsMessage();
        listenForMessages();
    }

}
