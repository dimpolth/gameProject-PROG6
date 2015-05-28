
public class Communication {
	
	static Communication canaux[]= new Communication[2];
	
	static final int IHM = 0;
	static final int MOTEUR= 1;
	
	boolean reseau = false;
	
	IHM ihm = null;
	Moteur moteur = null;
	int loc;
	
	
	// Constructeur Global
	Communication(IHM i, Moteur m, int l){
		ihm = i;
		moteur = m;
		loc = l;
		Communication.canaux[l] = this;
	}
	
	void setReseau(boolean b){
		reseau = b;
	}
	
	void envoyer(Echange e){
		
		System.out.println(""+loc+" : envoyer");
				
		if(loc == Communication.IHM && !reseau){
			Communication.canaux[ Communication.MOTEUR ].recevoir(e);			
		}
		else if(loc == Communication.MOTEUR && !reseau){
			Communication.canaux[ Communication.IHM ].recevoir(e);			
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
