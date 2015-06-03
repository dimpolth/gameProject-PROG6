package reseau;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connexion implements Runnable{
	
	private Serveur serveur;
	
	// Socket connecté au joueur
	private Socket socket;

	// Flux de sortie pour envoyer des données vers le joueur
	private ObjectOutputStream oos;

	// Flux d'entrée pour recevoir des données du joueur
	private ObjectInputStream ois;
	
	Connexion(Serveur serveur, Socket socket) throws UnknownHostException, IOException {
		
		try {
			this.serveur = serveur;
			this.socket = socket;
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			
			Thread t1 = new Thread(this, "envoi");
			t1.start();

			Thread t2 = new Thread(this, "recep");
			t2.start();
			System.out.println("Une connexion chez le serveur");
		} catch (Exception e) {
		}
		
		
	}
	public void envoyer(Echange e){
		try {			
			System.out.println("SERVER ENVOYER : "+((Echange) e).toString() );
			oos.writeObject(e);	
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	@Override
	public void run() {
		
		Thread currentTh = Thread.currentThread();
		Echange e;
		
		while (true) {
			
			// Envoi de données
			if (currentTh.getName().equals("envoi")) {

				/*try {
					this.oos.writeObject("jco="+getTousConnectes());
				} catch (Exception e) {
				}
				
				try {
					Thread.sleep(1500);
				} catch (Exception e) {
				}*/

			}
			
			// Réception de données
			else if (currentTh.getName().equals("recep")) {
				
				
				try {					
					Echange recu = (Echange)ois.readObject();
					//String e2 = (String)ois.readObject();
					//System.err.println("Reception d'une donnée cliente : "+e2);
					//Object e3 = ois.readObject();
					
					//System.err.println("Reception d'une donnée cliente : "+((Echange)e3).infos.size());
					//System.out.println("Reception d'une donnée cliente sur le serveur : "+recu.infos.size());					
					serveur.com.recevoir(recu);
					
					//System.out.println("Reception d'une donnée cliente sur le serveur : TRAITE");
					
				} catch (Exception ex) {
				}				

			}
			
		}
		
	}
}
