package BootstrapNameServer.BNS;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Scanner;

public class BNSExecutor implements Runnable {

    ServerSocket sktServer;
    String partiID;
    String partiIP;
    String response = "";
    Boolean ifQuit = false;
    String msgNow = "";
    Integer nPort;
    NameServerContainer nsContn;

    public BNSExecutor(ServerSocket nportServerSkt, NameServerContainer nsContn ) {
        this.sktServer = nportServerSkt;
        //this.partiIP = skt.getInetAddress().getHostAddress();
        this.nPort = nportServerSkt.getLocalPort();
        this.nsContn = nsContn;
    }

    public void run() {

        Scanner cmdRec = new Scanner(System.in);

        do {
            System.out.print("Cmd> ");
            String input = cmdRec.nextLine();        // input cmd
            try {
                cmdInterface(input);
                System.out.println( response );
            } catch (IOException e) {
                e.printStackTrace();
            }

        } while (true);
    }

    private void cmdInterface(String input) throws IOException {
        String cmd = "";
        ArrayList<String> para = new ArrayList<>();
        // split and assign input to cmd and para
        input = input.trim();
        if (-1 < input.indexOf(" ")) {
            String[] inputSplited = input.split(" ");
            cmd = inputSplited[0].toLowerCase();
            for (int i=1; i<inputSplited.length; i++) { para.add(inputSplited[i]);}
        } else {
            cmd = input;
        }
        // check cmd
        switch (cmd) {
            default:
                //System.out.println("Command not supported.");
                break;
            case "lookup":
                cmdLookup(para);
                break;
            case "insert":
                cmdInsert(para);
                break;
            case "delete":
                cmdDelete(para);
                break;
            case "quit":
                System.exit(0);
                break;
        }
    }

    private void cmdLookup( ArrayList<String> para ) throws IOException {
        if ( 0 == Integer.parseInt(para.get(0)) ) {  }          // 0 is special
        Integer key = Integer.parseInt( para.get(0) );
        searchByKey( "lookup", key );
    }

    private void cmdInsert( ArrayList<String> para ) {
        //operByKey();
        response = "";
    }

    public void cmdDelete(ArrayList<String> para) {

    }

    public void searchByKey(String oper, Integer key) throws IOException {

        /**
        DataInputStream recNportSkt = null;
        DataOutputStream sendNportSkt = null;
        for (NameServerContainer ns:allNsContn) {
            if ( key >= ns.tableHead && key <= ns.tableTail ) {
                try {
                    Socket nportSkt = new Socket( ns.IP, ns.port );
                    recNportSkt = new DataInputStream(nportSkt.getInputStream());
                    sendNportSkt = new DataOutputStream(nportSkt.getOutputStream());
                } catch (IOException eIO) {
                    System.out.println("Connect NS failed.");
                    //System.exit(0);
                }
                sendNportSkt.writeUTF(oper+ " " + key.toString());
                response = recNportSkt.readUTF();

        }
         */
    }


}
