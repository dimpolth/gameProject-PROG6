package ihm;

import java.awt.event.ActionEvent;
import javax.swing.Timer;

import modele.Case;

/**
 * Classe gérant les animations de disparition de pion.
 */
class AnimDisparition extends Animation {
	/**
	 * Constructeur unique.
	 * @param p Pion à faire disparaître.
	 */
	public AnimDisparition(Pion p) {
		pion = p;
		pion.tg.tempsGele = System.currentTimeMillis() + TerrainGraphique.ANIM_DISP;
		tempsDepart = System.currentTimeMillis();
		horloge = new Timer(10, this);
		horloge.start();
	}

	@Override
	/**
	 * Fait disparaître un pion.
	 * @param e Action qui initie la disparition.
	 */
	public void actionPerformed(ActionEvent e) {
		long actuel = System.currentTimeMillis();
		if (actuel - tempsDepart > TerrainGraphique.ANIM_DISP) {
			horloge.stop();
			pion.setCouleur(Case.Etat.vide);
			pion.repaint();
		} else {
			pion.setAlpha(1f - (float) ((double) (actuel - tempsDepart) / TerrainGraphique.ANIM_DISP));
			pion.repaint();
		}
	}
}
