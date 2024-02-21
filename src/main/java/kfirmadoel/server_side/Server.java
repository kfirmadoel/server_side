package kfirmadoel.server_side;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import kfirmadoel.server_side.documents.ChildForPar;
import kfirmadoel.server_side.documents.ChildInfo;
import kfirmadoel.server_side.services.ChildInfoService;
import kfirmadoel.server_side.services.ParentConService;
import kfirmadoel.server_side.services.UserService;
import objects.ChildCon;
import objects.Connections;
import objects.ParentCon;

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

    public Server(UserService userService) {
        parents = new Parents(this);
        childs = new Childs(this);
        connections = new Connections(this);
        openWelcomeSocketForParent();
        openWelcomeSocketForChild();
    }

    private void openWelcomeSocketForChild() {
        try {
            welcomeSocketChild = new ServerSocket(MAIN_PORT_FOR_CHILD);
            System.out.println(String.format("Welcome server of the parent is listening on port %d...", MAIN_PORT_FOR_CHILD));
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
            System.out.println(String.format("Welcome server of the parent is listening on port %d...", MAIN_PORT_FOR_PARENT));
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
                InputStream in;
                int port = 0;
                // Create a socket connection
                while (true) {
                    try {
                        connectionSocket = welcomeSocketParent.accept();
                        out = new DataOutputStream(connectionSocket.getOutputStream());
                        in = connectionSocket.getInputStream();

                        // Read a line of text from the input stream
                        //if (!parents.isParentExsistByIP(connectionSocket.getInetAddress().getHostAddress())) {
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
                        //} else {
                        //   out.writeUTF("person already connect");
                        //}
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
        if(childCon==null)
        {
            System.out.println("child disconnected");
            return;
        }
          ArrayList<Socket> childSockets=childCon.getSocketsForConnection();
          if(childSockets!=null&&childSockets.size()==4)
          {
            return;
          }
          connections.addConnection(childCon.getHeightSize(), childCon.getWidthSize(), actionSocket, photoSocket, keyboardSocket, mouseSocket, childSockets.get(0), photoSocket, keyboardSocket, mouseSocket);
    }
}
