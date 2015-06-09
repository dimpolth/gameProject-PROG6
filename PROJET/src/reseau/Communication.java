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
	
	public static void quitterReseau(){
		Communication.canaux[ Communication.IHM ].client.deconnexion();
		reseau=false;
		Communication.canaux[ Communication.MOTEUR ].serveur=null;
		Communication.canaux[ Communication.IHM ].serveur=null;
		Communication.canaux[ Communication.MOTEUR ].client=null;
		Communication.canaux[ Communication.IHM ].serveur=null;
	}
	
	public static int reseauHeberger(int port){
		if(port != 0){
			
			Serveur s = new Serveur( Communication.canaux[ Communication.MOTEUR ] );
			int retour = s.demarrer(port);
			if(retour > 0){
				Communication.canaux[ Communication.MOTEUR ].serveur = s;
				return port;
			}
					
		}
		
		return 0;
		
	}
	
	public static String reseauRejoindre(String host, int port, String identifiant){
		Client cl = new Client( Communication.canaux[ Communication.IHM ] );
		
		try{			
			cl.connexion(host, port);			
			cl.envoyer(identifiant);			
			Communication.canaux[ Communication.IHM ].client = cl;
			reseau = true;
		}
		catch(UnknownHostException ex){
			return "Host introuvable";
			
		}
		catch(IOException ex){
			return "Impossible de se connecter à l'hôte indiqué.";
			
		}
		catch(Exception ex){
			return "Une erreur est survenue.";			
		}
		
		return null;
		
	}
	
	/*
	public static String modeReseau(String host, String identifiant){
		// Si il y a une modification
		if(host == null && reseau || host != null && !reseau){
				
			
			// On veut passer en réseau
			if(!reseau){
				
				// Machine qui héberge le serveur (host = "")
				if(host == ""){
					Serveur s = new Serveur( Communication.canaux[ Communication.MOTEUR ] );
					int port = s.demarrer();
					host = "127.0.0.1:"+port;
					;//System.out.println("HOST : "+host);
					if(port == 0) return "Le port n'est pas disponible pour ouvrir une connexion.";
					
					Communication.canaux[ Communication.MOTEUR ].serveur = s;
				}
				
				
				Client cl = new Client( Communication.canaux[ Communication.IHM ] );
				
				try{
					cl.connexion(host);	
					cl.envoyer(identifiant);
					Communication.canaux[ Communication.IHM ].client = cl;
					reseau = true;
				}
				catch(UnknownHostException ex){
					return "Host introuvable";
					
				}
				catch(IOException ex){
					return "Impossible de se connecter à l'hôte indiqué.";
					
				}
				catch(Exception ex){
					return "Une erreur est survenue.";
					
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
	*/
	
	public void envoyer( Object e ){
		envoyer(e,0);
	}
	
	public void envoyer(Object e, int j){
		
		//System.out.println(""+canaux_nom[loc]+" : envoyer -> "+e.toString());
		
		if(e instanceof Echange && ((Echange)e).infos.size() == 0)
			return;
				
		if(loc == Communication.IHM && !reseau){
			Communication.canaux[ Communication.MOTEUR ].recevoir(e,0);			
		}
		else if(loc == Communication.IHM && reseau){	
			;//System.out.println("COMMUNICATION IHM : ENVOYER ");
			Communication.canaux[ Communication.IHM ].client.envoyer(e);			
		}
		else if(loc == Communication.MOTEUR && !reseau){
			Communication.canaux[ Communication.IHM ].recevoir(e,0);			
		}
		else if(loc == Communication.MOTEUR && reseau){
			//System.out.println("Serveur envoie à j="+j+" : "+e.toString());
			Communication.canaux[ Communication.MOTEUR ].serveur.envoyer(e,j);		
		}
		else{
			
		}
		
		
	}
	
	void recevoir(Object e, int j){		
		//;//System.out.println(""+canaux_nom[loc]+" : recevoir <- "+e.toString());
		if(loc == Communication.IHM){
			
			if(e instanceof Echange)
				ihm.notifier((Echange)e);
			else{
				System.out.println((String)e);
				ihm.notifier((String)e);
			}
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
	
	public static boolean estServeur(){
		return Communication.canaux[ Communication.MOTEUR ].serveur != null;
	}
	
	
	

}
