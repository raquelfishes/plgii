/* Generated By:JJTree: Do not edit this line. NodoNIgual.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=Nodo,NODE_EXTENDS=XNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package javacc;

public
class NodoNIgual extends SimpleNode {
  public NodoNIgual(int id) {
    super(id);
    this.value = ConstantesTipos.BOOLEAN;
  }

  public NodoNIgual(Compilador p, int id) {
    super(p, id);
    this.value = ConstantesTipos.BOOLEAN;
  }
  
  
  public void interpret()
  {      
	  
	  verificarComparacionValida();
	
  }

}
/* JavaCC - OriginalChecksum=c7b5acda0e0ecc98ad730d3e34a95f48 (do not edit this line) */
