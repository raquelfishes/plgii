/* Generated By:JJTree: Do not edit this line. NodoSubSub.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=Nodo,NODE_EXTENDS=XNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package javacc;

public
class NodoSubSub extends SimpleNode {
  public NodoSubSub(int id) {
    super(id);
    this.value = ConstantesTipos.INT;
  }

  public NodoSubSub(Compilador p, int id) {
    super(p, id);
  }

  public void interpret()
  {
     int i, k = jjtGetNumChildren();

     for (i = 0; i < k; i++)
        jjtGetChild(i).interpret();

  }
}
/* JavaCC - OriginalChecksum=54e50a0fbb9d7aae25048706a30af7b1 (do not edit this line) */
