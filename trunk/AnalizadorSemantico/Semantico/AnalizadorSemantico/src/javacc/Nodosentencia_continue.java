/* Generated By:JJTree: Do not edit this line. Nodosentencia_continue.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=Nodo,NODE_EXTENDS=XNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package javacc;

public
class Nodosentencia_continue extends SimpleNode {
  public Nodosentencia_continue(int id) {
    super(id);
  }

  public Nodosentencia_continue(Compilador p, int id) {
    super(p, id);
  }

  public void interpret()
  {
     int i, k = jjtGetNumChildren();

     for (i = 0; i < k; i++)
        jjtGetChild(i).interpret();

  }
}
/* JavaCC - OriginalChecksum=215d4a27d228629216e632ae9f0aa420 (do not edit this line) */
