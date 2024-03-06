package kfirmadoel.server_side;

import java.net.ServerSocket;
import java.util.ArrayList;

import kfirmadoel.server_side.documents.ChildInfo;
import kfirmadoel.server_side.objects.ChildCon;

public class Childs {
    private  ArrayList<ChildCon> childsList;
    private  Server server;// keep the class server

    public Childs(Server server) {
        this.server = server;

        childsList = new ArrayList<ChildCon>();
    }

    public void add(ServerSocket serverSocket) {
        synchronized (childsList) {
            childsList.add(new ChildCon(serverSocket,this));
            // כאן אני אמור לעדכן את הילד של
        }
    }

    public ChildCon removeByMac(String macAddr) {
        synchronized (childsList) {
            for (int i = 0; i < childsList.size(); i++) {
                if ((childsList.get(i)).getMacAddr().equals(macAddr))
                return childsList.remove(i);
            }
        }
        return null;
    }

	public void updateChildInfo(ChildInfo childInfon) {
		server.updateChildInfo(childInfon);
	}

    public ChildCon getChildByMacAddr(String macAddr) {
        synchronized(childsList)
        {
            for (int i = 0; i < childsList.size(); i++) {
                if(macAddr.equals(childsList.get(i).getMacAddr()))
                return childsList.get(i);
            }
        }
        return null;
    }

}
