package Coordinator;

import java.time.Duration;
import java.time.LocalDateTime;

public class MsgTimer extends Thread {
    PartiGroup partiGroup;
    Integer td;

    public MsgTimer ( PartiGroup partiGroup, Integer td ) {
        this.partiGroup = partiGroup;
        this.td = td;
    }

    public void run() {
        if ( 0< partiGroup.size() ) {
            for (Parti parti : partiGroup) {
                Integer msgNum = parti.msgList.size();
                for (int i = 0; i < msgNum; i++) {
                    if (td < Duration.between(parti.msgList.get(i).time, LocalDateTime.now()).toSeconds()) {
                        parti.msgList.remove(i);
                        i--;
                        msgNum--;
                    }
                }
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
