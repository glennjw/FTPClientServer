
import java.net.Socket;
import java.io.File;
import java.util.Scanner;

/**
 * This is the ftp client class.
 *
 */

public class Myftp {

    private String serverName;
    private Integer serverPort;

    public static void main(String[] args) {
        Myftp myftp = new Myftp();
        myftp.welcomeBanner();   // print welcome msg
        myftp.inputServerInfo();

        // cmd
        do {
            // receive cmd


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
        printMsg("\t******************************************\n" +
                 "\t*****          FTP Client           ******\n" +
                 "\t******************************************\n");
    }

    private void printMsg(String msg) {
        System.out.println(msg);
    }

    private void setServerName(String name) {
        this.serverName = name;
    }

    private void setServerPort(Integer port) {
        this.serverPort = port;
    }

    private void inputServerInfo() {
        boolean ifAgain;
        //String inputServerName;
        //Integer inputServerPort;
        // Get server name and port from input
        do {
            Scanner inputs = new Scanner(System.in);
            System.out.print("Server name: ");
            String inputServerName = inputs.nextLine();
            System.out.print("Server port: ");
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
                printMsg("Illegal input! Try again");
            }
        } while ( ifAgain );

    }


}



