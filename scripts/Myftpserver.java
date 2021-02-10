
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.List;


/**
 * This is the driver class.
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
 */
class Ftpserver {         // for each client
    final int serverPort = 2121;
    ServerSocket serverSkt;
    Socket skt;
    String response;
    String cur_path = System.getProperty("user.dir");
    boolean ifQuit = false;
    int transFile = 0;

    Ftpserver() throws IOException {
    }

    public void run() throws IOException {
        //skt = new Socket();    // init skt
        serverSkt = new ServerSocket(serverPort);

        do {
            // try connect
            try {
                skt = serverSkt.accept();
                //msgFromClient = new BufferedReader(new InputStreamReader(skt.getInputStream()));
                //msgToClient = new PrintWriter(skt.getOutputStream(), true);
                //msgFromClient = new DataInputStream(new BufferedInputStream(skt.getInputStream()));
                //msgToClient = new DataOutputStream(skt.getOutputStream());
                System.out.println("New client connected.");
            } catch (IOException exc) {
                System.out.println("Connection failed.");
            }

            //BufferedReader msgFromClient = new BufferedReader(new InputStreamReader(skt.getInputStream()));
            //PrintWriter msgToClient = new PrintWriter(skt.getOutputStream(), true);

            DataInputStream msgFromClient = new DataInputStream(skt.getInputStream());
            DataOutputStream msgToClient = new DataOutputStream(skt.getOutputStream());

            // execute cmd
            do {
                String recMsg = msgFromClient.readUTF();
                cmdInterface(recMsg, msgFromClient, msgToClient);
                msgToClient.writeUTF(response);

                if (0 == transFile) {             // 0:no file
                } else if (1 == transFile) {      // send file
                    sendFile(recMsg, msgToClient);
                } else if (2 == transFile) {      // rec file
                    recFile(recMsg, msgFromClient);
                }
                transFile = 0;


                // if end session
                if (true == ifQuit) {
                    msgFromClient.close();
                    msgToClient.close();
                    skt.close();
                }
                response = "";           // reset response

            } while (!ifQuit);          // each client
            ifQuit = false;             // reset quit
        } while (true);                 // keep server up

    }

    // all supported cmd
    private void cmdInterface(String input, DataInputStream msgFromClient, DataOutputStream msgToClient) throws IOException {
        String cmd = "";
        String path = "";
        // split and assign input to cmd and path
        input = input.trim();
        if (-1 < input.indexOf(" ")) {
            String[] inputSplited = input.split(" ");
            cmd = inputSplited[0].toLowerCase();
            path = inputSplited[1];
        } else {
            cmd = input;
        }
        // check cmd
        switch (cmd) {
            default:
                cmdNotFound();
                break;
            case "get":
                cmdGet(path, msgToClient);
                break;
            case "put":
                cmdPut(path, msgFromClient);
                break;
            case "delete":
                cmdDelete(path);
                break;
            case "ls":
                cmdLs(path);
                break;
            case "cd":
                cmdCd(path);
                break;
            case "mkdir":
                cmdMkdir(path);
                break;
            case "pwd":                  // done
                cmdPwd();
                break;
            case "quit":                 // done
                cmdQuit();
                break;
        }
    }

    private void cmdGet(String path, DataOutputStream msgToClient) throws IOException {
        File myFile = new File(cur_path + "/" + path);
        if (myFile.exists()) { response = path + " " + myFile.length(); transFile = 1; }
        else { response = "File not found."; }
    }

    private void cmdPut(String file, DataInputStream msgFromClient) throws IOException {
        transFile = 2;              // rec file from client
        response = "";
    }

    private void cmdDelete(String path) {
        //delete file
        File curFile = new File(cur_path+"/"+path);
        if (curFile.isFile()) {
            curFile.delete();
            response = "Deleted.";
        } else if ( curFile.isDirectory() ) {
            response = path + " is a directory.";
        } else {
            response = "File not found.";
        }
    }

    private void cmdLs(String path) {
        // check if path empty
        path = ""==path ? cur_path : path;
        // list path
        String files = "";
        File curDir = new File(cur_path + "/");
        File[] curFiles = curDir.listFiles();
        for (File file : curFiles) {
            files += file.isFile() ? (file.getName() + "\t") : (file.getName() + "/\t");
        }
        response = files;
    }

    private void cmdCd(String path) throws IOException {
        //cd path
        if (""==path) {                                  // for cd empty
            response = "Please specify directory.";
            return;
        }
        // add start /
        if (!path.startsWith("/") && !"..".equalsIgnoreCase(path) ) { path = cur_path + "/" + path;}
        // remove end /
        else if ( path.endsWith("/")) { path = path.substring(0,path.length()-1); }
        // handle full path
        File cur_dir = new File(cur_path);
        if ("..".equalsIgnoreCase(path)) {              // for cd ..
            cur_path = cur_dir.getParent();
            response = cur_path+"/";
        } else {                                        // for normal cd
            File dest_dir = new File(path);
            if (dest_dir.exists()) {
                cur_path = dest_dir.getAbsolutePath();
                response = cur_path+"/";
            } else {
                response = "Directory not found.";
            }
        }
    }

    private void cmdMkdir(String path) {
        //mkdir path
        File newDir = new File(cur_path+"/"+path);
        if (newDir.exists()) { response = "Already exists."; } else { newDir.mkdir(); response="";}
    }

    private void cmdPwd() throws IOException {
        //pwd path
        response = cur_path + "/";
    }

    private void cmdQuit() throws IOException {
        ifQuit = true;     // keep sever alive
        response = "Bye!";
    }

    private void cmdNotFound() {
        response = "Command not found.";
    }

    private void fileNotFound() {
        response = "File not found.";
    }

    public void close() throws IOException {
        skt.close();
    }

    private void sendFile(String path, DataOutputStream dataOut) throws IOException {    //send file
        File targetFile = new File(cur_path + "/" + path.split(" ")[1]);
        if ( !targetFile.exists() ) { fileNotFound(); return;}            // if file exists
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

    private void recFile(String path, DataInputStream dataIn) throws IOException {
        File targetFile = new File(cur_path+"/"+path.split(" ")[1]);    // create file
        FileOutputStream fileOut = new FileOutputStream(targetFile);
        int fileLeft = 0;
        long fileSize = dataIn.readLong();     // read file size
        byte[] fileBuf = new byte[4*1024];
        while (fileSize > 0 && (fileLeft = dataIn.read(fileBuf, 0, (int)Math.min(fileBuf.length, fileSize))) != -1) {
            fileOut.write(fileBuf,0,fileLeft);
            fileSize -= fileLeft;      // read upto file size
        }
        fileOut.close();

    }

}



