package ihm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import modele.Case;
import reseau.Echange;

@SuppressWarnings("serial")
public class TerrainGraphique extends JPanel implements ComponentListener {
	/**
	 * Durée de l'animation de déplacement.
	 */
	public static final int ANIM_DEPL = 1000;
	public static final int ANIM_DISP = 500;
	public static final int ANIM_SELECT = 500;

	protected Image imgPlateau;
	protected Image imgPion1;
	protected Image imgPion2;
	protected Image imgCroix;
	public IHM ihm;
	private AnimSelect select;
	protected LinkedList<EvenementGraphique> lCoups = new LinkedList<EvenementGraphique>();
	/**
	 * Temps de gel du programme.
	 */
	protected long tempsGele;
	private Dimensions dim;
	protected Pion[][] pions;

	protected ArrayList<Point> trait;

	private ArrayList<Point> prisesPossibles = new ArrayList<Point>(2);
	protected Point pionSelectionne = null;

	public TerrainGraphique(IHM i) {
		super(null);
		setOpaque(false);

		ihm = i;
		select = null;
		tempsGele = 0;
		dim = new Dimensions();
		pions = new Pion[5][9];
		for (int j = 0; j < 5; j++) {
			for (int k = 0; k < 9; k++) {
				pions[j][k] = new Pion(new Point(j, k), this, dim);
				add(pions[j][k]);
			}
		}
		addComponentListener(this);
	}

	public void dessinerTerrain(Case[][] c) {
		ihm.popupV.arreter();
		deselectionner();
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 9; j++) {
				pions[i][j].setCouleur(c[i][j].getOccupation());
				pions[i][j].repaint();
			}
		}
	}

	public void setTrait(ArrayList<Point> l) {
		trait = l;
		repaint();
	}

	public void cacherTrait() {
		trait = null;
		repaint();
	}

	public void selectionner(Point p) {
		if (select != null)
			deselectionner();
		select = new AnimSelect(pions[p.x][p.y]);
	}

	public void deselectionner() {
		if (select != null)
			select.stop();
		select = null;
	}

	public void deplacer(Point o, Point a) {
		pions[o.x][o.y].deplacer((Point) o.clone(), (Point) a.clone());
		Point tmp = pions[o.x][o.y].coord;
		pions[o.x][o.y].coord = pions[a.x][a.y].coord;
		pions[a.x][a.y].coord = tmp;
		Pion tmpP = pions[o.x][o.y];
		pions[o.x][o.y] = pions[a.x][a.y];
		pions[a.x][a.y] = tmpP;
		pions[o.x][o.y].majPosition();

	}

	public void afficherPrisesPossibles(Point[] p) {
		prisesPossibles.add(p[0]);
		prisesPossibles.add(p[1]);
		pions[p[0].x][p[0].y].setPrisePossible(true);
		pions[p[1].x][p[1].y].setPrisePossible(true);
	}

	public void cacherPrisesPossibles() {
		for (int i = 0; i < prisesPossibles.size(); i++) {
			pions[prisesPossibles.get(i).x][prisesPossibles.get(i).y].setPrisePossible(false);
		}
		prisesPossibles.clear();
	}

	public void manger(ArrayList<Point> pts) {
		for (int i = 0; i < pts.size(); i++)
			new AnimDisparition(pions[pts.get(i).x][pts.get(i).y]);
	}

	public void clicCase(Point pt) {
		Echange e = new Echange();
		e.ajouter("point", pt);
		ihm.com.envoyer(e);
	}

	public void paintComponent(Graphics g) {
		Graphics2D gra = (Graphics2D) g;
		gra.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		double largeur = getWidth(), hauteur = getHeight(), origX = 0, origY = 0;
		if (largeur / hauteur > 19.0 / 11.0) {
			origX = (int) ((largeur - (hauteur * 19.0 / 11.0)) / 2.0);
			largeur = hauteur * 19 / 11;
		} else {
			origY = (int) ((hauteur - (largeur * 11.0 / 19.0)) / 2.0);
			hauteur = largeur * 11 / 19;
		}
		gra.drawImage(imgPlateau, (int) origX, (int) origY, (int) largeur, (int) hauteur, null);
		if (trait != null && trait.size() > 1) {
			int d = (int) (dim.echelle / 4);
			Point p = trait.get(0);
			gra.setStroke(new BasicStroke(6));
			gra.setColor(ihm.theme.couleurChemin);
			for (int i = 1; i < trait.size(); i++) {
				gra.drawLine((int) ((p.y + 0.5) * dim.echelle + dim.origX) + d, (int) ((p.x + 0.5) * dim.echelle + dim.origY) + d, (int) ((trait.get(i).y + 0.5) * dim.echelle + dim.origX) + d, (int) ((trait.get(i).x + 0.5) * dim.echelle + dim.origY) + d);
				gra.fillOval((int) ((p.y + 0.5) * dim.echelle + dim.origX) + d - 10, (int) ((p.x + 0.5) * dim.echelle + dim.origY) + d - 10, 20, 20);
				p = trait.get(i);
			}
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
		dim.largeur = getWidth();
		dim.hauteur = getHeight();
		dim.echelle = -1;
		dim.origX = 0;
		dim.origY = 0;
		if (dim.largeur / dim.hauteur > 19.0 / 11.0) {
			dim.origX = (int) ((dim.largeur - (dim.hauteur * 19.0 / 11.0)) / 2.0);
			dim.largeur = dim.hauteur * 19 / 11;
			dim.echelle = dim.hauteur / 5.5;
		} else {
			dim.origY = (int) ((dim.hauteur - (dim.largeur * 11.0 / 19.0)) / 2.0);
			dim.hauteur = dim.largeur * 11 / 19;
			dim.echelle = dim.largeur / 9.5;
		}
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 9; j++) {
				pions[i][j].componentResized(null);
			}
		}
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}
}

/**
 * Paramètres de dimensions du plateau.
 */
class Dimensions {
	/**
	 * Abscisse en pixels du premier point du plateau.
	 */
	public int origX;
	/**
	 * Ordonnée en pixels du premier point du plateau.
	 */
	public int origY;
	/**
	 * Facteur de redimensionnement.
	 */
	public double echelle;
	
	public double largeur;
	public double hauteur;
}
