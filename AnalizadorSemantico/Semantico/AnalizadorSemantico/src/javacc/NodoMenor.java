/* Generated By:JJTree: Do not edit this line. NodoMenor.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=Nodo,NODE_EXTENDS=XNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package javacc;

public
class NodoMenor extends SimpleNode {
  public NodoMenor(int id) {
    super(id);
    this.value = ConstantesTipos.BOOLEAN;
  }

  public NodoMenor(Compilador p, int id) {
    super(p, id);
    this.value = ConstantesTipos.BOOLEAN;
  }
  
  @Override
  public void interpret(){
	  verificarComparacionValida();
  }

}
/* JavaCC - OriginalChecksum=e5dff7b91bd162109823f2a8b5e5a8ea (do not edit this line) */