
public class Main {

	public static void main(String[] args) {

		
		// le c++ sent la moule !
		
		Terrain t = new Terrain();
		t.afficherTableau();
		t.setCase(Case.Etat.vide, 3, 8);
		System.out.println();
		t.afficherTableau();
		t.dessineTableauAvecIntersections();
	}

}
