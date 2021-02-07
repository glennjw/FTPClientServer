
import java.io.*;
import java.net.Socket;
import java.util.Scanner;


/**
 * This is the ftp client class.
 *
 * @author
 */

public class Myftp {

    private String serverName;
    private Integer serverPort;
    private Socket socket;
    String msgToServer;
    //private PrintWriter sendSkt;
    //private BufferedReader recSkt;
    //private DataOutputStream sendSkt;
    //private DataInputStream recSkt;
    private boolean openPort = true;    // keep client running

    public static void main(String[] args) throws IOException {
        Myftp myftp = new Myftp();
        myftp.welcomeBanner();           // print welcome msg
        myftp.inputServerInfo();         // input server info
        // connect server
        try {
            // connect to server
            myftp.socket = new Socket(myftp.serverName, myftp.serverPort);

            //myftp.recSkt = new DataInputStream( new BufferedInputStream(myftp.socket.getInputStream()));
            //myftp.sendSkt = new DataOutputStream( myftp.socket.getOutputStream() );

        } catch (IOException eIO) {
            myftp.printlnMsg("Connecting failed. Please check config or network.");
            //eIO.printStackTrace();
        }
        PrintWriter sendSkt = new PrintWriter(myftp.socket.getOutputStream(), true);
        BufferedReader recSkt = new BufferedReader(new InputStreamReader(myftp.socket.getInputStream()));
        // send cmd
        Scanner cmdRec = new Scanner(System.in);

        do {
            // input cmd
            myftp.printMsg("myftp> ");
            String input = cmdRec.nextLine();

            // execute cmd
            myftp.cmdInterface(input);
            if ( "".equalsIgnoreCase(myftp.msgToServer)) {
                System.out.println("Invalid command.");
            } else {
                sendSkt.println(myftp.msgToServer);
                System.out.println(recSkt.readLine());
            }
        } while (myftp.openPort);

        myftp.close();

    }


    // all supported cmd
    private void cmdInterface(String input) throws IOException {
        String cmd = "";
        String path = "";
        // split and assign input to cmd and path
        input = input.trim();
        if (input.contains(" ")) {
            String[] inputSplited = input.split(" ");
            cmd = inputSplited[0].toLowerCase();
            path = inputSplited[1];
        } else {
            cmd = input;
        }

        // check cmd
        switch (cmd) {
            default:
                System.out.println("Command not found! Try again...");
            case "get":
                cmdGet();
                break;
            case "put":
                cmdPut();
                break;
            case "delete":
                cmdDelete();
                break;
            case "ls":
                cmdLs(path);
                break;
            case "cd":
                if ("".equalsIgnoreCase(path)) {tryagain();} else {cmdCd(path);}
                break;
            case "mkdir":
                cmdMkdir(path);
                break;
            case "pwd":
                cmdPwd();
                break;
            case "quit":
                cmdQuit();
                break;
        }

    }


    // cmd ls
    private void cmdLs(String path) {
        // client ls
        msgToServer = "ls" + " " + path;
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
    private void cmdPwd() throws IOException {
        msgToServer = "pwd";
    }

    // cmd cd path
    private void cmdCd(String path) {
        msgToServer = "cd"+" "+path;
    }

    // cmd mkdir
    private void cmdMkdir(String path) {
        msgToServer = "mkdir"+" "+path;
    }

    // cmd quit
    private void cmdQuit() throws IOException {
        openPort = false;
        msgToServer = "quit";
    }

    // try again
    private void tryagain() {
        msgToServer = "";
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
        } while (ifAgain);

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

    public void close() throws IOException {
        socket.close();
    }


}



