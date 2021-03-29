package Participant;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NPortThread extends Thread {
    private Socket skt;
    String response = "";
    Boolean ifQuit = false;


    NPortThread(Socket skt) {
        this.skt = skt;
    }

    public void run() {
        try {
            DataInputStream msgFromClient = new DataInputStream(skt.getInputStream());
            DataOutputStream msgToClient = new DataOutputStream(skt.getOutputStream());
            // execute cmd
            do {
                String recMsg = msgFromClient.readUTF();
                cmdInterface(recMsg, msgFromClient, msgToClient);
                msgToClient.writeUTF(response);

                // if end session
                if (true == ifQuit) {
                    msgFromClient.close();
                    msgToClient.close();
                    skt.close();
                }
                response = "";           // reset response

            } while (!ifQuit);          // each client
            ifQuit = false;             // reset quit
        } catch (IOException exc) {
            //exc.printStackTrace() ;
        }
    }

    private void cmdInterface(String input, DataInputStream msgFromClient, DataOutputStream msgToClient) throws IOException {
        String cmd = "";
        String path = "";
        // split and assign input to cmd and path
        System.out.println( input );
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
                System.out.println("Command not found.");
                break;
            case "register":
                cmdRegister(path, msgToClient);
                break;
            case "deregister":
                cmdDeregister(path, msgFromClient);
                break;
            case "disconnect":
                cmdDisconnect(path);
                break;
            case "reconnect":
                cmdReconnect(path);
                break;
            case "msend":
                cmdMsend(path);
                break;
        }
    }

    private void cmdRegister(String path, DataOutputStream msgToClient) {

    }

    private void cmdDeregister(String path, DataInputStream msgFromClient) {

    }

    public void cmdDisconnect(String path) {

    }

    public void cmdReconnect(String path) {

    }

    public void cmdMsend(String path) {

    }


}
