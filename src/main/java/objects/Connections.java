package objects;

import java.util.ArrayList;

import kfirmadoel.Connection;
import kfirmadoel.server_side.Server;

public class Connections {
    private Server server;
    private ArrayList<Connection> connectionList;

    public Connections(Server server) {
        this.server=server;
        connectionList=new ArrayList<Connection>();
    }

}
