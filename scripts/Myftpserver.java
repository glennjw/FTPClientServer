
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
            System.out.println("about to read input msg; ");
            String recMsg = msgFromClient.readUTF();
            System.out.println("rec msg is: "+recMsg);
            cmdInterface(recMsg, msgFromClient, msgToClient);
            msgToClient.writeUTF( response );
            System.out.println("after sent response: " + response );

            if (0==transFile) {             // 0:no file
            } else if (1==transFile) {      // send file
                System.out.println("this is fileSign=1");
                // maybe need to suspend sendSkt/recSkt.
                sendFile(recMsg, msgToClient);
            } else if (2==transFile) {      // rec file
                // ? maybe need to suspend sendSkt/recSkt.
                recFile(recMsg, msgFromClient);
            }
            System.out.println("reset transFile");
            transFile = 0;


            // if end session
            if ( true == ifQuit ) {
                msgFromClient.close();
                msgToClient.close();
                skt.close();
            }
            response = "";           // reset response

        } while (!ifQuit);          // set to false for 1 time test purpose

        //skt.close();
        //msgFromClient.close();
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
        response = "Transfer file...";
    }

    private void cmdDelete(String path) {
        //delete file
        File curFile = new File(path);
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
            response = cur_path;
        } else {                                        // for normal cd
            System.out.println("handle normal path... : "+path);
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
        ifQuit = true;
        response = "Bye!";
    }

    private void cmdNotFound() {
        response = "Command not found.";
    }

    public void close() throws IOException {
        skt.close();
    }

    private void sendFile(String path, DataOutputStream dataOut) throws IOException {    //send file
        /*
        File targetFile = new File(cur_path+"/"+path);
        if ( !targetFile.exists() ) { response="File not found!"; return;}

        FileInputStream fileIn = new FileInputStream(targetFile);

        msgToClient.writeLong(targetFile.length());
        byte[] fileBuffer = new byte[1024*4];
        int fileSeg = 0;
        while((fileSeg=fileIn.read(fileBuffer,0,1024))!=-1) {      // start sending
            msgToClient.write(fileBuffer,0,fileSeg);
            msgToClient.flush();
        }
        fileIn.close();

         */

        File myFile = new File(cur_path + "/" + path.split(" ")[1]);

        byte[] mybytearray = new byte[(int) myFile.length()];
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
        bis.read(mybytearray, 0, mybytearray.length);
        dataOut.flush();
        dataOut.write(mybytearray, 0, mybytearray.length);
        dataOut.flush();
        bis.close();

    }

    private void recFile(String path, DataInputStream dataIn) throws IOException {
        /*
        File tempFile = new File(cur_path+"/"+".temp");    // create file
        //if ( targetFile.exists() ) { System.out.println("File name conflict!"); return;
        OutputStream fileOut = new FileOutputStream(targetFile);
        long fileSize = dataIn.readLong();
        byte[] fileBuffer = new byte[1024];
        int fileSeg = 0;
        System.out.println("the file size is: " + fileSize);
        //while( (fileSeg=dataIn.read(fileBuffer, 0, 1024)) >= 1024)
        while (fileSize > 0 && (fileSeg = dataIn.read(fileBuffer, 0, (int)Math.min(fileBuffer.length, fileSize))) !=-1) {
            System.out.println("byte transferring fileSeg: "+ fileSeg);
            fileOut.write(fileBuffer,0,fileSeg);
            fileSize -= fileSeg;
            System.out.println("left file size: " + fileSize);
        }
        System.out.println("receiving finished");
        fileOut.close();
        File finalFile = new File( cur_path+"/"+path.split(" ")[1] );
        if ( !tempFile.renameTo(finalFile) ) { System.out.println("File name duplicated.");}
        else { tempFile.delete(); }
        //File fileWithNewName = new File(targetFile.getParent(), cur_path+"/"+path.split(" ")[1]);    // rename file
        */

        File recFile = new File(cur_path+"/"+path.split(" ")[1]);    // create file
        byte[] mybytearray = new byte[ Integer.parseInt(path.split(" ")[2]) ];
        FileOutputStream fos = new FileOutputStream(recFile);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        int bytesRead = dataIn.read(mybytearray, 0, mybytearray.length);
        bos.write(mybytearray, 0, bytesRead);
        bos.close();


    }    // inconsistent inside.txt

}



