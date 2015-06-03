package ihm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;




class Ecouteur implements ActionListener {
	public enum Bouton {
		REPRENDRE, SAUVEGARDER, CHARGER, REGLES, RECOMMENCER, QUITTER, MENU, PARAMETRES, ANNULER, REFAIRE, TERMINER, AIDE, OPTION_ANNULER, OPTION_VALIDER, REGLES_RETOUR
	}

	Bouton id;
	IHM i;

	public Ecouteur(Bouton id, IHM i) {
		this.id = id;
		this.i = i;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		i.action(id);
	}
}