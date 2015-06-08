package modele;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe permettant de gérer l'historique de la partie.
 */
public class Historique implements Serializable {

	/**
	 * Historique de la partie.
	 */
	public ArrayList<Terrain> histoPrincipal;
	/**
	 * Historique du tour en cours.
	 */
	public ArrayList<Point> histoTour;
	/**
	 * Indice du Terrain affiché à l'écran.
	 */
	private int itPrincipal;
	/**
	 * Indice du point de déplacement.
	 */
	private int itTour;
	
	/**
	 * Constructeur par défaut.
	 */
	public Historique() {
		histoPrincipal = new ArrayList<Terrain>();
		histoTour = new ArrayList<Point>();
		itPrincipal = 0;
	}
	
	/**
	 * Constructeur par copie.
	 * @param h L'historique à copier.
	 */
	public Historique(Historique h) {
		histoPrincipal = h.histoPrincipal;
		histoTour = h.histoTour;
		itPrincipal = h.itPrincipal;
		itTour = h.itTour;
	}
	
	/**
	 * Permet de savoir quel état du Terrain est affiché.
	 * @return L'itérateur de l'historique de la partie.
	 */
	public int getItPrincipal(){
		return itPrincipal;
	}
	
	/**
	 * Ajoute un Terrain à l'historique de la partie.
	 * @param t Le Terrain à ajouter.
	 */
	public void ajouterTour(Terrain t) {
		if(itPrincipal < histoPrincipal.size()-1) {
			for(int i = histoPrincipal.size()-1; i > itPrincipal; i--) { 
				histoPrincipal.remove(i);
			}
		}
		Terrain c = t.copie();
		histoPrincipal.add(c);
		itPrincipal = histoPrincipal.size()-1;
		//;//System.out.println("itprincipal"+itPrincipal);
		
	}
	
	/**
	 * Ajoute un coup à l'historique du tour.
	 * @param p Le point où s'est déplacé un pion.
	 */
	public void ajouterCoup(Point p) {
		histoTour.add((Point)p.clone());
		itTour++;
		
	}
	
	/**
	 * Fait revenir le terrain au tour précédant.
	 * @return Terrain du tour précédant.
	 */
	public Terrain annuler() {
		if(itPrincipal<=0){
			//;//System.out.println("PAS de ANNULER POSSIBLE");
			return null;}
		else {
			
			Terrain temp =histoPrincipal.get(--itPrincipal);
			//temp.dessineTableauAvecIntersections();
			return temp;
		}
	}
	
	/**
	 * Fait passer le terrain au tour suivant.
	 * @return Terrain du tour suivant.
	 */
	public Terrain refaire() {
		if(itPrincipal == histoPrincipal.size()-1)
			return null;
		else {
			return histoPrincipal.get(++itPrincipal);
		}
	}
	
	/**
	 * Efface l'historique du tour quand il est fini.
	 */
	public void effacerHistoTour() {
		histoTour = new ArrayList<Point>();
	}
	
	/**
	 * Affiche l'historique en console. Utilisé pour le debug.
	 */
	public void afficher() {
		;//System.out.println("----------------");
		for(int it = 0; it < histoPrincipal.size(); it++) {
			Terrain tmp = histoPrincipal.get(it);
			//Terrain tmp = histoPrincipal.get(itPrincipal);
			//tmp.dessineTableauAvecIntersections();
		}
		;//System.out.println("----------------");
		;//System.out.println(itPrincipal);
		;//System.out.println("----------------");
	}
	
}
