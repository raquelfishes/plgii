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
	  SimpleNode nTipo1 = null, nTipo2 = null, nIdent = null, nLitInt = null;
	  int tArray;
	  if (parent instanceof Nodocampo_declaracion || parent instanceof Nododeclaracion_variable_local){
		  try{
		  nTipo1 = (Nodotipo)((SimpleNode)parent).children[0];
		  nIdent = (Nododeclarador_variable_id)((SimpleNode)parent).children[1];
		  nTipo2 = (SimpleNode) children[0];
		  nLitInt = (NodoLiteralInteger) children[1];
		  }
		  catch (ClassCastException e){
			  addErrSemantico(((SimpleNode)parent).firstToken.beginLine, "error de tipos");
		  }
		  
		  if(nTipo1 != null && nTipo2 != null && nIdent != null && nLitInt != null){
			  
			  // comprobar tipos
			  if (ConstantesTipos.esCompatible((String)nTipo1.value, (String)nTipo2.value)){
				  // tomamos el tama�o del array
				  tArray = Integer.parseInt(nLitInt.firstToken.image);
				  if (tArray < 0){
					  addErrSemantico(firstToken.beginLine, "el tama�o del array no puede ser negativo");
				  }
				  else{
					  Atributos atribs_nuevos = new Atributos((String)nIdent.value, (String)nTipo1.value, tArray);
					  Atributos atribs_viejos = Compilador.gestorTS.getAtributos((String)nIdent.value);
					  if (atribs_viejos != null){
						  atribs_viejos.setAtributos(atribs_nuevos);
					  }
					  else {
						  addErrSemantico(firstToken.beginLine, "error inesperado al actualizar identificador tipo array");
					  }
				  }
			  }
			  else{
				  addErrSemantico(firstToken.beginLine, "el tipo del array difiere del identificador asignado a el");
			  }
		  }
		  else{
			  addErrSemantico(((SimpleNode)parent).firstToken.beginLine, "error de tipos");
		  }
	  }
	  else {
		  addErrSemantico(firstToken.beginLine, "hubo un error inesperado en la declaraci�n del array");
	  }

  }
}
/* JavaCC - OriginalChecksum=c1ec9cacf2c7dabf6bd1094bbaec4baf (do not edit this line) */
