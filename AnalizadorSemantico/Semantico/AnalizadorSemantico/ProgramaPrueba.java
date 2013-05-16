public class Prueba {
	int i, k;
	Persona paco;
	boolean isEnable;
		
	void main(){
		int unArray [ ] = new int [ 5 ] ;
		unArray[2] = 23;
		
		i = 2+3;
		//isEnable = 3;
		boolean b = false || isEnable;
		//b = true || 3;
		
		for (int r = 0; r < 10; r++){
			k = b*2;			
		}
		
	}
	
	
	//Hay que comprobar la sobrecarga de métodos?¿?¿?¿?¿¿?
	
	public int dameint1(char c){
		
		return 3+4;
		
	}
	
	public int dameint2(char c, boolean val){
		
		return 3;//+"4";
	}

	
	public int dameint3(char c,long l, String s){
		String s2 = s; 
		
		return 3+4;//"3+4";
	}
	
	
	void procedimiento(){
		
		return;
	}
}
