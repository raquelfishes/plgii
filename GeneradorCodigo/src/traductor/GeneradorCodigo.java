package traductor;

import java.io.*;
import java.util.ArrayList;



public class GeneradorCodigo {
	
	public static void main(String [] args) {
		
		//Comprobar el número de argumentos de la llamada a main
		if (args.length < 2) {
			System.out.println("USO: GeneradorCodigo\tfichero_de_entrada\tfichero_de_salida");
			return;
		}
		
		ArrayList<String> lineasIn;
		ArrayList<String> lineasOut;
		
		lineasIn = new ArrayList<String>();
		
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;
		FileWriter fichero = null;
		PrintWriter pw = null;
		
		try {
			//LECTURA//////////////////////////////////////////////////////////
			System.out.println("Leyendo de "+args[0]+"...");
			archivo = new File (args[0]);
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);
			String linea;
			while((linea=br.readLine())!=null) {
				lineasIn.add(linea);
			}
			
			//TRADUCCIÓN///////////////////////////////////////////////////////
			Traductor traductor = new Traductor(lineasIn);
			System.out.println("Traduciendo...");
			traductor.traduce();
			lineasOut = traductor.getOutput();
			
			//ESCRITURA////////////////////////////////////////////////////////
			System.out.println("Escribiendo en "+args[1]+"...");
			try {
				fichero = new FileWriter(args[1]);
				pw = new PrintWriter(fichero);
				for (int i = 0; i < lineasOut.size(); i++) {
					pw.println(lineasOut.get(i));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != fichero)
						fichero.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			///////////////////////////////////////////////////////////////////
		}
		catch(Exception e){
			e.printStackTrace();
		}finally{
			try{                    
				if( null != fr ){   
					fr.close();     
				}                  
			}catch (Exception e2){ 
				e2.printStackTrace();
			}
		}		
	}
}

