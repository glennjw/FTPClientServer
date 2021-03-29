package Coordinator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Coordinator {


    public void main(String[] args) throws IOException {
        SingleCoordinator msgCoord = new SingleCoordinator();
        System.out.println("******* Coordinator running ******");
        msgCoord.run();

    }


}
