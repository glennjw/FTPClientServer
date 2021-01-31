
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
        // start port
        do {
            skt.accept();
        } while ( running );

    }

    private static void printBanner() {
        System.out.println("\t******************************************\n" +
                           "\t*****     FTP Server Port: 2121     ******\n" +
                           "\t******************************************\n");
    }

    private void ftpServer() {

    }

}

