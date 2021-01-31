
import java.io.IOException;
import java.net.ServerSocket;

/**
 * This is the ftp server class.
 *
 */
public class Myftpserver {
    private final int serverPort = 2121;

    public static void main(String[] args) throws IOException {
        boolean running = true;
        printBanner();
        // assign port
        Myftpserver ftpServer = new Myftpserver();
        ServerSocket skt = new ServerSocket( ftpServer.serverPort );
        System.out.println("FTP port: " + String.valueOf(ftpServer.serverPort));
        // start port
        do {
            skt.accept();
        } while ( running );

    }

    private static void printBanner() {
        System.out.println("\t******************************************\n" +
                           "\t*****         FTP Server            ******\n" +
                           "\t******************************************\n");
    }


}

