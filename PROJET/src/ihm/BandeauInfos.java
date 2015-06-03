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



@SuppressWarnings("serial")
class BandeauInfos extends JPanel {
	
	TerrainGraphique tg;
	JLabel j1_identifiant, j1_score,  j2_identifiant, j2_score,
			texteSup, texteInf;
	JPanel j1_pion, j2_pion;

	BandeauInfos(TerrainGraphique pTg) {
		super( new BorderLayout(15,15) );
		tg = pTg;
		
		
		JPanel panJ1 = new JPanel( new BorderLayout() ); 
		j1_pion = new JPanel(){
			public void paintComponent(Graphics g){
				g.drawImage(tg.imgPion1, 0, 0, this.getWidth(),this.getWidth(), null);				
			}
		};
		j1_pion.setPreferredSize(new Dimension(50,0));
		j1_pion.setAlignmentY(CENTER_ALIGNMENT);
		
		panJ1.add(j1_pion,BorderLayout.WEST);
		
		JPanel sPanJ1 = new JPanel( new GridLayout(2,1) );
		sPanJ1.setOpaque(false);
		j1_identifiant = formater(new JLabel("Joueur 1"));		
		sPanJ1.add(j1_identifiant);
		j1_score = formater(new JLabel("Score : 22"));		
		sPanJ1.add(j1_score);
		
		panJ1.add(sPanJ1,BorderLayout.CENTER);
		panJ1.setPreferredSize(new Dimension(200,0));
		panJ1.setBorder(BorderFactory.createLineBorder(Color.black));
		panJ1.setBackground(Color.LIGHT_GRAY);
		panJ1.setOpaque(true);
	
		add(panJ1, BorderLayout.WEST);
		
		JPanel panTextes = new JPanel(new GridLayout(2,1));
		
		texteSup = formater(new JLabel("Chargement..."));
		texteSup.setFont(new Font(getToolTipText(), Font.BOLD, 22));
		texteInf = formater(new JLabel("..."));
		panTextes.add(texteSup);
		panTextes.add(texteInf);
		
		
		
		
		panTextes.setBorder(BorderFactory.createLineBorder(Color.black));
		panTextes.setBackground(Color.LIGHT_GRAY);
		panTextes.setOpaque(true);
		
		
		add(panTextes, BorderLayout.CENTER);
		
		JPanel panJ2 = new JPanel( new BorderLayout() ); 
		j2_pion = new JPanel(){
			public void paintComponent(Graphics g){
				g.drawImage(tg.imgPion2, 0, 0, this.getWidth(),this.getWidth(),  null);				
			}
		};
		j2_pion.setPreferredSize(new Dimension(50,0));
		j2_pion.setAlignmentY(CENTER_ALIGNMENT);

		panJ2.add(j2_pion,BorderLayout.EAST);
		
		JPanel sPanJ2 = new JPanel( new GridLayout(2,1) );
		sPanJ2.setOpaque(false);
		j2_identifiant = formater(new JLabel("Joueur 2"));		
		sPanJ2.add(j2_identifiant);
		j2_score = formater(new JLabel("Score : 22"));		
		sPanJ2.add(j2_score);
		
		panJ2.setPreferredSize(new Dimension(200,0));
		panJ2.setBorder(BorderFactory.createLineBorder(Color.black));
		panJ2.setBackground(Color.LIGHT_GRAY);
		panJ2.setOpaque(true);
		
		panJ2.add(sPanJ2,BorderLayout.CENTER);
		add(panJ2, BorderLayout.EAST);
		add(new JPanel(), BorderLayout.SOUTH);
		
	
	}

	JLabel formater(JLabel lab) {
		//lab.setBorder(BorderFactory.createLineBorder(Color.black));
		lab.setOpaque(false);
		//lab.setBackground(Color.LIGHT_GRAY);
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
		texteInf.setText(txt);
	}

}