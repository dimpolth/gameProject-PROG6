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
	public boolean connexion(String pHost) throws UnknownHostException, IOException {
		
		host = pHost;
		// On récupère le nom de la machine
		final InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();			
		}
		catch (final Exception e) {
			return false;
		}
		
		//;//System.out.println(addr.toString());
		
		String[] hostInfos = host.split(":");
		if(hostInfos[1] == null)
			;//System.out.println("Pas de port");
		
		
		int port = Integer.valueOf(hostInfos[1]);
		// Nouveau socket pour la connexion
		socket = new Socket(hostInfos[0], port);
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());		
		
		// Démarrage de threads pour le dialogue Client/Serveur (envoi/reception)	
		Thread thReception = new Thread(this, "recep");
		thReception.start();	
		
		return true;

	}
	
	// DECONNEXION DU SERVEUR
	public void deconnexion(){		
		
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
					deconnexion();
				}
				
				
			}

		}
		
	}
    
    
}