/* Generated By:JJTree: Do not edit this line. Nodosentencia_if.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=Nodo,NODE_EXTENDS=XNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package javacc;

public
class Nodosentencia_if extends SimpleNode {
  public Nodosentencia_if(int id) {
    super(id);
  }

  public Nodosentencia_if(Compilador p, int id) {
    super(p, id);
  }
  
  @Override
  public void interpret(){
	  
	  int i, k = jjtGetNumChildren();

	     for (i = 0; i < k; i++)
	        jjtGetChild(i).interpret();
  }

}
/* JavaCC - OriginalChecksum=37e4e7aa1e782902f2498461d1839656 (do not edit this line) */