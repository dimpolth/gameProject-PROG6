package ihm;

import java.awt.Point;
import java.util.ArrayList;

public class CoupGraphique implements Runnable {
	private Point[] deplacement;
	private Point[] choixPrise;
	private ArrayList<Point> pionsManges;
	private int[] score;
	private String bandeauSup, beandeauInf;
	private static TerrainGraphique tg;
	private static boolean animationEnCours = false;
	
	public CoupGraphique(Point[] d, Point[] c, ArrayList<Point> p, int[] s) {
		deplacement = d;
		choixPrise = c;
		pionsManges = p;
		score = s;
	}
	
	public void lancer(){
		Thread t = new Thread(this);
		t.start();
	}
	public void run() {
		CoupGraphique.animationEnCours = true;	
		
		
		
		
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
		} else if(choixPrise != null){
			tg.afficherPrisesPossibles(choixPrise);
		}
		
		
		try {
			Thread.sleep(TerrainGraphique.ANIM_DISP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		tg.ihm.bandeauInfos.setScore(1,score[0]);
		tg.ihm.bandeauInfos.setScore(2,score[1]);
		
		//tg.ihm.bandeauInfos.setTexteSup(bandeauSup);
		//tg.ihm.bandeauInfos.setTexteInf(bandeauInf);
		
		CoupGraphique.animationEnCours = false;
		
		if(tg.lCoups.size() != 0) {
			tg.lCoups.pollFirst().lancer();			
		}
		
		
	}
	
	public static void afficherCoups(TerrainGraphique tg){
		CoupGraphique.tg = tg;
		if(!CoupGraphique.animationEnCours){			
			tg.lCoups.pollFirst().lancer();
		}
	}
}
