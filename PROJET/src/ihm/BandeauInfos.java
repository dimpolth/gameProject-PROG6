package ihm;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;



@SuppressWarnings("serial")
class BandeauInfos extends JPanel {

	JLabel j1_identifiant, j1_score, j1_pion, j2_identifiant, j2_score,
			j2_pion, texteSup, texteInf;

	BandeauInfos() {
		super(new GridBagLayout());

		GridBagConstraints contraintes = new GridBagConstraints();
		contraintes.gridwidth = 1;
		contraintes.weightx = 1;
		// contraintes.insets = new Insets(5,5,5,5);

		contraintes.fill = GridBagConstraints.HORIZONTAL;
		j1_identifiant = formater(new JLabel("Joueur 1"));
		contraintes.gridwidth = 5;
		contraintes.gridheight = 1;
		contraintes.gridx = 0;
		contraintes.gridy = 0;

		add(j1_identifiant, contraintes);

		j1_score = formater(new JLabel("Pions : 10"));
		contraintes.gridx = 0;
		contraintes.gridy = 1;
		add(j1_score, contraintes);

		texteSup = formater(new JLabel("Au tour de Joueur 2"));
		contraintes.fill = GridBagConstraints.BOTH;
		contraintes.gridwidth = 10;
		contraintes.gridheight = 2;
		contraintes.gridx = 7;
		contraintes.gridy = 0;

		add(texteSup, contraintes);

		j2_identifiant = formater(new JLabel("Joueur 2"));
		contraintes.gridx = 17;
		contraintes.gridy = 0;
		contraintes.gridwidth = 5;
		contraintes.gridheight = 1;
		// contraintes.fill = GridBagConstraints.HORIZONTAL;
		add(j2_identifiant, contraintes);

		j2_score = formater(new JLabel("Pions: 3"));

		contraintes.gridy = 1;
		add(j2_score, contraintes);

	}

	JLabel formater(JLabel lab) {
		lab.setBorder(BorderFactory.createLineBorder(Color.black));
		lab.setOpaque(true);
		lab.setBackground(Color.LIGHT_GRAY);
		lab.setForeground(Color.BLACK);
		lab.setHorizontalAlignment(JLabel.CENTER);
		lab.setFont(new Font(getToolTipText(), Font.BOLD, 18));
		return lab;
	}

	void setIdentifiant(int j, String nom) {
		if (j == 1)
			j1_identifiant.setText(nom);
		else
			j2_identifiant.setText(nom);
	}

	void setScore(int j, int val) {
		if (j == 1)
			j1_score.setText("Score: "+Integer.toString(val));
		else
			j2_score.setText("Score: "+Integer.toString(val));
	}

	void setTexteSup(String txt) {
		texteSup.setText(txt);
	}
	void setTexteInf(String txt) {
		//texte.setText(txt);
	}

}