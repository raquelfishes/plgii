package tabla.simbolos.v2;

import java.util.ArrayList;

public class Atributos 
{
	private static int aliasCounter = 0;
    private String lexema, tipo, alias;
    private boolean esArray;
    private int tamArray;        
    private boolean esMetodo;
    private ArrayList<Atributos> parametros;
    
    public Atributos(String lexema, String tipo) {
    	this.lexema = lexema;
    	this.tipo = tipo;
    	this.alias = "$var_" + (aliasCounter++);
    }
    
    public Atributos(String lexema, String tipo, String alias) {
    	this.lexema = lexema;
    	this.tipo = tipo;
    	this.alias = alias;
    	//this.alias = "$var_" + (aliasCounter++);
    }
    
    public Atributos(String lexema, String tipo, int tamArray) {
    	this.lexema = lexema;
    	this.tipo = tipo;
    	this.esArray = true;
    	this.tamArray = tamArray;
    	//this.alias = "$var_" + (aliasCounter++);
    }
    
    public Atributos(String lexema, String tipo, ArrayList<Atributos> params){
    	this.lexema = lexema;
    	this.tipo = tipo;
    	this.esMetodo = true;
    	this.parametros = params;
    	this.alias = "metodos_sin_alias";
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
    	this.alias = a.alias;
    	this.esArray = a.esArray;
    	this.tamArray = a.tamArray;
    	this.esMetodo = a.esMetodo;
    	this.parametros = a.parametros;
    }
    
    public String getAlias(){
    	return alias;
    }
    
    public String toString(){
    	return "{Lexema:"+lexema+", Alias:"+alias+", Tipo:"+tipo+"}";
    }
}
