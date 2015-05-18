<<<<<<< HEAD

public class Case {

=======
import java.awt.*;
import java.util.*;
public class Case {
	private enum Etat {
	joueur1,
	joueur2,
	vide;
	}
	
	ArrayList<Point> succ;
	Etat occupation;
	Point pos;
	
	Case(Etat e, Point p) {
		occupation = e;
		pos = p;
		succ = new ArrayList<Point>();
		if(pos.x%2 != 0 && pos.y%2 != 0) {
		
		}
	}
	
	Etat getOccupation() {
		return occupation;
	}
	
	void setOccupation(Etat e) {
		occupation = e;
	}
>>>>>>> branch 'master' of https://github.com/dimpolth/gameProject-PROG6.git
}
