package ihm;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

@SuppressWarnings("serial")
class PopupMenu extends JPanel {
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
		contraintesCategorie.insets = new Insets(2, 2, 25, 2);

		Bouton boutonMenuReprendre = new Bouton("Reprendre la partie");
		boutonMenuReprendre.addActionListener(new Ecouteur(Ecouteur.Bouton.REPRENDRE, i));
		add(boutonMenuReprendre, contraintesCategorie);

		Bouton boutonMenuSauvegarder = new Bouton("Sauvegarder la partie");
		boutonMenuSauvegarder.addActionListener(new Ecouteur(Ecouteur.Bouton.SAUVEGARDER, i));
		add(boutonMenuSauvegarder, contraintes);
		Bouton boutonMenuCharger = new Bouton("Charger une partie");
		boutonMenuCharger.addActionListener(new Ecouteur(Ecouteur.Bouton.CHARGER, i));
		add(boutonMenuCharger, contraintes);
		Bouton boutonMenuRegles = new Bouton("Regles du jeu");
		boutonMenuRegles.addActionListener(new Ecouteur(Ecouteur.Bouton.REGLES, i));
		add(boutonMenuRegles, contraintesCategorie);

		Bouton buttonRecommencer = new Bouton("Nouvelle partie");
		buttonRecommencer.addActionListener(new Ecouteur(Ecouteur.Bouton.RECOMMENCER, i));
		add(buttonRecommencer, contraintes);
		Bouton boutonMenuQuitter = new Bouton("Quitter le jeu");
		boutonMenuQuitter.addActionListener(new Ecouteur(Ecouteur.Bouton.QUITTER, i));
		add(boutonMenuQuitter, contraintes);
	}

	public void paintComponent(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.white);
		g.fillRect(10, 10, getWidth() - 20, getHeight() - 20);
	}
}