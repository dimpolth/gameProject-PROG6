package ihm;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import javax.swing.JComponent;

import modele.Case;

@SuppressWarnings("serial")
class Pion extends JComponent implements MouseListener, ComponentListener {
	protected Point coord;
	protected TerrainGraphique tg;
	private Case.Etat etat;
	private boolean croix;
	/**
	 * Niveau de transparence.
	 */
	private float alpha;
	/**
	 * Dimensions et paramètres de la fenêtre.
	 */
	protected Dimensions dim;
	/**
	 * Coordonnées en pixel d'un pion.
	 */
	protected Point coordReelles = new Point();
	protected float facteurTaille = 1;
	/**
	 * Transformation de la position du pion.
	 */
	protected Point2D.Float facteurPos = new Point2D.Float();

	public Pion(Point p, TerrainGraphique t, Dimensions d) {
		super();
		coord = p;
		facteurPos.x = coord.x;
		facteurPos.y = coord.y;
		tg = t;
		etat = Case.Etat.vide;
		croix = false;
		alpha = 1.0f;
		dim = d;
		addComponentListener(this);
		addMouseListener(this);

	}

	public void setCouleur(Case.Etat t) {
		etat = t;
		setAlpha((t == Case.Etat.vide) ? 0.0f : 1.0f);
	}

	public void setAlpha(float f) {
		alpha = f;
	}

	public void setPrisePossible(boolean b) {
		croix = b;
		repaint();
	}

	public void deplacer(Point o, Point d) {
		new AnimDeplacement(this, o, d);

	}

	public void cacher() {
		etat = Case.Etat.vide;
		repaint();
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		g2.setComposite(ac);
		if (etat == Case.Etat.joueur1) {
			g2.drawImage(tg.imgPion1, 0, 0, getWidth(), getHeight(), null);
		} else if (etat == Case.Etat.joueur2) {
			g2.drawImage(tg.imgPion2, 0, 0, getWidth(), getHeight(), null);
		}
		if (croix) {
			g2.drawImage(tg.imgCroix, 0, 0, getWidth(), getHeight(), null);
		}

	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentResized(ComponentEvent e) {
		replacer();
	}
	/**
	 * Replace le pion en fonction des redimensionnements et des animations..
	 */
	public void replacer() {
		setBounds((int) ((facteurPos.y + 0.5) * dim.echelle + dim.origX) - (int) ((facteurTaille * dim.echelle / 2) - (dim.echelle / 2)) / 2, (int) ((facteurPos.x + 0.5) * dim.echelle + dim.origY) - (int) ((facteurTaille * dim.echelle / 2) - (dim.echelle / 2)) / 2, (int) (facteurTaille * dim.echelle / 2), (int) (facteurTaille * dim.echelle / 2));
		repaint();
	}
	/**
	 * Téléportation d'un pion à l'endroit où il doit être.
	 */
	public void majPosition() {
		facteurPos.x = coord.x;
		facteurPos.y = coord.y;
		replacer();
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (System.currentTimeMillis() > tg.tempsGele && e.getX() >= 0 && e.getX() < getHeight() && e.getY() >= 0 && e.getY() < getHeight()) {
			tg.clicCase(coord);
		}
	}
}