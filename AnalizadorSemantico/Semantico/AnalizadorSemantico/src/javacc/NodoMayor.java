/* Generated By:JJTree: Do not edit this line. NodoMayor.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=Nodo,NODE_EXTENDS=XNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package javacc;

public
class NodoMayor extends SimpleNode {
  public NodoMayor(int id) {
    super(id);
    this.value = ConstantesTipos.BOOLEAN;
  }

  public NodoMayor(Compilador p, int id) {
    super(p, id);
    this.value = ConstantesTipos.BOOLEAN;
  }

  public void interpret()
  {      
	 verificarComparacionValida();
  }
}
/* JavaCC - OriginalChecksum=ac74468899c7b137d7d286dabda3019b (do not edit this line) */
