
import java.io.*;
import java.net.InetAddress;
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
    private String msgToServer;
    private int fileSign;                // 0:no file; 1:send file; 2:rec file;
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
                System.out.println("Invalid command or path.");
            } else {
                sendSkt.println(myftp.msgToServer);
                System.out.println(recSkt.readLine());
            }
            if (0==myftp.fileSign) {             // no file

            } else if (1==myftp.fileSign) {      // send file
                // maybe need to suspend sendSkt/recSkt.
                myftp.sendFile(myftp.msgToServer);
            } else if (2==myftp.fileSign) {      // rec file
                // maybe need to suspend sendSkt/recSkt.
                myftp.recFile(myftp.msgToServer);
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
                otherCmd(cmd);
                break;
            case "get":
                cmdGet(path);
                break;
            case "put":
                cmdPut(path);
                break;
            case "delete":
                if ("".equalsIgnoreCase(path)) {emptyPath();} else {cmdDelete(path);}
                break;
            case "ls":
                cmdLs(path);
                break;
            case "cd":
                if ("".equalsIgnoreCase(path)) {emptyPath();} else {cmdCd(path);}
                break;
            case "mkdir":
                if ("".equalsIgnoreCase(path)) {emptyPath();} else {cmdMkdir(path);}
                break;
            case "pwd":
                cmdPwd();
                break;
            case "quit":
                cmdQuit();
                break;
        }

    }

    // other cmd
    private void otherCmd(String cmd) {
        msgToServer = cmd;
    }
    // cmd ls
    private void cmdLs(String path) {
        // client ls
        msgToServer = "ls" + " " + path;
    }

    // cmd get
    private void cmdGet(String cmd) throws IOException {
        File targetFile = new File(cmd.split(" ")[1]);
        if ( !targetFile.exists() ) { emptyPath(); return;}

        BufferedInputStream  fileIn = new BufferedInputStream(new FileInputStream(targetFile));
        PrintStream fileOut = new PrintStream(socket.getOutputStream(),true);
        byte[] fileBuffer = new byte[1024*4];
        int fileSeg = 0;
        while((fileSeg=fileIn.read(fileBuffer,0,1024))!=-1)
        {
            fileOut.write(fileBuffer,0,fileSeg);
        }
        fileIn.close();
        fileOut.close();
    }

    // cmd put
    private void cmdPut(String cmd) throws IOException {

    }

    // cmd delete
    private void cmdDelete(String path) {
        msgToServer = "delete" + " " + path;
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
    private void emptyPath() {
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

    private void sendFile(String cmdMsg) {    //send file

    }

    private void recFile(String cmdMsg) {     //rec file

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



