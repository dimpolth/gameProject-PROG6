package ihm;

import java.awt.Point;
import java.util.ArrayList;

public class CoupGraphique implements Runnable {
	private Point[] deplacement;
	private Point[] choixPrise;
	private ArrayList<Point> pionsManges;
	private int[] score;
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
		tg.deplacer(deplacement[0], deplacement[1]);
		try {
			Thread.sleep(TerrainGraphique.ANIM_DEPL);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(choixPrise == null) {
			tg.manger(pionsManges);
		} else {
			tg.afficherPrisesPossibles(choixPrise);
		}
		//TODO maj du score
		try {
			Thread.sleep(TerrainGraphique.ANIM_DISP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		CoupGraphique.animationEnCours = false;
		
		if(tg.lCoups.size() != 0) {
			tg.lCoups.pollFirst().lancer();			
		}
		
		
	}
	
	public static void afficherCoups(TerrainGraphique tg){
		if(!CoupGraphique.animationEnCours)
			tg.lCoups.pollFirst().lancer();
	}
}
