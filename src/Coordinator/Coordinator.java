package Coordinator;

import java.io.IOException;

public class Coordinator {


    public static void main(String[] args) throws IOException {

        String config = args[0];

        SingleCoordinator msgCoord = new SingleCoordinator( config );
        System.out.println("******* Coordinator running ******");
        msgCoord.run();

    }


}
