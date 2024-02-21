package objects;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import kfirmadoel.server_side.Parents;

public class ParentCon {
    private String email;
    private Parents parents;
    private Socket mainActionSocket;
    private ServerSocket mainServerSocket;
    private ArrayList<String> ChikdNames = new ArrayList<>();

    public ParentCon(ServerSocket mainServerSocket, Parents parents) {
        this.mainServerSocket = mainServerSocket;
        this.parents = parents;
        // this.mainActionSocket=mainActinSocket;
        // port = mainServerSocketForParent.getLocalPort();
        try {
            mainActionSocket = mainServerSocket.accept();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleIncomeMessages() {
        System.out.println("start to handle action connection");
        String action = null;
        DataInputStream actionInputStream = null;
        try {
            // Set up communication streams
            actionInputStream = new DataInputStream(mainActionSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (mainActionSocket != null && !mainActionSocket.isClosed()) {
            try {
                // Set up communication streams
                action = actionInputStream.readUTF();
                handleCommand(action);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    

    public void handleCommand(String command)
    {
        System.out.println("enter the loop of the action handel");
            switch (command) {
                case "open screen share":
                    openNewConnection();
                    break;
                case "close connection":
                    closeConnection();
                    break;
                
                // additional cases as needed
                default:
                    System.out.println("command dont support");
                    // code to be executed if none of the cases match
    }
}

    private void closeConnection() {
        closeActionConnection();
        closeServerSocket();
        parents.removeConnection(this);
        
    }

    public void closeActionConnection()
    {
        try
        {
            if (mainActionSocket != null && !mainActionSocket.isClosed())
            {
                mainActionSocket.close();
                System.out.println("Closed the actionSocket");
                mainActionSocket = null;
            }

        } catch (IOException ex)
        {
            Logger.getLogger(ParentCon.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public void closeServerSocket()
    {
        try
        {
            if (mainServerSocket != null && !mainServerSocket.isClosed())
            {
                mainServerSocket.close();
                System.out.println("Closed the actionSocket");
                mainServerSocket = null;
            }

        } catch (IOException ex)
        {
            Logger.getLogger(ParentCon.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
