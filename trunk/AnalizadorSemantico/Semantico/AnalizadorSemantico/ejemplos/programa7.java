public class Prueba {
	int i;
	Persona paco;
	boolean isEnable;
		
	void main(){
		//Gato unArray[] = new Gato[5];
		int k;
		int p;
		k = 33;
		p = 55;
		i = 2+3;
		isEnable = true;
		boolean b = false && isEnable;
		metodo1(k,p);
		metodo1(5,5);
		
		if (i==5 || i>10){
			for (int j=0; j<i; j++){
				int aux = 20;
				int aux1 = 25;
				k += aux + j;
				k -= aux1 - j;
			}
		}
	}
	
	void metodo1(int a, int b){
		if (a<20)
			i = b;
		else
			i = a;
	}
	
}
