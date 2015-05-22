

public class Theme  {
	
	public enum Type{
		BOIS("bois");
		
		private String s;
		Type(String s){
			this.s = s;
		}
		
		public String getId(){
			return this.s;
		}
	}
	
	Type id ;
	
	Theme(){
		this.id = Type.BOIS; 
		 
	}
	
	public void setTheme(Type pId){
		this.id = pId;
		Bouton.setThemeTous(pId);
		
	}
	
}
