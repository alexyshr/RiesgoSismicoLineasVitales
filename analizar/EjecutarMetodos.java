package analizar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import conectar.InicioLicenciasVariables;

public class EjecutarMetodos {
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
								+"parametrosEjecutarMetodos.txt";
			System.out.println("___________________________");
			System.out.println("Archivo de Parámetros = ");
			System.out.println("  " + RutaArchivoParametros);
			System.out.println("___________________________");
			System.out.println("___________________________");
			
			BufferedReader bReader = new BufferedReader(new FileReader(RutaArchivoParametros));  	    
					
			//Loop through the text file that contains the parameters needed to execute
			//the analysis with the custom model
			int i = 0;
			CalcularNuevoCampo cnc = new CalcularNuevoCampo();
			
			for(String line = bReader.readLine(); line != null; line = bReader.readLine()){
				i++;
				if (line.startsWith("//") || line.startsWith(" *") || line.startsWith("/*")) {
					//System.out.println("La línea '" + i + "' es un comentario. No se tiene en cuenta!");
					continue;
				}
				System.out.println("***Inicia Ejecución (GP SIN MODELO) de la línea: '"+ i + "'");
				System.out.println("  " + line);					
				String [] tokens = line.split("; ");
				if (tokens.length != 11){
					System.out.println("  Error en la línea : '" + i + "'. Debería tener 11 parámetros y tiene '" + tokens.length + "'" );
					continue;
				}
				System.out.println("  Número de parámetros en la línea = '" + tokens.length + "'");				
				String rutaGDB = tokens[0];
				System.out.println("  1- rutaGDB = '" + rutaGDB + "'");				
				String tabla_o_FC = tokens[1];
				System.out.println("  2- Nombre nombre FClass o Tabla = '" + tabla_o_FC + "'");				
				String campoMMI = tokens[2];
				System.out.println("  3- Nombre del campo en el FClass o Tabla (existente o a crear!!) = '" + campoMMI + "'");				
				
				Integer tipoCampo = -1;
				if (!tokens[3].equals("")){
					tipoCampo = Integer.parseInt(tokens[3]);	
				}				
				System.out.println("  4- Tipo de campo a crear (si aplica). SmallInteger: 0, Integer: 1, Single: 2, Double: 3, String: 4, Date: 5 (esriFieldType) = '" + tipoCampo + "'");
				
				Integer precisionCampo = -1;
				if (!tokens[4].equals("")){
					precisionCampo = Integer.parseInt(tokens[4]);
				}					
				System.out.println("  5- Precisión del campo (cantidad máxima de digitos sin importar a que lado del punto están) = '" + precisionCampo + "'");
				
				Integer scaleCampo = -1;
				if (!tokens[5].equals("")){
					scaleCampo = Integer.parseInt(tokens[5]);
				}
				System.out.println("  6- Escala del campo (cantidad máxima de digitos decimales) = '" + scaleCampo + "'");
				
				Integer longitudCampo = -1;
				if (!tokens[6].equals("")){
					longitudCampo = Integer.parseInt(tokens[6]);
				}
				System.out.println("  7- Longitud del campo (cantidad máxima de digitos en campos tipo String) = '" + longitudCampo + "'");
				
				String codeBlock = tokens[7];
				codeBlock = codeBlock.replace("\\n", "\n");				
				System.out.println("  8- Bloque de código para el Cálculo del campo = \n'" + codeBlock + "'");				
				
				String expressionType = tokens[8];
				System.out.println("  9- Tipo de expresión (PYTHON, PYTHON_9.3) = '" + expressionType + "'");
				String ClausulaWhereFiltro = tokens[9];
				System.out.println("  10- Clausula Where para filtrar el Cálculo del campo = '" + ClausulaWhereFiltro + "'");
				String  expression = tokens[10];
				System.out.println("  11- Expresión para el Cálculo del campo = '" + expression + "'");				
				
				
				
//				System.out.println("  Nombre del campo existente o a crear en '" + tablaValoresUnicos + "': '" + campoTVU_Y + "'");
//				System.out.println("  Nota 1: Asegúrese que '" + rutaGDB + "' exista con las tablas correspondientes: 'PAV_PARAMETROS_VULN', 'DAV_DATOS_VULN', y la tabla de valores únicos '" + tablaValoresUnicos + "'");
//				System.out.println("  Nota 2: Asegúrese que la tabla 'PAV_PARAMETROS_VULN', tenga para el identificador de la curva a usar: PAV_ID = '" + idCurva + "', los valores de 'pav_formula', 'pav_minx', 'pav_maxx', 'pav_miny', 'pav_maxy'");
//				System.out.println("  Nota 3: Si el campo 'pav_formula' en 'PAV_PARAMETROS_VULN' no almacena una formula, se usarán los puntos de la curva en 'DAV_DATOS_VULN' para interpolar el daño. Verifique que los valores DAV_X, DAV_Y para la curva PAV_ID = '" + idCurva + "' estén almacenados en esta tabla");
//				System.out.println("  Nota 4: Los valores de 'pav_minx', 'pav_maxx', 'pav_miny', 'pav_maxy' son obligatorios en 'PAV_PARAMETROS_VULN' para la curva PAV_ID = '"+ idCurva+"'");
//				System.out.println("  Nota 5: Verifique correctamente los puntos de la curva almacenados en 'DAV_DATOS_VULN' para que cumplan con los valores mínimos ('pav_minx', 'pav_miny') y máximos ('pav_maxx', 'pav_maxy') y así poder interpolar");
//				System.out.println("  Nota 6: La tabla de valores únicos '" + tablaValoresUnicos + "' debe poseer el siguiente campo tipo FLOAT 'lleno' con valores de amenaza: '" + campoTVU_X +"'. Si en esta tabla el campo '" + campoTVU_Y+ "' existe se sobreescribirá, si no se creará");
//				System.out.println("  Nota 7: Importante VERIFIQUE que la curva de vulnerabilidd con PAV_ID = '" + idCurva + "', tenga las mismas unidades en X, que las unidades de la amenaza almacenada en el campo '" + campoTVU_X + "' de la tabla '" + tablaValoresUnicos + "'");
//				System.out.println("  Nota 8: Los valores de daño corresponden el eje Y de las curvas de vulnerabilidad almacenados, calculados por fórmula o interpolados por los puntos de la curva almacenados en 'DAV_DATOS_VULN'");
//				System.out.println("  Nota 9: Asegúrese que la base de datos no esté bloqueada por ArcGIS!");

				String resultado = cnc.Calcular(rutaGDB, tabla_o_FC, 
						campoMMI,  tipoCampo, precisionCampo, scaleCampo, longitudCampo,
						codeBlock, expression, expressionType, ClausulaWhereFiltro);
				System.out.println(resultado);
										
				System.out.println("  //////////El proceso ha terminado para la línea: '" + i + "'");
			}
			bReader.close(); //Alexys 2019
			iul.finalizarUsoLicencias();
		}	
		catch(Exception e){
			System.out.println("Error en la aplicación: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
