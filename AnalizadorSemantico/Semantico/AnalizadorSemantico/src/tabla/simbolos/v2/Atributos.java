package tabla.simbolos.v2;

public class Atributos 
{
        private String lexema, tipo;
        private boolean esArray; // provisional
        private int tamArray;
        //...
        
        public Atributos(String lexema, String tipo) {
        	this.lexema = lexema;
        	this.tipo = tipo;
        }
        
        public Atributos(String lexema, String tipo, int tamArray) {
        	this.lexema = lexema;
        	this.tipo = tipo;
        	this.esArray = true;
        	this.tamArray = tamArray;
        }
        
        public String getTipo(){
        	return tipo;
        }
        
        public String getLexema(){
        	return lexema;
        }
        
        public boolean getEsArray(){
        	return esArray;
        }
        
        public int getTamArray(){
        	return tamArray;
        }
        
        public void setAtributos(Atributos a) {
        	this.lexema = a.lexema;
        	this.tipo = a.tipo;
        	this.esArray = a.esArray;
        	this.tamArray = a.tamArray;
        }
}
