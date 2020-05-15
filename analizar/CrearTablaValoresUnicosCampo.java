package analizar;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import analizar.modelos_rslv.ValoresUnicosCampo;
import com.esri.arcgis.geoprocessing.GeoProcessor;
import conectar.InicioLicenciasVariables;
/**************************************************************************
 * <pre>
 * -----------------
 * <i><b>Orden de ejecuci�n de las clases para calcular da�os en l�neas vitales:</b></i>
 * -----------------
 *  1- PasarAmenazaRaster_a_FClass.java
 *            Archivo de configuraci�n: parametrosPasarAmenazaALineas.txt
 *            Clases utilizadas internamente: PasarAmenazaALineas.java y PasarAmenazaALineas2.java
 *  2- CrearTablaValoresUnicosCampo.java
 *            Archivo de configuraci�n: parametros_ValoresUnicosCampo.txt
 *            Clases utilizadas internamente: ValoresUnicosCampo.java
 *  3- CalcularDanio.java
 *            Archivo de configuraci�n: parametrosRepairRateCurvasVulnerabilidad.txt
 *            Clases utilizadas internamente: UsarCurvaVulnerabilidad.java, Curva.java, Interpolador.java
 *  4- UnirTVU_ConDanio_a_FC.java
 *            Archivo de configuraci�n: parametros_UnirTablaValoresUnicosaFC.txt
 *            Clases utilizadas internamente: UnirTablaValoresUnicosaFC.java
 * -----------------
 * <i><b>Uso de la clase 'CrearTablaValoresUnicosCampo.java':</b></i>
 * -----------------
 * Desde el c�digo fuente:         
 *           <b>import analizar.CrearTablaValoresUnicosCampo;</b>
 *           <b>CrearTablaValoresUnicosCampo.main(null);</b>
 * Desde l�nea de comandos (uso de binarios):
 *      Usando al archivo binario CrearTablaValoresUnicosCampo.class: 
 *           <b>java analizar.CrearTablaValoresUnicosCampo</b>
 *      Apuntando el archivo rslv.jar:
 *           <b>java -cp ./rslv.jar analizar.CrearTablaValoresUnicosCampo</b>
 * -----------------
 * <b><i>Descripci�n de la clase 'CrearTablaValoresUnicosCampo.java':</b></i>
 * -----------------
 *  A partir de un modelo de geoprocesamiento construido con Model Builder, 
 *  pasa los valores de amenaza almacenados en un campo de las l�neas vitales, 
 *  a una nueva tabla en la base de datos con valores �nicos (elimina valores repetidos) de amenaza,
 *  agrega tambi�n un campo nuevo a esta tabla para que en otro proceso posterior (en CalcularDanio.java) se calcule el da�o.
 *  Utilizando un archivo de texto de configuraci�n (parametros_ValoresUnicosCampo.txt) 
 *  y un modelo de geoprocesamiento (ValoresUnicosCampo).
 * -----------------
 * <b><i>Notas para un correcto funcionamiento:</b></i>
 * -----------------
 *  Nota 1: La variable de sistema 'RiesgoSismicoLineasVitales' debe definir la ruta a la carpeta que contiene el aplicativo. Ejemplo:
 *            RiesgoSismicoLineasVitales = D:\geo\
 *  Nota 2: El Toolbox con el modelo debe estar en %RiesgoSismicoLineasVitales%\modelos_geoprocesamieto\Modelos_RSLV.tbx
 *  Nota 3: El modelo de geoprocesamiento usados por esta clase es:
 *            - ValoresUnicosCampo (Crea la tabla de valores �nicos - par�metro 3). Usa internamente la clase 'ValoresUnicosCampo.java'
 *  Nota 4: El archivo de texto de par�metros debe estar en %RiesgoSismicoLineasVitales%\modelos_geoprocesamieto\parametros_ValoresUnicosCampo.txt
 *            Toda l�nea en ese archivo de texto que inicie por (quitando las comillas) ' *', '/*' o '//' es un comentario
 *            No puede existir una l�nea vac�a al final del archivo de texto
 *            Todos los par�metros de la ejecuci�n de los modelos de geoprocesamiento van en una sola l�nea
 *            Coloque tantas l�neas como corridas o ejecuciones del modelo desee (la clase itera leyendo los par�metros de cada l�nea y ejecutando el modelo por cada l�nea)
 *            Cada par�metro en la l�nea va separado por punto y coma y un espacio. Ejemplo: '; '
 *            Espacios adicionales o sobrantes entre par�metros causan que el modelo falle
 *            Los par�metros que son lista (si los hay) se separan por espacios sencillos. Ejemplo: '; campo1 campo2 campo3'
 *          Los par�metros en cada l�nea del archivo de par�metros son los siguientes:
 *            1-tool.setFClass(rutaFC): Ruta al Feature Class de l�neas vitales
 *            2-tool.setCampoFCX(campoEstadisticas): Campo existente en el FC del par�metro 1, que contiene los valores de amenaza (El campo debe ser tipo FLOAT)
 *            3-tool.setTablaValoresUnicos(nuevaTabla): Ruta de la nueva tabla que se crear� con los valores �nicos de amenaza provenientes de los par�metros anteriores (1, 2)
 *            4-tool.setCampoNuevoTVUY(campoAdicionalValoresYCurva): Este campo que se agregar� a la nueva tabla ser� (en otro proceso separado) usado por CalcularDanio.java para calcular da�os
 *  Nota 5: Aseg�rese que si exista el campo 'campoEstadisticas' (par�metro 2) en el FC 'rutaFC' (par�metro 1)
 *  Nota 6: Aseg�rese que el campo 'campoEstadisticas' (par�metro 2) sea de tipo FLOAT
 *  Nota 7: Si la tabla 'nuevaTabla' existe en la base de datos, el proceso la sobreescribir�. Tenga cuidado con sus datos.
 *  Nota 8: Aseg�rese que la base de datos no est� bloqueada por ArcGIS!
 * </pre>
 *  @version 1.0
 *  @author Msc. Esp. Ing. Alexys H Rodr�guez Avellaneda, <a href="mailto:alexyshr@gmail.com">alexyshr@gmail.com</a> - Nov 2009 - Maestr�a en Geom�tica (Universidad Nacional de Colombia)
 ***************************************************************************/
