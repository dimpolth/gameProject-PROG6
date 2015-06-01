package ihm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import modele.Case;


class AnimDisparition extends Animation {
	public AnimDisparition(Pion p) {
		pion = p;
		pion.tg.tempsGele = System.currentTimeMillis()+500;
		tempsDepart = System.currentTimeMillis();
		horloge = new Timer(10,this);
		horloge.start();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		long actuel = System.currentTimeMillis();
		if(actuel - tempsDepart > 500) {
			horloge.stop();
			pion.setCouleur(Case.Etat.vide);
			pion.repaint();
		} else {
			pion.setAlpha(1f-(float)((double)(actuel - tempsDepart)/500.0));
			pion.repaint();
		}
	}
}