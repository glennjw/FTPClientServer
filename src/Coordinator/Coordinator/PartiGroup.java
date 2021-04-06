package Coordinator;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class PartiGroup extends ArrayList<Parti> {

    // participant group: [ [ ID, IP, port#, status, msg], [] ]

    public PartiGroup () {



    }

    public boolean has (String partiID) {
        for (Parti each : this) {
            if (each.ID.equals( partiID )) { return true; }
        }
        return false;
    }

    public Parti get (String partiID) {
        for (Parti each : this) {
            if (each.ID.equals( partiID )) { return each; }
        }
        return null;
    }

    public boolean disconn(String partiID) {
        for (Parti each : this) {
            if (each.ID.equals(partiID)) {
                each.status = "disconnected";
                return true;
            }
        }
        return false;
    }

    public Parti use(String partiID) {
        for (Parti each : this) {
            if (each.ID.equals(partiID)) { return each; }
        }
        return null;
    }

    public void sendMsgToGroup( String msg) throws IOException {
        for ( Parti parti : this ) {
            Socket skt = null;
            if ( "registered".equals(parti.status) ) {
                try {
                    skt = new Socket(parti.IP, parti.port);
                    DataOutputStream msgToClient = new DataOutputStream(skt.getOutputStream());
                    msgToClient.writeUTF(msg);
                    skt.close();
                } catch (Error e) {
                    parti.addMsg( msg );
                }
            } else if ( "disconnected".equals(parti.status) ) {
                System.out.println("adding msg to disconnected");
                parti.addMsg(msg);
            } else {
                // Drop msg
            }
        }
    }

    public void sendMsgToIdv( String partiID) throws IOException {
        for ( Parti parti : this ) {
            if ( partiID.equals(parti.ID) && !parti.msgList.isEmpty()) {
                System.out.println("reading msg");
                try {
                    for (int i=0; i<parti.msgList.size(); i++) {
                        Socket skt;
                        skt = new Socket(parti.IP, parti.port);
                        DataOutputStream msgToClient = new DataOutputStream(skt.getOutputStream());
                        System.out.println("Sending msg: " + parti.msgList.size());
                        msgToClient.writeUTF(parti.msgList.get(i).msg);
                        parti.msgList.remove(i);
                        i--;
                        skt.close();
                    }


                } catch (Error e) {
                    System.out.println("Connect to " + parti.ID + " failed!");
                }
            }
            break;
        }
    }

}
