package kfirmadoel.server_side;

import java.net.ServerSocket;
import java.util.ArrayList;

import kfirmadoel.server_side.documents.ChildInfo;
import kfirmadoel.server_side.services.ChildInfoService;
import objects.ChildCon;

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

    public ChildCon removeByIp(String ip) {
        synchronized (childsList) {
            for (int i = 0; i < childsList.size(); i++) {
                if ((childsList.get(i)).getIp().equals(ip))
                    return childsList.get(i);
            }
        }
        return null;
    }

	public void updateChildInfo(ChildInfo childInfon) {
		server.updateChildInfo(childInfon);
        childsList.remove(childInfon)
	}

}
