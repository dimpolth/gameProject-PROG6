package ihm;

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
		horloge = new Timer(10,this);
		horloge.start();
	}
	
	public void stop() {
		stop = true;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		long actuel = System.currentTimeMillis();
		if(actuel - tempsDepart > 500) {
			grandir = !grandir;
			if(grandir) {
				if(stop) {
					horloge.stop();
					pion.setBounds((int) ((pion.coord.y + 0.5) * pion.dim.echelle + pion.dim.origX), (int) ((pion.coord.x + 0.5) * pion.dim.echelle + pion.dim.origY), (int) (pion.dim.echelle / 2), (int) (pion.dim.echelle / 2));
					return;
				}
			}
		}
		double x;
		if(grandir) {
			x = (double)(actuel - tempsDepart)/500;
		} else {
			x = (double)(actuel - tempsDepart)/500;
		}
		pion.setBounds((int) ((pion.coord.y + 0.5) * pion.dim.echelle + pion.dim.origX), (int) ((pion.coord.x + 0.5) * pion.dim.echelle + pion.dim.origY), (int) (x*pion.dim.echelle / 2), (int) (x*pion.dim.echelle / 2));
	}
}
