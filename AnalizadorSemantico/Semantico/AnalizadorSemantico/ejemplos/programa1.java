public class Prueba {
	
	int a = 1;
	int b = 2;
	int c = 3;
	int d = 4;
	int e = 5;
	
	void main(){
		//int a = 0;
		int[] array = new int[10];
		
	}
	
	void llamada1(int x){
		//x = x+b; //FIXME Parece ser que la TS no encuentra las variables del método, como esta "int x"
		int w = 0; //FIXME o esta w;
	}
	
	void llamada2(){
		//x = x+b; //FIXME Parece ser que la TS no encuentra las variables del método, como esta "int x"
		int w = 0; //FIXME o esta w;
	}
	
	/*
	int a = 1;
	int b = 2;
	int c = 3;
	int d = 4;
	int e = 5;
	
	void main(){
		//int a = 0;
		int[] array = new int[10];
		int f, g, h, i, j;
		f = a;
		g = b;
		h = c;
		i = d;
		j = e;
		a = 6;
		b = 7;
		c = 8;
		d = 9;
		e = 10;
		//llamada(i, j, a);
		//f = array[3];
	}
	
	void llamada(int x){
		//x = x+b; //FIXME Parece ser que la TS no encuentra las variables del método, como esta "int x"
		int w = 0; //FIXME o esta w;
	}
	*/
}
