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
		horloge = new Timer(10, this);
		horloge.start();
	}

	public void stop() {
		stop = true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
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
		double f;
		if(grandir) {
			f = 1+(double)(actuel - tempsDepart)/TerrainGraphique.ANIM_SELECT;
		} else {
			f = 1+2*TerrainGraphique.ANIM_SELECT/1000-(double)(actuel - tempsDepart)/TerrainGraphique.ANIM_SELECT;
		}
		pion.setBounds(10,10,44,44);
		int w = (int) (f*pion.dim.echelle / 2);
		int h = (int) (f*pion.dim.echelle / 2);
		int ec = (w - (int) (pion.dim.echelle / 2))/2;
		int x = (int) ((pion.coord.y + 0.5) * pion.dim.echelle + pion.dim.origX) - ec;
		int y = (int) ((pion.coord.x + 0.5) * pion.dim.echelle + pion.dim.origY) - ec;
		pion.setBounds(x,y,w,h);
	}
}
