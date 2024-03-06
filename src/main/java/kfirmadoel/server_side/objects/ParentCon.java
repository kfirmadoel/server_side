package kfirmadoel.server_side.objects;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import kfirmadoel.server_side.Parents;
import kfirmadoel.server_side.documents.User;

public class ParentCon {
    private String email;
    private Parents parents;
    private Socket mainActionSocket;
    private ServerSocket mainServerSocket;
    // private ArrayList<String> ChildNames = new ArrayList<>();

    public ParentCon(ServerSocket mainServerSocket, Parents parents) {
        this.mainServerSocket = mainServerSocket;
        this.parents = parents;
        // this.mainActionSocket=mainActinSocket;
        // port = mainServerSocketForParent.getLocalPort();
        try {
            mainActionSocket = mainServerSocket.accept();
            ObjectInputStream inputStream = new ObjectInputStream(mainActionSocket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(mainActionSocket.getOutputStream());
            User user = (User) inputStream.readObject();
            while (!isUserOkay(user)) {
                System.out.println("sign failed");
                outputStream.writeObject(user);
                user = (User) inputStream.readObject();
            }
            this.email = user.getEmail();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    handleIncomeMessages();
                }
            });
            thread.start();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            closeConnection();
        }

    }

    private boolean isUserOkay(User user) {
        if (user.getFirstName() == null && user.getSecondName() == null) {
            if (parents.doesEmailAndPasswordCorrect(user))
                return true;
            return false;
        }
        if (!parents.doesEmailExsist(user.getEmail())) {
            parents.addNewUser(user);
            return true;
        }
        return false;

    }

    public void handleIncomeMessages() {
        System.out.println("start to handle action connection");
        String action = null;
        DataInputStream actionInputStream = null;
        try {
            // Set up communication streams
            actionInputStream = new DataInputStream(mainActionSocket.getInputStream());

            while (mainActionSocket != null && !mainActionSocket.isClosed()) {

                // Set up communication streams
                action = actionInputStream.readUTF();
                handleCommand(action);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

    }

    public void handleCommand(String command) {
        System.out.println("Entering the action handle loop");

        // Compile the regular expression pattern outside the loop
        Pattern pattern = Pattern.compile("open new connection \\((.*?)\\)");
        Matcher matcher = pattern.matcher(command);

        if (matcher.find()) {
            String name = matcher.group(1);
            openNewConnection(name);
        } else {
            // Reuse the pattern and matcher for the next regular expression
            pattern = Pattern.compile("request to \\((.*?)\\)");
            matcher = pattern.matcher(command);

            if (matcher.find()) {
                String ip = matcher.group(1);
                System.out.println("Match found. IP address: " + ip);
            } else {
                // Handle other commands
                switch (command) {
                    case "close connection":
                        closeConnection();
                        break;
                    case "refresh":
                        refresh();
                        break;
                    default:
                        System.out.println("Command not supported");
                        // code to be executed if none of the cases match
                }
            }
        }
    }

    private void refresh() {
        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(mainActionSocket.getOutputStream());
            outputStream.writeObject(parents.getParentConInfoByEmail(this.email));
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
            closeConnection();
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openNewConnection(String name) {
        try {
            Socket actionSocket = mainServerSocket.accept();
            Socket photoSocket = mainServerSocket.accept();
            Socket keyboardSocket = mainServerSocket.accept();
            Socket mouseSocket = mainServerSocket.accept();
            parents.openConnection(email, name, actionSocket, photoSocket, keyboardSocket, mouseSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void closeConnection() {
        closeActionConnection();
        closeServerSocket();
        parents.removeConnection(this);

    }

    public void closeActionConnection() {
        try {
            if (mainActionSocket != null && !mainActionSocket.isClosed()) {
                mainActionSocket.close();
                mainActionSocket = null;
                System.out.println("Closed the actionSocket");
            }

        } catch (IOException ex) {
            Logger.getLogger(ParentCon.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void closeServerSocket() {
        try {
            if (mainServerSocket != null && !mainServerSocket.isClosed()) {
                mainServerSocket.close();
                mainServerSocket = null;
                System.out.println("Closed the actionSocket");
            }

        } catch (IOException ex) {
            Logger.getLogger(ParentCon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
