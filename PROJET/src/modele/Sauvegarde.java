package modele;
import java.io.Serializable;


@SuppressWarnings("serial")
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
