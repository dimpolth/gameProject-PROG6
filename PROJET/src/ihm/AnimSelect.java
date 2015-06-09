package ihm;

import java.awt.event.ActionEvent;

import javax.swing.Timer;

/**
 * Classe gérant les animations de séléction.
 */
public class AnimSelect extends Animation {
	/**
	 * Définit si l'animation est croissante ou décroissante.
	 */
	private boolean grandir;
	/**
	 * Averti si l'animation doit s'arrêter.
	 */
	private boolean stop;
	
	/**
	 * Constructeur unique.
	 * @param p Pion à animer.
	 */
	public AnimSelect(Pion p) {
		pion = p;
		tempsDepart = System.currentTimeMillis();
		grandir = true;
		stop = false;
		horloge = new Timer(10, this);
		horloge.start();
	}

	/**
	 * Arrête l'animation.
	 */
	public void stop() {
		stop = true;
	}

	/**
	 * Lance l'animation du pion.
	 * @param e Action qui initie l'animation.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		long actuel = System.currentTimeMillis();
		if (actuel - tempsDepart > TerrainGraphique.ANIM_SELECT) {
			tempsDepart = System.currentTimeMillis();
			grandir = !grandir;
			if (grandir) {
				if (stop) {
					horloge.stop();
					pion.setBounds((int) ((pion.coord.y + 0.5) * pion.dim.echelle + pion.dim.origX), (int) ((pion.coord.x + 0.5) * pion.dim.echelle + pion.dim.origY), (int) (pion.dim.echelle / 2), (int) (pion.dim.echelle / 2));
					return;
				}
			}
		}
		float f;
		if (grandir) {
			f = 1 + ((float) (actuel - tempsDepart) / TerrainGraphique.ANIM_SELECT) / 5;
		} else {
			f = 1.2f - ((float) (actuel - tempsDepart) / TerrainGraphique.ANIM_SELECT) / 5;
		}
		pion.facteurTaille = f;
		pion.replacer();
	}
}
