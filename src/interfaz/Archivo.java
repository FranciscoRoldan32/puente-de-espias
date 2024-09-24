package interfaz;
import java.io.BufferedReader;
	import java.io.FileReader;
	import java.io.IOException;
public class Archivo {
	
	public String [] leerArchivo(String filePath){
		String[] nombres = null;
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
		            String linea;
		            if ((linea = br.readLine()) != null) {
		                nombres = linea.split(",");
		            }
		 } catch (IOException e) {
		            e.printStackTrace();
		 }
		 return nombres;}
	    
	
}

