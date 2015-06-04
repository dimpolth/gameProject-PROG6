package ihm;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PopupVictoire extends JPanel implements ActionListener {
	private Timer t;
	private long prochainLancement, precTemps;
	private LinkedList<Fusee> fusees;
	private Random aleat;

	public PopupVictoire() {
		super();
		setOpaque(false);
		t = new Timer(10, this);
		prochainLancement = 0;
		precTemps = 0;
		fusees = new LinkedList<Fusee>();
		aleat = new Random();
	}

	public void lancer() {
		setVisible(true);
		repaint();
		tirer();
		precTemps = System.currentTimeMillis();
		t.start();
	}

	public void arreter() {
		setVisible(false);
		t.stop();
	}

	private void tirer() {
		prochainLancement = System.currentTimeMillis()+2000;
		for (int i = 0; i < aleat.nextInt(3) + 1; i++) {
			fusees.add(new Fusee(new Point2D.Float(aleat.nextInt(getWidth()), getHeight()), new Point(aleat.nextInt(20) - 10, aleat.nextInt(30) + 10), System.currentTimeMillis()+aleat.nextInt(3000) + 1000, aleat.nextInt(100) + 50));
		}
	}

	public void paintComponent(Graphics g) {
		if(System.currentTimeMillis() > prochainLancement) {
			tirer();
		}
		g.setColor(Color.PINK);
		Iterator<Fusee> it = fusees.iterator();
		while(it.hasNext()) {
			it.next().afficher(g);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		long difTemps = System.currentTimeMillis()-precTemps;
		precTemps = System.currentTimeMillis();
		Iterator<Fusee> it = fusees.iterator();
		while(it.hasNext()) {
			Fusee fusee = it.next();
			if(System.currentTimeMillis() > fusee.tempsExplosion) {
				fusee.exploser();
				it.remove();
			}
			fusee.maj(difTemps);
		}
		repaint();
	}
}

abstract class Particule {
	public Point2D.Float coord;
	public Point vecteur;
	
	public void maj(long difTemps) {
		float facteur = difTemps/30;
		coord.x = coord.x + facteur*vecteur.x;
		coord.y = coord.y + facteur*vecteur.y;
		if(vecteur.x > 0)
			vecteur.x--;
		else if(vecteur.x < 0)
			vecteur.x++;
		vecteur.y = vecteur.y-3;
	}
}

class Fusee extends Particule {
	public long tempsExplosion;
	public int nombreParticule;

	public Fusee(Point2D.Float c, Point v, long t, int n) {
		coord = c;
		vecteur = v;
		tempsExplosion = t;
		nombreParticule = n;
	}
	
	public void exploser() {
		
	}
	
	public void afficher(Graphics g) {
		g.fillRect((int)coord.x, (int)coord.y, 10, 20);
	}
}