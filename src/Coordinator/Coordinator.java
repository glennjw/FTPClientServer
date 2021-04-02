package Coordinator;

import java.io.IOException;

public class Coordinator {


    public static void main(String[] args) throws IOException {
        SingleCoordinator msgCoord = new SingleCoordinator();
        System.out.println("******* Coordinator running ******");
        msgCoord.run();

    }


}
