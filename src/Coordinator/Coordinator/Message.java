package Coordinator;

import java.time.LocalDateTime;

public class Message {
        LocalDateTime time;
        String msg;

    public Message (LocalDateTime time, String msg) {
        this.time = time;
        this.msg = msg;
    }

}
