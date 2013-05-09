package javacc;

public class ConstantesTipos {
	
	public static final String INT = "int";
	public static final String INTEGER = "Integer";
	public static final String SHORT = "short";
	public static final String LONG = "long";
	public static final String FLOAT = "float";
	public static final String DOUBLE = "double";
	public static final String CHAR = "char";
	public static final String STRING = "String";
	public static final String BOOLEAN = "boolean";
	public static final String NULL = "null";
	
	
	public static boolean esCompatible(String t1, String t2){
		
		if (t1.equals(t2))
			return true;
		
		if(	(t1.equals(INT) || t1.equals(INTEGER) || t1.equals(SHORT) || t1.equals(LONG) || t1.equals(FLOAT) || t1.equals(DOUBLE)) &&
			(t2.equals(INT) || t2.equals(INTEGER) || t2.equals(SHORT) || t2.equals(LONG) || t2.equals(FLOAT) || t2.equals(DOUBLE))){
			return true;
		}
		
		if(	(t1.equals(STRING)) &&
			(t2.equals(STRING) || t2.equals(CHAR))){
				return true;
		}
		
		if(	t1.equals(BOOLEAN) && t2.equals(BOOLEAN)){
				return true;
		}
		
		return false;
		
	}

}
