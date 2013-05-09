/* Generated By:JJTree: Do not edit this line. Nodoexpresion_asignacion_array.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=Nodo,NODE_EXTENDS=XNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package javacc;

import tabla.simbolos.Atributos;

public
class Nodoexpresion_asignacion_array extends SimpleNode {
  public Nodoexpresion_asignacion_array(int id) {
    super(id);
  }

  public Nodoexpresion_asignacion_array(Compilador p, int id) {
    super(p, id);
  }

  public void interpret()
  {
	  SimpleNode nTipo = null;
	  if (parent instanceof Nodocampo_declaracion || parent instanceof Nododeclaracion_variable_local){
		  nTipo = (Nodotipo)((SimpleNode)parent).children[0];
		  if(nTipo != null){
			  Atributos atribs = new Atributos((String)value, (String)nTipo.value);
			  Compilador.gestorTS.insertar((String)value, atribs);
			  System.out.println("Insertando identificador en TS: " + (String)value +
					  " de tipo " + (String)nTipo.value);
		  }
		  else{
			  throw new RuntimeException("Error obteniendo el tipo del identificador");
		  }
	  }

  }
}
/* JavaCC - OriginalChecksum=c1ec9cacf2c7dabf6bd1094bbaec4baf (do not edit this line) */
