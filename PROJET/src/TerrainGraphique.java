import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JComponent;


@SuppressWarnings("serial")
public class TerrainGraphique extends JComponent {
	Image imgPlateau;
	Image imgPiece1;
	Image imgPiece2;
	public TerrainGraphique() {
		imgPlateau = new ImageIcon("images/plateau.png").getImage();
		imgPiece1 = new ImageIcon("images/pionBlanc.png").getImage();
		imgPiece2 = new ImageIcon("images/pionNoir.png").getImage();
		
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D gra = (Graphics2D) g;
		/*gra.setColor(Color.red);
		gra.fillRect(0, 0, getWidth(), getHeight());
		gra.setColor(Color.white);
		gra.fillRect(10, 10, getWidth()-20, getHeight()-20);*/
		int origX = 0, origY = 0, echelle = 0, largeur = getWidth(), hauteur = getHeight();
		if((double)largeur/(double)hauteur > 1900.0/1100.0) {
			origX = (int) (((double)largeur - ((double)hauteur * 19.0/11.0))/2.0);
			largeur = (int) ((double)hauteur*19.0/11.0);
			echelle = (int) ((double)hauteur/5.5);
		} else {
			origY = (int) (((double)hauteur - ((double)largeur * 11.0/19.0))/2.0);
			hauteur = (int) ((double)largeur * 11.0/19.0);
			echelle = (int) ((double)largeur/9.5);
		}
		gra.drawImage(imgPlateau, origX, origY, largeur, hauteur, null);
		Random r = new Random();
		for(int i=0 ; i<9 ; i++) {
			for(int j=0 ; j<5 ; j++) {
				if(r.nextInt(2) == 10)
					gra.drawImage(imgPiece1, i*echelle+origX+echelle/2, j*echelle+origY+echelle/2, echelle/2, echelle/2, null);
				else
					gra.drawImage(imgPiece2, i*echelle+origX+echelle/2, j*echelle+origY+echelle/2, echelle/2, echelle/2, null);
			}
		}
	}
}
