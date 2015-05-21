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

@SuppressWarnings("serial")
public class TerrainGraphique extends JPanel implements ComponentListener{
	Image imgPlateau;
	Pion[][] pions;
	Image imgPiece1;
	Image imgPiece2;

	public TerrainGraphique() {
		super(null);
		addComponentListener(this);
		imgPlateau = new ImageIcon("images/plateau.png").getImage();
		imgPiece1 = new ImageIcon("images/pionBlanc.png").getImage();
		imgPiece2 = new ImageIcon("images/pionNoir.png").getImage();
		pions = new Pion[5][9];
		for(int i=0 ; i<5 ; i++) {
			for(int j=0 ; j<9 ; j++) {
				pions[i][j] = new Pion(new Point(i,j), imgPiece1);
				add(pions[i][j]);
			}
		}
	}

	public void deplacer(Point o, Point a) {
		
	}
	
	public void paintComponent(Graphics g) {
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
		double largeur = getWidth(), hauteur = getHeight(), echelle = -1, origX = 0, origY = 0;
		if (largeur / hauteur > 19.0 / 11.0) {
			origX = (int) ((largeur - (hauteur * 19.0 / 11.0)) / 2.0);
			largeur = hauteur * 19 / 11;
			echelle = hauteur / 5.5;
		} else {
			origY = (int) ((hauteur - (largeur * 11.0 / 19.0)) / 2.0);
			hauteur = largeur * 11 / 19;
			echelle = largeur / 9.5;
		}
		for(int i=0 ; i<5 ; i++) {
			for(int j=0 ; j<9 ; j++) {
				pions[i][j].setBounds((int) ((j + 0.5) * echelle + origX), (int) ((i + 0.5) * echelle + origY), (int) (echelle / 2), (int) (echelle / 2));
			}
		}
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}
}

@SuppressWarnings("serial")
class Pion extends JComponent implements MouseListener, ActionListener {
	private Image img;
	protected Point co;
	Pion(Point p, Image i) {
		addMouseListener(this);
		co = p;
		img = i;
	}
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
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
			System.out.println(co.x+"-"+co.y);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
	}
}
