package ihm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import modele.Case;


class AnimDisparition extends Animation {
	public AnimDisparition(Pion p) {
		pion = p;
		pion.tg.tempsGele = System.currentTimeMillis()+TerrainGraphique.ANIM_DISP;
		tempsDepart = System.currentTimeMillis();
		horloge = new Timer(10,this);
		horloge.start();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		long actuel = System.currentTimeMillis();
		if(actuel - tempsDepart > TerrainGraphique.ANIM_DISP) {
			horloge.stop();
			pion.setCouleur(Case.Etat.vide);
			pion.repaint();
		} else {
			pion.setAlpha(1f-(float)((double)(actuel - tempsDepart)/TerrainGraphique.ANIM_DISP));
			pion.repaint();
		}
	}
}
