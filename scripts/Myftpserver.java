
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.List;


/**
 * This is the driver class.
 *
 */
public class Myftpserver {


    public static void main(String[] args) throws IOException {
        Ftpserver ftpserver = new Ftpserver();
        printBanner();
        System.out.println("FTP port: 2121");
        ftpserver.run();
        //ftpserver.close();

    }

    private static void printBanner() {
        System.out.println("\t******************************************\n" +
                "\t*****         FTP Server            ******\n" +
                "\t******************************************\n" +
                "\t Press Ctl+c to exit."
        );
    }

}

/**
 * This is the server class.
 *
 */
class Ftpserver {
    final int serverPort = 2121;
    ServerSocket skt = new ServerSocket(serverPort);
    //BufferedReader msgFromClient;
    //PrintWriter msgToClient;
    DataInputStream msgFromClient;
    DataOutputStream msgToClient;
    String cur_path = System.getProperty("user.dir");

    Ftpserver() throws IOException { }

    public void run() throws IOException {
        Socket acceptedSkt;

        // try connect
        try {
            acceptedSkt = skt.accept();
            //msgFromClient = new BufferedReader(new InputStreamReader(acceptedSkt.getInputStream()));
            //msgToClient = new PrintWriter(acceptedSkt.getOutputStream(), true);
            msgFromClient = new DataInputStream( new BufferedInputStream(acceptedSkt.getInputStream()));
            msgToClient = new DataOutputStream( acceptedSkt.getOutputStream() );
            System.out.println("New client connected.");
        } catch (IOException exc) {
            System.out.println("Connection failed.");
        }

        // execute cmd
        do {
            //msgFromClient = new BufferedReader(new InputStreamReader(acceptedSkt.getInputStream()));
            //msgToClient = new PrintWriter(acceptedSkt.getOutputStream(), true);
            String recMsg = msgFromClient.readUTF();     // received msg
            cmdInterface(recMsg);
            //msgToClient.flush();
            //msgFromClient.reset();
        } while ( true );          // set to false for 1 time test purpose

        //acceptedSkt.close();
        //msgFromClient.close();
    }

    // all supported cmd
    private void cmdInterface(String input) throws IOException {
        String cmd = "";
        String path = "";
        // split and assign input to cmd and path
        input.trim();
        if ( input.contains(" ") ) {
            String[] inputSplited = input.split(" ");
            cmd = inputSplited[0];
            path = inputSplited[1];
        } else {
            cmd = input;
        }

        // check cmd
        switch (cmd) {
            default:
                System.out.println("Command not found! Try again...");
            case "get":
                cmdGet(path);
            case "put":
                cmdPut(path);
            case "delete":
                cmdDelete(path);
            case "ls":
                cmdLs(path);
            case "cd":
                cmdCd(path);
            case "mkdir":
                cmdMkdir(path);
            case "pwd":                  // done
                cmdPwd();
            case "quit":
                cmdQuit();
        }

    }

    private void cmdGet(String file) {
        //get file
    }

    private void cmdPut(String file) {
        //put file
    }

    private void cmdDelete(String file) {
        //delete file
    }

    private void cmdLs(String path) {
        //ls path
    }

    private void cmdCd(String path) throws IOException {
        //cd path
        cur_path =  path.trim() + "/" ;
        msgToClient.writeUTF( cur_path );
    }

    private void cmdMkdir(String path) {
        //mkdir path
    }

    private void cmdPwd() throws IOException {
        //pwd path
        System.out.println( cur_path );
        msgToClient.writeUTF( cur_path );
    }

    private void cmdQuit() throws IOException {

        msgToClient.writeUTF("Bye!");
        //msgToClient.close();
        //msgFromClient.close();
        skt.close();
    }

    public void close() throws IOException {
        skt.close();
    }


}



