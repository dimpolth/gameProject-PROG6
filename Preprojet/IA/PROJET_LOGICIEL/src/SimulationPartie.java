import java.awt.Point;
import java.util.*;

public class SimulationPartie {
	
	public static void main(String[] args)
	{
		
		Map map;
		IntelligenceArtificielle IA;
		Point CoupAJouer;
		Scanner sc = new Scanner(System.in);
		Point posJouee= new Point(0,0);
		boolean TourJoueur = false;
		
		if(args.length > 1) // Taille fournie en paramètres
		{
			map = new Map(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
		}
		else
		{
			map = new Map(4,4);	
		}
			
		IA = new IntelligenceArtificielle();
		
		while(!partieTerminee(map))
		{
			System.out.println(map.ToString());
			
			if(TourJoueur)
			{
				while(map.getMap()[posJouee.x][posJouee.y] != 1)
				{
					System.out.println("Entrez la ligne : ");
					posJouee.x = sc.nextInt();
					System.out.println("Entrez la colonne : ");
					posJouee.y = sc.nextInt();
					
					if(map.getMap()[posJouee.x][posJouee.y] != 1)
						System.out.println("Erreur, position incorrecte !");
				}		
			}
			else
			{
				posJouee = IA.PosGagnante(map);
				System.out.println("Pos jouée : x = " + posJouee.x + " ; y = " + posJouee.y);
			}
				
			map.mangerPart(posJouee);
			
			
			TourJoueur = !TourJoueur;
		}
		
		if(TourJoueur ) // Si la partie est finie lors du tour du joueur l'IA a gagné
			System.out.print("L'IA gagne !");
		else
			System.out.print("Bravo vous avez gagné !");
		
		System.out.println(" Map finale : \n"+map.ToString());
	}
	
	public static boolean partieTerminee(Map map)
	{	
		for(int i = 0; i < map.getLigne(); i++)
			for(int j = 0; j < map.getColonne(); j++)
				if(map.getMap()[i][j] == 1)
					return false;
		
		return true;
	}
}
