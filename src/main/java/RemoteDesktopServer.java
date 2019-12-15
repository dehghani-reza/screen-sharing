

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.*;
import java.net.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class RemoteDesktopServer extends JFrame implements Serializable{
    ServerSocket serverSocket;
    Socket socket;

    public RemoteDesktopServer() {
        try {
            serverSocket = new ServerSocket(2009);
            while (true) {
                socket = serverSocket.accept();
                new ChatHandeler(socket);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        RemoteDesktopServer server=new RemoteDesktopServer();


    }
}

//========================================

class ChatHandeler extends Thread {

    FileInputStream fis;
    ObjectInputStream ois;
    ImageIcon img;
    Image image;
    File file;
    JFrame frame;
    RemoteDesktopServer server;

//static Vector<ChatHandeler> clientsVector = new Vector<ChatHandeler>();

    public ChatHandeler(Socket s) {
        //server=new RemoteDesktopServer();
        file=new File("Server.png");
        frame=new JFrame();
        try {
            //scnr = new Scanner(s.getInputStream());
            ois=new ObjectInputStream(s.getInputStream());
            //clientsVector.add(this);
            start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {

                img = (ImageIcon) ois.readObject();
                image= img.getImage();
                frame.add(new MyPanel(image), BorderLayout.CENTER);
                img = null;
                image = null;
                Runtime runtime= Runtime.getRuntime();
                runtime.gc();
                frame.setSize(1300, 800);
                frame.setVisible(true);
                try {
                    sleep(1000);
                } catch (InterruptedException ex) {
                    System.out.println(" sleep  exception");
                }
                System.out.println("Read");
            } catch (ClassNotFoundException ex) {
                //ex.printStackTrace();
                System.out.println(" Class not found exception");
            } catch (IOException ex) {
                //ex.printStackTrace();
                System.out.println("IOException");
            }
        }
    }
}

class MyPanel extends JPanel {
    Image image;
    public MyPanel(Image image  ) {
        this.image=image;
        Toolkit kit = Toolkit.getDefaultToolkit(  );
        image = kit.getImage("client.png");

    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.drawImage(image, 10, 10, this);

    }


}

