package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class PopupReseau extends Popup {

	protected JLabel etiqId;
	protected JTextField champId;
	protected JLabel etiqHebergerIp;
	protected JTextField champHebergerIp;
	protected JLabel etiqRejoindreIp;
	protected JTextField champRejoindreIp;
	protected JLabel etiqHebergerPort;
	protected JTextField champHebergerPort;
	protected JLabel etiqRejoindrePort;
	protected JTextField champRejoindrePort;
	protected JLabel message;
	protected Bouton boutonHeberger, boutonRetour, boutonRejoindre;

	public PopupReseau(IHM i) {
		
		
		
		super(  new BorderLayout()  );
		this.setOpaque(false);
		PanelReseau nord = new PanelReseau();
		
		PanelReseau identifiant = new PanelReseau( new GridLayout(2,1) );
		etiqId = new JLabel("Mon identifiant");
		champId = formater(new JTextField("Joueur-" + System.currentTimeMillis() % 100));
		identifiant.add(etiqId);
		identifiant.add(champId);		
		nord.add(identifiant);
		
		PanelReseau heberger_rejoindre = new PanelReseau( new BorderLayout() );
		
		PanelReseau gauche = new PanelReseau();
		PanelReseau droite = new PanelReseau();
		PanelReseau milieu = new PanelReseau();
		//gauche.setBorder(BorderFactory.createLineBorder(Color.black));
		gauche.setPreferredSize(new Dimension(300,0));
		droite.setPreferredSize(new Dimension(300,0));
		heberger_rejoindre.add(gauche, BorderLayout.WEST);
		heberger_rejoindre.add(milieu, BorderLayout.CENTER);
		heberger_rejoindre.add(droite, BorderLayout.EAST);
		
		add(nord, BorderLayout.NORTH);
		add(heberger_rejoindre, BorderLayout.CENTER);
		
		PanelReseau heberger = new PanelReseau( new GridLayout(3,1) );
		PanelReseau rejoindre = new PanelReseau( new GridLayout(3,1) );
		gauche.add(heberger, BorderLayout.WEST);
		//heberger_rejoindre.add(new PanelReseau(),BorderLayout.CENTER);
		droite.add(rejoindre, BorderLayout.EAST);
		
		PanelReseau hebergerIp = new PanelReseau(  new GridLayout(2,1)  );
		etiqHebergerIp = new JLabel( "Mon adresse ip" );
		champHebergerIp = formater(new JTextField(""));
		champHebergerIp.setEditable(false);
		hebergerIp.add(etiqHebergerIp);
		hebergerIp.add(champHebergerIp);
		heberger.add(hebergerIp);
		
		PanelReseau hebergerPort = new PanelReseau(  new GridLayout(2,1)  );
		etiqHebergerPort = new JLabel( "Port" );
		champHebergerPort = formater(new JTextField("bla"));
		hebergerPort.add(etiqHebergerPort);
		hebergerPort.add(champHebergerPort);
		heberger.add(hebergerPort);
		
		PanelReseau hebergerBoutons = new PanelReseau();
		boutonHeberger = new Bouton("Heberger");
		boutonHeberger.addActionListener(new Ecouteur(Ecouteur.Bouton.RESEAU_HEBERGER, i));
		hebergerBoutons.add(boutonHeberger);
		heberger.add(hebergerBoutons);		
		
		
		PanelReseau rejoindreIp = new PanelReseau(  new GridLayout(2,1)  );
		etiqRejoindreIp = new JLabel( "Hôte" );
		champRejoindreIp = formater(new JTextField(""));
		rejoindreIp.add(etiqRejoindreIp);
		rejoindreIp.add(champRejoindreIp);
		rejoindre.add(rejoindreIp);
		
		PanelReseau rejoindrePort = new PanelReseau(  new GridLayout(2,1)  );
		etiqRejoindrePort = new JLabel( "Le port" );
		champRejoindrePort = formater(new JTextField(""));
		rejoindrePort.add(etiqRejoindrePort);
		rejoindrePort.add(champRejoindrePort);
		rejoindre.add(rejoindrePort);
		
		PanelReseau rejoindreBoutons = new PanelReseau();
		boutonRejoindre = new Bouton("Rejoindre");
		boutonRejoindre.addActionListener(new Ecouteur(Ecouteur.Bouton.RESEAU_REJOINDRE, i));
		rejoindreBoutons.add(boutonRejoindre);
		rejoindre.add(rejoindreBoutons);
		
		
		boutonRetour = new Bouton("Retour");
		boutonRetour.addActionListener(new Ecouteur(Ecouteur.Bouton.RESEAU_RETOUR, i));
		PanelReseau sud = new PanelReseau();
		
		PanelReseau bas = new PanelReseau(new GridLayout(2,1));
		message = new JLabel("");	
		bas.add(message);
		bas.add(boutonRetour);
		sud.add(bas);
		
		add(sud, BorderLayout.SOUTH);
		
		
		
		
		/*
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
		*/
		
	}
	
	JLabel formater(JLabel j){
		
		j.setOpaque(false);
		j.setPreferredSize(new Dimension(200,20));
		
		return j;
	}
	
	JTextField formater(JTextField e){
		
		e.setOpaque(true);
		e.setPreferredSize(new Dimension(160,35));
		
		return e;
	}
	
	JPanel formater(JPanel e){
		
		e.setOpaque(false);	
		
		return e;
	}
	
	class PanelReseau extends JPanel{
		PanelReseau(){
			super();
			this.formater();
		}
		PanelReseau(LayoutManager l){
			super(l);
			this.formater();
		}
		
		void formater(){
			this.setOpaque(false);	
		}
	}
	
}
