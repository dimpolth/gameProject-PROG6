import java.io.Serializable;


public class Sauvegarde implements Serializable {
	Terrain plateau;
	Boolean joueur;
	Historique histo;
	
	Sauvegarde(Terrain t, Boolean j, Historique h){
		plateau=t.copie();
		joueur = j;
		histo=h;
	}

	
}
