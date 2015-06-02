package reseau;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.InputStream;
import java.io.IOException;

public class Serveur implements Runnable{
	
		
    
    Serveur(Communication c){
    	try {
    		// Ouverture d'une socket sur n'importe quel port
            ServerSocket listener = new ServerSocket(0);
            boolean finished = false;

            System.out.println("Serveur en ecoute sur le port : " +
                               listener.getLocalPort());
            while (!finished) {
                Socket client = listener.accept();
                InputStream in = client.getInputStream();
                byte [] buffer = new byte [1024];
                int number;

                while ((number = in.read(buffer)) != -1) {
                    System.out.write(buffer, 0, number);
                }
                finished = true;
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}