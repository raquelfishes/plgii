package tabla.simbolos.v2;

public class PalabrasReservadas {
	private final static String[] palRes = { "abstract","boolean","break","byte","case","catch","char","class","const","continue",
			"default","do","double","else","enum","extends","final","finally","float","for",
			"goto","if","implements","import","instanceof","int","interface","long","native",
			"new","package","protected","public","return","short","static","super","switch","synchronized",
			"this","throw","throws","transient","try","void","volatile","while"};
	
	public static boolean esReservada(String s){
		boolean b= false;
		for (int i=0; i<=palRes.length && !b; i++) {
			b = s.equals(palRes[i]);
		}
		return b;
	}

};
