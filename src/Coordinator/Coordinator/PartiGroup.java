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
        System.out.println( "sendMsgToGroup: " + this.size() );
        for ( Parti parti : this ) {
            Socket skt = null;
            if ( "registered".equals(parti.status) ) {
                System.out.println("registered found: " + parti.ID);
                try {
                    skt = new Socket(parti.IP, parti.port);
                    DataOutputStream msgToClient = new DataOutputStream(skt.getOutputStream());
                    msgToClient.writeUTF(msg);
                } catch (Error e) {
                    parti.addMsg( msg );
                } finally {
                    System.out.println("close send port");
                    if ( skt != null ) { skt.close(); }
                }
            } else if ( "disconnected".equals(parti.status) ) {
                //System.out.println("adding msg to disconnected");
                parti.addMsg(msg);
            }
        }
    }

    public void sendMsgToIdv( String partiID) throws IOException {
        for ( Parti parti : this ) {
            Socket skt = null;
            if ( partiID.equals(parti.ID) && !parti.msgList.isEmpty()) {
                System.out.println("reading msg");
                try {
                    skt = new Socket(parti.IP, parti.port);
                    DataOutputStream msgToClient = new DataOutputStream(skt.getOutputStream());

                    for (int i=0; i<parti.msgList.size(); i++) {
                        System.out.println("msgList size: " + parti.msgList.size());
                        msgToClient.writeUTF(parti.msgList.get(i).msg);
                        parti.msgList.remove(i);
                        i--;
                    }
                    /**
                    for ( Message msg : parti.msgList ) {
                        System.out.println(parti.msgList.get(0));
                        msgToClient.writeUTF(msg.time + " " + msg.msg);
                        parti.msgList.remove(msg);
                        System.out.println(parti.msgList.get(0));
                    }
                    */

                    msgToClient.writeUTF("///");     // end sending
                } catch (Error e) {
                    System.out.println("Connect to " + parti.ID + " failed!");
                }finally {
                    if ( skt != null ) { skt.close(); }
                }
            }
            break;
        }
    }

}
