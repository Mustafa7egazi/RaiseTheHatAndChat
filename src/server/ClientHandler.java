package server;

import database.DBHelper;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUserName = "";
    Set<String> innerContacts;


    public ClientHandler(Socket socket) {
        System.out.println(" client handler constructor");
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("waiting for username");
            this.clientUserName = bufferedReader.readLine();
//           System.out.println(bufferedReader.readLine());
            System.out.println("got the username");
            clientHandlers.add(this);
            //broadCastMessage("SERVER: " + "clientUserName" + " has entered the chat!");
            innerContacts = new HashSet<>();
        } catch (IOException e) {
            closeAll(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {

        System.out.println("Server run");

        String messageFromClient;
        try {

            while (socket.isConnected()) {

                messageFromClient = bufferedReader.readLine();

                String[] messageSplit = messageFromClient.split(" : ");
                System.out.println(Arrays.toString(messageSplit));
                String clientUsername = messageSplit[0];
                String contactList = messageSplit[1];
                String wantedAction = messageSplit[2];
                String[] actionSplit = wantedAction.split(" ");

                if (actionSplit[0].equals("add")) {
                    DBHelper.insertNewRequest(clientUsername, actionSplit[1]);
                } else if (actionSplit[0].equals("accept")) {
                    DBHelper.insertNewContact(clientUsername, actionSplit[1]);
                    DBHelper.insertNewContact(actionSplit[1], clientUsername);
                    DBHelper.deleteRequest(actionSplit[1], clientUsername);
                } else {
                    //String newContactList = contactList.substring(1, contactList.length() - 1);
                    String newContactList = contactList.replaceAll("[\\[\\]]", "");
                    String[] names = newContactList.split(",");
                    innerContacts.addAll(List.of(names));
                    //System.out.println(clientUsername +" : "+ wantedAction);
                    broadCastMessage(clientUsername + " : " + wantedAction);
                }
                //Server.userRequests.put(clientUsername , DBHelper.getSpecificUserRequests(clientUsername));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    // Send a message to everyone except me.
    public void broadCastMessage(String messageToSend) throws IOException {

        if (!clientHandlers.isEmpty()) {
            for (ClientHandler clientHandler : clientHandlers) {
                try {
                    System.out.println("my name: " + clientHandler.clientUserName);
                    System.out.println("my list: " + innerContacts);

                    if (innerContacts.contains(clientHandler.clientUserName)) {
                        System.out.println("Begin sending");
                        clientHandler.bufferedWriter.write(messageToSend);
                        clientHandler.bufferedWriter.newLine();
                        clientHandler.bufferedWriter.flush();
                    }
                } catch (IOException e) {
                    closeAll(socket, bufferedReader, bufferedWriter);
                }
            }

        }
    }


    // Client left the chat.
    public void removeClientHandler() {
        clientHandlers.remove(this);
        //broadCastMessage("SERVER: " + clientUserName + " has left the chat!");
    }

    public void closeAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
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
}
