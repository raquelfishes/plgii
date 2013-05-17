/* Generated By:JJTree: Do not edit this line. Nodoasignacion.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=Nodo,NODE_EXTENDS=XNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package javacc;

import tabla.simbolos.v2.Atributos;

public
class Nodoasignacion extends SimpleNode {
  public Nodoasignacion(int id) {
    super(id);
  }

  public Nodoasignacion(Compilador p, int id) {
    super(p, id);
  }
  
  public void interpret() {
	  SimpleNode n1 = null, n2 = null;
	  Atributos a1 = null, a2 = null;
	  
	  n1 = (SimpleNode) jjtGetChild(0);
	  n2 = (SimpleNode) jjtGetChild(1);
	  
	  // Interpretar sus 2 hijos, para comprobar sus tipos
	  n1.interpret();
	  n2.interpret();
	  
	  if (n1 instanceof Nodoidentificador){
		  a1 = Compilador.gestorTS.getAtributos((String)n1.value);
		  if (a1 == null ){
			  if (!Compilador.gestorTS.esMetodoRepetido((String)n1.value))
			  addErrSemantico(firstToken.beginLine, "el identificador '"+
					  (String)n1.value+"' no existe en este �mbito o es un tipo");
		  }
	  }
	  
	  if (n2 instanceof Nodoidentificador){
		  a2 = Compilador.gestorTS.getAtributos((String)n2.value);
		  if (a2 == null){
			  addErrSemantico(firstToken.beginLine, "el identificador '"+
					  (String)n2.value+"' no existe en este �mbito  o es un tipo");
		  }
	  }
	  
	  if (a1 != null && a2 != null){
		  if (!ConstantesTipos.esCompatible(a1.getTipo(), a2.getTipo())){
			  addErrSemantico(firstToken.beginLine, "no puede asignarse '"+
					  (String)n2.value+"' a '"+(String)n1.value+"' al tener tipos incompatibles");
		  }
	  }
	  else if(a1 != null){
		  if (!ConstantesTipos.esCompatible(a1.getTipo(), (String)n2.value)){
			  addErrSemantico(firstToken.beginLine, "no puede asignarse '"+
					  (String)n2.value+"' a '"+(String)n1.value+"' al tener tipos incompatibles");
		  }
	  }
	  else{
		  if (n1 instanceof NodoLiteralInteger){
			  // estamos en un array
			  Nodoidentificador idenArray = (Nodoidentificador)parent.jjtGetChild(0);
			  String tipoArray = Compilador.gestorTS.getAtributos((String)idenArray.value).getTipo();
			  String tipoVariable = a2 != null ? a2.getTipo() : (String)n2.value;
			  
			  if (!ConstantesTipos.esCompatible(tipoArray, tipoVariable)){
				  addErrSemantico(firstToken.beginLine, "tipo de datos del array '"+
						  (String)idenArray.value+"', incompatible");
			  }
		  }
		  else{	  
			  if (!(n2 instanceof Nodoidentificador && 
					  Compilador.gestorTS.esMetodoRepetido((String)n1.value))){
				  addErrSemantico(firstToken.beginLine, "no puede asignarse '"+
						  (String)n1.value+"', debe ser un identificador");
			  }
		  }
	  }	  
  }
}
/* JavaCC - OriginalChecksum=90ac55abfb3ed5f52371f3ed38ec99c7 (do not edit this line) */
