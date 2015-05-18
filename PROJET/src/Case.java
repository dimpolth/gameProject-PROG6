import java.awt.*;
import java.util.*;

public class Case {
	private enum Etat {
	joueur1,
	joueur2,
	vide;
	}
	
	private ArrayList<Point> succ;
	private Etat occupation;
	private Point pos;
	
	Case(Etat e, Point p) {
		occupation = e;
		pos = p;
		succ = new ArrayList<Point>();
		
	}
	
	Etat getOccupation() {
		return occupation;
	}
	
	void setOccupation(Etat e) {
		occupation = e;
	}
}
