package traductor;

import java.util.ArrayList;

public class Traductor {

	private ArrayList<String> input;
	private ArrayList<String> output;
	
	public Traductor(ArrayList<String> lineas){
		input = lineas;
		output = new ArrayList<String>(); 
	}
	
	public ArrayList<String> getOutput() {
		return output;
	}
	
	public void traduce() {
		for (int i=0; i<input.size(); i++) {
			//TODO: cambiar
			//Aqu� es donde empezaremos a partir cada l�nea con un tokenizer,
			//para descubrir qu� instrucci�n es, y llamaremos a un m�todo que traduzca ese tipo.
			//Una vez dentro de ese m�todo, se tomar�n decisiones m�s concretas, para a�adir a output
			//las l�neas que correspondan como fruto de la traducci�n.
			output.add(input.get(i));
		}
	}
	
}
