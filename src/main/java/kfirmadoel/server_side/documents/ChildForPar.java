package kfirmadoel.server_side.documents;

import org.springframework.data.annotation.Id;

public class ChildForPar {
    @Id
    private String macAddr;
    private String name;

    public ChildForPar(String macAddr, String name) {
        this.macAddr = macAddr;
        this.name = name;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("childForPar [macAddr=%s, name=%s]", macAddr, name);
    }
}