public class CrearTablaValoresUnicosCampo {
	public static void main(String[] args) throws UnknownHostException, IOException {		
		try{
			InicioLicenciasVariables iul = new InicioLicenciasVariables();
			iul.initializeArcGISLicenses();
			String RiesgoSismicoLineasVitales = iul.iniciarRSLV();
			//String ubicacionTool = "D:/geo/modelos_geoprocesamieto/Modelos_RSLV.tbx";		    
			//String ubicacionTool = "D:"+File.separator+"geo"+File.separator+"" +
			//						"modelos_geoprocesamieto"+File.separator+"Modelos_RSLV.tbx";
			String ubicacionTool = RiesgoSismicoLineasVitales + File.separator + 
									"modelos_geoprocesamieto"+File.separator+"Modelos_RSLV.tbx";
			System.out.println("___________________________");
			System.out.println("Archivo del ToolBox = ");
			System.out.println("  " + ubicacionTool);
			System.out.println("___________________________");
			System.out.println("Nombre del Modelo = ");
			System.out.println("  ValoresUnicosCampo");
			System.out.println("___________________________");
			
			//String RutaArchivoParametros = "D:"+File.separator+"geo"+File.separator+
			//"modelos_geoprocesamieto"+File.separator+"parametros_ValoresUnicosCampo.txt";
			String RutaArchivoParametros = RiesgoSismicoLineasVitales + File.separator +
									"modelos_geoprocesamieto"+File.separator+"parametrosValoresUnicosCampo.txt";
			System.out.println("Archivo de Par�metros = ");
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
						//System.out.println("La l�nea '" + i + "' es un comentario. No se tiene en cuenta!");
						continue;
					}
					System.out.println("***Inicia Ejecuci�n (GP) de la l�nea: '"+ i + "'");
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
					if (tokens.length != 2){
						System.out.println("  Error en la l�nea : '" + i + "'. Deber�a tener 2 par�metros y tiene '" + tokens.length + "'" );
						continue;
					}
					String rutaFC = tokens[0];
					System.out.println("  1- rutaFC = '" + rutaFC + "'");
					String campoEstadisticas = tokens[1];
					System.out.println("  2- campoEstadisticas = '" + campoEstadisticas + "'");
					String nuevaTabla = "";
					if (rutaFC.contains(".gdb")){
						nuevaTabla = rutaFC.substring(0,rutaFC.lastIndexOf(".gdb")+5) 
						+ rutaFC.substring(rutaFC.lastIndexOf("/")+1) + "_" + campoEstadisticas;
					}else if (rutaFC.contains(".mdb")){
						nuevaTabla = rutaFC.substring(0,rutaFC.lastIndexOf(".gdb")+5) 
						+ rutaFC.substring(rutaFC.lastIndexOf("/")+1) + "_" + campoEstadisticas;
				    }else{
				    	throw new IllegalStateException("Este proceso solo funciona con datos en Personal o File Geodatabase!!");
				    }			    					 		

					System.out.println("  La nueva tabla que se crear� se llamar�: '" + nuevaTabla + "'");
					System.out.println("  Nota 1: Aseg�rese que exista el campo '" + campoEstadisticas + "' (par�metro 2) en el FC '" + rutaFC + "' (par�metro 1)");
					System.out.println("  Nota 2: El campo '" + campoEstadisticas + "' debe ser tipo FLOAT");
					System.out.println("  Nota 3: Si la tabla '" + nuevaTabla+ "' existe en la base de datos, el proceso la sobreescribir�");
					System.out.println("  Nota 4: Aseg�rese que la base de datos no est� bloqueada por ArcGIS!");
					System.out.println("  Nota 5: El archivo de par�metros no debe tener l�neas vacias al final, ni contenido extra�o fuera de comentarios!");
					System.out.println("  Nota 6: Verifique la documentaci�n del uso de esta clase(archivo de par�metros o documentos HTML)");
				    GeoProcessor gp = new GeoProcessor();
				    gp.setOverwriteOutput(true);
					gp.addToolbox(ubicacionTool);
					ValoresUnicosCampo tool = new ValoresUnicosCampo();	
					tool.setFClass(rutaFC);
					tool.setCampoFCX(campoEstadisticas);			
					tool.setTablaValoresUnicos(nuevaTabla);
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
												
					//GeoProcessorResult gpResult = (GeoProcessorResult) gp.execute(tool, null);
					
				    //tool = new valoresUnicosCampo(campoEstadisticas, rutaFC, nuevaTabla, campoAdicionalValoresYCurva);
					//tool = new valoresUnicosCampo();
		
				    //gp.execute(tool, new TrackCancel());
				    //gp.execute(tool, null);
				    System.out.println("  //////////El modelo se ha corrido para la l�nea: '" + i + "'");
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








