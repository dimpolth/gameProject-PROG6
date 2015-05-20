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
		double largeur = getWidth(), hauteur = getHeight(), echelle = -1, origX = 0, origY = 0;
		if(largeur/hauteur > 19.0/11.0) {
			origX = (int)((largeur - (hauteur * 19.0/11.0))/2.0);
			largeur = hauteur * 19/11;
			echelle = hauteur/5.5;
		} else {
			origY = (int)((hauteur - (largeur * 11.0/19.0))/2.0);
			hauteur = largeur * 11/19;
			echelle = largeur/9.5;
		}
		gra.drawImage(imgPlateau, (int)origX, (int)origY, (int)largeur, (int)hauteur, null);
		for(int i=0 ; i<9 ; i++) {
			for(int j=0 ; j<5 ; j++) {
				gra.drawImage(imgPiece1, (int)((i+0.5)*echelle+origX), (int)((j+0.5)*echelle+origY), (int)(echelle/2), (int)(echelle/2), null);
			}
		}
	}
}
