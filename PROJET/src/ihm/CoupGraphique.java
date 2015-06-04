package ihm;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import modele.Case;

public class CoupGraphique implements Runnable, Serializable {
	private Case[][] terrain;
	private Point[] deplacement;
	private Point[] choixPrise;
	private ArrayList<Point> pionsManges, chemin;
	private int[] score;
	private static TerrainGraphique tg;
	private static boolean animationEnCours = false;
	private String bandeauSup,bandeauInf;
	
	public CoupGraphique(Point[] d, Point[] c, ArrayList<Point> p, int[] s,String bS,String bI, ArrayList<Point> chemin) {
		deplacement = d;
		choixPrise = c;
		pionsManges = p;
		score = s;
		bandeauSup = bS;
		bandeauInf = bI;
		this.chemin = chemin;
	}
	public CoupGraphique(Point[] d, Point[] c, ArrayList<Point> p, int[] s, ArrayList<Point> chemin) {
		deplacement = d;
		choixPrise = c;
		pionsManges = p;
		score = s;
		bandeauSup = null;
		bandeauInf = null;
		this.chemin = chemin;
		
	}
	
	public CoupGraphique(Case[][] t){
		terrain = t;
	}
	
	
	public void lancer(){
		Thread t = new Thread(this);
		t.start();
	}
	public void run() {
		
		if(terrain != null){
			tg.dessinerTerrain(terrain);
		}
		
		
		if(deplacement != null){
			tg.deplacer(deplacement[0], deplacement[1]);
			//System.out.println("DEPLACEMENT : "+deplacement[0]+" > "+deplacement[1]);
		}
		
		try {
			Thread.sleep(TerrainGraphique.ANIM_DEPL/4);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(pionsManges != null) {
			tg.manger(pionsManges);
		}
		if(choixPrise != null){
			tg.afficherPrisesPossibles(choixPrise);
		} else {
			tg.cacherPrisesPossibles();
		}
		try {
			Thread.sleep(TerrainGraphique.ANIM_DISP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		tg.trait = chemin;
		tg.repaint();
		
		if(score != null){
			tg.ihm.bandeauInfos.setScore(1,score[0]);
			tg.ihm.bandeauInfos.setScore(2,score[1]);
		}
		
		if(bandeauSup != null){
		tg.ihm.bandeauInfos.setTexteSup(bandeauSup);}
		
		if(bandeauInf != null){
		tg.ihm.bandeauInfos.setTexteInf(bandeauInf);}
		
		CoupGraphique.animationEnCours = false;
		
		if(tg.lCoups.size() != 0) {
			tg.lCoups.pollFirst().lancer();
		}
		
		
	}
	
	public static void afficherCoups(TerrainGraphique tg){
		CoupGraphique.tg = tg;
		if(!CoupGraphique.animationEnCours){
			CoupGraphique.animationEnCours = true;	
			tg.lCoups.pollFirst().lancer();
		}
	}
}
