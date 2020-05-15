package analizar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import conectar.InicioLicenciasVariables;

public class ConvertirCodigos_a_FactorCorreccion {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			InicioLicenciasVariables iul = new InicioLicenciasVariables();
			iul.initializeArcGISLicenses();
			String RiesgoSismicoLineasVitales = iul.iniciarRSLV();			
			//String RutaArchivoParametros = "D:"+File.separator+"geo"
			//					+File.separator+"archivos_configuracion_clases_java"
			//					+File.separator+"ParametrosRepairRateCurvasVulnerabilidad.txt";

			String RutaArchivoParametros = RiesgoSismicoLineasVitales + File.separator +
								"archivos_configuracion_clases_java"+File.separator
								+"parametrosConvertirCodigos_a_FactorCorreccion.txt";
			System.out.println("___________________________");
			System.out.println("Archivo de Par�metros = ");
			System.out.println("  " + RutaArchivoParametros);
			System.out.println("___________________________");
			System.out.println("___________________________");
			
			BufferedReader bReader = new BufferedReader(new FileReader(RutaArchivoParametros));  	    
					
			//Loop through the text file that contains the parameters needed to execute
			//the analysis with the custom model
			int i = 0;
			ConvertirCodigo_a_FactorCorreccion ccfc = new ConvertirCodigo_a_FactorCorreccion();
			
			for(String line = bReader.readLine(); line != null; line = bReader.readLine()){
				i++;
				if (line.startsWith("//") || line.startsWith(" *") || line.startsWith("/*")) {
					//System.out.println("La l�nea '" + i + "' es un comentario. No se tiene en cuenta!");
					continue;
				}
				System.out.println("***Inicia Ejecuci�n (NO GP) de la l�nea: '"+ i + "'");
				System.out.println("  " + line);					
				String [] tokens = line.split("; ");
				if (tokens.length != 6){
					System.out.println("  Error en la l�nea : '" + i + "'. Deber�a tener 6 par�metros y tiene '" + tokens.length + "'" );
					continue;
				}
				String rutaGDB = tokens[0];
				System.out.println("  1- rutaGDB = '" + rutaGDB + "'");				
				String nombreFC_o_T = tokens[1];
				System.out.println("  2- Nombre nombre FClass o Tabla = '" + nombreFC_o_T + "'");
				String CampoCodigoFC_o_T = tokens[2];
				System.out.println("  3- Nombre del campo en el FClass o Tabla (debe existir!!, tipo SHORT) = '" + CampoCodigoFC_o_T + "'");
				String CampoFactorCorreccion = tokens[3];
				System.out.println("  4- Nombre del campo en el FClass o Tabla (existente o a crear!!, tipo FLOAT) = '" + CampoFactorCorreccion + "'");				
				int codigoTablaFactorCorreccion = Integer.parseInt(tokens[4]);
				System.out.println("  5- C�digo entero que identifica el factor de correcci�n en la tabla 'factores_correccion_tuberias', fct_id = '" + codigoTablaFactorCorreccion + "'");				
				int valorCodigoFactorCorrecionCastigo = Integer.parseInt(tokens[5]);
				System.out.println("  6- C�digo entero de castigo para l�neas que no lo tengan almacenado en ('" + CampoCodigoFC_o_T + "'). Tabla 'factores_correccion_valor', fcv_codigo = '" + valorCodigoFactorCorrecionCastigo + "'");
				
//				System.out.println("  Nombre del campo existente o a crear en '" + tablaValoresUnicos + "': '" + campoTVU_Y + "'");
//				System.out.println("  Nota 1: Aseg�rese que '" + rutaGDB + "' exista con las tablas correspondientes: 'PAV_PARAMETROS_VULN', 'DAV_DATOS_VULN', y la tabla de valores �nicos '" + tablaValoresUnicos + "'");
//				System.out.println("  Nota 2: Aseg�rese que la tabla 'PAV_PARAMETROS_VULN', tenga para el identificador de la curva a usar: PAV_ID = '" + idCurva + "', los valores de 'pav_formula', 'pav_minx', 'pav_maxx', 'pav_miny', 'pav_maxy'");
//				System.out.println("  Nota 3: Si el campo 'pav_formula' en 'PAV_PARAMETROS_VULN' no almacena una formula, se usar�n los puntos de la curva en 'DAV_DATOS_VULN' para interpolar el da�o. Verifique que los valores DAV_X, DAV_Y para la curva PAV_ID = '" + idCurva + "' est�n almacenados en esta tabla");
//				System.out.println("  Nota 4: Los valores de 'pav_minx', 'pav_maxx', 'pav_miny', 'pav_maxy' son obligatorios en 'PAV_PARAMETROS_VULN' para la curva PAV_ID = '"+ idCurva+"'");
//				System.out.println("  Nota 5: Verifique correctamente los puntos de la curva almacenados en 'DAV_DATOS_VULN' para que cumplan con los valores m�nimos ('pav_minx', 'pav_miny') y m�ximos ('pav_maxx', 'pav_maxy') y as� poder interpolar");
//				System.out.println("  Nota 6: La tabla de valores �nicos '" + tablaValoresUnicos + "' debe poseer el siguiente campo tipo FLOAT 'lleno' con valores de amenaza: '" + campoTVU_X +"'. Si en esta tabla el campo '" + campoTVU_Y+ "' existe se sobreescribir�, si no se crear�");
//				System.out.println("  Nota 7: Importante VERIFIQUE que la curva de vulnerabilidd con PAV_ID = '" + idCurva + "', tenga las mismas unidades en X, que las unidades de la amenaza almacenada en el campo '" + campoTVU_X + "' de la tabla '" + tablaValoresUnicos + "'");
//				System.out.println("  Nota 8: Los valores de da�o corresponden el eje Y de las curvas de vulnerabilidad almacenados, calculados por f�rmula o interpolados por los puntos de la curva almacenados en 'DAV_DATOS_VULN'");
//				System.out.println("  Nota 9: Aseg�rese que la base de datos no est� bloqueada por ArcGIS!");

				ccfc.PasarAtributo(rutaGDB, nombreFC_o_T, CampoCodigoFC_o_T, 
						CampoFactorCorreccion, codigoTablaFactorCorreccion, valorCodigoFactorCorrecionCastigo);
				
				System.out.println("  //////////El proceso ha terminado para la l�nea: '" + i + "'");
			}
			bReader.close(); //Alexys 2019
			iul.finalizarUsoLicencias();
		}	
		catch(Exception e){
			System.out.println("Exception caught: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
