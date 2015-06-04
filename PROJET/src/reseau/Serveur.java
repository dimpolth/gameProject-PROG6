package reseau;

import java.net.Socket;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.Vector;
import java.io.InputStream;
import java.io.IOException;

public class Serveur implements Runnable{
	
	Communication com;
	// Socket Passif
	private ServerSocket passiveSocket = null;	
	
	private int port = 0;

	// Les différents joueurs connectés sont stockés dans un vecteur
	public Vector<Connexion> connexions = new Vector<Connexion>();
	
	Serveur(Communication c){		
		com = c;		
	}
	
	public int demarrer(){
		try{
			//port = 55555;
			passiveSocket = new ServerSocket(port);
			port = passiveSocket.getLocalPort();
			Thread th = new Thread(this);
			th.start();
			return port;
		}
		catch(Exception e){
			return 0;
		}
	}
	
	public int getPort(){
		return this.port;
	}
	
	public void run(){
		
		
			
			
			// On garde le port pour pouvoir connecter d'autres clients sur ce même port
			

			System.out.println("En ecoute sur : " + this.passiveSocket);
			while (true) {
				
				
				try{
					Socket activeSocket = this.passiveSocket.accept();
					System.out.println("Nouvelle connexion");
				
					// Lorsqu'un utilisateur se connecte, on créé une nouvelle instance
					Connexion connexion = new Connexion(this,activeSocket);
					
					// On sauvegarde la nouvelle connexion
					nouvelleConnexion(connexion);
				
				}
				catch(Exception ex){
					System.out.println("Impossible de récupérer le nouveau joueur.");
				}			
				
				
			}
	
	}
	
	public void envoyer(Echange e, Connexion con){
		if(con != null){
			con.envoyer(e);
		}
		else{
			
			Iterator<Connexion> it = connexions.iterator();
			while(it.hasNext()){				
				it.next().envoyer(e);
			}
		}
	}
	
	public void nouvelleConnexion(Connexion c){		
		connexions.addElement(c);		
	}
	
	public void fermerConnexion(Connexion c){
		connexions.remove(c);
	}
	
	public void stopperServeur(){
		try{
			passiveSocket.close();
		}
		catch(Exception e){
			
		}
	}
	
	
    
   
}