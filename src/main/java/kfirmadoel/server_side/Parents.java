package kfirmadoel.server_side;

import java.net.ServerSocket;
import java.util.ArrayList;

import kfirmadoel.server_side.services.ParentConService;
import objects.ParentCon;

public class Parents {
    private static ArrayList<ParentCon> parentsList;
    private static Server server;

    public Parents(Server server) {
        this.server = server;

        parentsList = new ArrayList<ParentCon>();
    }

    public void add(ServerSocket serverSocket) {
        synchronized (parentsList) {
            parentsList.add(new ParentCon(serverSocket,this));
        }
    }

    public ParentCon removeByIp(String ip) {
        synchronized (parentsList) {
            for (int i = 0; i < parentsList.size(); i++) {
                if ((parentsList.get(i)).getIp().equals(ip))
                    return parentsList.get(i);
            }
        }
        return null;
    }


    public boolean isParentExsistByIp(String ip) {
        synchronized (parentsList) {
            for (int i = 0; i < parentsList.size(); i++) {
                if ((parentsList.get(i)).getIp().equals(ip))
                    return true;
            }
        }
        return false;
    }

    public ParentCon getParentByIp(String ip)
    {
        synchronized (parentsList) {
            for (int i = 0; i < parentsList.size(); i++) {
                if ((parentsList.get(i)).getIp().equals(ip))
                    return parentsList.get(i);
            }
        }
        return null;
    }

    public void removeConnection(ParentCon parentCon) {
        synchronized(parentsList)
        {
            parentsList.remove(parentCon);
        }
    }
}
