package reseau;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.Iterator;


/*
 * 
 * Echange e = new Echange();
 * e.addIndication("Joueur 1 a gagné !");
 * 
 */

public class Echange implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 6L;
	public LinkedHashMap<String,Object> infos = new LinkedHashMap<String,Object>() ;
	
	public Echange(){
		
	}
	
	public void vider(){
		infos.clear();
		infos = new LinkedHashMap<String,Object>();
	}
	
	public Echange clone(){
		Echange e = new Echange();
		for (String dataType : this.getAll()) {
			Object dataValue = this.get(dataType);
			e.ajouter(dataType,dataValue);
		}
		return e;
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
	
	public String toString(){
		String s = "["+infos.size()+"]";
		for (String dataType : infos.keySet()) {
			Object dataValue = infos.get(dataType);
			s+=""+dataType+" : "+dataValue+" , ";
		}
		return s;
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
		    	case "point" : ;//System.out.println("Point reçu :"+((java.awt.Point)dataValue).toString()+""); break;
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

