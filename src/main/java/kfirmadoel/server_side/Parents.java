package kfirmadoel.server_side;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import kfirmadoel.server_side.documents.ParentConInfo;
import kfirmadoel.server_side.documents.User;
import kfirmadoel.server_side.objects.ParentCon;

public class Parents {
    private  ArrayList<ParentCon> parentsList;
    private  Server server;

    public Parents(Server server) {
        this.server = server;

        parentsList = new ArrayList<ParentCon>();
    }

    public void add(ServerSocket serverSocket) {
        synchronized (parentsList) {
            parentsList.add(new ParentCon(serverSocket,this));
        }
    }

    public void removeConnection(ParentCon parentCon) {
        synchronized(parentsList)
        {
            parentsList.remove(parentCon);
        }
    }

    public void openConnection(String email,String name, Socket actionSocket, Socket photoSocket, Socket keyboardSocket,
            Socket mouseSocket) {
        server.openConnection(email,name,actionSocket,photoSocket,keyboardSocket,mouseSocket);
    }

    public boolean doesEmailAndPasswordCorrect(User user) {
       return server.doesEmailAndPasswordCorrect(user);
    }

    public boolean doesEmailExsist(String email) {
        return server.doesEmailExsistInUsers(email);
    }

    public void addNewUser(User user) {
        addNewUser(user);
    }

    public ParentConInfo getParentConInfoByEmail(String email) {
        return server.getParentConInfoByEmail(email);
    }
}
