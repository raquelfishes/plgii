/* Generated By:JJTree: Do not edit this line. NodoNot.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=Nodo,NODE_EXTENDS=XNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package javacc;

public
class NodoNot extends SimpleNode {
  public NodoNot(int id) {
    super(id);
    this.value = ConstantesTipos.BOOLEAN;
  }

  public NodoNot(Compilador p, int id) {
    super(p, id);
  }
  
  public void interpret()
  {
     int i, k = jjtGetNumChildren();

     for (i = 0; i < k; i++)
        jjtGetChild(i).interpret();

  }
  
}
/* JavaCC - OriginalChecksum=838833b5f524669a742eb913eaddf7f8 (do not edit this line) */
