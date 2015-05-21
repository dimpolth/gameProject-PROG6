import java.util.HashMap;
//import java.util.Iterator;

/*
 * 
 * Echange e = new Echange();
 * e.addIndication("Joueur 1 a gagné !");
 * 
 */

public class Echange {
	
	HashMap<String,Object> infos = new HashMap<String,Object>();
	
	public void vider(){
		infos.clear();
	}
	// Moteur -> IHM
	public void addIndication(String indic){
		infos.put("indication", indic);
	}
	
	public String getIndication(){
		return (String)infos.get("indication");
	}
	
	public void addTerrain(Terrain terrain){
		infos.put("terrain", terrain);
	}
	
	public Terrain getTerrain(){
		return (Terrain)infos.get("terrain");
	}
	
	// IHM -> MOTEUR
	
	public void addAnnuler(){
		infos.put("annuler", true);
	}
	public boolean getAnnuler(){
		return (boolean)infos.get("annuler");
	}
	
	
	/*public Iterator getInfos(){
		return infos;
	}*/
		

}

/*
// Moteur
Echange e = new Echange();


e.addTerrain( ... );
ihm.notifier(e);

// IHM

Echange e = ...;
if(e.getTerrain() != null){
	...
}
*/

