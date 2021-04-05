package Coordinator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SingleCoordinator {

    int nPort;         // coordinator port
    int td;            // timer

    ServerSocket nportServerSkt;
    Socket nportSkt;
    PartiGroup partiGroup = new PartiGroup();

    public SingleCoordinator(String fileName) {
        List<String> fileContents = new ArrayList<>();
        try {
            List<String> allLines = Files.readAllLines(Paths.get(fileName));
            for (String line : allLines) {
                fileContents.add( line );
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        } catch (IOException e) {
            System.out.println("Cannot open file.");
        }
        this.nPort = Integer.parseInt( fileContents.get(0).trim() );
        this.td = Integer.parseInt( fileContents.get(1).trim() );
    }

    public void run() throws IOException {
        nportServerSkt = new ServerSocket(nPort);
        //tportServerSkt = new ServerSocket(tPort);
        MsgTimer msgTimer = new MsgTimer(partiGroup, td);
        msgTimer.start();
        do {
            System.out.println( "Waiting participants on " + nPort );
            try {
                // Thread-A : cmd (server mode)
                nportSkt = nportServerSkt.accept();
                System.out.println("New client connected.");
                Thread nportThread = new NPortThread(nportSkt, partiGroup, nPort );
                nportThread.start();
            } catch (IOException exc) {
                System.out.println("Connection failed.");
                System.exit(1);
            }
        } while (true);                 // keep server up
    }

}
