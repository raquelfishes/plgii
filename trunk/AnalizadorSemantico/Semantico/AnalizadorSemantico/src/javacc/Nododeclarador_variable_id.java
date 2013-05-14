/* Generated By:JJTree: Do not edit this line. Nododeclarador_variable_id.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=Nodo,NODE_EXTENDS=XNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package javacc;

import tabla.simbolos.v2.Atributos;

public
class Nododeclarador_variable_id extends SimpleNode {
  String name;
  
  public Nododeclarador_variable_id(int id) {
    super(id);
  }

  public Nododeclarador_variable_id(Compilador p, int id) {
    super(p, id);
  }

  public void interpret()
  {
	  SimpleNode nTipo = null;
	  if (parent instanceof Nodocampo_declaracion || parent instanceof Nododeclaracion_variable_local){
		  nTipo = (Nodotipo)((SimpleNode)parent).children[0];
		  if(nTipo != null){
			  if (Compilador.gestorTS.esLexemaValido((String)value)){
				  Atributos atribs = new Atributos((String)value, (String)nTipo.value);
				  Compilador.gestorTS.insertar((String)value, atribs);
				  System.out.println("Insertando identificador en TS: " + (String)value +
						  " de tipo " + (String)nTipo.value);
			  }
			  else{
				  addErrSemantico(firstToken.beginLine, "el identificador: "+(String)value+" no es v�lido");
			  }
		  }
		  else{
			  throw new RuntimeException("Error obteniendo el tipo del identificador");
		  }
	  }
  }
}
/* JavaCC - OriginalChecksum=265a78532ed9fe4f80d1d6beac516403 (do not edit this line) */
