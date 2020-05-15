package analizar;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.File;
import java.io.FileReader;
import analizar.modelos_rslv.UnirTablaValoresUnicosaFC;
import com.esri.arcgis.geoprocessing.GeoProcessor;
import conectar.InicioLicenciasVariables;
/**************************************************************************
 * <pre>
 * -----------------
 * <i><b>Orden de ejecución de las clases para calcular daños en líneas vitales:</b></i>
 * -----------------
 *  1- PasarAmenazaRaster_a_FClass.java
 *            Archivo de configuración: parametrosPasarAmenazaALineas.txt
 *            Clases utilizadas internamente: PasarAmenazaALineas.java y PasarAmenazaALineas2.java
 *  2- CrearTablaValoresUnicosCampo.java
 *            Archivo de configuración: parametros_ValoresUnicosCampo.txt
 *            Clases utilizadas internamente: ValoresUnicosCampo.java
 *  3- CalcularDanio.java
 *            Archivo de configuración: parametrosRepairRateCurvasVulnerabilidad.txt
 *            Clases utilizadas internamente: UsarCurvaVulnerabilidad.java, Curva.java, Interpolador.java
 *  4- UnirTVU_ConDanio_a_FC.java
 *            Archivo de configuración: parametros_UnirTablaValoresUnicosaFC.txt
 *            Clases utilizadas internamente: UnirTablaValoresUnicosaFC.java
 * -----------------
 * <i><b>Uso de la clase 'UnirTVU_ConDanio_a_FC.java':</b></i>
 * -----------------
 * Desde el código fuente:         
 *           <b>import analizar.UnirTVU_ConDanio_a_FC;</b>
 *           <b>UnirTVU_ConDanio_a_FC.main(null);</b>
 * Desde línea de comandos (uso de binarios):
 *      Usando al archivo binario UnirTVU_ConDanio_a_FC.class: 
 *           <b>java analizar.UnirTVU_ConDanio_a_FC</b>
 *      Apuntando el archivo rslv.jar:
 *           <b>java -cp ./rslv.jar analizar.UnirTVU_ConDanio_a_FC</b>
 * -----------------
 * <b><i>Descripción de la clase 'UnirTVU_ConDanio_a_FC.java':</b></i>
 * -----------------
 *  A partir de un modelo de geoprocesamiento construido con Model Builder, se une (Join) temporalmente una tabla de valores únicos de amenaza que contiene 
 *  los datos de daño, a un Feature Class de líneas vitales para calcular en este último los valores de daño provenientes de esta tabla. 
 *  La tabla y el Feature Class deben tener un campo común con el mismo nombre, para hacer el 'Join'.
 *  El modelo crea un índice en el campo llave para el Join, residente en el Feature Class.
 *  La tabla de valores únicos de amenaza debe tener los valores de daño almacenados en un campo. Esta tabla puede crearse previamente con la clase 'CrearTablaValoresUnicosCampo.java'
 *  El campo que contiene los datos de daño en la tabla, servirá para crear uno idéntico en el Feature Class y ahí se calcularán los datos de daño.
 *  Utiliza el modelo de geoprocesamiento llamado 'UnirTablaValoresUnicosaFC' y lee los parámetros del modelo de una línea del archivo 'parametros_UnirTablaValoresUnicosaFC.txt'
 * -----------------
 * <b><i>Notas para un correcto funcionamiento:</b></i>
 * -----------------
 *  Nota 1: La variable de sistema 'RiesgoSismicoLineasVitales' debe definir la ruta a la carpeta que contiene el aplicativo. Ejemplo:
 *            RiesgoSismicoLineasVitales = D:\geo\
 *  Nota 2: El Toolbox con el modelo debe estar en %RiesgoSismicoLineasVitales%\modelos_geoprocesamieto\Modelos_RSLV.tbx
 *  Nota 3: El modelo de geoprocesamiento usado por esta clase es:
 *            - UnirTablaValoresUnicosaFC: 
 *              Une la tabla (parámetro 3) al Feature Class (parámetro 1) a partir de campos comunes (parámetro 2). 
 *              Crea un índice en un campo (parámetro 2) del Feature Class, para que la unión ('Join') sea más rápida
 *              Calcula un campo nuevo (parámetro 4) en el Feature Class para almacenar los datos de daño provenientes de un campo de la tabla (parámetro 4). 
 *              El campo en la tabla con los datos de daño y campo nuevo en el FC tendrán el mismo nombre (parámetro 4)
 *              Usa internamente la clase 'UnirTablaValoresUnicosaFC.java'
 *  Nota 4: El archivo de texto de parámetros debe estar en %RiesgoSismicoLineasVitales%\modelos_geoprocesamieto\parametros_UnirTablaValoresUnicosaFC.txt
 *            Toda línea en ese archivo de texto que inicie por (quitando las comillas) ' *', '/*' o '//' es un comentario
 *            No puede existir una línea vacía al final del archivo de texto
 *            Todos los parámetros de la ejecución de los modelos de geoprocesamiento van en una sola línea
 *            Coloque tantas líneas como corridas o ejecuciones del modelo desee (la clase itera leyendo los parámetros de cada línea y ejecutando el modelo por cada línea)
 *            Cada parámetro en la línea va separado por punto y coma y un espacio. Ejemplo: '; '
 *            Espacios adicionales o sobrantes entre parámetros causan que el modelo falle.
 *          Los parámetros en cada línea del archivo de parámetros son los siguientes:
 *            1-fClass: Ruta al Feature Class que se desea unir los valores de amenaza.
 *            2-campoFCJoin, campoIndexFC y campoTablaUnirJoin: Nombre del campo residente en el Feature Class y en la tabla de amenaza con el cuál se hará el 'Join'. En este campo existente para el FC se hará un índice.
 *            3-tablaUnir: Ruta a la tabla con los valores únicos de amenaza. Debe tener el campo común con el FC para hacer el 'Join' y otro campo con los valores de amenaza.
 *            4-CampoDaniosTablaUnir (campo nuevo en el FC): Nombre del campo en la tabla de valores únicos que contiene los valores de daño. Con este nombre de campo se creará uno idéntico en el FC
 *  Nota 5: El campo 'CampoDaniosTablaUnir' (parámetro 4) no debe existir en el 'fClass' (parámetro 1)
 *  Nota 6: El índice en el campo 'campoIndexFC' (parámetro 2) del 'fClass' (parámetro 1) no debe existir!
 *  Nota 7: El campo 'CampoDaniosTablaUnir' (parámetro 4) debe existir con datos de daño en la 'tablaUnir' (parámetro 3)
 *  Nota 8: El 'fClass' (parámetro 1) y la 'tablaUnir' (parámetro 3) pueden existir en diferentes bases de datos
 *  Nota 9: Asegúrese que no haya bloqueo en las bases de datos donde reside la Tabla y el Feature Class (Cierre ArcGIS)!
 * </pre>
 *  @version 1.0
 *  @author Msc. Esp. Ing. Alexys H Rodríguez Avellaneda, <a href="mailto:alexyshr@gmail.com">alexyshr@gmail.com</a> - Nov 2009 - Maestría en Geomática (Universidad Nacional de Colombia)
 ***************************************************************************/
