package tabla.simbolos.v2;


public class Test {
	
	
	public static void main(String[] args){
	
		/**
		 * Se tiene el siguiente codigo
		 * 
		 * class MiClase{
		 * 		
		 * 		int variableGlobal;
		 * 		
		 * 		public void main(){
		 * 		
		 * 			int x,y;	
		 * 			x = 2;
		 * 			y = 3; 
		 * 			
		 * 		}
		 * 
		 * 		public int dameInt(){
		 * 		
		 * 			int a = x+y;	
		 * 
		 * 			if(true){
		 * 				int b;
		 * 				while(b<10){
		 * 					int entero;
		 * 					int variableGlobal;
		 * 				}
		 * 			}
		 * 
		 * 			int entero;
		 * 		
		 * 		}
		 * 
		 * }
		 *
		 */
		
		CGestorTS gestor = new CGestorTS();
		
		gestor.insertar("variableGlobal", new Atributos("variableGlobal", "int"));
		
		//Se le el metodo main y por tanto una "{"
		gestor.nuevoAmbito("ambito_main");
			gestor.insertar("x", new Atributos("x", "int"));
			gestor.insertar("y", new Atributos("y", "int"));
		gestor.cierraAmbito();
		
		gestor.nuevoAmbito("ambito_dameInt");
			gestor.insertar("a", new Atributos("a", "int"));
			gestor.nuevoAmbito("ambito_if");
				gestor.insertar("b", new Atributos("b", "int"));
				gestor.nuevoAmbito("ambito_while");
				
					if(!gestor.esLexemaValido("entero"))
						System.err.println("variableGlobal ya esta definida cazurro!");
				
					gestor.insertar("entero", new Atributos("entero", "int"));
					
					if(!gestor.esLexemaValido("variableGlobal"))
						System.err.println("variableGlobal ya esta definida cazurro!");
					
					gestor.insertar("variableGlobal", new Atributos("variableGlobal", "int"));
				gestor.cierraAmbito();
			gestor.cierraAmbito();
			
			if(!gestor.esLexemaValido("b"))
				System.err.println("b ya esta definida cazurro!");
			
			if(!gestor.esLexemaValido("entero"))
				System.err.println("variableGlobal ya esta definida cazurro!");
			
			gestor.insertar("entero", new Atributos("entero", "int"));
			
		gestor.cierraAmbito();
		
		
		gestor.listarTodosLosLexemas();
	}

}
