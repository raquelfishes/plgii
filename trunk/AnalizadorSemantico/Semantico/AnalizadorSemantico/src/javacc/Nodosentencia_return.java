/* Generated By:JJTree: Do not edit this line. Nodosentencia_return.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=Nodo,NODE_EXTENDS=XNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package javacc;

public
class Nodosentencia_return extends SimpleNode {
  public Nodosentencia_return(int id) {
    super(id);
  }

  public Nodosentencia_return(Compilador p, int id) {
    super(p, id);
  }
  
  public void interpret()
  {
	 
//    int i, k = jjtGetNumChildren();
//
//       for (i = 0; i < k; i++)
//          jjtGetChild(i).interpret();
      
      //Puede tener uno o ning�n hijo
      int numHijos = jjtGetNumChildren();
      
      if(numHijos==0){
              //Estoy 99% seguro de que no deberia comprobar nada
      }
      else{//numHijos==1
              
              SimpleNode padreNodoDeclaracionMetodo=null;
              if(this.parent!=null && this.parent instanceof SimpleNode)
                      padreNodoDeclaracionMetodo = (SimpleNode)this.parent;
              
              jjtGetChild(0).interpret();
              
              String tipoMetodo = padreNodoDeclaracionMetodo.value.toString();
              boolean compatibilidad = ConstantesTipos.esCompatible(tipoMetodo, ((SimpleNode)jjtGetChild(0)).value.toString());
              if(!compatibilidad){
                      addErrSemantico(firstToken.beginLine, "el tipo devuelto no es de tipo "+tipoMetodo);
              }
      }
  }

}
/* JavaCC - OriginalChecksum=38cbab4f3b995f337c6ccbf7e6b06854 (do not edit this line) */
