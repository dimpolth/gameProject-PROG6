import java.awt.Point;

public class Main {

	public static void main(String[] args) {

		
		// le c++ sent la moule !
		
		Terrain t = new Terrain();
		t.dessineTableauAvecIntersections();
		t.setCase(Case.Etat.vide, 3, 8);
		t.dessineTableauAvecIntersections();
		Point depart = new Point(3,8);
		Point arrive = new Point(3,8);
		int deplace = t.deplacement(depart, arrive);
		switch(deplace) {
		case 0:
			t.dessineTableauAvecIntersections();
			break;
		case 1:
			System.out.println("La case d'arrivée n'est pas atteignable");
			break;
		case 2:
			System.out.println("La case d'arrivée est déjà occupée par un pion");
			break;
		case 3:
			System.out.println("La case choisie est vide");
			break;
		}
	}

}
