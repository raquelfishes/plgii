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
			//Aquí es donde empezaremos a partir cada línea con un tokenizer,
			//para descubrir qué instrucción es, y llamaremos a un método que traduzca ese tipo.
			//Una vez dentro de ese método, se tomarán decisiones más concretas, para añadir a output
			//las líneas que correspondan como fruto de la traducción.
			output.add(input.get(i));
		}
	}
	
}
