package tabla.simbolos;

public class Atributos 
{
        private String lexema, tipo;
        //...
        
        public Atributos(String lexema, String tipo) {
        	this.lexema = lexema;
        	this.tipo = tipo;
        }
        
        public String getTipo(){
        	return tipo;
        }
}
