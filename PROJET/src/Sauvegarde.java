import java.io.Serializable;


public class Sauvegarde implements Serializable {
	public Terrain plateau;
	public Boolean joueur;
	public Historique histo;
	
	Sauvegarde(Terrain t, Boolean j, Historique h){
		plateau=t.copie();
		joueur = j;
		histo=h;
	}

	
}
