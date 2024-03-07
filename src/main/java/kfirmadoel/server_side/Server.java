package kfirmadoel.server_side;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.springframework.stereotype.Component;

import kfirmadoel.server_side.documents.ChildForPar;
import kfirmadoel.server_side.documents.ChildInfo;
import kfirmadoel.server_side.documents.ParentConInfo;
import kfirmadoel.server_side.documents.User;
import kfirmadoel.server_side.objects.ChildCon;
import kfirmadoel.server_side.objects.Connections;
import kfirmadoel.server_side.services.ChildInfoService;
import kfirmadoel.server_side.services.ParentConService;
import kfirmadoel.server_side.services.UserService;

@Component
public class Server {
    private static final int MAIN_PORT_FOR_PARENT = 12345;
    private static final int MAIN_PORT_FOR_CHILD = 54321;
    private ServerSocket welcomeSocketParent;
    private ServerSocket welcomeSocketChild;
    private Parents parents;
    private Childs childs;
    private Connections connections;
    private ParentConService parentConService;
    private ChildInfoService childInfoService;
    private UserService userService;

    public Server(ParentConService parentConService, ChildInfoService childInfoService, UserService userService) {
        this.parentConService = parentConService;
        this.childInfoService = childInfoService;
        this.userService = userService;
        parents = new Parents(this);
        childs = new Childs(this);
        connections = new Connections(this);
        openWelcomeSocketForParent();
        openWelcomeSocketForChild();
    }

    private void openWelcomeSocketForChild() {
        try {
            welcomeSocketChild = new ServerSocket(MAIN_PORT_FOR_CHILD);
            System.out.println(
                    String.format("Welcome server of the parent is listening on port %d...", MAIN_PORT_FOR_CHILD));
            waitToConnectionFromChild();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void waitToConnectionFromChild() {
        Thread childSocketThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Socket connectionSocket;
                DataOutputStream out;
                int port = 0;
                // Create a socket connection
                while (true) {
                    try {
                        connectionSocket = welcomeSocketChild.accept();
                        out = new DataOutputStream(connectionSocket.getOutputStream());
                        ServerSocket socket = new ServerSocket(0);
                        port = socket.getLocalPort();
                        System.out.println(String.format("Free port found: %d", port));
                        out.writeInt(port);
                        Thread thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                childs.add(socket);
                            }

                        });
                        thread.start();
                        connectionSocket.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }
        });
        // Start the thread
        childSocketThread.start();
    }

    private void openWelcomeSocketForParent() {
        try {
            welcomeSocketParent = new ServerSocket(MAIN_PORT_FOR_PARENT);
            System.out.println(
                    String.format("Welcome server of the parent is listening on port %d...", MAIN_PORT_FOR_PARENT));
            waitToConnectionFromParent();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void waitToConnectionFromParent() {
        Thread parentSocketThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Socket connectionSocket;
                DataOutputStream out;
                int port = 0;
                // Create a socket connection
                while (true) {
                    try {
                        connectionSocket = welcomeSocketParent.accept();
                        out = new DataOutputStream(connectionSocket.getOutputStream());
                        ServerSocket socket = new ServerSocket(0);
                        port = socket.getLocalPort();
                        String.format("Free port found: %d", port);
                        out.writeInt(port);
                        Thread thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                parents.add(socket);
                            }

                        });
                        thread.start();
                        connectionSocket.close();
                        // } else {
                        // out.writeUTF("person already connect");
                        // }
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }
        });
        // Start the thread
        parentSocketThread.start();
    }

    public void updateChildInfo(ChildInfo childInfon) {
        childInfoService.createChild(childInfon);
    }

    public void openConnection(String email, String name, Socket actionSocket, Socket photoSocket,
            Socket keyboardSocket,
            Socket mouseSocket) {
        ChildForPar childForPar = parentConService.getChildInfoByEmailAndName(email, name);
        ChildCon childCon = childs.getChildByMacAddr(childForPar.getMacAddr());
        if (childCon == null) {
            System.out.println("child disconnected");
            return;
        }
        ArrayList<Socket> childSockets = childCon.getSocketsForConnection();
        if (childSockets == null || childSockets.size() != 4) {

            return;
        }
        connections.addConnection(actionSocket, photoSocket,
                keyboardSocket, mouseSocket, childSockets.get(0), childSockets.get(1), childSockets.get(2), childSockets.get(3));
    }

    public boolean doesEmailAndPasswordCorrect(User user) {
        User resultUser = userService.getUserByEmail(user.getEmail());
        if (resultUser == null)
            return false;
        if (resultUser.getPassword().equals(user.getPassword()))
            return true;
        return false;
    }

    public boolean doesEmailExsistInUsers(String email) {
        User user=userService.getUserByEmail(email);
        if(user==null)
        return false;
        return true;
    }

    public void addNewUser(User user) {
        userService.createUser(user);
    }

    public ParentConInfo getParentConInfoByEmail(String email) {
         return parentConService.getParentConByEmail(email);
    }

    public void updateParentConInfo(ParentConInfo parentConInfo) {
        parentConService.updateParentCon(parentConInfo);
    }

    
}
