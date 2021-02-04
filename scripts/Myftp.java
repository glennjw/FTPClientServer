
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.io.File;
import java.util.Scanner;
import java.io.PrintWriter;
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
    private Socket socket;
    private PrintWriter recSkt;
    private BufferedReader sendSkt;
    private boolean openPort = true;

    public static void main(String[] args) throws IOException {
        Myftp myftp = new Myftp();
        myftp.welcomeBanner();           // print welcome msg
        myftp.inputServerInfo();         // input server info
        // connect server
        try {
            // connect to server
            myftp.socket = new Socket(myftp.serverName, myftp.serverPort);
            // read server output
            myftp.recSkt = new PrintWriter( myftp.socket.getOutputStream(), true);
            // send to server
            myftp.sendSkt = new BufferedReader( new InputStreamReader( myftp.socket.getInputStream() ) );
            // login succeed

            myftp.recSkt.println("ls");
            //myftp.recSkt.println("");
            System.out.println("===================");
            String recString = myftp.sendSkt.readLine();
            System.out.println("*******************");
            do {
                System.out.println("start receiving...");
                System.out.println(recString);
                recString = myftp.sendSkt.readLine();
            } while ( false );

        } catch (IOException eIO) {
            myftp.printlnMsg("Connection failed. Please check config or network.");
            //eIO.printStackTrace();
        }

        // send cmd
        Scanner cmdRec = new Scanner(System.in);
        do {
            // give cmd
            myftp.printMsg("myftp> ");
            String cmd = cmdRec.nextLine().trim().toLowerCase();

            // handle cmd
            myftp.cmdInterface( cmd );

        } while ( myftp.openPort );

        // close connection
        myftp.recSkt.close();
        myftp.sendSkt.close();
        myftp.socket.close();

    }


    // all supported cmd
    private void cmdInterface(String input) {
        String cmd, path;
        cmd = input.split(" ")[0];
        if (!(null==input.split(" ")[1])) path = null;
        // check cmd
        switch (cmd) {
            case "get":
                cmdGet();
            case "put":
                cmdPut();
            case "delete":
                cmdDelete();
            case "ls":
                cmdLs();
            case "cd":
                cmdCd();
            case "mkdir":
                cmdMkdir();
            case "pwd":
                cmdPwd();
            case "quit":
                cmdQuit();
            default:
                System.out.println("Command not found! Try again...");
        }

    }


    // cmd ls
    private void cmdLs() {
        // client ls
        printlnMsg(System.getProperty("user.dir"));
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
        recSkt.println("pwd");
    }

    // cmd cd
    private void cmdCd() {

    }

    // cmd mkdir
    private void cmdMkdir() {

    }

    // cmd quit
    private void cmdQuit() {
        openPort = false;
    }

    private void welcomeBanner() {
        printlnMsg("\t******************************************\n" +
                 "\t*****          FTP Client           ******\n" +
                 "\t******************************************\n" +
                 "\tSupported commands: pwd, ls, get, put, delete, cd, mkdir, quit\n");
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
            PrintWriter recSkt = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException eIO) {
            printlnMsg("Connection failed");
            eIO.printStackTrace();
        }

    }

}



