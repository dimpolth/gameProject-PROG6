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
	public Vector<Connexion> connexions;
	
	Serveur(Communication c){		
		com = c;		
	}
	
	public int demarrer(){
		try{
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
	
	public void run(){
		
		try {
			
			
			// On garde le port pour pouvoir connecter d'autres clients sur ce même port
			

			System.out.println("En ecoute sur : " + this.passiveSocket);
			while (true) {
				System.out.println("Attente des connexions");

				Socket activeSocket = this.passiveSocket.accept();
				
				// Lorsqu'un utilisateur se connecte, on créé une nouvelle instance
				// de "ConnexionJoueur".
				Connexion connexion = new Connexion(this,activeSocket);
			}
		} catch (Exception e) {
			System.out.println("Aucun port disponible");
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