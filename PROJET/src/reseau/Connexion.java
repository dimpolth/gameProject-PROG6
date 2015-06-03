package reseau;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connexion implements Runnable{
	
	private Serveur serveur;
	
	// Socket connecté au joueur
	private Socket socket;

	// Flux de sortie pour envoyer des données vers le joueur
	private ObjectOutputStream oos;

	// Flux d'entrée pour recevoir des données du joueur
	private ObjectInputStream ois;
	
	Connexion(Serveur serveur, Socket socket){
		
		try {

			this.socket = socket;
			this.ois = new ObjectInputStream(this.socket.getInputStream());
			this.oos = new ObjectOutputStream(this.socket.getOutputStream());



			// On sauvegarde la nouvelle connexion
			serveur.nouvelleConnexion(this);

			

			Thread t1 = new Thread(this, "envoi");
			t1.start();

			Thread t2 = new Thread(this, "recep");
			t2.start();

		} catch (Exception e) {
		}
		
		
	}
	public void envoyer(Echange e){
		try {
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
					e = (Echange)ois.readObject();
					serveur.com.recevoir(e);
					

				} catch (Exception ex) {
				}				

			}
			
		}
		
	}
}
