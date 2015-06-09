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
			
			// Premier envoi = identifiant
			identifiant = (String)ois.readObject();	
			System.out.println("1ere reception : identifiant : "+identifiant +" (connexion)");

			Thread t = new Thread(this, "recep");
			t.start();
			
		} catch (Exception e) {
		}
		
		
	}
	public void envoyer(Object o){
		try {			
			//;//System.out.println("SERVER ENVOYER : "+((Echange) e).toString() );
			if(o instanceof Echange)
				oos.writeObject(((Echange)o).clone());
			else 
				oos.writeObject(o);
		} catch (IOException ioe) {
			serveur.terminerConnexion(this);
		}
	}
	
	public void fermer(){
		try{
			this.socket.close();
			socket=null;
		}
		catch(Exception e){}
	}

	@Override
	public void run() {
		
		Thread currentTh = Thread.currentThread();
		
		
		while (socket!=null && !socket.isClosed()) {
			
					
			// Réception de données
			if (currentTh.getName().equals("recep")) {
				
				
				try {				
					Object recu = ois.readObject();		
					
					if(recu instanceof Echange){
						
						Echange ech = (Echange)recu;					
						
						int j = 0;
						if(serveur.joueurs.get(1).equals(this))
							j=1;
						else if(serveur.joueurs.get(2).equals(this))
							j=2;
						
						serveur.com.recevoir(ech,j);					
						
					}
					else{
						String ordre = (String)recu;
						
						if(ordre.equals("/QUIT")){								
							serveur.terminerConnexion(this);
						}
					}
					
					
					
				} catch (Exception ex) {
					System.out.println("catch connexion");
					//ex.printStackTrace();
				}				

			}
			
		}
		
	}
}
