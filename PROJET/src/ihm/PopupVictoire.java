package ihm;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D.Float;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class PopupVictoire extends JPanel implements ActionListener {
	private Timer t;
	private long prochainLancement, precTemps;
	private LinkedList<Particule> particules = new LinkedList<Particule>();
	private Random aleat;

	public PopupVictoire() {
		super();
		setOpaque(false);
		t = new Timer(10, this);
		precTemps = 0;
		aleat = new Random();
	}

	public void lancer() {
		setVisible(true);
		repaint();
		tirer(aleat);
		precTemps = System.currentTimeMillis();
		t.start();
	}

	public void arreter() {
		setVisible(false);
		t.stop();
	}

	private void tirer(Random a) {
		prochainLancement = System.currentTimeMillis() + a.nextInt(3000)+2000;
		for(int i=0 ; i<aleat.nextInt(500)+2 ; i++) {
			particules.add(new Fusee(aleat, Forme.FUSEE, Color.GRAY, particules));
		}
	}

	public void paintComponent(Graphics g) {
		Iterator<Particule> it = particules.iterator();
		while(it.hasNext()) {
			it.next().afficher(g, getWidth(), getHeight());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		float facteurSeconde = (System.currentTimeMillis()-precTemps)/1000f;
		precTemps = System.currentTimeMillis();
		if(precTemps > prochainLancement) {
			tirer(aleat);
		}
		Iterator<Particule> it = particules.iterator();
		while(it.hasNext()) {
			if(it.next().maj(facteurSeconde))
				it.remove();
		}
		repaint();
	}
}

enum Forme {OVALE, FUSEE};

class Particule {
	private LinkedList<Particule> particules;
	
	public Float coord;
	public Float vecteur;
	public Forme forme;
	public Color couleur;
	public Float taille;
	
	public Particule(Random a, Forme f, Color c, LinkedList<Particule> p) {
		coord = new Float(a.nextFloat(), 1);
		vecteur = new Float((a.nextFloat()/2)-0.5f,a.nextFloat()/3);
		forme = f;
		couleur = c;
		if(forme == Forme.OVALE || forme == Forme.FUSEE) {
			taille = new Float(0.01f, 0.03f);
		}
		particules = p;
		//;//System.out.println("POP");
	}
	public boolean maj(float facteurSeconde) {
		coord.x = coord.x + vecteur.x*facteurSeconde;
		//;//System.out.println(coord.x+"-"+vecteur.x+"-"+facteurSeconde);
		coord.y = coord.y + vecteur.y*facteurSeconde;
		vecteur.x = vecteur.x *0.75f;
		vecteur.y = vecteur.y - 0.2f*facteurSeconde;
		return false;
	}
	public void afficher(Graphics g, int w, int h) {
		g.setColor(couleur);
		if(forme == Forme.OVALE || forme == Forme.FUSEE) {
			g.fillOval((int)(coord.x*w), (int)(coord.y*h), (int)(taille.x*w), (int)(taille.y*h));
		}
	}
}

class Fusee extends Particule {
	public long tempsExplosion;
	public Fusee(Random a, Forme f, Color c, LinkedList<Particule> p) {
		super(a, f, c, p);
		tempsExplosion = System.currentTimeMillis()+a.nextInt(2000)+3000;
	}
	public boolean maj(float facteurSeconde) {
		super.maj(facteurSeconde);
		if(System.currentTimeMillis() > tempsExplosion) {
			//;//System.out.println("ANIHILIATION !");
			return true;
		}
		return false;
	}
	
}