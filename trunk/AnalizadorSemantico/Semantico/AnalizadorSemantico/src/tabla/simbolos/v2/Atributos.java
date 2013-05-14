package tabla.simbolos.v2;

public class Atributos 
{
        private String lexema, tipo, alias;
        private boolean esArray; // provisional
        private int tamArray;
        private static int aliasCounter = 0;
        //...
        
        public Atributos(String lexema, String tipo) {
        	this.lexema = lexema;
        	this.tipo = tipo;
        	this.alias = "$var_" + (aliasCounter++);
        }
        
        public Atributos(String lexema, String tipo, int tamArray) {
        	this.lexema = lexema;
        	this.tipo = tipo;
        	this.esArray = true;
        	this.tamArray = tamArray;
        	this.alias = "$var_" + (aliasCounter++);
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
        
        public String getAlias(){
        	return alias;
        }
        
        public String toString(){
        	return "{Lexema:"+lexema+", Alias:"+alias+", Tipo:"+tipo+"}";
        }
}
