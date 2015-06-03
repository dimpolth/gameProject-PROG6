package reseau;

import java.io.IOException;
import java.net.UnknownHostException;

import ihm.*;
import moteur.*;

public class Communication {
	
	static Communication canaux[]= new Communication[2];
	
	public static final int IHM = 0;
	public static final int MOTEUR= 1;
	
	boolean reseau = false;
	
	Serveur serv = null;
	Client cl = null;
	
	IHM ihm = null;
	Moteur moteur = null;
	int loc;
	
	
	// Constructeur Global
	public Communication(IHM i, Moteur m, int l){
		ihm = i;
		moteur = m;
		loc = l;
		Communication.canaux[l] = this;
	}
	
	void setReseau(String host){
		if(host == null && reseau || host != null && !reseau){
				
			
			
			if(!reseau){
				
				// Machine qui héberge le serveur
				if(host == ""){
					serv = new Serveur(this);
					int port = serv.demarrer();
					host = "127.0.0.1:"+port;
					if(port == 0) return;
				}
				cl = new Client(this);
				
				try{
					cl.connexion(host);					
					reseau = true;
				}
				catch(UnknownHostException ex){
					System.out.println("Host introuvable");	
					
				}
				catch(IOException ex){
					System.out.println("Problème réseau");
					
				}
				finally{
					System.out.println("Une erreur est survenue");
					
				}
				
			}
			else{
				cl = null;
				serv = null;
			}
			
			
		
		}
	}
	
	public void envoyer(Echange e){
		
		//System.out.println(""+loc+" : envoyer");
		
		if(e.infos.size() == 0)
			return;
				
		if(loc == Communication.IHM && !reseau){
			Communication.canaux[ Communication.MOTEUR ].recevoir(e);			
		}
		else if(loc == Communication.IHM && reseau){
			cl.envoyer(e);			
		}
		else if(loc == Communication.MOTEUR && !reseau){
			Communication.canaux[ Communication.IHM ].recevoir(e);			
		}
		else if(loc == Communication.MOTEUR && reseau){
			serv.envoyer(e,null);		
		}
		else{
			
		}
		
		
	}
	
	void recevoir(Echange e){
		
		if(loc == Communication.IHM){
			ihm.notifier(e);			
		}
		else if(loc == Communication.MOTEUR){			
			moteur.action(e);
		}
	}
	
	
	
	

}
