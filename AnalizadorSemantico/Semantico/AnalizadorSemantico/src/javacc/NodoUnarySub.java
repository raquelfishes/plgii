/* Generated By:JJTree: Do not edit this line. NodoUnarySub.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=Nodo,NODE_EXTENDS=XNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package javacc;

public
class NodoUnarySub extends SimpleNode {
  public NodoUnarySub(int id) {
    super(id);
    this.value = ConstantesTipos.INT;
  }

  public NodoUnarySub(Compilador p, int id) {
    super(p, id);
    this.value = ConstantesTipos.INT;
  }
  
  public void interpret()
  {
     int i, k = jjtGetNumChildren();

     for (i = 0; i < k; i++)
        jjtGetChild(i).interpret();

  }

}
/* JavaCC - OriginalChecksum=a00b3ab1f13f97af45ae7b091fb275e9 (do not edit this line) */
