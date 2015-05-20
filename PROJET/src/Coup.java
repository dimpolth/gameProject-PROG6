import java.awt.Point;

/*
 * Un coup est un couple de case jouées : une case de départ et une case d'arrivée
 */
public class Coup {
	
	private Point pDepart;
	private Point pArrivee;
	
	public Coup(Point pDepart, Point pArrivee) {
		this.setpDepart(pDepart);
		this.setpArrivee(pArrivee);
	}

	public Point getpDepart() {
		return pDepart;
	}

	public void setpDepart(Point pDepart) {
		this.pDepart = pDepart;
	}

	public Point getpArrivee() {
		return pArrivee;
	}

	public void setpArrivee(Point pArrivee) {
		this.pArrivee = pArrivee;
	}
}
