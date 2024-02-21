package kfirmadoel.server_side.documents;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "parentsCon")
public class ParentConInfo {
    @Id
    private String email;
    private int connectionCode;
    private ArrayList<childForPar> childsMac;

    public ParentConInfo(String email, int connectionCode) {
        this.email = email;
        this.connectionCode = connectionCode;
        childsMac = new ArrayList();
    }

    public String getEmail() {
        return email;
    }

    public int getConnectionCode() {
        return connectionCode;
    }

    public ArrayList<childForPar> getChildsMac() {
        return childsMac;
    }

    public void setConnectionCode(int connectionCode) {
        this.connectionCode = connectionCode;
    }

    public void addChild(childForPar child) {
        synchronized (childsMac) {
            childsMac.add(child);
        }
    }

    public void removeChild(String mac) {
        synchronized (childsMac) {
            for (int i = 0; i < childsMac.size(); i++) {
                if (childsMac.get(i).getMacAddr().equals(mac)) {
                    childsMac.remove(i);
                    return;
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("parentsCon [macAddr=").append(macAddr)
                .append(", connectionCode=").append(connectionCode)
                .append(", childsMac=");

        for (int i = 0; i < childsMac.size(); i++) {
            sb.append(childsMac.get(i));
            if (i < childsMac.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");

        System.out.println(sb.toString());
        return sb.toString();
    }

}
