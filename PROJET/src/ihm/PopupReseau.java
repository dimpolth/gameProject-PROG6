package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class PopupReseau extends Popup {

	protected JLabel etiqId;
	protected JTextField champId;
	protected JLabel etiqHeberger;
	protected JTextField champHeberger;
	protected JLabel etiqRejoindre;
	protected JTextField champRejoindre;
	protected JLabel etiqHebergerPort;
	protected JTextField champHebergerPort;
	protected JLabel etiqRejoindrePort;
	protected JTextField champRejoindrePort;
	protected JLabel message;
	protected Bouton boutonHeberger, boutonRetour, boutonRejoindre;

	public PopupReseau(IHM i) {
		super(new GridLayout(5, 1));
		JPanel panneauNord = new JPanel();
		this.add(panneauNord);
		panneauNord.setOpaque(false);
		etiqId = new JLabel("Mon identifiant");
		panneauNord.add(etiqId);
		champId = new JTextField("Joueur-" + System.currentTimeMillis() % 100);
		panneauNord.add(champId);

		JPanel panneauCentre = new JPanel(new GridLayout(1, 2));
		panneauCentre.setOpaque(false);
		this.add(panneauCentre);
		JPanel panneauCentreOuest = new JPanel();
		panneauCentre.add(panneauCentreOuest);
		panneauCentreOuest.setOpaque(false);
		etiqHeberger = new JLabel("Votre adresse IP :");
		panneauCentreOuest.add(etiqHeberger);
		try {
			champHeberger = new JTextField(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			champHeberger = new JTextField("UNKNOWN");
		}
		panneauCentreOuest.add(champHeberger);
		JPanel panneauCentreEst = new JPanel();
		panneauCentre.add(panneauCentreEst);
		panneauCentreEst.setOpaque(false);
		etiqRejoindre = new JLabel("IP à rejoindre :");
		panneauCentreEst.add(etiqRejoindre);
		try {
			champRejoindre = new JTextField(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			champRejoindre = new JTextField("UNKNOWN");
		}
		panneauCentreEst.add(champRejoindre);

		panneauCentre = new JPanel(new GridLayout(1, 2));
		panneauCentre.setOpaque(false);
		this.add(panneauCentre);
		panneauCentreOuest = new JPanel();
		panneauCentre.add(panneauCentreOuest);
		panneauCentreOuest.setOpaque(false);
		etiqHebergerPort = new JLabel("Votre adresse IP :");
		panneauCentreOuest.add(etiqHebergerPort);
		champHebergerPort = new JTextField("55555");
		panneauCentreOuest.add(champHebergerPort);
		panneauCentreEst = new JPanel();
		panneauCentre.add(panneauCentreEst);
		panneauCentreEst.setOpaque(false);
		etiqRejoindrePort = new JLabel("IP à rejoindre :");
		panneauCentreEst.add(etiqRejoindrePort);
		champRejoindrePort = new JTextField("55555");
		panneauCentreEst.add(champRejoindrePort);

		message = new JLabel("");
		this.add(message);

		JPanel panneauSud = new JPanel();
		this.add(panneauSud);
		panneauSud.setOpaque(false);
		boutonHeberger = new Bouton("Héberger");
		boutonHeberger.addActionListener(new Ecouteur(Ecouteur.Bouton.RESEAU_HEBERGER, i));
		panneauSud.add(boutonHeberger);
		boutonRetour = new Bouton("Retour");
		boutonRetour.addActionListener(new Ecouteur(Ecouteur.Bouton.RESEAU_RETOUR, i));
		panneauSud.add(boutonRetour);
		boutonRejoindre = new Bouton("Rejoindre");
		boutonRejoindre.addActionListener(new Ecouteur(Ecouteur.Bouton.RESEAU_REJOINDRE, i));
		panneauSud.add(boutonRejoindre);
	}
}
