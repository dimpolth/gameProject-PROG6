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
		t.start();
	}

	public void arreter() {
		setVisible(false);
		t.stop();
	}

	private void tirer() {
	}

	public void paintComponent(Graphics g) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}

enum Forme {Ovale};

class Particule {
	public Float coord;
	public Float vecteur;
	public Forme forme;
	public Color couleur;
	public Float taille;
	
	public Particule(Random a, Forme f, Color c) {
		coord = new Float(a.nextFloat(), 1);
		vecteur = new Float((a.nextFloat()/5)-0.1f,a.nextFloat()/3);
		forme = f;
		couleur = c;
		if(f == Forme.Ovale) {
			taille.x = 0.001f;
			taille.y = 0.003f;
		}
	}
	public void maj(float facteurSeconde) {
		coord.x = coord.x + vecteur.x*facteurSeconde;
		coord.y = coord.y + vecteur.y*facteurSeconde;
		vecteur.x = vecteur.x *0.75f;
		vecteur.y = vecteur.y - 0.2f*facteurSeconde; 
	}
	public void afficher(Graphics g, int w, int h) {
		g.setColor(couleur);
		if(forme == Forme.Ovale) {
			g.fillOval((int)(coord.x*w), (int)(coord.y*h), (int)(taille.x*w), (int)(taille.y*h));
		}
	}
}

class Fusee extends Particule {
	public Fusee(Random a, Forme f, Color c) {
		super(a, f, c);
	}
	
}