public class UnirTVU_ConDanio_a_FC {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			InicioLicenciasVariables iul = new InicioLicenciasVariables();
			iul.initializeArcGISLicenses();
			String RiesgoSismicoLineasVitales = iul.iniciarRSLV();						
			//String ubicacionTool = "D:/geo/modelos_geoprocesamieto/Modelos_RSLV.tbx";		    
			//String ubicacionTool = "D:"+File.separator+"geo"+File.separator+"" +
			//						"modelos_geoprocesamieto"+File.separator+"Modelos_RSLV.tbx";			
			//String RutaArchivoParametros = "D:"+File.separator+"geo"+File.separator+
			//"modelos_geoprocesamieto"+File.separator+"parametros_UnirTablaValoresUnicosaFC.txt";
			String ubicacionTool = RiesgoSismicoLineasVitales + File.separator + 
			"modelos_geoprocesamieto"+File.separator+"Modelos_RSLV.tbx";
			
			System.out.println("___________________________");
			System.out.println("Archivo del ToolBox = ");
			System.out.println("  " + ubicacionTool);
			System.out.println("___________________________");
			System.out.println("Nombre del Modelo = ");
			System.out.println("  UnirTablaValoresUnicosaFC");
			System.out.println("___________________________");
			String RutaArchivoParametros = RiesgoSismicoLineasVitales + File.separator +
						"modelos_geoprocesamieto"+File.separator+"parametrosUnirTVU_ConDanio_a_FC.txt";
			System.out.println("Archivo de Parámetros = ");
			System.out.println("  " + RutaArchivoParametros);
			System.out.println("___________________________");
			System.out.println("___________________________");
						
