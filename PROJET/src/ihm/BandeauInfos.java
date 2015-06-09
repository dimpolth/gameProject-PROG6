package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import javax.swing.BorderFactory;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Classe du bandeau en haut de fenêtre.
 */
@SuppressWarnings("serial")
class BandeauInfos extends JPanel {
	/**
	 * Plateau de jeu.
	 */
	TerrainGraphique tg;
	/**
	 * Nom du joueur 1.
	 */
	JLabel j1_identifiant;
	/**
	 * Score du joueur 1.
	 */
	JLabel j1_score;
	/**
	 * Nom du joueur 2.
	 */
	JLabel j2_identifiant;
	/**
	 * Score du joueur 2.
	 */
	JLabel j2_score;
	/**
	 * Texte supérieur du bandeau.
	 */
	JLabel texteSup;
	/**
	 * Texte inférieur du bandeau.
	 */
	JLabel texteInf;
	/**
	 * Pion du joueur 1.
	 */
	JPanel j1_pion;
	/**
	 * Pion du joueur 2.
	 */
	JPanel j2_pion;
	/**
	 * Cadre du joueur 1.
	 */
	JPanel panJ1;
	/**
	 * Cadre du joueur 2.
	 */
	JPanel panJ2;
	/**
	 * Cadre des textes.
	 */
	JPanel panTextes;
	/**
	 * Identifiant du joueur en cours.
	 */
	int idSelect;
	
	/**
	 * Constructeur unique.
	 * @param pTg Plateau de jeu.
	 */
	BandeauInfos(TerrainGraphique pTg) {
		super(new BorderLayout(15, 15));
		tg = pTg;

		setOpaque(false);
		panJ1 = new JPanel(new BorderLayout());
		j1_pion = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(tg.imgPion1, 5, (getHeight() - (getWidth() - 2)) / 2, getWidth() - 5, getWidth() - 5, null);
			}
		};
		j1_pion.setPreferredSize(new Dimension(50, 0));
		j1_pion.setAlignmentY(CENTER_ALIGNMENT);

		panJ1.add(j1_pion, BorderLayout.WEST);

		JPanel sPanJ1 = new JPanel(new GridLayout(2, 1));
		sPanJ1.setOpaque(false);
		j1_identifiant = formater(new JLabel("Joueur 1"));
		sPanJ1.add(j1_identifiant);
		j1_score = formater(new JLabel("Pions : 22"));
		sPanJ1.add(j1_score);

		panJ1.add(sPanJ1, BorderLayout.CENTER);
		panJ1.setPreferredSize(new Dimension(230, 0));
		panJ1.setBorder(BorderFactory.createLineBorder(Color.black));
		panJ1.setBackground(Color.LIGHT_GRAY);
		panJ1.setOpaque(true);

		add(panJ1, BorderLayout.WEST);

		panTextes = new JPanel(new GridLayout(2, 1));

		texteSup = formater(new JLabel("Chargement..."));
		texteSup.setFont(new Font(getToolTipText(), Font.BOLD, 22));
		texteInf = formater(new JLabel("..."));
		panTextes.add(texteSup);
		panTextes.add(texteInf);

		panTextes.setBorder(BorderFactory.createLineBorder(Color.black));
		panTextes.setBackground(Color.LIGHT_GRAY);
		panTextes.setOpaque(true);

		add(panTextes, BorderLayout.CENTER);

		panJ2 = new JPanel(new BorderLayout());
		j2_pion = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(tg.imgPion2, 0, (getHeight() - (getWidth() - 5)) / 2, getWidth() - 5, getWidth() - 5, null);
			}
		};
		j2_pion.setPreferredSize(new Dimension(50, 0));
		j2_pion.setAlignmentY(CENTER_ALIGNMENT);

		panJ2.add(j2_pion, BorderLayout.EAST);

		JPanel sPanJ2 = new JPanel(new GridLayout(2, 1));
		sPanJ2.setOpaque(false);
		j2_identifiant = formater(new JLabel("Joueur 2"));
		sPanJ2.add(j2_identifiant);
		j2_score = formater(new JLabel("Pions : 22"));
		sPanJ2.add(j2_score);

		panJ2.setPreferredSize(new Dimension(230, 0));
		panJ2.setBorder(BorderFactory.createLineBorder(Color.black));
		panJ2.setBackground(Color.LIGHT_GRAY);
		panJ2.setOpaque(true);

		panJ2.add(sPanJ2, BorderLayout.CENTER);
		add(panJ2, BorderLayout.EAST);
		JPanel marge = new JPanel();
		marge.setOpaque(false);
		add(marge, BorderLayout.SOUTH);

	}
	/**
	 * Applique un style au bandeau.
	 * @param lab Style à appliquer.
	 * @return Bandeau avec le nouveau style.
	 */
	JLabel formater(JLabel lab) {
		// lab.setBorder(BorderFactory.createLineBorder(Color.black));
		lab.setOpaque(false);
		// lab.setBackground(Color.LIGHT_GRAY);
		lab.setForeground(Color.BLACK);
		lab.setHorizontalAlignment(JLabel.CENTER);
		lab.setFont(new Font(getToolTipText(), Font.BOLD, 18));
		return lab;
	}

	/**
	 * Modifie le nom d'un joueur.
	 * @param j Joueur dont le nom doit être modifié.
	 * @param nom Nouveau nom du joueur.
	 */
	void setIdentifiant(int j, String nom) {
		if (j == 1)
			j1_identifiant.setText(nom);
		else
			j2_identifiant.setText(nom);

	}

	/**
	 * Modifie le score d'un joueur.
	 * @param j Joueur dont le score doit être modifié.
	 * @param val Nouvelle valeur du score.
	 */
	void setScore(int j, int val) {
		if (j == 1)
			j1_score.setText("Score: " + Integer.toString(val));
		else
			j2_score.setText("Score: " + Integer.toString(val));
	}

	/**
	 * Modifie le texte du bandeau supérieur.
	 * @param txt Nouveau texte.
	 */
	void setTexteSup(String txt) {
		texteSup.setText(txt);
	}

	/**
	 * Modifie le texte du bandeau inférieur.
	 * @param txt Nouveau texte.
	 */
	void setTexteInf(String txt) {
		texteInf.setText(txt);
	}

	/**
	 * Met en valeur le bandeau du joueur courant.
	 * @param id Identifiant du joueur courant.
	 */
	public void setJoueurActif(int id) {
		idSelect = id;
		if (id == 1) {
			panJ1.setBackground(tg.ihm.theme.couleurJ1);
			panJ2.setBackground(tg.ihm.theme.couleurDefaut);
		} else if (id == 2) {
			panJ1.setBackground(tg.ihm.theme.couleurDefaut);
			panJ2.setBackground(tg.ihm.theme.couleurJ2);
		}
	}

}
