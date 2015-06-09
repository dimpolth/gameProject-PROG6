package ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
//import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;

/**
 * Classe gérant les boutons.
 */
@SuppressWarnings("serial")
public class Bouton extends JButton implements MouseListener {
	/**
	 * Liste des boutons.
	 */
	static List<Bouton> boutons = new ArrayList<Bouton>();
	/**
	 * Image du bouton en état normal.
	 */
	Image fondNormal;
	/**
	 * Image du bouton lorsqu'il est survolé par la souris.
	 */
	Image fondSurvol;
	/**
	 * Image du bouton lorsqu'il est cliqué ou grisé.
	 */
	Image fondClique;
	/**
	 * Image courante du bouton.
	 */
	Image fond;
	/**
	 * Couleur de la police en état normal.
	 */
	Color couleurNormal;
	/**
	 * Couleur de la police en état grisé. 
	 */
	Color couleurGrisee;
	/**
	 * Couleur de police courante.
	 */
	Color couleur;
	/**
	 * Vrai si le bouton est actif, faux sinon.
	 */
	boolean active = true;
	/**
	 * Texte du bouton.
	 */
	String texte;
	
	/**
	 * Constructeur unique.
	 * @param texte Texte à inscrire sur le bouton.
	 */
	Bouton(String texte) {
		this.texte = texte;
		this.setBorderPainted(false);
		Bouton.boutons.add(this);
		this.addMouseListener(this);
		this.setPreferredSize(new Dimension(120, 45));
		this.setOpaque(false);

	}
	/**
	 * Dessine les boutons en fonction des paramètres.
	 */
	public void paintComponent(Graphics gr) {
		Graphics2D g = (Graphics2D) gr;
		Font font = new Font("Arial", Font.BOLD, 14);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();

		g.drawImage(fond, 0, 0, this.getWidth(), this.getHeight(), null);

		g.setColor(couleur);

		g.drawString(texte, this.getWidth() / 2 - fm.stringWidth(texte) / 2, this.getHeight() / 2 + fm.getHeight() / 4);

	}
	/**
	 * Modifie le thème d'un bouton.
	 * @param pTheme Type du nouveau thème.
	 * @param cNorm Couleur des boutons en état normal.
	 * @param cGris Couleur des boutons en état grisé.
	 */
	public void setTheme(Theme.Type pTheme, Color cNorm, Color cGris) {
		couleurNormal = cNorm;
		couleurGrisee = cGris;
		try {
			fondNormal = ImageIO.read(getClass().getResource("/images/themes/" + pTheme.getId() + "/bouton_normal.png"));
			fondSurvol = ImageIO.read(getClass().getResource("/images/themes/" + pTheme.getId() + "/bouton_survol.png"));
			fondClique = ImageIO.read(getClass().getResource("/images/themes/" + pTheme.getId() + "/bouton_clique.png"));
			if (active) {
				fond = fondNormal;
				couleur = couleurNormal;
			} else {
				fond = fondClique;
				couleur = couleurGrisee;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Modifie le thème de tous les boutons.
	 * @param pTheme Type du nouveau thème.
	 * @param cNorm Couleur des boutons en état normal.
	 * @param cGris Couleur des boutons en état grisé.
	 */
	public static void setThemeTous(Theme.Type pTheme, Color cNorm, Color cGris) {

		Iterator<Bouton> it = boutons.iterator();
		while (it.hasNext()) {
			Bouton bt = (Bouton) it.next();
			bt.setTheme(pTheme, cNorm, cGris);
		}

	}
	
	/**
	 * Active ou désactive un bouton.
	 * @param b Vrai si le bouton doit être activé, faux sinon.
	 */
	@Override
	public void setEnabled(boolean b) {
		super.setEnabled(b);
		active = b;
		fond = (b) ? fondNormal : fondClique;
		couleur = (b) ? couleurNormal : couleurGrisee;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// fond = fondClique;

	}

	/**
	 * Passe le bouton de l'état normal à l'état survolé.
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
		if (active) {
			if (fond == fondNormal)
				fond = fondSurvol;
		}
	}
	
	/**
	 * Passe le bouton de l'état survolé à l'état normal.
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {
		if (active) {
			if (fond == fondSurvol)
				fond = fondNormal;
		}
	}
	/**
	 * Passe le bouton de l'état survolé à l'état cliqué.
	 */
	@Override
	public void mousePressed(MouseEvent arg0) {
		if (active) {
			fond = fondClique;
			couleur = couleurGrisee;
		}
	}
	/**
	 * Passe le bouton de l'état cliqué à l'état survolé ou normal.
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if (active) {
			if (e.getY() > 0 && e.getY() < this.getHeight() && (e.getX() > 0 && e.getX() < this.getWidth())) {
				fond = fondSurvol;
			} else
				fond = fondNormal;
			couleur = couleurNormal;
		}
	}
}
