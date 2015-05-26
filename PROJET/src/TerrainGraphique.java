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
	private Dimensions dim;
	private Pion[][] pions;
	public TerrainGraphique(IHM i) {
		super(null);
		imgPlateau = new ImageIcon("images/plateau.png").getImage();
		imgPion1 = new ImageIcon("images/pionBlanc.png").getImage();
		imgPion2 = new ImageIcon("images/pionNoir.png").getImage();
		ihm = i;
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
	public void deplacer(Point o, Point a) {
		pions[o.x][o.y].deplacer(o);
		Point tmp = pions[o.x][o.y].coord;
		pions[o.x][o.y].coord = pions[a.x][a.y].coord;
		pions[a.x][a.y].coord = tmp;
		Pion tmpP = pions[o.x][o.y];
		pions[o.x][o.y] = pions[a.x][a.y];
		pions[a.x][a.y] = tmpP;
	}
	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
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
class Pion extends JComponent implements MouseListener, ActionListener, ComponentListener {
	public Point coord;
	private TerrainGraphique tg;
	private Dimensions dim;
	private Image img;
	private Timer anim;
	private long animT;
	private Point animO;
	public Pion(Point p, TerrainGraphique t, Dimensions d) {
		super();
		coord = p;
		tg = t;
		dim = d;
		img = null;
		anim = new Timer(17, this);
		addComponentListener(this);
		addMouseListener(this);
	}
	public void setImg(Image i) {
		img = i;
	}
	public void deplacer(Point o) {
		anim.start();
		animT = System.currentTimeMillis();
		animO = o;
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
	public void actionPerformed(ActionEvent e) {
		long actuel = System.currentTimeMillis();
		if(actuel - animT > 750) {
			anim.stop();
			animT = 0;
			animO = null;
			setBounds((int) ((coord.y + 0.5) * dim.echelle + dim.origX), (int) ((coord.x + 0.5) * dim.echelle + dim.origY), (int) (dim.echelle / 2), (int) (dim.echelle / 2));
		} else {
			double xo = ((animO.y + 0.5) * dim.echelle + dim.origX);
			double xa = ((coord.y + 0.5) * dim.echelle + dim.origX);
			double yo =((animO.x + 0.5) * dim.echelle + dim.origY);
			double ya = ((coord.x + 0.5) * dim.echelle + dim.origY);
			double facteur = (double)(actuel - animT)/750;
			setBounds((int)((xa-xo)*facteur+xo),(int)((ya-yo)*facteur+yo), (int) (dim.echelle / 2), (int) (dim.echelle / 2));
		}
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
		if(e.getX()  >= 0 && e.getX() < getHeight() && e.getY() >= 0 && e.getY() < getHeight())
			tg.deplacer(new Point(coord.x, coord.y), new Point(coord.x+1, coord.y+1));
	}
}

class Dimensions {
	public int origX;
	public int origY;
	public double echelle;
	public double largeur;
	public double hauteur;
}