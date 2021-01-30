
import java.net.Socket;
import java.io.File;
import java.util.Scanner;

/**
 * This is the ftp client class.
 *
 */

public class Myftp {





    public static void main(String[] args) {

        boolean ifExit = true;


        printMsg("\t******************************************\n" +
                "\t*****          FTP Client           ******\n" +
                "\t******************************************\n");

        // Got server name and port
        do {
            Scanner inputs = new Scanner(System.in);
            System.out.print("FTP server name: ");
            String serverName = inputs.nextLine();
            System.out.print("FTP server port: ");
            Integer serverPort = inputs.nextInt();
            // connect name / port

        } while ( "connection".equals("true"));


        // cmd
        do {
            // receive cmd


            // handle cmd


        } while ( false == ifExit );


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

    private static void printMsg(String msg) {
        System.out.println(msg);
    }

}



