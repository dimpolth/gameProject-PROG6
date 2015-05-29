import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class TerrainGraphique extends JPanel implements ComponentListener{
	public static final int ANIM_DEPL = 1500;
	
	private Image imgPlateau;
	protected Image imgPion1;
	protected Image imgPion2;
	protected Image imgCroix;
	public IHM ihm;
	
	protected long tempsGele;
	private Dimensions dim;
	protected Pion[][] pions;
	
	private ArrayList<Point> prisesPossibles = new ArrayList<Point>(2);
	
	public TerrainGraphique(IHM i) {
		super(null);
		imgPlateau = new ImageIcon("images/themes/bois/plateau.png").getImage();
		imgPion1 = new ImageIcon("images/themes/bois/pion1.png").getImage();
		imgPion2 = new ImageIcon("images/themes/bois/pion2.png").getImage();
		imgCroix = new ImageIcon("images/themes/bois/croix.png").getImage();
		ihm = i;
		tempsGele = 0;
		dim = new Dimensions();
		pions = new Pion[5][9];
		for(int j = 0 ; j<5 ; j++) {
			for(int k = 0 ; k<9 ; k++) {
				pions[j][k] = new Pion(new Point(j,k), this, dim);
				add(pions[j][k]);
			}
		}
		addComponentListener(this);
	}
	public void dessinerTerrain( Case[][] c ){
		for(int i=0 ; i<5 ; i++) {
			for(int j=0 ; j<9 ; j++) {
				pions[i][j].setCouleur(c[i][j].getOccupation());
				pions[i][j].repaint();
			}
		}
	}
	public void deplacer(Point o, Point a) {
		pions[o.x][o.y].deplacer((Point)o.clone(), (Point)a.clone());
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
	public void cacherPrisesPossibles(){
		for(int i=0; i<prisesPossibles.size(); i++){
			pions[prisesPossibles.get(i).x][prisesPossibles.get(i).y].setPrisePossible(false);
		}
		prisesPossibles.clear();
	}
	
	public void manger(ArrayList<Point> pts){
		for(int i=0 ; i<pts.size() ; i++)
			new AnimDisparition(pions[pts.get(i).x][pts.get(i).y]);
	}
	
	public void clicCase(Point pt){
		
		if(prisesPossibles.contains(pt)){
			cacherPrisesPossibles();
		}
		
		Echange e = new Echange();
		e.ajouter("point", pt);
		ihm.com.envoyer(e);
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(new Color(238,238,238));
		g.fillRect(0, 0, getWidth(), getHeight());
		double largeur = getWidth(), hauteur = getHeight(), origX = 0, origY = 0;
		if (largeur / hauteur > 19.0 / 11.0) {
			origX = (int) ((largeur - (hauteur * 19.0 / 11.0)) / 2.0);
			largeur = hauteur * 19 / 11;
		} else {
			origY = (int) ((hauteur - (largeur * 11.0 / 19.0)) / 2.0);
			hauteur = largeur * 11 / 19;
		}
		g.drawImage(imgPlateau, (int) origX, (int) origY, (int) largeur, (int) hauteur, null);
	}
	@Override
	public void componentHidden(ComponentEvent e) {
	}
	@Override
	public void componentMoved(ComponentEvent e) {
	}
	@Override
	public void componentResized(ComponentEvent e) {
		dim.largeur = getWidth(); dim.hauteur = getHeight(); dim.echelle = -1; dim.origX = 0; dim.origY = 0;
		if (dim.largeur / dim.hauteur > 19.0 / 11.0) {
			dim.origX = (int) ((dim.largeur - (dim.hauteur * 19.0 / 11.0)) / 2.0);
			dim.largeur = dim.hauteur * 19 / 11;
			dim.echelle = dim.hauteur / 5.5;
		} else {
			dim.origY = (int) ((dim.hauteur - (dim.largeur * 11.0 / 19.0)) / 2.0);
			dim.hauteur = dim.largeur * 11 / 19;
			dim.echelle = dim.largeur / 9.5;
		}
		for(int i=0 ; i<5 ; i++) {
			for(int j=0 ; j<9 ; j++) {
				pions[i][j].componentResized(null);
			}
		}
	}
	@Override
	public void componentShown(ComponentEvent e) {
	}
}

@SuppressWarnings("serial")
class Pion extends JComponent implements MouseListener, ComponentListener {
	protected Point coord;
	protected TerrainGraphique tg;
	private Case.Etat etat;
	private boolean croix;
	private float alpha;
	protected Dimensions dim;
	public Pion(Point p, TerrainGraphique t, Dimensions d) {
		super();
		coord = p;
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
	}
	public void setAlpha(float f) {
		alpha = f;
	}
	public void setPrisePossible(boolean b){		
		croix = b;
		repaint();
	}
	public void deplacer(Point o, Point d) {
		new AnimDeplacement(this, o, d);
	}
	public void cacher(){
		etat = Case.Etat.vide;
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		g2.setComposite(ac);
		if(etat == Case.Etat.joueur1) {
			g2.drawImage(tg.imgPion1, 0, 0, getWidth(), getHeight(), null);
		} else if(etat == Case.Etat.joueur2) {
			g2.drawImage(tg.imgPion2, 0, 0, getWidth(), getHeight(), null);
		}
		if(croix) {
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
		setBounds((int) ((coord.y + 0.5) * dim.echelle + dim.origX), (int) ((coord.x + 0.5) * dim.echelle + dim.origY), (int) (dim.echelle / 2), (int) (dim.echelle / 2));
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
		if(System.currentTimeMillis() > tg.tempsGele && e.getX()  >= 0 && e.getX() < getHeight() && e.getY() >= 0 && e.getY() < getHeight()) {
			tg.clicCase(coord);
		}
	}
}

class AnimDeplacement implements ActionListener {
	private Pion pion;
	private long tempsDepart;
	private Point origine;
	private Point destination;
	private Timer horloge;
	public AnimDeplacement(Pion p, Point o, Point d) {
		pion = p;
		pion.tg.tempsGele = System.currentTimeMillis()+TerrainGraphique.ANIM_DEPL;
		tempsDepart = System.currentTimeMillis();
		origine = o;
		destination = d;
		horloge = new Timer(10,this);
		horloge.start();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		long actuel = System.currentTimeMillis();
		if(actuel - tempsDepart > TerrainGraphique.ANIM_DEPL) {
			horloge.stop();
			pion.setBounds((int) ((destination.y + 0.5) * pion.dim.echelle + pion.dim.origX), (int) ((destination.x + 0.5) * pion.dim.echelle + pion.dim.origY), (int) (pion.dim.echelle / 2), (int) (pion.dim.echelle / 2));
		} else {
			double xo = ((origine.y + 0.5) * pion.dim.echelle + pion.dim.origX);
			double xa = ((destination.y + 0.5) * pion.dim.echelle + pion.dim.origX);
			double yo =((origine.x + 0.5) * pion.dim.echelle + pion.dim.origY);
			double ya = ((destination.x + 0.5) * pion.dim.echelle + pion.dim.origY);
			double x = (double)(actuel - tempsDepart)/TerrainGraphique.ANIM_DEPL*12;
			double facteur = (1/(1+Math.exp(-x+6)));
			pion.setBounds((int)((xa-xo)*facteur+xo),(int)((ya-yo)*facteur+yo), (int) (pion.dim.echelle / 2), (int) (pion.dim.echelle / 2));
		}
	}
}
class AnimDisparition implements ActionListener {
	private Pion pion;
	private long tempsDepart;
	private Timer horloge;
	public AnimDisparition(Pion p) {
		pion = p;
		pion.tg.tempsGele = System.currentTimeMillis()+500;
		tempsDepart = System.currentTimeMillis();
		horloge = new Timer(10,this);
		horloge.start();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		long actuel = System.currentTimeMillis();
		if(actuel - tempsDepart > 500) {
			horloge.stop();
			pion.setCouleur(Case.Etat.vide);
			pion.repaint();
		} else {
			pion.setAlpha(1f-(float)((double)(actuel - tempsDepart)/500.0));
			pion.repaint();
		}
	}
}

class Dimensions {
	public int origX;
	public int origY;
	public double echelle;
	public double largeur;
	public double hauteur;
}