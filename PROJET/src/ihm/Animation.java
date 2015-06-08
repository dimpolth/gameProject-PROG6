package ihm;

import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * Classe abstraite définissant les animations des pions. 
 */
public abstract class Animation implements ActionListener {
	/**
	 * Le pion à animer.
	 */
	protected Pion pion;
	/**
	 * Timer utilisé pour le départ de l'animation.
	 */
	protected long tempsDepart;
	/**
	 * Timer qui appelle la classe régulièrement.
	 */
	protected Timer horloge;
}
