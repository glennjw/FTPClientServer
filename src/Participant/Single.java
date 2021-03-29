package Participant;

import Coordinator.NPortThread;
import Coordinator.TPortThread;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Single {

    public String partiID;
    public String logPath;
    public String coorIP;
    public Integer coorNPort;        // command port
    public Integer coorTPort;        // msg port
    public Socket nportSkt;
    public Socket tportSkt;
    public String msgToServer;

    Single (String fileName) throws IOException {
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
        this.partiID = fileContents.get(0);
        this.logPath = fileContents.get(1);
        this.coorIP = fileContents.get(2).trim().split(" ")[0];
        this.coorNPort = Integer.parseInt( fileContents.get(2).trim().split(" ")[1] );
        File logFile = new File( logPath );     // create log file
        logFile.createNewFile();
    }

    public void run () throws IOException {
        nportSkt = new Socket( coorIP, coorNPort);
        tportSkt = new Socket( coorIP, coorTPort);

        Thread nportThread = new NPortThread(nportSkt);     // nport thread
        nportThread.start();
        Thread tportThread = new TPortThread( tportSkt, nportThread.getId() );     // tport thread
        tportThread.start();

        DataInputStream recNportSkt = new DataInputStream(nportSkt.getInputStream());
        DataOutputStream sendNportSkt = new DataOutputStream(nportSkt.getOutputStream());
        DataInputStream recTportSkt = new DataInputStream(tportSkt.getInputStream());
        DataOutputStream sendTportSkt = new DataOutputStream(tportSkt.getOutputStream());

        // try send msg

        
    }




}
