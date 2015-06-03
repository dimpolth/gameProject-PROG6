package reseau;

import java.io.IOException;
import java.net.UnknownHostException;

import ihm.*;
import moteur.*;

public class Communication {
	
	static Communication canaux[]= new Communication[2];
	
	static String[] canaux_nom = {"IHM","MOTEUR"};
	public static final int IHM = 0;
	public static final int MOTEUR= 1;
	
	static boolean reseau = false;
	
	Serveur serveur = null;
	Client client = null;
	
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
		
		
	}
	
	public static void modeReseau(String host){
		// Si il y a une modification
		if(host == null && reseau || host != null && !reseau){
				
			
			// On veut passer en réseau
			if(!reseau){
				
				// Machine qui héberge le serveur (host = "")
				if(host == ""){
					Serveur s = new Serveur( Communication.canaux[ Communication.MOTEUR ] );
					int port = s.demarrer();
					host = "127.0.0.1:"+port;
					System.out.println("HOST : "+host);
					if(port == 0) return;
					System.out.println(s);
					Communication.canaux[ Communication.MOTEUR ].serveur = s;
				}
				
				
				Client cl = new Client( Communication.canaux[ Communication.IHM ] );
				
				try{
					cl.connexion(host);	
					Communication.canaux[ Communication.IHM ].client = cl;
					reseau = true;
				}
				catch(UnknownHostException ex){
					System.out.println("Host introuvable");	
					
				}
				catch(IOException ex){
					System.out.println("Problème réseau");
					
				}
				catch(Exception ex){
					System.out.println("Une erreur est survenue");
					
				}
				
			}
			else{
				
				Communication.canaux[ Communication.MOTEUR ].serveur = null;
				Communication.canaux[ Communication.IHM ].serveur = null;
				Communication.canaux[ Communication.MOTEUR ].client = null;
				Communication.canaux[ Communication.IHM ].client = null;
				
			}
			
			
		
		}
	}
	
	public void envoyer(Echange e){
		
		//System.out.println(""+canaux_nom[loc]+" : envoyer -> "+e.toString());
		
		if(e.infos.size() == 0)
			return;
				
		if(loc == Communication.IHM && !reseau){
			Communication.canaux[ Communication.MOTEUR ].recevoir(e);			
		}
		else if(loc == Communication.IHM && reseau){	
			Communication.canaux[ Communication.IHM ].client.envoyer(e);			
		}
		else if(loc == Communication.MOTEUR && !reseau){
			Communication.canaux[ Communication.IHM ].recevoir(e);			
		}
		else if(loc == Communication.MOTEUR && reseau){
			
			Communication.canaux[ Communication.MOTEUR ].serveur.envoyer(e,null);		
		}
		else{
			
		}
		
		
	}
	
	void recevoir(Echange e){		
		//System.out.println(""+canaux_nom[loc]+" : recevoir <- "+e.toString());
		if(loc == Communication.IHM){
			ihm.notifier(e);			
		}
		else if(loc == Communication.MOTEUR){			
			moteur.action(e);
		}
	}
	
	
	
	

}
