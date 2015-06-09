package ihm;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.JComponent;
import javax.swing.Timer;

/**
 * Classe gérant les animations de déplacement des pions.
 */
class AnimDeplacement extends Animation {

	/**
	 * Point de départ du pion.
	 */
	private Point origine;
	/**
	 * Point d'arrivé du pion.
	 */
	private Point destination;

	/**
	 * Constructeur unique.
	 * 
	 * @param p
	 *            Le pion à animer.
	 * @param o
	 *            Le point de départ du pion à animer.
	 * @param d
	 *            Le point d'arrivé du pion à animer.
	 */
	public AnimDeplacement(Pion p, Point o, Point d) {
		pion = p;
		pion.tg.tempsGele = System.currentTimeMillis() + TerrainGraphique.ANIM_DEPL;
		tempsDepart = System.currentTimeMillis();
		origine = o;
		destination = d;
		pion.tg.td.setAnim(p, origine);
		horloge = new Timer(10, this);
		horloge.start();
	}

	/**
	 * Effectue l'animation lors de la récéption d'un évènement.
	 * 
	 * @param e
	 *            Evènement déclacheur.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		long actuel = System.currentTimeMillis();
		if (actuel - tempsDepart > TerrainGraphique.ANIM_DEPL) {
			horloge.stop();
			pion.tg.td.setAnim(null, null);
			pion.setBounds((int) ((destination.y + 0.5) * pion.dim.echelle + pion.dim.origX), (int) ((destination.x + 0.5) * pion.dim.echelle + pion.dim.origY), (int) (pion.dim.echelle / 2), (int) (pion.dim.echelle / 2));
		} else {
			double x = (double) (actuel - tempsDepart) / TerrainGraphique.ANIM_DEPL * 12;
			double facteur = (1 / (1 + Math.exp(-x + 6)));
			pion.facteurPos.x = (float) (((destination.x - origine.x) * facteur) + origine.x);
			pion.facteurPos.y = (float) (((destination.y - origine.y) * facteur) + origine.y);
			pion.replacer();
			pion.tg.td.repaint();
		}
	}
}

@SuppressWarnings("serial")
class TraitDeplacement extends JComponent {
	private Pion pion;
	private Point depart;

	public void setAnim(Pion p, Point d) {
		pion = p;
		depart = d;
	}

	public void paintComponent(Graphics g) {
		Graphics2D gra = (Graphics2D)g;
		if (pion != null) {
			gra.setColor(Theme.couleurChemin);
			gra.setStroke(new BasicStroke(6));
			int d = (int) (pion.dim.echelle / 4);
			gra.fillOval((int) ((depart.y + 0.5) * pion.dim.echelle + pion.dim.origX) + d - 10, (int) ((depart.x + 0.5) * pion.dim.echelle + pion.dim.origY) + d - 10, 20, 20);
			gra.drawLine((int) ((depart.y + 0.5) * pion.dim.echelle + pion.dim.origX) + d,
						 (int) ((depart.x + 0.5) * pion.dim.echelle + pion.dim.origY) + d,
						 (int) ((pion.facteurPos.y + 0.75) * pion.dim.echelle + pion.dim.origX),
						 (int) ((pion.facteurPos.x + 0.75) * pion.dim.echelle + pion.dim.origY));
		}
	}
}
