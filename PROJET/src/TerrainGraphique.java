import java.awt.Color;
import java.awt.Graphics;
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
	private Image imgPlateau;
	private Image imgPion1;
	private Image imgPion2;
	private IHM ihm;
	protected long tempsGele;
	private Dimensions dim;
	protected Pion[][] pions;
	public TerrainGraphique(IHM i) {
		super(null);
		imgPlateau = new ImageIcon("images/plateau.png").getImage();
		imgPion1 = new ImageIcon("images/pionBlanc.png").getImage();
		imgPion2 = new ImageIcon("images/pionNoir.png").getImage();
		ihm = i;
		tempsGele = 0;
		dim = new Dimensions();
		pions = new Pion[5][9];
		for(int j = 0 ; j<5 ; j++) {
			for(int k = 0 ; k<9 ; k++) {
				pions[j][k] = new Pion(new Point(j,k), this, dim);
				pions[j][k].setImg(imgPion1);
				add(pions[j][k]);
			}
		}
		addComponentListener(this);
	}
	public void dessinerTerrain( Case[][] c ){
		for(int i=0 ; i<5 ; i++) {
			for(int j=0 ; j<9 ; j++) {
				if(c[i][j].getOccupation() == Case.Etat.joueur1)
					pions[i][j].setImg(imgPion1);
				else if(c[i][j].getOccupation() == Case.Etat.joueur2)
					pions[i][j].setImg(imgPion2);
				else
					pions[i][j].setImg(null);
				pions[i][j].repaint();
			}
		}
	}
	public void deplacer(Point o, Point a, ArrayList<Point> l) {
		pions[o.x][o.y].deplacer((Point)o.clone(), (Point)a.clone(), l);
		Point tmp = pions[o.x][o.y].coord;
		pions[o.x][o.y].coord = pions[a.x][a.y].coord;
		pions[a.x][a.y].coord = tmp;
		Pion tmpP = pions[o.x][o.y];
		pions[o.x][o.y] = pions[a.x][a.y];
		pions[a.x][a.y] = tmpP;
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
	protected Dimensions dim;
	private Image img;
	public Pion(Point p, TerrainGraphique t, Dimensions d) {
		super();
		coord = p;
		tg = t;
		dim = d;
		img = null;
		addComponentListener(this);
		addMouseListener(this);
	}
	public void setImg(Image i) {
		img = i;
	}
	public void deplacer(Point o, Point d, ArrayList<Point> l) {
		new AnimDeplacement(this, o, d, l);
	}
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
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
		}
	}
}

class AnimDeplacement implements ActionListener {
	private Pion pion;
	private long tempsDepart;
	private Point origine;
	private Point destination;
	private ArrayList<Point> aSupprimer;
	private Timer horloge;
	public AnimDeplacement(Pion p, Point o, Point d, ArrayList<Point> l) {
		pion = p;
		pion.tg.tempsGele = System.currentTimeMillis();
		tempsDepart = System.currentTimeMillis();
		origine = o;
		destination = d;
		aSupprimer = l;
		horloge = new Timer(10,this);
		horloge.start();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		long actuel = System.currentTimeMillis();
		if(actuel - tempsDepart > 1500) {
			horloge.stop();
			pion.setBounds((int) ((destination.y + 0.5) * pion.dim.echelle + pion.dim.origX), (int) ((destination.x + 0.5) * pion.dim.echelle + pion.dim.origY), (int) (pion.dim.echelle / 2), (int) (pion.dim.echelle / 2));
			for(int i=0 ; i<aSupprimer.size() ; i++) {
				new AnimDisparition(pion.tg.pions[aSupprimer.get(i).x][aSupprimer.get(i).y]);
			}
		} else {
			double xo = ((origine.y + 0.5) * pion.dim.echelle + pion.dim.origX);
			double xa = ((destination.y + 0.5) * pion.dim.echelle + pion.dim.origX);
			double yo =((origine.x + 0.5) * pion.dim.echelle + pion.dim.origY);
			double ya = ((destination.x + 0.5) * pion.dim.echelle + pion.dim.origY);
			double x = (double)(actuel - tempsDepart)/1500*12;
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
		tempsDepart = System.currentTimeMillis();
		horloge = new Timer(10,this);
		horloge.start();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		long actuel = System.currentTimeMillis();
		if(actuel - tempsDepart > 500) {
			horloge.stop();
			pion.setImg(null);
			pion.repaint();
		} else {
			pion.setImg(null);
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