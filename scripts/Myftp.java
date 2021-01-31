
import java.io.IOException;
import java.net.Socket;
import java.io.File;
import java.util.Scanner;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.nio.file.Path;


/**
 * This is the ftp client class.
 * @author
 */

public class Myftp {

    private String serverName;
    private Integer serverPort;

    public static void main(String[] args) throws IOException {
        Myftp myftp = new Myftp();
        myftp.welcomeBanner();           // print welcome msg
        myftp.inputServerInfo();         // input server info
        myftp.connectServer(myftp.serverName, myftp.serverPort);   // connect server


        // cmd
        Scanner cmdRec = new Scanner(System.in);
        do {
            // receive cmd
            myftp.printMsg("myftp> ");
            String cmd = cmdRec.nextLine().trim().toLowerCase();
            myftp.cmdInterface( cmd );

            // handle cmd


        } while ( false );


    }


    // all supported cmd
    private void cmdInterface(String cmd) {
        // check cmd
        if (cmd.equals("get")) {
            cmdGet();
        } else if (cmd.equals("put")) {
            cmdPut();
        } else if (cmd.equals("delete")) {
            cmdDelete();
        } else if (cmd.equals("ls")) {
            cmdLs();
        } else if (cmd.equals("cd")) {
            cmdCd();
        } else if (cmd.equals("mkdir")) {
            cmdMkdir();
        } else if (cmd.equals("pwd")) {
            cmdPwd();
        } else if (cmd.equals("quit")) {
            cmdQuit();
        } else {
            printMsg("Command not found! Try again...");
        }

    }


    // cmd ls
    private void cmdLs() {

    }

    // cmd get
    private void cmdGet() {

    }

    // cmd put
    private void cmdPut() {

    }

    // cmd delete
    private void cmdDelete() {

    }

    // cmd pwd
    private void cmdPwd() {

    }

    // cmd cd
    private void cmdCd() {

    }

    // cmd mkdir
    private void cmdMkdir() {

    }

    // cmd cd
    private void cmdQuit() {

    }

    private void welcomeBanner() {
        printlnMsg("\t******************************************\n" +
                 "\t*****          FTP Client           ******\n" +
                 "\t******************************************\n");
    }

    private void printlnMsg(String msg) {
        System.out.println(msg);
    }
    private void printMsg(String msg) {
        System.out.print(msg);
    }

    private void setServerName(String name) {
        this.serverName = name;
    }

    private void setServerPort(Integer port) {
        this.serverPort = port;
    }

    private void inputServerInfo() {
        boolean ifAgain;
        // Get server name and port from input
        do {
            Scanner inputs = new Scanner(System.in);
            printMsg("Server name: ");
            String inputServerName = inputs.nextLine();
            printMsg("Server port: ");
            Integer inputServerPort = inputs.nextInt();
            // check input legality
            ifAgain = (0 < inputServerPort &&
                       65353 > inputServerPort &&
                       !inputServerName.isEmpty()
                      ) ? false : true;

            // global values
            if (!ifAgain) {
                this.setServerName(inputServerName);
                this.setServerPort(inputServerPort);
            } else {
                printlnMsg("Illegal input! Try again");
            }
        } while ( ifAgain );

    }

    private void connectServer(String serverName, Integer serverPort) throws IOException {
        // connect server
        try {
            Socket socket = new Socket(serverName, serverPort);
            PrintStream recSkt = new PrintStream(socket.getOutputStream(), true);
        } catch (IOException eIO) {
            printlnMsg("Connection failed");
            eIO.printStackTrace();
        }

    }

}



