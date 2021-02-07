
import java.io.*;
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
    ServerSocket skt = new ServerSocket(serverPort);
    String response;
    String cur_path = System.getProperty("user.dir");
    boolean ifQuit = false;

    Ftpserver() throws IOException {
    }

    public void run() throws IOException {
        Socket acceptedSkt = new Socket();    // init skt

        // try connect
        try {
            acceptedSkt = skt.accept();
            //msgFromClient = new BufferedReader(new InputStreamReader(acceptedSkt.getInputStream()));
            //msgToClient = new PrintWriter(acceptedSkt.getOutputStream(), true);
            //msgFromClient = new DataInputStream(new BufferedInputStream(acceptedSkt.getInputStream()));
            //msgToClient = new DataOutputStream(acceptedSkt.getOutputStream());
            System.out.println("New client connected.");
        } catch (IOException exc) {
            System.out.println("Connection failed.");
        }

        BufferedReader msgFromClient = new BufferedReader(new InputStreamReader(acceptedSkt.getInputStream()));
        PrintWriter msgToClient = new PrintWriter(acceptedSkt.getOutputStream(), true);
        // execute cmd
        do {
            System.out.println("beginning...");

            System.out.println("b4 wait input");
            String recMsg = msgFromClient.readLine();     // received msg
            System.out.println("b4 handle cmd"+recMsg);
            cmdInterface(recMsg);
            System.out.println("after handle cmd");
            System.out.println("response is: "+response);
            msgToClient.println( response );
            System.out.println("after sent response");
            //msgToClient.flush();
            if ( true == ifQuit ) {
                msgFromClient.close();
                msgToClient.close();
                acceptedSkt.close();
            }
        } while (!ifQuit);          // set to false for 1 time test purpose

        //acceptedSkt.close();
        //msgFromClient.close();
    }

    // all supported cmd
    private void cmdInterface(String input) throws IOException {
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
                System.out.println("Command not found! Try again...");
                break;
            case "get":
                cmdGet(path);
                break;
            case "put":
                cmdPut(path);
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
        if (""==path) {
            response = "Please specify directory.";
            return;
        }
        // remove / from path
        if (path.endsWith("/")) { path = path.substring(0,path.length()-1);}
        File cur_dir = new File(cur_path);
        if ("..".equalsIgnoreCase(path)) {
            cur_path = cur_dir.getParent() + "/";
            response = cur_path;
        } else {
            System.out.println("handle normal path... : "+path);
            File dest_dir = new File(path);
            if (dest_dir.exists()) {
                cur_path = dest_dir.getName();
                response = dest_dir.getName()+"/";
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

    public void close() throws IOException {
        skt.close();
    }


}



