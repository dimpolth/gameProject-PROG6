package reseau;

import java.io.IOException;
import java.net.UnknownHostException;

import ihm.*;
import moteur.*;

public class Communication {
	
	public static Communication canaux[]= new Communication[2];
	
	static String[] canaux_nom = {"IHM","MOTEUR"};
	public static final int IHM = 0;
	public static final int MOTEUR= 1;
	
	static boolean reseau = false;
	
	public Serveur serveur = null;
	public Client client = null;
	
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
	
	public static String modeReseau(String host){
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
					if(port == 0) return "Aucun port disponible pour ouvrir une connexion.";
					
					Communication.canaux[ Communication.MOTEUR ].serveur = s;
				}
				
				
				Client cl = new Client( Communication.canaux[ Communication.IHM ] );
				
				try{
					cl.connexion(host);	
					Communication.canaux[ Communication.IHM ].client = cl;
					reseau = true;
				}
				catch(UnknownHostException ex){
					return "Host introuvable";
					
				}
				catch(IOException ex){
					return "Un problème réseau est survenue";
					
				}
				catch(Exception ex){
					return "Une erreur est survenue";
					
				}
				
			}
			else{
				
				Communication.canaux[ Communication.MOTEUR ].serveur = null;
				Communication.canaux[ Communication.IHM ].serveur = null;
				Communication.canaux[ Communication.MOTEUR ].client = null;
				Communication.canaux[ Communication.IHM ].client = null;
				
			}
			
			
		
		}
		
		return null;
	}
	
	public void envoyer( Echange e ){
		envoyer(e,0);
	}
	
	public void envoyer(Echange e, int j){
		
		//System.out.println(""+canaux_nom[loc]+" : envoyer -> "+e.toString());
		
		if(e.infos.size() == 0)
			return;
				
		if(loc == Communication.IHM && !reseau){
			Communication.canaux[ Communication.MOTEUR ].recevoir(e,0);			
		}
		else if(loc == Communication.IHM && reseau){	
			System.out.println("COMMUNICATION IHM : ENVOYER ");
			Communication.canaux[ Communication.IHM ].client.envoyer(e);			
		}
		else if(loc == Communication.MOTEUR && !reseau){
			Communication.canaux[ Communication.IHM ].recevoir(e,0);			
		}
		else if(loc == Communication.MOTEUR && reseau){			
			Communication.canaux[ Communication.MOTEUR ].serveur.envoyer(e,j);		
		}
		else{
			
		}
		
		
	}
	
	void recevoir(Echange e, int j){		
		//System.out.println(""+canaux_nom[loc]+" : recevoir <- "+e.toString());
		if(loc == Communication.IHM){
			ihm.notifier(e);			
		}
		else if(loc == Communication.MOTEUR){			
			moteur.action(e,j);
		}
	}
	
	public static int getPort(){
		return Communication.canaux[ Communication.MOTEUR ].serveur.getPort();
	}
	
	public static boolean enReseau(){
		return Communication.reseau;
	}
	
	
	
	

}
