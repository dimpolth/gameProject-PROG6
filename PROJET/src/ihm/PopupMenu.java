package ihm;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

@SuppressWarnings("serial")
class PopupMenu extends Popup {
	Bouton boutonMenuReseau, boutonMenuLocal;
	Bouton boutonMenuSauvegarder, boutonMenuCharger;
	Bouton boutonRecommencer;
	
	public PopupMenu(IHM i) {
		super(new GridBagLayout());
		// this.setBorder(BorderFactory.createLineBorder(Color.black));
		GridBagConstraints contraintes = new GridBagConstraints();
		contraintes.gridwidth = GridBagConstraints.REMAINDER;
		contraintes.fill = GridBagConstraints.BOTH;
		contraintes.insets = new Insets(2, 2, 2, 2);
		contraintes.ipady = 50;
		contraintes.ipadx = 180;

		GridBagConstraints contraintesCategorie = (GridBagConstraints) contraintes.clone();
		contraintesCategorie.insets = new Insets(2, 2, 15, 2);

		Bouton boutonMenuReprendre = new Bouton("Reprendre la partie");
		boutonMenuReprendre.addActionListener(new Ecouteur(Ecouteur.Bouton.REPRENDRE, i));
		add(boutonMenuReprendre, contraintesCategorie);

		boutonMenuSauvegarder = new Bouton("Sauvegarder la partie");
		boutonMenuSauvegarder.addActionListener(new Ecouteur(Ecouteur.Bouton.SAUVEGARDER, i));
		add(boutonMenuSauvegarder, contraintes);		
		boutonMenuCharger = new Bouton("Charger une partie");
		boutonMenuCharger.addActionListener(new Ecouteur(Ecouteur.Bouton.CHARGER, i));
		add(boutonMenuCharger, contraintes);
		boutonMenuReseau = new Bouton("Jouer en réseau");
		boutonMenuReseau.addActionListener(new Ecouteur(Ecouteur.Bouton.MODE, i));
		add(boutonMenuReseau, contraintesCategorie);
		
		boutonMenuLocal = new Bouton("Jouer en local");
		boutonMenuLocal.addActionListener(new Ecouteur(Ecouteur.Bouton.MODE, i));
		add(boutonMenuLocal, contraintesCategorie);
		
		Bouton boutonMenuRegles = new Bouton("Regles du jeu");
		boutonMenuRegles.addActionListener(new Ecouteur(Ecouteur.Bouton.REGLES, i));
		add(boutonMenuRegles, contraintesCategorie);

		boutonRecommencer = new Bouton("Nouvelle partie");
		boutonRecommencer.addActionListener(new Ecouteur(Ecouteur.Bouton.RECOMMENCER, i));
		add(boutonRecommencer, contraintes);
		Bouton boutonMenuQuitter = new Bouton("Quitter le jeu");
		boutonMenuQuitter.addActionListener(new Ecouteur(Ecouteur.Bouton.QUITTER, i));
		add(boutonMenuQuitter, contraintes);
	}
}