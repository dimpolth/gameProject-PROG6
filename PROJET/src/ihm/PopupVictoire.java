package ihm;

import ihm.EvenementGraphique.FinPartie;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class PopupVictoire extends JPanel implements ActionListener {

	protected static final int DELAI = 30, DIVIDER = 180, MULTIPLY_FACTOR = 36, LINE_LENGTH = 2, FEU_RAYON_MIN = 100, FEU_RAYON_MAX = 200;
	private static final int NOMBRE_COULEURS = 11;
	private static Color colors[] = new Color[NOMBRE_COULEURS];
	protected static final double PI = 3.14159, VITESSE = 5;
	private Timer horloge;
	private long prochainLancement;
	private FinPartie fin = FinPartie.AUCUNE;
	List<Fusee> fusees = new ArrayList<Fusee>();

	public PopupVictoire() {
		setOpaque(false);

		colors[0] = Color.ORANGE;
		colors[1] = Color.BLUE;
		colors[2] = Color.CYAN;
		colors[3] = Color.RED;
		colors[4] = Color.PINK;
		colors[5] = Color.YELLOW;
		colors[6] = Color.DARK_GRAY;
		colors[7] = Color.GREEN;
		colors[8] = Color.WHITE;
		colors[9] = Color.GRAY;
		colors[10] = Color.MAGENTA;

		horloge = new Timer(DELAI, this);
	}

	public void lancer(FinPartie f) {
		setVisible(true);
		fin = f;
		if (fin == FinPartie.VICTOIRE) {
			horloge.start();
		} else {
			repaint();
		}
	}

	public void arreter() {
		if (fin != FinPartie.VICTOIRE) {
			setVisible(false);
		} else {
			setVisible(false);
			horloge.stop();
			fusees.clear();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (fin == FinPartie.NUL) {
			g.setColor(new Color(0, 0, 0, 96));
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(Theme.couleurTImportant);
			Font f = new Font(" TimesRoman ",Font.BOLD+Font.ITALIC,50);
			g.setFont(f);
			g.drawString("Match Nul", getWidth()/2-121, getHeight()/2-18);
		}else if (fin == FinPartie.DEFAITE) {
				g.setColor(new Color(0, 0, 0, 96));
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(Theme.couleurTImportant);
				Font f = new Font(" TimesRoman ",Font.BOLD+Font.ITALIC,50);
				g.setFont(f);
				g.drawString("Défaite !", getWidth()/2-121, getHeight()/2-18);
		} else if (fin == FinPartie.VICTOIRE){
			Graphics2D gra = (Graphics2D) g;
			Iterator<Fusee> it = fusees.iterator();
			while (it.hasNext()) {
				it.next().afficher(gra);
			}
		}
	}

	private void tirer() {
		Random r = new Random();
		prochainLancement = System.currentTimeMillis() + r.nextInt(1000) + 2000;
		for (int i = 0; i < r.nextInt(4) + 1; i++) {
			fusees.add(new Fusee(new Point(r.nextInt(getWidth() - 200) + 100, r.nextInt(getHeight() - 200) + 100), colors[r.nextInt(NOMBRE_COULEURS)]));
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (System.currentTimeMillis() > prochainLancement) {
			tirer();
		}
		Iterator<Fusee> it = fusees.iterator();
		while (it.hasNext()) {
			if (it.next().maj())
				it.remove();
		}
		repaint();
	}
}

class Fusee {
	Point centre;
	Color couleur;
	int var;
	int taille;
	List<Point> points = new ArrayList<Point>();

	public Fusee(Point p, Color c) {
		centre = p;
		couleur = c;
		var = 0;
		Random a = new Random();
		taille = a.nextInt(PopupVictoire.FEU_RAYON_MAX - PopupVictoire.FEU_RAYON_MIN) + PopupVictoire.FEU_RAYON_MIN;
	}

	public boolean maj() {
		var += PopupVictoire.VITESSE;
		if (var < taille) {
			points.clear();
			for (int i = 0; i < 10; i++) {
				points.add(new Point((int) (centre.x + var * Math.cos((PopupVictoire.MULTIPLY_FACTOR * i * PopupVictoire.PI) / PopupVictoire.DIVIDER)), (int) (centre.y + var
						* Math.sin((PopupVictoire.MULTIPLY_FACTOR * i * PopupVictoire.PI) / PopupVictoire.DIVIDER))));
			}
			return false;
		} else {
			return true;
		}
	}

	public void afficher(Graphics2D g) {
		Stroke stroke = new BasicStroke(20, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 5, new float[] { 9 }, 0);
		g.setStroke(stroke);
		g.setColor(new Color(couleur.getRed(),couleur.getGreen(),couleur.getBlue(),(int)((1-((double)var/(double)taille))*255)));
		for (int i = 0; i < points.size(); i++) {
			g.drawLine(points.get(i).x, points.get(i).y, points.get(i).x + PopupVictoire.LINE_LENGTH, points.get(i).y + PopupVictoire.LINE_LENGTH);
		}
	}
}