package modele;

public class Parametres {
	public enum NiveauJoueur {
		HUMAIN, FACILE, MOYEN, DIFFICILE;
		static public NiveauJoueur getFromIndex(int i) {
			switch(i) {
			case 0:
				return HUMAIN;
			case 1:
				return FACILE;
			case 2:
				return MOYEN;
			default:
				return DIFFICILE;
			}
		}
	}

	public String j1_identifiant, j2_identifiant;
	public NiveauJoueur j1_type, j2_type;

}
