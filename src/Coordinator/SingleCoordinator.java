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

    int nPort = 2121;   // coordinate port
    int td;            // timer
    String coorID = "000";           // coor ID
    String coorIP = "localhost";     // coor IP
    Integer tPort;            // msg port

    ServerSocket nportServerSkt;
    //ServerSocket tportServerSkt;
    Socket nportSkt;
    //Socket tportSkt;
    PartiGroup partiGroup = new PartiGroup();
    String msgNow;


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
        MsgTimer msgTimer = new MsgTimer(partiGroup, td);
        msgTimer.start();
        do {
            System.out.println( "waiting new " + nPort );
            // try connect
            try {
                // Thread-A : cmd (server mode)
                nportSkt = nportServerSkt.accept();
                System.out.println("New client connected.");
                Thread nportThread = new NPortThread(nportSkt, partiGroup, tPort );
                nportThread.start();
                System.out.println("Coordinator port: " + nPort );


                // Thread-B : msg (client mode)
                //tportSkt = tportServerSkt.accept();
                //Thread tportThread = new TPortThread( tportSkt, partiGroup, msgNow );
                //tportThread.start();
                //System.out.println("Msg port: " + tPort );
            } catch (IOException exc) {
                System.out.println("Connection failed.");
                System.exit(1);
            }
        } while (true);                 // keep server up
    }

}