			Reader reader = new FileReader(RutaArchivoParametros);
			
			try(BufferedReader bReader = new BufferedReader(reader)){
				//Loop through the text file that contains the parameters needed to execute
				//the analysis with the custom model
				int i = 0;
				for(String line = bReader.readLine(); line != null; line = bReader.readLine()){
					i++;
					if (line.startsWith("//") || line.startsWith(" *") || line.startsWith("/*")) {
						//System.out.println("La línea '" + i + "' es un comentario. No se tiene en cuenta!");
						continue;
					}
					System.out.println("***Inicia Ejecución (GP) de la línea: '"+ i + "'");
					System.out.println("  " + line);
					//VarArray parametros = new VarArray();
					//String campoEstadisticas = "pvmaxs_250 COUNT";
					//parametros.add(campoEstadisticas);
					//String rutaFC = "D:/geo/geo.gdb/datos_geograficos/prueba";		    
					//String rutaFC = "D:"+File.separator+"geo"+File.separator+"geo.gdb"+File.separator+
					//				"datos_geograficos"+File.separator+"prueba";
					//String rutaFC = "D:"+File.separator+"geo"+File.separator+
					//				"borrar.mdb"+File.separator+"prueba";
					
					//System.out.println("rutaFC = " + rutaFC);
					//parametros.add(rutaFC);
				    //String nuevaTabla = "D:/geo/geo.gdb/prueba_pvmaxs_250";
					//String nuevaTabla = "D:"+File.separator+"geo"+File.separator+"geo.gdb"+File.separator+
					//					"prueba_pvmaxs_250";
					//String nuevaTabla = "D:"+File.separator+"geo"+File.separator+"borrar.mdb"+File.separator+
					//					"prueba_pvmaxs_250";
					
				    //parametros.add(nuevaTabla);
				    //String campoAdicionalValoresYCurva = "pvmaxs_250_cv28";		    
				    //parametros.add(campoAdicionalValoresYCurva);
			    					   
					//Tokenize the line of text based on a comma seperator
					String [] tokens = line.split("; ");
					if (tokens.length != 3){
						System.out.println("  Error en la línea : '" + i + "'. Debería tener 3 parámetros y tiene '" + tokens.length + "'" );
						continue;
					}
					String fClass= tokens[0];
					System.out.println("  1- fClass = '" + fClass + "'");
					String campoFCJoin= tokens[1];
					System.out.println("  2- campoFCJoin, campoTablaUnirJoin = '" + campoFCJoin + "'");
					String tablaUnir = "";
					if (fClass.contains(".gdb")){
						tablaUnir = fClass.substring(0,fClass.lastIndexOf(".gdb")+5)
						+ fClass.substring(fClass.lastIndexOf("/")+1) + "_" + campoFCJoin;
					}else if (fClass.contains(".mdb")){
						tablaUnir = fClass.substring(0,fClass.lastIndexOf(".mdb")+5)
						+ fClass.substring(fClass.lastIndexOf("/")+1) + "_" + campoFCJoin;
				    }else{
				    	throw new IllegalStateException("Este proceso solo funciona con datos en Personal o File Geodatabase!!");
				    }			    					 		
					System.out.println("  La tabla '" + tablaUnir + "' se unirá (JOIN) al Feature Class: '" + fClass + "'");				
					//System.out.println("  3- tablaUnir = '" + tablaUnir + "'");
					String campoNuevoFC = tokens[2];
					System.out.println("  3- CampoDaniosTablaUnir (campo a crear en el FC) = '" + campoNuevoFC + "'"); 				
					String campoNuevoFC2 = fClass.substring(fClass.lastIndexOf("/")+1) + "." + campoNuevoFC;
					System.out.println("  *** Campo nuevo en el Feature Class (campoNuevoFC2) para almacenar datos de daños, formateado para el Calculate = '" + campoNuevoFC2 + "'");						
					String expresionCalculoFC = "[" + tablaUnir.substring(tablaUnir.lastIndexOf("/")+1) + "." + campoNuevoFC + "]";
					System.out.println("  *** Expresión de Calculo en el FC = '" + expresionCalculoFC + "'");
									
					System.out.println("  Nota 1: El campo '" + campoFCJoin + "' debe existir en el Feature Class '" + fClass + "' y en la tabla '" + tablaUnir + "'");
					System.out.println("  Nota 1: El campo '" + campoNuevoFC + "' no debe existir en el Feature Class '" + fClass + "'");
					System.out.println("  Nota 2: El campo '" + campoNuevoFC + "' debe existir con datos de daño en la tabla '" + tablaUnir + "'");
					System.out.println("  Nota 3: Es recomendable que cree primero (antes de correr este proceso) un índice en el campo '" + campoFCJoin + "' de '" + fClass);
					System.out.println("  Nota 4: Asegúrese que no haya bloqueo en las bases de datos donde reside la Tabla y el Feature Class (cierre ArcGIS)!");
				    GeoProcessor gp = new GeoProcessor();
				    gp.setOverwriteOutput(true);
					gp.addToolbox(ubicacionTool);
					UnirTablaValoresUnicosaFC tool = new UnirTablaValoresUnicosaFC();
					
					tool.setFClass(fClass);
					tool.setCampoNuevoFC(campoNuevoFC);
					tool.setCampoFCJoin(campoFCJoin);
					tool.setTablaUnir(tablaUnir);
					tool.setCampoTablaUnirJoin(campoFCJoin);
					tool.setCampoNuevoFC2(campoNuevoFC2);
					tool.setExpresionCalculoFC(expresionCalculoFC);

					ValidarGP_MensajesGP validar = new ValidarGP_MensajesGP();
					if (validar.Validar(gp, tool) == true){
						System.out.println("  **** Error: El registro actual no se procesa por tener los errores descritos con anterioridad!");
						continue;
					}
					
					try {
						gp.execute(tool, null);				
						validar.returnMessagesGP(gp, tool.getToolName());
					} catch (Exception e){
						System.out.println("Mensaje de error: " + e.getMessage());
						e.printStackTrace();								
						validar.returnMessagesGP(gp, tool.getToolName());
					}
									
				    //tool = new valoresUnicosCampo(campoEstadisticas, rutaFC, nuevaTabla, campoAdicionalValoresYCurva);
					//tool = new valoresUnicosCampo();
		
				    //gp.execute(tool, new TrackCancel());
				    //gp.execute(tool, null);
					System.out.println("  //////////El proceso se ha corrido para la línea: '" + i + "'");
				}				
			}
			iul.finalizarUsoLicencias();
		}	
		catch(Exception e){
			System.out.println("Mensaje de error: " + e.getMessage());
			e.printStackTrace();
		}					
	}
}


