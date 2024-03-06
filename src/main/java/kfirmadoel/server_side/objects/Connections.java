package kfirmadoel.server_side.objects;

import java.net.Socket;
import java.util.ArrayList;

import kfirmadoel.server_side.Connection;
import kfirmadoel.server_side.Server;

public class Connections {
    private Server server;
    private ArrayList<Connection> connectionList;

    public Connections(Server server) {
        this.server = server;
        connectionList = new ArrayList<Connection>();
    }

    public void removeConnection(Connection connection) {
        synchronized (connectionList) {
            connectionList.remove(connection);
        }
        connection.closeChildActionSocket();
        connection.closeChildKeyboardSocket();
        connection.closeChildMouseSocket();
        connection.closeChildPhotoSocket();
        connection.closeParentActionSocket();
        connection.closeParentKeyboardSocket();
        connection.closeParentMouseSocket();
        connection.closeParentPhotoSocket();
    }

    public void addConnection( Socket parentActionSocket, Socket parentPhotoSocket, Socket parentKeyboardSocket,
    Socket parentMouseSocket, Socket childActionSocket, Socket childPhotoSocket, Socket childKeyboardSocket,
    Socket childMouseSocket)
    {
        synchronized(connectionList)
        {
            connectionList.add(new Connection(parentActionSocket, parentPhotoSocket, 
            parentKeyboardSocket, parentMouseSocket, childActionSocket, childPhotoSocket, childKeyboardSocket, childMouseSocket, this));
        }
    }


}
