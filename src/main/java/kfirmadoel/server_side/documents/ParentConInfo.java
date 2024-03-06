package kfirmadoel.server_side.documents;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "parentsCon")
public class ParentConInfo {
    @Id
    private String email;
    private ArrayList<ChildForPar> childsMac;

    public ParentConInfo(String email) {
        this.email = email;
        childsMac = new ArrayList<ChildForPar>();
    }

    public String getEmail() {
        return email;
    }


    public ArrayList<ChildForPar> getChildsMac() {
        return childsMac;
    }

    

    public void addChild(ChildForPar child) {
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
        sb.append("parentsCon [email=").append(email)
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
