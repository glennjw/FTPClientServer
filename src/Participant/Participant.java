package Participant;

import java.io.IOException;



public class Participant {

    public static void main(String[] args) throws IOException {

        try {
            String config = args[0];
            Single parti = new Single(config);
            parti.run();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Please specify config file.");
        }




    }







}
