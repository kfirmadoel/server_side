package kfirmadoel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import objects.Connections;
import objects.KeyboardButton;
import objects.MouseOpetions;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connection {
    public static final int defaultHeight = 1080;
    public static final int defaultWidth = 1920;
    private Connections connections;
    private Socket parentActionSocket;
    private Socket parentPhotoSocket;
    private Socket parentKeyboardSocket;
    private Socket parentMouseSocket;
    private Socket childActionSocket;
    private Socket childPhotoSocket;
    private Socket childKeyboardSocket;
    private Socket childMouseSocket;
    private boolean isScreenShared;
    private boolean isUnderControl;
    private int childHeightResolution;
    private int childWidthResolution;

    public Connection(int childHeightResolution,
            int childWidthResolution, Socket parentActionSocket, Socket parentPhotoSocket, Socket parentKeyboardSocket,
            Socket parentMouseSocket, Socket childActionSocket, Socket childPhotoSocket, Socket childKeyboardSocket,
            Socket childMouseSocket, Connections connections) {
        this.childHeightResolution = childHeightResolution;
        this.childWidthResolution = childWidthResolution;
        this.connections = connections;
        this.parentActionSocket = parentActionSocket;
        this.parentPhotoSocket = parentPhotoSocket;
        this.parentKeyboardSocket = parentKeyboardSocket;
        this.parentMouseSocket = parentMouseSocket;
        this.childActionSocket = childActionSocket;
        this.childPhotoSocket = childPhotoSocket;
        this.childKeyboardSocket = childKeyboardSocket;
        this.childMouseSocket = childMouseSocket;
        this.isScreenShared = false;
        this.isUnderControl = false;
        handleActionTransfer();
        handlePhotoTransfer();
        handleMouseTransfer();
        handleMouseTransfer();
    }

    public void startScreenShare() {
        this.isScreenShared = true;
    }

    public void stopScreenShare() {
        this.isScreenShared = false;
    }

    public void startcontrol() {
        this.isUnderControl = true;
    }

    public void stopcontrol() {
        this.isUnderControl = false;
    }

    public void handleActionTransfer() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("start to handle action connection");
                String action = null;
                DataInputStream actionInputStream = null;
                try {
                    // Set up communication streams
                    actionInputStream = new DataInputStream(parentActionSocket.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while (parentActionSocket != null && !parentActionSocket.isClosed() && childActionSocket != null
                        && !childActionSocket.isClosed()) {
                    try {
                        // Set up communication streams
                        action = actionInputStream.readUTF();
                        handleIncomeAction(action);
                    } catch (IOException e) {
                        e.printStackTrace();
                        closeConnection();
                    }
                }

            }
        });
        thread.start();

    }

    public void handleIncomeAction(String action) {
        System.out.println("enter the loop of the action handel");
        switch (action) {
            case "start screen share":
                startScreenShare();
                break;
            case "give control":
                startcontrol();
                break;
            case "stop screen share":
                startScreenShare();
                break;
            case "stop give control":
                stopcontrol();
                break;

            // additional cases as needed
            default:
                System.out.println("command dont support");
                // code to be executed if none of the cases match
        }
    }

    public void handlePhotoTransfer() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int imageSize;
                    DataInputStream incomePhotoStreamFromChild = new DataInputStream(childPhotoSocket.getInputStream());
                    DataOutputStream outPhotoStreamToParent = new DataOutputStream(parentPhotoSocket.getOutputStream());

                    while (parentPhotoSocket != null && !parentPhotoSocket.isClosed() &&
                            childPhotoSocket != null && !childPhotoSocket.isClosed()) {

                        imageSize = incomePhotoStreamFromChild.readInt();

                        // Receive the image data
                        byte[] imageData = new byte[imageSize];
                        incomePhotoStreamFromChild.readFully(imageData);

                        if (isScreenShared && parentPhotoSocket != null && !parentPhotoSocket.isClosed()) {
                            synchronized (outPhotoStreamToParent) {
                                // Send the size of the image data
                                outPhotoStreamToParent.writeInt(imageData.length);
                                outPhotoStreamToParent.flush();
                                // Send the image data
                                outPhotoStreamToParent.write(imageData);
                                outPhotoStreamToParent.flush();
                                System.out.println("send photo to parent");
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                }
            }
        });
        thread.start();
    }

    public void handleMouseTransfer() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                MouseOpetions recivedkey;
                try (ObjectInputStream incomeMouseStreamFromParent = new ObjectInputStream(
                        parentMouseSocket.getInputStream());
                        ObjectOutputStream outMouseStreamToChild = new ObjectOutputStream(
                                childMouseSocket.getOutputStream())) {

                    while (parentMouseSocket != null && !parentMouseSocket.isClosed() &&
                            childMouseSocket != null && !childMouseSocket.isClosed()) {

                        recivedkey = (MouseOpetions) incomeMouseStreamFromParent.readObject();
                        System.out.println("got recivedkey of mouse");

                        if (isUnderControl && childMouseSocket != null && !childMouseSocket.isClosed()) {
                            synchronized (outMouseStreamToChild) {
                                // 1080*1920
                                recivedkey.setHeight(recivedkey.getHeight() * (childHeightResolution / defaultHeight));
                                recivedkey.setWidth(recivedkey.getWidth() * (childWidthResolution / defaultWidth));
                                outMouseStreamToChild.writeObject(recivedkey);
                                outMouseStreamToChild.flush();
                                System.out.println("send mouse to child");
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                }
            }
        });
        thread.start();
    }

    public void handleKeyboardTransfer() {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                KeyboardButton recivedkey;
                try (ObjectInputStream incomeKeyboardStreamFromParent = new ObjectInputStream(
                        parentKeyboardSocket.getInputStream());
                        ObjectOutputStream outKeyboardStreamToChild = new ObjectOutputStream(
                                childKeyboardSocket.getOutputStream())) {
        
                    while (parentMouseSocket != null && !parentMouseSocket.isClosed() &&
                            childMouseSocket != null && !childMouseSocket.isClosed()) {
        
                        recivedkey = (KeyboardButton) incomeKeyboardStreamFromParent.readObject();
                        System.out.println("got recivedkey of mouse");
        
                        if (isUnderControl && childMouseSocket != null && !childMouseSocket.isClosed()) {
                            synchronized (outKeyboardStreamToChild) {
                                outKeyboardStreamToChild.writeObject(recivedkey);
                                outKeyboardStreamToChild.flush();
                                System.out.println("send mouse to child");
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                }        
            }
        });
        thread.start();
    }

    public void closeParentActionSocket() {
        try {
            if (parentActionSocket != null && !parentActionSocket.isClosed()) {
                parentActionSocket.close();
                System.out.println("Closed the action socket of parent");
                parentActionSocket = null;
            }

        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeParentPhotoSocket() {
        try {
            if (parentPhotoSocket != null && !parentPhotoSocket.isClosed()) {
                parentPhotoSocket.close();
                System.out.println("Closed the photo socket of parent");
                parentPhotoSocket = null;
            }

        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeParentMouseSocket() {
        try {
            if (parentMouseSocket != null && !parentMouseSocket.isClosed()) {
                parentMouseSocket.close();
                System.out.println("Closed the mouseSocket of parent");
                parentMouseSocket = null;
            }

        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeParentKeyboardSocket() {
        try {
            if (parentKeyboardSocket != null && !parentKeyboardSocket.isClosed()) {
                parentKeyboardSocket.close();
                System.out.println("Closed the mouseSocket of parent");
                parentKeyboardSocket = null;
            }

        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeChildActionSocket() {
        try {
            if (childActionSocket != null && !childActionSocket.isClosed()) {
                childActionSocket.close();
                System.out.println("Closed the action socket of child");
                childActionSocket = null;
            }

        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeChildPhotoSocket() {
        try {
            if (childPhotoSocket != null && !childPhotoSocket.isClosed()) {
                childPhotoSocket.close();
                System.out.println("Closed the photo socket of child");
                childPhotoSocket = null;
            }

        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeChildMouseSocket() {
        try {
            if (childMouseSocket != null && !childMouseSocket.isClosed()) {
                childMouseSocket.close();
                System.out.println("Closed the mouseSocket of child");
                childMouseSocket = null;
            }

        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeChildKeyboardSocket() {
        try {
            if (childKeyboardSocket != null && !childKeyboardSocket.isClosed()) {
                childKeyboardSocket.close();
                System.out.println("Closed the mouseSocket of child");
                childKeyboardSocket = null;
            }

        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeConnection() {
        connections.removeConnection(this);
    }
}
