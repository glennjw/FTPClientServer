package Coordinator;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Parti {
    String ID;
    String IP;
    Integer port;
    String status;
    ArrayList<Message> msgList = new ArrayList<>();


    public Parti (String ID, String IP, Integer port) {
        // [ID, IP, port#, status, msg]
        this.ID = ID;
        this.IP = IP;
        this.port = port;
        this.status = "registered";

    }

    public void addMsg(String msg) {
        msgList.add(new Message(LocalDateTime.now(), msg));
    }

    public void delLastMsg() {
        msgList.remove( msgList.size() - 1 );
    }
}
