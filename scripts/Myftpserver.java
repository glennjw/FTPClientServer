
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;


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
    }

    private static void printBanner() {
        System.out.println("\t******************************************\n" +
                "\t*****         FTP Server            ******\n" +
                "\t******************************************\n" +
                "\t Press Ctl+c to leave."
        );
    }

}

/**
 * This is the server class.
 *
 */
class Ftpserver {
    final int serverPort = 2121;

    public void run() throws IOException {
        // assign port
        ServerSocket skt = new ServerSocket(serverPort);
        Socket acceptedSkt = skt.accept();
        //msgFromClient = new DataInputStream( new BufferedReader( skt.getInputStream() ) );
        BufferedReader msgFromClient = new BufferedReader(new InputStreamReader(acceptedSkt.getInputStream()));
        PrintWriter msgToClient = new PrintWriter(acceptedSkt.getOutputStream(), true);
        System.out.println("New client connected.");

        // start port
        do {
            String recMsg = msgFromClient.readLine();     // received msg

        } while (false);
    }

    // all supported cmd
    private void cmdInterface(String input) {
        String cmd, path;
        cmd = input.split(" ")[0];
        if (!(null==input.split(" ")[1])) path = null;
        // check cmd
        switch (cmd) {
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
            case "pwd":
                cmdPwd();
            case "quit":
                cmdQuit();
            default:
                System.out.println("Command not found! Try again...");
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

    private void cmdCd(String path) {
        //cd path
    }

    private void cmdMkdir(String path) {
        //mkdir path
    }

    private void cmdPwd() {
        //pwd path
    }

    private void cmdQuit() {

    }
}



