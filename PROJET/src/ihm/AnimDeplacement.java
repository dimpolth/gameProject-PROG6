package ihm;

import java.awt.Point;
import java.awt.event.ActionEvent;
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
	 * @param p Le pion à animer.
	 * @param o Le point de départ du pion à animer.
	 * @param d Le point d'arrivé du pion à animer.
	 */
	public AnimDeplacement(Pion p, Point o, Point d) {
		pion = p;
		pion.tg.tempsGele = System.currentTimeMillis() + TerrainGraphique.ANIM_DEPL;
		tempsDepart = System.currentTimeMillis();
		origine = o;
		destination = d;
		horloge = new Timer(10, this);
		horloge.start();
	}

	@Override
	/**
	 * Effectue l'animation lors de la récéption d'un évènement.
	 * @param e Evènement déclacheur.
	 */
	public void actionPerformed(ActionEvent e) {

		long actuel = System.currentTimeMillis();
		if (actuel - tempsDepart > TerrainGraphique.ANIM_DEPL) {
			horloge.stop();
			pion.setBounds((int) ((destination.y + 0.5) * pion.dim.echelle + pion.dim.origX), (int) ((destination.x + 0.5) * pion.dim.echelle + pion.dim.origY), (int) (pion.dim.echelle / 2), (int) (pion.dim.echelle / 2));
		} else {
			double x = (double) (actuel - tempsDepart) / TerrainGraphique.ANIM_DEPL * 12;
			double facteur = (1 / (1 + Math.exp(-x + 6)));
			pion.facteurPos.x = (float) (((destination.x - origine.x) * facteur) + origine.x);
			pion.facteurPos.y = (float) (((destination.y - origine.y) * facteur) + origine.y);
			pion.replacer();
		}
	}
}
