package ihm;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumSet;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class PopupOptions extends Popup {
	JComboBox<String> theme;

	public enum TypeJoueur {

		HUMAIN("Humain"), IA1("IA facile"), IA2("IA moyenne"), IA3("IA difficile");

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

	JLabel selectJoueur1Etiq, selectJoueur2Etiq, themeEtiq;
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
		selectJoueur2.setSelectedItem(TypeJoueur.IA2.getTexte());
		
		
		
		
		contraintes.gridwidth = GridBagConstraints.REMAINDER;

		add(selectJoueur2, contraintes);

		themeEtiq = new JLabel("Thème graphique : ");
		contraintes.gridwidth = GridBagConstraints.REMAINDER;

		add(themeEtiq, contraintes);
		theme = new JComboBox<>(new String[] { "Boisé", "Marbre", "Sombre", "Standard", "Cochonou" });
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
		
	
		
		selectJoueur1.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {		        
		    	changementTypeJoueur(1);
		    }
		});
		
		selectJoueur2.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {		        
		    	changementTypeJoueur(2);
		    }
		});
		
		changementTypeJoueur(1);
		changementTypeJoueur(2);

	}
	
	public void changementTypeJoueur(int j){
		if(j==1)
			identifiantJoueur1.setEnabled( selectJoueur1.getSelectedIndex() <= 0  );
		else if(j==2)
			identifiantJoueur2.setEnabled( selectJoueur2.getSelectedIndex() <= 0  );
	}
	
	public void bloquer(boolean b){
		
		identifiantJoueur1.setEnabled(!b);
		identifiantJoueur2.setEnabled(!b);
		
		selectJoueur1.setEnabled(!b);
		selectJoueur2.setEnabled(!b);
		
		if(!b){
			changementTypeJoueur(1);
			changementTypeJoueur(2);
		}
		
	}
}
