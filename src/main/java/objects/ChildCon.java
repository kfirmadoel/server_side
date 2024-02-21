package objects;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import kfirmadoel.server_side.Childs;
import kfirmadoel.server_side.Server;
import kfirmadoel.server_side.documents.ChildInfo;
import kfirmadoel.server_side.services.ChildInfoService;

public class ChildCon {
    private Childs childs;
    private ServerSocket serverSocket;
    private Socket mainActinSocket;
    private String macAddr;
    private String ipAddr;
    private int heightSize;
    private int widthSize;

    public ChildCon(ServerSocket serverSocket, Childs childs) {
        this.serverSocket = serverSocket;
        this.childs=childs;
        mainActinSocket=null;
        try {
            mainActinSocket = serverSocket.accept();
            // Get InputStream from Socket
            ObjectInputStream objectInputStream = new ObjectInputStream(mainActinSocket.getInputStream());

            // Receive Object
            Object receivedObject = objectInputStream.readObject();

            // Cast the received object to your desired type
            if (receivedObject instanceof ChildInfo) {
                ChildInfo childInfon = (ChildInfo) receivedObject;
                this.macAddr = childInfon.getMacAddr();
                this.ipAddr = childInfon.getIpAddr();
                childs.updateChildInfo(childInfon);
            }
            
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    // private void listenToIncomeActions() {
    //    Thread thread=new Thread(new Runnable() {
    //     @Override
    //     public void run() {
    //         while (mainActinSocket!=null&&!mainActinSocket.isClosed()) {
                
    //         }
    //     }
    //    });
    // }

    public void sendOverMainActionSocket(String msg) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (mainActinSocket) {
                    try {
                        // Get OutputStream from the main socket
                        OutputStream outputStream = mainActinSocket.getOutputStream();

                        // Send String
                        PrintWriter printWriter = new PrintWriter(outputStream, true); // Auto-flush enabled
                        printWriter.println(msg);

                        // No need to close PrintWriter here, as it's done automatically due to
                        // auto-flushing
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    public Socket openActionSocket()
    {
        try (Socket actionSocket = serverSocket.accept()) {
            System.out.println("child connected to action socket");
            return actionSocket;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Socket openMouseSocket()
    {
        try (Socket mouseSocket = serverSocket.accept()) {
            System.out.println("child connected to mouse socket");
            return mouseSocket;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Socket openKeyboardSocket()
    {
        try (Socket keyboardSocket = serverSocket.accept()) {
            System.out.println("child connected to keyboard socket");
            return keyboardSocket;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Socket openPhotoSocket()
    {
        try (Socket photoSocket = serverSocket.accept()) {
            System.out.println("child connected to photo socket");
            return photoSocket;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void closeServerSocket()
    {
        if(serverSocket!=null&&!serverSocket.isClosed())
        {
            try {
                serverSocket.close();
                System.out.println("closed the server socket");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeMainActionSocket()
    {
        if(mainActinSocket!=null&&!mainActinSocket.isClosed())
        {
            try {
                mainActinSocket.close();
                System.out.println("closed the main action socket");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public int getHeightSize() {
        return heightSize;
    }

    public void setHeightSize(int heightSize) {
        this.heightSize = heightSize;
    }

    public int getWidthSize() {
        return widthSize;
    }

    public void setWidthSize(int widthSize) {
        this.widthSize = widthSize;
    }

    
}
