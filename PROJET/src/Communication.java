
public class Communication {
	
	static final boolean IHM = false;
	static final boolean MOTEUR= true;
	
	boolean reseau = false;
	
	IHM ihm = null;
	Moteur moteur = null;
	boolean loc;
	
	
	// Constructeur Globale
	Communication(IHM i, Moteur m, boolean l){
		ihm = i;
		moteur = m;
		loc = l;
	}
	
	void setReseau(boolean b){
		reseau = b;
	}
	
	void envoyer(Echange e){
		
		if(!reseau){
			recevoir(e);
		}
		else{
			
		}
		
		
	}
	
	void recevoir(Echange e){
		
		if(loc == Communication.IHM){
			//moteur.action(e);
		}
		else if(loc == Communication.MOTEUR){
			//ihm.notifier(e);
		}
	}
	
	
	
	

}
