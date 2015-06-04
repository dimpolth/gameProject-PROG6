package ihm;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumSet;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.html.HTMLDocument.Iterator;

@SuppressWarnings("serial")
public class PopupOptions extends JPanel {
	JComboBox<String> theme;
	public enum TypeJoueur {

		HUMAIN("Humain"), IA1("IA Facile"), IA2("IA moyenne"), IA3("IA difficile");

		private String texte;

		private TypeJoueur(String texte) {
			this.texte = texte;
		}

		public String getTexte() {
			return this.texte;
		}

		public TypeJoueur getEnum() {
			return this;
		}
	}
	JLabel selectJoueur1Etiq, selectJoueur2Etiq;
	JTextField identifiantJoueur1, identifiantJoueur2;
	JComboBox<String> selectJoueur1, selectJoueur2;

	public PopupOptions(IHM i) {
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
		
	
		selectJoueur1Etiq = new JLabel("1er joueur : ");
		contraintes.gridwidth = GridBagConstraints.REMAINDER;
		add(selectJoueur1Etiq, contraintes);

		identifiantJoueur1 = new JTextField("Joueur 1");
		contraintes.gridwidth = 1;
		add(identifiantJoueur1, contraintes);

		String[] typeJoueurChoix = new String[4];
		EnumSet<TypeJoueur> enumSet = EnumSet.allOf(TypeJoueur.class);
		int indice = 0;
		for (TypeJoueur type : enumSet) {
			typeJoueurChoix[indice] = type.getTexte();
			indice++;
		}
		selectJoueur1 = new JComboBox<>(typeJoueurChoix);
		contraintes.gridwidth = GridBagConstraints.REMAINDER;
		
		add(selectJoueur1, contraintes);

		selectJoueur2Etiq = new JLabel("2ème joueur : ");
		contraintes.gridwidth = GridBagConstraints.REMAINDER;
		
		add(selectJoueur2Etiq, contraintes);
		identifiantJoueur2 = new JTextField("Joueur 2");
		contraintes.gridwidth = 1;
		add(identifiantJoueur2, contraintes);
		selectJoueur2 = new JComboBox<>(typeJoueurChoix);
		contraintes.gridwidth = GridBagConstraints.REMAINDER;
		
		add(selectJoueur2, contraintes);

		JLabel themeEtiq = new JLabel("Thème graphique : ");
		contraintes.gridwidth = GridBagConstraints.REMAINDER;
		
		add(themeEtiq, contraintes);
		theme = new JComboBox<>(new String[] { "Boisé", "Marbre" });
		contraintes.gridwidth = GridBagConstraints.REMAINDER;
		
		add(theme, contraintes);

		/*
		 * JLabel tourEtiq = new JLabel("Premier joueur alÃ©atoire : ");
		 * contraintes.gridwidth = 1; add(tourEtiq,contraintes); JCheckBox tour
		 * = new JCheckBox(); contraintes.gridwidth =
		 * GridBagConstraints.REMAINDER;; add(tour,contraintes);
		 */
		Bouton annuler = new Bouton("Annuler");
		annuler.addActionListener(new Ecouteur(Ecouteur.Bouton.OPTION_ANNULER, i));
		contraintes.gridwidth = 1;
		add(annuler, contraintes);
		Bouton valider = new Bouton("Valider");
		valider.addActionListener(new Ecouteur(Ecouteur.Bouton.OPTION_VALIDER, i));
		contraintes.gridwidth = GridBagConstraints.REMAINDER;

		add(valider, contraintes);

	}

	public void paintComponent(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.white);
		g.fillRect(10, 10, getWidth() - 20, getHeight() - 20);
	}
}
