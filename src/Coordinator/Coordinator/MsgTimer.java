package Coordinator;

import java.time.Duration;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class MsgTimer extends Thread {
    PartiGroup partiGroup;
    Integer td;
    boolean ifQuit = false ;

    public MsgTimer ( PartiGroup partiGroup, Integer td ) {
        this.partiGroup = partiGroup;
        this.td = td;
    }

    public void run() {
        do {
            if (0 < partiGroup.size()) {
                System.out.println("checking msg duration");
                for (Parti parti : partiGroup) {
                    Integer msgNum = parti.msgList.size();
                    for (int i = 0; i < msgNum; i++) {
                        if (td < (int) Duration.between(parti.msgList.get(i).time, LocalDateTime.now()).getSeconds()) {
                            System.out.println((int) Duration.between(parti.msgList.get(i).time, LocalDateTime.now()).getSeconds());
                            System.out.println("removed: " + parti.ID + " " + parti.msgList.get(i).time + " " + parti.msgList.get(i).msg);
                            parti.msgList.remove(i);
                            i--;
                            msgNum--;
                        }
                    }
                }
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!ifQuit);
    }
}
