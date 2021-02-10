
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
    private String msgToServer;
    private int transFile;                // 0:no file; 1:send file; 2:rec file;
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
        //PrintWriter sendSkt = new PrintWriter(myftp.socket.getOutputStream(), true);
        //BufferedReader recSkt = new BufferedReader(new InputStreamReader(myftp.socket.getInputStream()));

        DataInputStream recSkt = new DataInputStream(myftp.socket.getInputStream());
        DataOutputStream sendSkt = new DataOutputStream(myftp.socket.getOutputStream());
        // send cmd
        Scanner cmdRec = new Scanner(System.in);
        String secondMsg = "";
        do {
            // input cmd
            myftp.printMsg("myftp> ");
            String input = cmdRec.nextLine();

            // execute cmd
            myftp.cmdInterface(input);
            if ( "".equalsIgnoreCase(myftp.msgToServer)) {
                System.out.println("Invalid command or path.");
            } else {
                sendSkt.writeUTF(myftp.msgToServer);
                System.out.println( secondMsg = recSkt.readUTF());
            }
            if (0==myftp.transFile) {             // 0:no file
            } else if (1==myftp.transFile) {      // send file
                myftp.sendFile(myftp.msgToServer, sendSkt);
            } else if (2==myftp.transFile) {      // rec file
                myftp.recFile( secondMsg, recSkt);
            }
            myftp.msgToServer = "";     // reset msgToServer
            myftp.transFile = 0;         // reset fileSign
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
    private void cmdGet(String path) {
        transFile =2;
        msgToServer = "get" + " " + path;
    }

    // cmd put
    private void cmdPut(String path) {
        transFile =1;
        File file = new File(path);
        if (file.exists()) { msgToServer = "put" + " " + path + " " + file.length(); }
        else { System.out.println("File not found."); }
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

    private void sendFile(String cmdMsg, DataOutputStream dataOut) throws IOException {    //send file

        File targetFile = new File(cmdMsg.split(" ")[1]);
        if ( !targetFile.exists() ) { emptyPath(); return;}            // if file exists
        int segFile = 0;
        FileInputStream fileInputStream = new FileInputStream(targetFile);
        dataOut.writeLong(targetFile.length());    // send file size
        byte[] fileBuf = new byte[4*1024];         // break file into chunks
        while ((segFile=fileInputStream.read(fileBuf))!=-1){
            dataOut.write(fileBuf,0,segFile);
            dataOut.flush();
        }
        fileInputStream.close();

    }

    private void recFile(String cmdMsg, DataInputStream dataIn) throws IOException {     // rec file
        File targetFile = new File(cmdMsg.split(" ")[0]);    // create file
        FileOutputStream fileOut = new FileOutputStream(targetFile);
        int fileLeft = 0;
        long fileSize = dataIn.readLong();     // read file size
        byte[] fileBuf = new byte[4*1024];
        while (fileSize > 0 && (fileLeft = dataIn.read(fileBuf, 0, (int)Math.min(fileBuf.length, fileSize))) != -1) {
            fileOut.write(fileBuf,0,fileLeft);
            fileSize -= fileLeft;      // read up to file size
        }
        fileOut.close();

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



