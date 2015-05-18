
public class Terrain {
	enum typeCase
	{
		j1,
		j2,
		vide
	}
	
	private Case tableau[][];
	
	public Terrain() 
	{
		this.tableau = new Case[5][9];
	}

	public Case[][] getTableau() {
		return tableau;
	}

	public void setTableau(Case tableau[][]) {
		this.tableau = tableau;
	}
}
