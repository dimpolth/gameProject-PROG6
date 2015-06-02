package reseau;

import java.net.*;
import java.io.*;

class Client {
    public static void main(String args[]) {
        if (args.length < 2) {
            System.err.println("Il me faut une IP et un numéro de port !");
            System.exit(1);
        }

        try {
            InetAddress addr = InetAddress.getByName(args[0]);
            int port = Integer.valueOf(args[1]);
            Socket sock = new Socket(addr, port);
            PrintStream print = new PrintStream(sock.getOutputStream());

            print.println("Salut, je viens de me connecter");
            print.println("Bye bye");
            print.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
    
}