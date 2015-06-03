package ihm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import reseau.Echange;
import modele.Case;
import modele.Terrain;

@SuppressWarnings("serial")
public class TerrainGraphique extends JPanel implements ComponentListener {
	public static final int ANIM_DEPL = 1000;
	public static final int ANIM_DISP = 500;
	public static final int ANIM_SELECT = 500;

	private Image imgPlateau;
	protected Image imgPion1;
	protected Image imgPion2;
	protected Image imgCroix;
	public IHM ihm;
	private AnimSelect select;
	protected LinkedList<CoupGraphique> lCoups = new LinkedList<CoupGraphique>();

	protected long tempsGele;
	private Dimensions dim;
	protected Pion[][] pions;
	
	private ArrayList<Point> trait;

	private ArrayList<Point> prisesPossibles = new ArrayList<Point>(2);
	protected Point pionSelectionne = null;

	public TerrainGraphique(IHM i) {
		super(null);
		/*
		 * imgPlateau = new
		 * ImageIcon(getClass().getResource("images/themes/bois/plateau.png"
		 * )).getImage(); imgPion1 = new
		 * ImageIcon(getClass().getResource("images/themes/bois/pion1.png"
		 * )).getImage(); imgPion2 = new
		 * ImageIcon(getClass().getResource("images/themes/bois/pion2.png"
		 * )).getImage(); imgCroix = new
		 * ImageIcon(getClass().getResource("images/themes/bois/croix.png"
		 * )).getImage();
		 */
		try {
			imgPlateau = ImageIO.read(getClass().getResource("/images/themes/bois/plateau.png"));
			imgPion1 = ImageIO.read(getClass().getResource("/images/themes/bois/pion1.png"));
			imgPion2 = ImageIO.read(getClass().getResource("/images/themes/bois/pion2.png"));
			imgCroix = ImageIO.read(getClass().getResource("/images/themes/bois/croix.png"));
		} catch (Exception e) {

		}

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
		select = new AnimSelect(pions[p.x][p.y]);
	}

	public void deselectionner() {
		if (select != null)
			select.stop();
		select = null;
	}

	public void deplacer(Point o, Point a) {

		// deselectionner();

		// Pour repositionner l'autre pion ( sinon pas cliquable )
		pions[a.x][a.y].deplacer((Point) a.clone(), (Point) o.clone());

		pions[o.x][o.y].deplacer((Point) o.clone(), (Point) a.clone());
		Point tmp = pions[o.x][o.y].coord;
		pions[o.x][o.y].coord = pions[a.x][a.y].coord;
		pions[a.x][a.y].coord = tmp;
		Pion tmpP = pions[o.x][o.y];
		pions[o.x][o.y] = pions[a.x][a.y];
		pions[a.x][a.y] = tmpP;

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
		if (prisesPossibles.contains(pt)) {
			cacherPrisesPossibles();
		}

		Echange e = new Echange();
		e.ajouter("point", pt);
		ihm.com.envoyer(e);
	}

	public void paintComponent(Graphics g) {
		Graphics2D gra = (Graphics2D)g;
		gra.setColor(new Color(238, 238, 238));
		gra.fillRect(0, 0, getWidth(), getHeight());
		double largeur = getWidth(), hauteur = getHeight(), origX = 0, origY = 0;
		if (largeur / hauteur > 19.0 / 11.0) {
			origX = (int) ((largeur - (hauteur * 19.0 / 11.0)) / 2.0);
			largeur = hauteur * 19 / 11;
		} else {
			origY = (int) ((hauteur - (largeur * 11.0 / 19.0)) / 2.0);
			hauteur = largeur * 11 / 19;
		}
		gra.drawImage(imgPlateau, (int) origX, (int) origY, (int) largeur, (int) hauteur, null);
		if(trait != null && trait.size() > 1) {
			int d = (int) (dim.echelle / 4);
			Point p = trait.get(0);
		    gra.setStroke(new BasicStroke(6));
			for(int i=1 ; i<trait.size() ; i++) {
				gra.setColor(Color.WHITE);
				gra.drawLine((int)((p.x+0.5)*dim.echelle+dim.origX)+d, (int)((p.y+0.5)*dim.echelle+dim.origY)+d, (int)((trait.get(i).x+0.5)*dim.echelle+dim.origX)+d, (int)((trait.get(i).y+0.5)*dim.echelle+dim.origY)+d);
				if(i != 1)
					gra.fillOval((int)((p.x+0.5)*dim.echelle+dim.origX)+d-10, (int)((p.y+0.5)*dim.echelle+dim.origY)+d-10, 20, 20);
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

class Dimensions {
	public int origX;
	public int origY;
	public double echelle;
	public double largeur;
	public double hauteur;
}
