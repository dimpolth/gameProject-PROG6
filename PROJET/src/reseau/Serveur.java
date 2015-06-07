package reseau;

import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Vector;
import java.io.InputStream;
import java.io.IOException;

import modele.Parametres;

public class Serveur implements Runnable{
	
	Communication com;
	// Socket Passif
	private ServerSocket passiveSocket = null;	
	
	private int port = 0;

	// Les différents joueurs connectés sont stockés dans un vecteur
	public ArrayList<Connexion> connexions = new ArrayList<Connexion>();
	public LinkedHashMap<Integer,Connexion> joueurs = new LinkedHashMap<Integer,Connexion>(2);
	
	Serveur(Communication c){		
		com = c;		
	}
	
	public int demarrer(){
		try{
			port = 55555;
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
			

			;//System.out.println("En ecoute sur : " + this.passiveSocket);
			while (true) {
				
				
				try{
					Socket activeSocket = this.passiveSocket.accept();
					;//System.out.println("Nouvelle connexion");
				
					// Lorsqu'un utilisateur se connecte, on créé une nouvelle instance
					Connexion connexion = new Connexion(this,activeSocket);
					
					// On sauvegarde la nouvelle connexion
					nouvelleConnexion(connexion);
				
				}
				catch(Exception ex){
					;//System.out.println("Impossible de récupérer le nouveau joueur.");
				}			
				
				
			}
	
	}
	
	public void envoyer(Object o, int j){
		
		if(j >= 1 && j<= 2){
			joueurs.get(j).envoyer(o);
			com.envoyer(o);
		}
		else{
			int exception = 0;
			if(j < 0)
				exception = j * -1;
			
			Iterator<Connexion> it = connexions.iterator();
			Connexion con;
			while(it.hasNext()){
				con = it.next();
				if(exception >= 1 && exception <=2){
					if(joueurs.get(exception).equals(con))
						continue;
				}
				con.envoyer(o);
			}
		}
	}
	
	public void nouveauJoueur(Connexion c){		
		Echange e = new Echange();
		Parametres params = new Parametres();
		
		if(joueurs.get(1) == null){			
			joueurs.put(1,c);		
			params.j1_identifiant = c.identifiant;			
		}
		else if(joueurs.get(2) == null){			
			joueurs.put(2,c);			
			params.j2_identifiant = c.identifiant;			
		}		
	
		e.ajouter("parametres", params);	
		com.recevoir(e, 0);
			
	}
	
	public void nouvelleConnexion(Connexion c){		
		connexions.add(c);	
		if(joueurs.size() < 2)
			nouveauJoueur(c);
	}
	
	public void terminerConnexion(Connexion c){
		connexions.remove(c);
		
		// Joueur qui s'en va
		if(joueurs.containsValue(c)){
			
			joueurs.remove(c);
			
			if(connexions.size() >= 2){
				Iterator<Connexion>it = connexions.iterator();
				while(it.hasNext()){
					Connexion con = it.next();
					
					if(!joueurs.containsValue(con)){						
						nouveauJoueur(con);
						break;
					}
				}
			}
			else{
				System.out.println("Envoi d'un message aux cliens");
				String mes = "reseau_interruption:Le joueur adverse a quitté le jeu et a donc mis fin à la partie en réseau.";				
				envoyer(mes,0);
			}
		}
	}
	
	public void stopperServeur(){
		try{
			passiveSocket.close();
		}
		catch(Exception e){
			
		}
	}
	
	
    
   
}
