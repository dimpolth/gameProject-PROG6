package reseau;

import java.net.*;
import java.io.*;

class Client implements Runnable{
    
	private Communication com;
	
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	private String host;
	
	private boolean running=true;
	
	
	// CONSTRUCTEUR
	public Client(Communication c) {		
		com = c;
	}
	
	// CONNEXION AU SEVEUR
	public void connexion(String host, int port) throws UnknownHostException, IOException {
		
		
		// Nouveau socket pour la connexion
		socket = new Socket(host, port);
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());		
		
		// Démarrage de threads pour le dialogue Client/Serveur (envoi/reception)	
		Thread thReception = new Thread(this, "recep");
		thReception.start();		

	}
	
	// DECONNEXION DU SERVEUR
	public void deconnexion(){		
		envoyer("/QUIT");
		try{
			socket.close();		
		}
		catch(Exception e){}
		
		socket = null;
		Communication.reseau = false;
		
	}
	
	// ENVOYER UNE INFORMATION VERS LE SERVEUR
	public void envoyer(Object o) {
		
		try {			
			if(o instanceof Echange)
				oos.writeObject( ((Echange)o).clone() );
			else{		
				oos.writeObject( (String)o );
			}
		}
		catch (Exception ex) {
			System.out.println("CATCH CLIENT");
		}
	}
	
	
	@Override
	public void run() {
		Thread currentTh = Thread.currentThread();
		
		if (currentTh.getName().equals("recep")) {
			
			while (socket != null && !socket.isClosed()) {			
				
				try {
					Object recu = ois.readObject();		
					
					// INFO SERVEUR
					if(recu instanceof String){
						String info = (String)recu;
						
						if(info.equals("/INTER_SERVEUR") || info.equals("/ABANDON")){
							deconnexion();
						}
					}
					
					com.recevoir(recu,0);					
				}
				catch (Exception ex) {
					ex.printStackTrace();
					deconnexion();
				}
				
				
			}

		}
		
	}
    
    
}