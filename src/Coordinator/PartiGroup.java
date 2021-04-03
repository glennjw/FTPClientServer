package Coordinator;

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

}
