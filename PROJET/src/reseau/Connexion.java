package reseau;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import modele.Parametres;

public class Connexion implements Runnable{
	
	private Serveur serveur;
	
	protected String identifiant;
	
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
			
		} catch (Exception e) {
		}
		
		
	}
	public void envoyer(Echange e){
		try {			
			//;//System.out.println("SERVER ENVOYER : "+((Echange) e).toString() );
			oos.writeObject(e.clone());	
			
		} catch (IOException ioe) {
			serveur.terminerConnexion(this);
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
					Object recu = ois.readObject();		
					
					if(recu instanceof Echange){
						
						Echange ech = (Echange)recu;					
						
						int j = 0;
						if(serveur.joueurs.get(1).equals(this))
							j=1;
						else if(serveur.joueurs.get(2).equals(this))
							j=2;
						
						if(ech.get("parametres") != null){
							Parametres params =(Parametres)ech.get("parametres");
							if(params.j1_identifiant != null){
								identifiant = params.j1_identifiant;
								ech.infos.remove("parametres");
							}
						}
						
						serveur.com.recevoir(ech,j);					
						
					}
					else{
						String ordre = (String)recu;
						
						if(ordre.equals("/quit")){
							serveur.terminerConnexion(this);
						}
					}
					
					
					
				} catch (Exception ex) {
				}				

			}
			
		}
		
	}
}
