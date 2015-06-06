package ihm;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class PopupReseau extends Popup {

	JCheckBox etreHote;
	JTextField hote;
	JLabel message;
	JTextField identifiant;

	public PopupReseau(IHM i) {
		super(new GridBagLayout());

		GridBagConstraints contraintes = new GridBagConstraints();
		contraintes.gridwidth = GridBagConstraints.REMAINDER;
		contraintes.fill = GridBagConstraints.BOTH;
		contraintes.insets = new Insets(5, 5, 5, 5);
		contraintes.ipady = 15;
		contraintes.ipadx = 15;

		GridBagConstraints contraintes_groupe = (GridBagConstraints) contraintes.clone();
		contraintes_groupe.gridwidth = 1;
		GridBagConstraints contraintes_groupe_fin = (GridBagConstraints) contraintes.clone();
		contraintes_groupe_fin.gridwidth = GridBagConstraints.REMAINDER;

		JLabel selectJoueurEtiq = new JLabel("Mon identifiant");
		contraintes.gridwidth = GridBagConstraints.REMAINDER;
		add(selectJoueurEtiq, contraintes);

		identifiant = new JTextField("Joueur-" + System.currentTimeMillis() / 1000 / 1000);
		contraintes.gridwidth = 1;
		add(identifiant, contraintes_groupe_fin);

		JLabel selectJoueur1Etiq = new JLabel("Nouveau serveur");
		contraintes.gridwidth = GridBagConstraints.REMAINDER;
		add(selectJoueur1Etiq, contraintes);

		etreHote = new JCheckBox("Lancer un serveur sur cette machine");

		contraintes_groupe.gridwidth = GridBagConstraints.REMAINDER;
		add(etreHote, contraintes_groupe);

		/*
		 * hote = new JTextField(); contraintes.gridwidth =
		 * GridBagConstraints.REMAINDER;
		 * 
		 * add(hote, contraintes);
		 */

		JLabel selectJoueur2Etiq = new JLabel("Rejoindre une partie");
		contraintes.gridwidth = GridBagConstraints.REMAINDER;

		add(selectJoueur2Etiq, contraintes);
		hote = new JTextField("127.0.0.1:55555");
		contraintes.gridwidth = 1;
		add(hote, contraintes_groupe_fin);

		/*
		 * JLabel tourEtiq = new JLabel("Premier joueur alÃ©atoire : ");
		 * contraintes.gridwidth = 1; add(tourEtiq,contraintes); JCheckBox tour
		 * = new JCheckBox(); contraintes.gridwidth =
		 * GridBagConstraints.REMAINDER;; add(tour,contraintes);
		 */

		message = new JLabel("");
		message.setForeground(Color.RED);
		contraintes.gridwidth = GridBagConstraints.REMAINDER;
		add(message, contraintes);

		Bouton annuler = new Bouton("Annuler");
		annuler.addActionListener(new Ecouteur(Ecouteur.Bouton.RESEAU_ANNULER, i));
		contraintes.gridwidth = 1;
		add(annuler, contraintes);
		Bouton valider = new Bouton("Valider");
		valider.addActionListener(new Ecouteur(Ecouteur.Bouton.RESEAU_VALIDER, i));
		contraintes.gridwidth = GridBagConstraints.REMAINDER;

		add(valider, contraintes);

	}
}
