import java.awt.Point;
import java.util.*;


public class IntelligenceArtificielle {
	
	private Map map;
	private static final int MIN = -100;
	private static final int MAX = 100;
	
	
	public Point PosGagnante(Map map)
	{
		//ArrayList<Point> ListePos = new ArrayList<Point>();
		ArrayList<Point> CoupsJouables = new ArrayList<Point>();
		Iterator<Point> it;
		Random rand = new Random();
		
		Point pointGagnant = new Point();
		Point pointCourantJoue = new Point();
		
		Map mapClonee;
		this.map = map;
		
		int maxCourant = IntelligenceArtificielle.getMin();
		int tmp;
		int nombreAleatoire;
		
		CoupsJouables = listeCoupsJouables(this.map);
		System.out.println(CoupsJouables.size());
		
		it = CoupsJouables.iterator();
		
		while(it.hasNext()) // On parcourt tous les coups jouables (les cases à 1)
		{
			nombreAleatoire = rand.nextInt();
			pointCourantJoue = it.next();
			System.out.println("\nPosition testée : x = " + pointCourantJoue.x + " ; y = " + pointCourantJoue.y );
			
			mapClonee =  this.map.clone();
			
			System.out.println("Clone de base : \n"+ mapClonee.ToString());
			mapClonee.mangerPart(pointCourantJoue);
			
			System.out.println(mapClonee.ToString());
			
			tmp = Max(mapClonee,1);
			
			if(tmp > maxCourant) // || (tmp >= maxCourant && (nombreAleatoire%2 == 0)))
			{
				maxCourant = tmp;
				pointGagnant = pointCourantJoue;
				System.out.println("---- Nouveau max = "+maxCourant +" : Pos -> x = " + pointGagnant.x + " ; y = "+ pointGagnant.y);
			}
		}
		
		if(pointGagnant.x == 0 && pointGagnant.y == 0) // cas où on a trouvé aucune solution gagnante 
		{
			System.out.println("\nAucune solution gagnante !");
			pointGagnant = pointCourantJoue;
		}
			
		
		return pointGagnant;
	}
	/*
	 * Max : 
	 * entrées : mapClone, une map modifiée en fonction des coups simulés
	 * 			 niveau, le niveau de profondeur auquel on se trouve (ex : 3 = 3 coups simulés joués, 2 = 2 coups, etc)
	 */
	public int Max(Map mapClone, int niveau)
	{	
		ArrayList<Point> CoupsJouables = new ArrayList<Point>();
		Iterator<Point> it = null;
		Point pointCourantJoue = new Point();
		Map mapClonee;
		
		int maxCourant = IntelligenceArtificielle.getMin();
		int tmp;
		
		if(SimulationPartie.partieTerminee(mapClone))
		{
			System.out.println("\tMax trouvé : niveau "+ niveau + " !");
			return IntelligenceArtificielle.getMax();
		}
			
		else
		{
			CoupsJouables = listeCoupsJouables(mapClone);
			it = CoupsJouables.iterator();
			
			while(it.hasNext()) // On parcourt tous les coups jouables (les cases à 1)
			{
				pointCourantJoue = it.next();
		
				System.out.println("\tPosition testée (User/Max) : x = " + pointCourantJoue.x + " ; y = " + pointCourantJoue.y+" ; niveau = " + niveau  );
				
				mapClonee =  mapClone.clone();
				mapClonee.mangerPart(pointCourantJoue);
				
				tmp = Min(mapClonee, niveau+1);
				
				System.out.println("\tMin = "+tmp + " ; niveau : "+ niveau);
				
				if(tmp > maxCourant)
				{
					maxCourant = tmp;
				}
			}
		}
		
		return maxCourant;
	}
	
	public int Min(Map mapClone, int niveau)
	{
		ArrayList<Point> CoupsJouables = new ArrayList<Point>();
		Iterator<Point> it = null;
		Point pointCourantJoue = new Point();
		Map mapClonee;
		
		int minCourant = IntelligenceArtificielle.getMax();
		int tmp;
		
		if(SimulationPartie.partieTerminee(mapClone))
		{
			//System.out.println("\tMin trouvé : niveau "+ niveau + " !");
			return IntelligenceArtificielle.getMin();
		}
			
		else
		{
			CoupsJouables = listeCoupsJouables(mapClone);
			it = CoupsJouables.iterator();
			
			while(it.hasNext()) // On parcourt tous les coups jouables (les cases à 1)
			{
				pointCourantJoue = it.next();
			
				//System.out.println("\tPosition testée (IA/Min) : x = " + pointCourantJoue.x + " ; y = " + pointCourantJoue.y +" ; niveau = " + niveau );
				mapClonee =  mapClone.clone();
				mapClonee.mangerPart(pointCourantJoue);
				
				tmp = Max(mapClonee,niveau+1);
				
				System.out.println("\tMax = "+tmp + " ; niveau : "+ niveau);
				
				if(tmp < minCourant)
				{
					minCourant = tmp;
				}
			}
		}
		
		return minCourant;
	}
	
	public ArrayList<Point> listeCoupsJouables(Map map)
	{
		ArrayList<Point> CoupsJouables = new ArrayList<Point>();
		
		for(int i = 0; i < map.getLigne(); i++)
			for(int j = 0; j < map.getColonne(); j++)
				if(map.getMap()[i][j] == 1)
					CoupsJouables.add(new Point(i,j));
		
		return CoupsJouables;
	}
	public static int getMin() {
		return MIN;
	}
	public static int getMax() {
		return MAX;
	}
}
