package ihm;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.Timer;

import modele.Case;

public class AnimSelect extends Animation {
	private boolean grandir;
	private boolean stop;

	public AnimSelect(Pion p) {
		pion = p;
		tempsDepart = System.currentTimeMillis();
		grandir = true;
		stop = false;
		horloge = new Timer(10, this);
		horloge.start();
	}

	public void stop() {
		stop = true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("DESACTIVE LES SORTIES ENCULE !!");
		long actuel = System.currentTimeMillis();
		if(actuel - tempsDepart > TerrainGraphique.ANIM_SELECT) {
			tempsDepart = System.currentTimeMillis();
			grandir = !grandir;
			if(grandir) {
				if(stop) {
					horloge.stop();
					pion.setBounds((int) ((pion.coord.y + 0.5) * pion.dim.echelle + pion.dim.origX), (int) ((pion.coord.x + 0.5) * pion.dim.echelle + pion.dim.origY), (int) (pion.dim.echelle / 2), (int) (pion.dim.echelle / 2));
					return;
				}
			}
		}
		float f;
		if(grandir) {
			f = 1+((float)(actuel - tempsDepart)/TerrainGraphique.ANIM_SELECT)/5;
		} else {
			f = 1.2f-((float)(actuel - tempsDepart)/TerrainGraphique.ANIM_SELECT)/5;
		}
		pion.facteurTaille = f;
		pion.componentResized(null);
	}
}
