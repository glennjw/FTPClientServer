package Coordinator;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class PartiGroup extends ArrayList<Parti> {

    // participant group: [ [ ID, IP, port#, status, msg], [] ]

    public PartiGroup () {



    }

    public boolean has(String partiID) {
        for (Parti each : this) {
            if (each.ID.equals( partiID )) { return true; }
        }
        return false;
    }

    public boolean has(Parti parti) {
        for (Parti each : this) {
            if (each.ID.equals( parti.ID )) { return true; }
        }
        return false;
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
            if (each.ID == partiID) { return each; }
        }
        return null;
    }

    public void sendMsg(Integer tPort, String msg) throws IOException {
        for ( Parti parti : this ) {
            Socket skt = null;
            if ( "registered".equals(parti.status) || "reconnected".equals(parti.status) ) {
                try {
                    skt = new Socket(parti.IP, parti.port);
                    DataOutputStream msgToClient = new DataOutputStream(skt.getOutputStream());
                    msgToClient.writeUTF(msg);
                } catch (Error e) {
                    parti.addMsg( msg );
                }finally {
                    if ( skt != null ) { skt.close(); }
                }
            } else if ( "disconnected".equals(parti.status) ) {
                parti.addMsg(msg);
            }
        }
    }

}
