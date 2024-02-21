package kfirmadoel.server_side.documents;

import org.springframework.data.annotation.Id;

public class ChildForPar {
    @Id
    private String macAddr;
    private String name;

    public childForPar(String macAddr, String name) {
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
        return "childForPar [macAddr=" + macAddr + ", name=" + name + "]";
    }
   
    
}
