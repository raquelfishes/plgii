/* Generated By:JJTree: Do not edit this line. Nodotipo_metodo.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=Nodo,NODE_EXTENDS=XNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package javacc;

public
class Nodotipo_metodo extends SimpleNode {
  public Nodotipo_metodo(int id) {
    super(id);
  }

  public Nodotipo_metodo(Compilador p, int id) {
    super(p, id);
  }

  @Override
  public void interpret(){
	  
	  if(this.parent!=null && this.parent instanceof SimpleNode)
		  ((SimpleNode)this.parent).setValor(this.value);
	  
	  
	  int i, k = jjtGetNumChildren();

	     for (i = 0; i < k; i++)
	        jjtGetChild(i).interpret();
	  
  }
}
/* JavaCC - OriginalChecksum=cb4975a9c5a3174504d881246903da0c (do not edit this line) */
