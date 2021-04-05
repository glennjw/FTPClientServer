package Participant;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TPortThread extends Thread {
    Integer tPort;
    String partiID;
    String logFileName = "message-log.txt";
    Boolean ifQuit = false;


    public TPortThread(Integer tPort, String partiID, String logPath) {
        this.tPort = tPort;
        this.partiID = partiID;
        this.logFileName = logPath;
    }

    public void run() {
        try {

            do {
                String recMsg;
                ServerSocket tportServerSkt = new ServerSocket(tPort);
                Socket skt = tportServerSkt.accept();
                DataInputStream msgRec = new DataInputStream(skt.getInputStream());
                recMsg = msgRec.readUTF();
                if (!"".equals(msgRec)) {System.out.println("\nIncoming: " + recMsg); }
                logMsg( recMsg );
                tportServerSkt.close();
            } while (!ifQuit);          // each client

        } catch (IOException exc) {
            //System.out.println("Broken pipe");
            //exc.printStackTrace();
        }

    }

    public void logMsg(String msg) throws IOException {
        msg = msg + "\n";
        File file;
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(logFileName, true);
            fileWriter.write(msg); 
        }
        catch (IOException e) {
            file = new File(logFileName);
            file.createNewFile();
            fileWriter = new FileWriter(file, true);
            fileWriter.write(msg);
        }
        fileWriter.close();
    }

}
