import java.awt.Point;


public class Map {
	private int[][] map;
	private int ligne;
	private int colonne;
	
	
	public Map(int ligne, int colonne)
	{
		this.map = new int[ligne][colonne];
		this.ligne = ligne;
		this.colonne = colonne;
		
		initMap();
	}
	
	public void initMap()
	{
		for(int i = 0; i < this.ligne; i++)
			for(int j = 0; j < this.colonne;j++)
				this.map[i][j] = 1;
		
		this.map[0][0] = 2; // case empoisonnée en haut à gauche
		
		/*
		this.map[1][0] = 1;
		this.map[0][1] = 1;
		this.map[0][2] = 1;
		*/
	}
	
	public void mangerPart(Point pos)
	{
		for(int i = 0; i < this.ligne; i++)
			for(int j = 0; j < this.colonne; j++)
				if(i >= pos.x && j >= pos.y)
				{
					this.map[i][j] = 0;
				}
	}
	
	public int getLigne()
	{
		return this.ligne;
	}
	
	public int getColonne()
	{
		return this.colonne;
	}
	
	public int[][] getMap()
	{
		return this.map;
	}
	
	public String ToString(){
		String s = "";
		
		for(int i = 0; i < this.ligne; i++)
		{
			for(int j = 0; j < this.colonne; j++)
			{
				switch(this.map[i][j])
				{
					case 0:
						s += "-";
					break;
					case 1:
						s += "O";
					break;
					case 2:
						s += "X";
					break;
				}
			}
			
			s += "\n";
		}
		
		return s;
	}

	@Override
	public Map clone()
	{
		Map mapClonee = new Map(this.ligne,this.colonne);
		
		for(int i = 0; i < this.ligne; i++)
			for(int j = 0; j < this.colonne; j++)
				mapClonee.map[i][j] = this.map[i][j];
		
		return mapClonee;
	}
	

}
