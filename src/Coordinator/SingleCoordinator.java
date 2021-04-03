package Coordinator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SingleCoordinator {

    int nPort = 2121;   // coordinate port
    int tPort ;        // msg port
    int td;            // timer

    ServerSocket nportServerSkt;
    ServerSocket tportServerSkt;
    Socket nportSkt;
    Socket tportSkt;
    ArrayList<ArrayList<String>> partiGroup = new ArrayList<>();        // participant group: [ [ ID, IP, port#, status, msg], [] ]


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
        this.tPort = Integer.parseInt( fileContents.get(0).trim() );
        this.td = Integer.parseInt( fileContents.get(1).trim() );
        if ( nPort == tPort ) {
            System.out.println("Msg port is occupied.");
            System.exit(1);
        }
    }

    public void run() throws IOException {
        nportServerSkt = new ServerSocket(nPort);
        //tportServerSkt = new ServerSocket(tPort);

        do {
            System.out.println( "waiting new " + nPort );
            // try connect
            try {
                // Thread-A : coordinate
                nportSkt = nportServerSkt.accept();
                System.out.println("New client connected.");
                Thread nportThread = new NPortThread(nportSkt, partiGroup);
                nportThread.start();

                // Thread-B : msg
                tportSkt = tportServerSkt.accept();
                Thread tportThread = new TPortThread( tportSkt, nportThread.getId() );
                tportThread.start();
                System.out.println("Coordinate port: " + nPort );
            } catch (IOException exc) {
                System.out.println("Connection failed.");
                System.exit(1);
            }
        } while (true);                 // keep server up
    }

}
