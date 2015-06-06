package ihm;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import modele.Joueur;

class ExecuterDans implements ActionListener {
	IHM ihm;
	String id;
	Object dataValue;
	Timer t;

	public ExecuterDans(IHM pI, String pId, Object pO, int pT) {
		ihm = pI;
		id = pId;
		dataValue = pO;
		t = new Timer(pT, this);
		t.start();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (id) {
		case "pionsManges":
			ihm.tg.manger((ArrayList<Point>) dataValue);
			break;
		case "choixPrise":
			ihm.tg.afficherPrisesPossibles((Point[]) dataValue);
			break;
		case "joueurs":
			Joueur[] joueurs = (Joueur[]) dataValue;
			for (int j = 1; j <= 2; j++) {
				ihm.bandeauInfos.setIdentifiant(j, joueurs[j - 1].getNom());
				ihm.bandeauInfos.setScore(j, joueurs[j - 1].getScore());
			}
			break;
		}
		t.stop();
	}
}