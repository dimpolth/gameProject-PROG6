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
	
	public int demarrer(int pPort){
		try{
			
			passiveSocket = new ServerSocket(pPort);
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
			while (passiveSocket != null) {
				
				
				try{
					Socket activeSocket = this.passiveSocket.accept();
					System.out.println("Nouvelle connexion run serveur");
				
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
	
	public void getInfosJoueurs(){
		Parametres params = new Parametres();	
		
		if(joueurs.get(1) != null)
			params.j1_identifiant = joueurs.get(1).identifiant;
		
		if(joueurs.get(2) != null){
			params.j2_identifiant = joueurs.get(2).identifiant;
		}else{
			params.j2_identifiant = "En attente...";
		}
		
		com.recevoir(new Echange("parametres",params), 0);	
	}
	
	public void nouveauJoueur(Connexion c){		
		
		// Premier joueur = serveur
		if(joueurs.get(1) == null)		
			joueurs.put(1,c);			
		
		else if(joueurs.get(2) == null)			
			joueurs.put(2,c);
		
			
		getInfosJoueurs();
		
			
	}
	
	public void nouvelleConnexion(Connexion c){		
		connexions.add(c);	
		if(joueurs.size() < 2)
			nouveauJoueur(c);
		
		getInfosJoueurs();
		
		if(joueurs.size() >= 1){
			Echange ech = new Echange();
			ech.ajouter("terrain", com.moteur.t.getTableau());
			c.envoyer(ech);
		}
		
		
	}
	
	public void terminerConnexion(Connexion c){
		c.fermer();	
	
		connexions.remove(c);
		
		
		// Joueur qui s'en va
		if(joueurs.containsValue(c)){
			
			// Le serveur quitte
			if(joueurs.get(1) == c){				
				envoyer("/INTER_SERVEUR",0);
				joueurs.remove(c);
				stopperServeur();				
			}
			else if(connexions.size() < 2){
				envoyer("/ABANDON",0);
				joueurs.remove(c);
				stopperServeur();		
			}
			else{
				
				joueurs.remove(2);				
				Iterator<Connexion>it = connexions.iterator();
				while(it.hasNext()){
					Connexion con = it.next();
					
					if(!joueurs.containsValue(con)){						
						nouveauJoueur(con);
						break;
					}
				}
			}
			
			
			
		}
	}
	
	public void stopperServeur(){
		Iterator<Connexion>it = connexions.iterator();
		while(it.hasNext()){
			Connexion c = it.next();
			System.out.println("Fin de connion (stopperServeur)");
			c.fermer();
			connexions.remove(c);
		}
		
		try{			
			passiveSocket.close();
		}
		catch(Exception e){
			
		}
	}
	
	
    
   
}
