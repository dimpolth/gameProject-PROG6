package reseau;
import java.util.HashMap;
import java.util.Map;


/*
 * 
 * Echange e = new Echange();
 * e.addIndication("Joueur 1 a gagné !");
 * 
 */

public class Echange {
	
	public Map<String,Object> infos = new HashMap<String,Object>();
	
	public void vider(){
		infos.clear();
	}
	
	public void ajouter(String id, Object donnee){
		infos.put(id, donnee);
	}
	
	public java.util.Set<String> getAll(){
		return infos.keySet();
	}
	
	public Object get(String cle){
		return infos.get(cle);
	}
	
	public void retirer(String cle){
		infos.remove(cle);
	}
	
	/*
	public void setPoint(Point pt){
		infos.put("point", pt);
	}
	
	public Point getPoint(){
		return (Point)infos.get("point");
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
	
	public  void addChoix() {
		
	}
	
	// IHM -> MOTEUR
	
	public void setAnnuler(){
		infos.put("annuler", true);
	}
	public boolean getAnnuler(){
		return (boolean)infos.get("annuler");
	}
	*/
	
	public void toto(){
	
		for (String dataType : this.getAll()) {		   
		    Object dataValue = this.get(dataType);
		    
		    switch(dataType){
		    	case "point" : System.out.println("Point reçu :"+((java.awt.Point)dataValue).toString()+""); break;
		    }
		    // ...
		}
	}
	
	
	
	/*public Iterator getInfos(){
		return infos;
	}*/
		

}

/*
 * 
 

 
 
 
 */


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

