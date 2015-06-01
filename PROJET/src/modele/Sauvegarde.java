package modele;
import java.io.Serializable;


@SuppressWarnings("serial")
public class Sauvegarde implements Serializable {
	public Terrain plateau;
	public Joueur joueur1,joueur2,joueurCourant;
	public Historique histo;
	
	Sauvegarde(Terrain t, Historique h,Joueur j1,Joueur j2 ,Joueur joueurCourant){
		this.plateau = t.copie();
		this.joueur1 = j1; 
		this.joueur2 = j2;
		this.joueurCourant = joueurCourant;
		this.histo = h;
	}

	
}
