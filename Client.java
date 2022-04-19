package sample;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Client extends Thread{

    Socket connection = null;

    public Client(String server,int port) throws Exception{
        connection = new Socket(server,port);
    }
    public void handleConnection(){
        while(true){
            try{
                ObjectInputStream data = new ObjectInputStream(connection.getInputStream());
                String method = data.readUTF();
                System.out.println("[+]Command "+ method+" Received");
                if(method.equals("shutdown")){
                    System.out.println("shutdown");
                    Runtime runtime = Runtime.getRuntime();
                    Process proc = runtime.exec("shutdown -s -t 0");
                    data.close();
                    System.exit(0);
                }
            }catch(Exception ignored){

            }
        }


    }
    public void closeConnection()throws Exception{
        connection.close();
        System.out.println("[+]Connection Closed Successfully");
    }

    @Override
    public void run() {
        try {
            while(true){
                handleConnection();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
