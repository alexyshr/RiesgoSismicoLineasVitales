package analizar;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.File;
import java.io.FileReader;
import analizar.modelos_rslv.PasarAmenazaALineas;
import analizar.modelos_rslv.PasarAmenazaALineas2;
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
 * <i><b>Uso de la clase 'PasarAmenazaRaster_a_FClass.java':</b></i>
 * -----------------
 * Desde el c�digo fuente:         
 *           <b>import analizar.PasarAmenazaRaster_a_FClass;</b>
 *           <b>PasarAmenazaRaster_a_FClass.main(null);</b>
 * Desde l�nea de comandos (uso de binarios):
 *      Usando al archivo binario PasarAmenazaRaster_a_FClass.class: 
 *           <b>java analizar.PasarAmenazaRaster_a_FClass</b>
 *      Apuntando el archivo rslv.jar:
 *           <b>java -cp ./rslv.jar analizar.PasarAmenazaRaster_a_FClass</b>
 * -----------------
 * <b><i>Descripci�n de la clase 'PasarAmenazaRaster_a_FClass.java':</b></i>
 * -----------------
 *  A partir de modelos de geoprocesamiento construidos con Model Builder, pasa los valores de amenaza de un mapa raster a las l�neas vitales, utilizando un archivo intermedio de centroides de las l�neas.
 *  Utiliza dos modelos de geoprocesamiento, para tener la opci�n de crear el Feature Class de centroides o utilizar un archivo existente.
 * -----------------
 * <b><i>Notas para un correcto funcionamiento:</b></i>
 * -----------------
 *  Nota 1: La variable de sistema 'RiesgoSismicoLineasVitales' debe definir la ruta a la carpeta que contiene el aplicativo. Ejemplo:
 *            RiesgoSismicoLineasVitales = D:\geo\
 *  Nota 2: El Toolbox con los dos modelos debe estar en %RiesgoSismicoLineasVitales%\modelos_geoprocesamieto\Modelos_RSLV.tbx
 *  Nota 3: Los modelos de geoprocesamiento usados por esta clase son:
 *            - PasarAmenazaALineas (Crea el Feature Class de puntos con centroides - par�metro 4). Usa internamente la clase 'PasarAmenazaALineas.java'
 *            - PasarAmenazaALineas2 (Utiliza un Feature Class existente--debe existir-- de puntos con centroides - par�metro 4). Usa internamente la clase 'PasarAmenazaALineas2.java'
 *  Nota 4: El archivo de texto de par�metros debe estar en %RiesgoSismicoLineasVitales%\modelos_geoprocesamieto\parametrosPasarAmenazaALineas.txt
 *            Toda l�nea en ese archivo de texto que inicie por (quitando las comillas) ' *', '/*' o '//' es un comentario
 *            No puede existir una l�nea vac�a al final del archivo de texto
 *            Todos los par�metros de la ejecuci�n de los modelos de geoprocesamiento van en una sola l�nea
 *            Coloque tantas l�neas como corridas o ejecuciones del modelo desee (la clase itera leyendo los par�metros de cada l�nea y ejecutando el modelo por cada l�nea)
 *            Cada par�metro en la l�nea va separado por punto y coma y un espacio. Ejemplo: '; '
 *            Espacios adicionales o sobrantes entre par�metros causan que el modelo falle.
 *            Los par�metros que son lista se separan por espacios sencillos. Ejemplo: '; campo1 campo2 campo3'
 *          Los par�metros en cada l�nea del archivo de par�metros son los siguientes:
 *            1- setRutaFCLassLV(rutaFCLassLV): Ruta al Feature Class de l�neas vitales
 *            2- setNuevoCampoAmenazaFC(nuevoCampoAmenazaFC): Nuevo campo en el Feature Class anterior que almacenar� los valores de amenaza
 *            3- setCampoAmenazaFCACalcular(campoAmenazaFCACalcular): El mismo campo del par�metro anterior (2) pero en el formato 'nombreFC.campo'
 *            4- setRutaClassCentroides(rutaFClassCentroides): Ruta al FC (existente o a crear -- ver par�metro 9) de puntos con centroides del FC del par�metro 1
 *            5- setListaCamposEliminarFCCentroides(listaCamposEliminarFCCentroides): lista separada por espacios de todos los nombres de los campos del FC pasado en el par�metro 1. Ejemplo: campo1 campo2 campo3
 *            6- setRutaNuevoFCLassCentroidesAmenaza(rutaNuevoFCLassCentroidesAmenaza): Ruta del nuevo FC de puntos que contendr� almacenado el valor de amenaza extraido del raster del par�metro 7
 *            7- setRutaRasterAmenaza(rutaRasterAmenaza): Ruta de raster que contiene los valores de amenaza s�smica                
 *            8- setExpresionCalculoCampoAmenaza(expresionCalculoCampoAmenaza): Expresi�n de calculo utilizada por la funci�n 'Calculate' en un layer con 'Join' en el formato: Round([nombre_FC_del_par�metro_6.RASTERVALU], 2)
 *            9- creoCentroides: par�metro para decidir si se crea el FC de centroides o se utiliza uno existente
 *               0 = No (PasarAmenazaALineas2.java: Utiliza centroides existentes. No utiliza la lista de campos a eliminar (par�metro 4)!!!)
 *               1 = Si (PasarAmenazaALineas.java: Crea nuevos puntos con centroides)
 *  Nota 5: Aseg�rese que no exista el campo 'nuevoCampoAmenazaFC' (par�metro 2) en el FC 'rutaFCLassLV' (par�metro 1)
 *  Nota 6: Si los par�metros 4 y 6 existen en la base de datos, es decir rutaFClassCentroides(--solo si el par�metro nueve = 1--) y rutaNuevoFCLassCentroidesAmenaza, el proceso los sobreescribir�. Tenga cuidado con sus datos.
 *  Nota 7: Aseg�rese que el campo 'nuevoCampoAmenazaFC' (par�metro 2) no est� en la lista de campos a eliminar. 
 *            Esta lista est� compuesta por todos los campos del FC 'rutaFCLassLV' (par�metro 1), menos OBJECTID (la idea eliminar esa lista de campos y dejar solo el campo OBJECTID)
 *  Nota 8: La lista de campos (par�metro 5) debe ir separada por espacios (internamente reemplaza por un ';' para que el modelo funcione)
 *  Nota 9: La lista de campos (par�metro 5) no se utiliza si el par�metro 9 (creoCentroides) es igual a '0', en este caso si no desea colocar el par�metro 5, debe dejar el punto y coma separado por un espacio: ejemplo '; ;'
 *  Nota 10: El par�metro 4 (rutaFClassCentroides) significa:
 *            Si el par�metro 9 (creoCentroides) vale '0': significa una ruta existente del Feature Class de centroides
 *            Si el par�metro 9 (creoCentroides) vale '1': significa una ruta nueva para crear el Feature Class de centroides
 *  Nota 11: Aseg�rese que la base de datos no est� bloqueada por ArcGIS!
 * </pre>
 *  @version 1.0
 *  @author Msc. Esp. Ing. Alexys H Rodr�guez Avellaneda, <a href="mailto:alexyshr@gmail.com">alexyshr@gmail.com</a> - Nov 2009 - Maestr�a en Geom�tica (Universidad Nacional de Colombia)
 ***************************************************************************/
public class PasarAmenazaRaster_a_FClass {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			InicioLicenciasVariables iul = new InicioLicenciasVariables();
			iul.initializeArcGISLicenses();
			String RiesgoSismicoLineasVitales = iul.iniciarRSLV();
			String ubicacionTool = RiesgoSismicoLineasVitales + File.separator + 
									"modelos_geoprocesamieto"+File.separator+"Modelos_RSLV.tbx";
			System.out.println("___________________________");
			System.out.println("Archivo del ToolBox = ");
			System.out.println("  " + ubicacionTool);
			System.out.println("___________________________");
			System.out.println("Nombre de los Modelos = ");
			System.out.println("  PasarAmenazaALineas (crea puntos con centroides) y PasarAmenazaALineas2(utiliza un FC existente de puntos con centroides)");
			System.out.println("___________________________");
			String RutaArchivoParametros = RiesgoSismicoLineasVitales + File.separator +
									"modelos_geoprocesamieto"+File.separator+"parametrosPasarAmenazaAFClass.txt";
			System.out.println("Archivo de Par�metros = ");
			System.out.println("  " + RutaArchivoParametros);
			System.out.println("___________________________");
			System.out.println("___________________________");

			Reader reader = new FileReader(RutaArchivoParametros);
					
			try(BufferedReader bReader = new BufferedReader(reader)){
				int i = 0;
				for(String line = bReader.readLine(); line != null; line = bReader.readLine()){
					i++;
					if (line.startsWith("//") || line.startsWith(" *") || line.startsWith("/*")) {
						//System.out.println("La l�nea '" + i + "' es un comentario. No se tiene en cuenta!");
						continue;
					}
					System.out.println("***Inicia Ejecuci�n (GP) de la l�nea: '"+ i + "'");
					System.out.println("  " + line);				
			    					   
					//Tokenize the line of text based on a comma seperator
					String [] tokens = line.split("; ");
					String rutaFCLassLV = tokens[0];
					if (tokens.length != 5){
						System.out.println("  Error en la l�nea : '" + i + "'. Deber�a tener 5 par�metros y tiene '" + tokens.length + "'" );
						continue;
					}				
					System.out.println("  1- rutaFCLassLV = '" + rutaFCLassLV + "'");
					String rutaNuevoFClassCentroides = tokens[1];
					System.out.println("  2- rutaFClassCentroides = '" + rutaNuevoFClassCentroides + "'");
					String listaCamposEliminarFCCentroides = tokens[2];
					//System.out.println("  5- listaCamposEliminarFCCentroides = '" + listaCamposEliminarFCCentroides + "'");
					listaCamposEliminarFCCentroides = listaCamposEliminarFCCentroides.replace(" ", ";");
					System.out.println("  3- listaCamposEliminarFCCentroides = '" + listaCamposEliminarFCCentroides + "'");
					String rutaRasterAmenaza = tokens[3];
					System.out.println("  4- rutaRasterAmenaza = '" + rutaRasterAmenaza + "'");				
					String nuevoCampoAmenazaFC = rutaRasterAmenaza.substring(rutaRasterAmenaza.lastIndexOf("/")+1);
					System.out.println("  Nuevo campo en '" + rutaFCLassLV + "' que almacenar� los valores de amenaza: '" + nuevoCampoAmenazaFC + "'");				
					String campoAmenazaFCACalcular = rutaFCLassLV.substring(rutaFCLassLV.lastIndexOf("/")+1) + "." + nuevoCampoAmenazaFC;
					System.out.println("  Campo anterior formateado para el Calculate = '" + campoAmenazaFCACalcular + "'");				
					String rutaNuevoFCLassCentroidesAmenaza = rutaNuevoFClassCentroides+ "_" + nuevoCampoAmenazaFC;
					System.out.println("  Ruta al nuevo Feature Class de puntos con la amenaza en los centroides = '" + rutaNuevoFCLassCentroidesAmenaza + "'");
		
					String expresionCalculoCampoAmenaza = "Round([" + rutaNuevoFCLassCentroidesAmenaza.substring(rutaNuevoFCLassCentroidesAmenaza.lastIndexOf("/")+1) + ".RASTERVALU], 2)";
					System.out.println("  Expresi�n de Calculo = '" + expresionCalculoCampoAmenaza + "'");
					int creoCentroides = Integer.parseInt(tokens[4]);
					if (creoCentroides == 0) {
						System.out.println("  5- creoCentroides = '0': Utiliza puntos de centroides existentes");
						System.out.println("  Nota 1: Si el Feature Class '" + rutaNuevoFCLassCentroidesAmenaza + "' existe en la base de datos el proceso lo sobreescribir�");
						System.out.println("  Nota 2: Aseg�rese que el FC '" + rutaNuevoFClassCentroides + "' del par�metro 2 exista!!");
					}else if (creoCentroides == 1){
						System.out.println("  5- creoCentroides = '1': Crea puntos con centroides de las l�neas");
						System.out.println("  Nota 1: Si el Feature Class '" + rutaNuevoFClassCentroides + "' y '" + rutaNuevoFCLassCentroidesAmenaza + "', existen en la base de datos el proceso los sobreescribir�");
						System.out.println("  Nota 2: Se crear� el FC del par�metro 2: '" + rutaNuevoFClassCentroides +"'");
					}	
					System.out.println("  Nota 3: El campo '" + nuevoCampoAmenazaFC + "' no debe existir en '" + rutaFCLassLV + "'");				
					System.out.println("  Nota 4: Aseg�rece que no haya bloqueo en las bases de datos utilizadas (Cierre ArcGIS)!");
					System.out.println("  Nota 5: El archivo de par�metros no debe tener l�neas vacias al final, ni contenido extra�o fuera de comentarios!");
					System.out.println("  Nota 6: Verifique la documentaci�n del uso de esta clase(archivo de par�metros o documentos HTML)");
					
				    GeoProcessor gp = new GeoProcessor();
				    ValidarGP_MensajesGP validar = new ValidarGP_MensajesGP();
				    gp.setOverwriteOutput(true);
				    gp.addToolbox(ubicacionTool);
				    
					if (creoCentroides == 0) {
						PasarAmenazaALineas2 tool_no_crea_centroides = new PasarAmenazaALineas2();
						tool_no_crea_centroides.setRutaFCLassLV(rutaFCLassLV);
						tool_no_crea_centroides.setNuevoCampoAmenazaFC(nuevoCampoAmenazaFC);		
						tool_no_crea_centroides.setCampoAmenazaFCACalcular(campoAmenazaFCACalcular);				
						tool_no_crea_centroides.setRutaFClassCentroidesLV(rutaNuevoFClassCentroides);
						tool_no_crea_centroides.setRutaNuevoFCLassCentroidesAmenaza(rutaNuevoFCLassCentroidesAmenaza);
						tool_no_crea_centroides.setRutaRasterAmenaza(rutaRasterAmenaza);				
						tool_no_crea_centroides.setExpresionCalculoCampoAmenaza(expresionCalculoCampoAmenaza);												
						if (validar.Validar(gp, tool_no_crea_centroides) == true){
							System.out.println("  **** Error: El registro actual no se procesa por tener los errores descritos con anterioridad!");
							continue;
						}

						try {
							gp.execute(tool_no_crea_centroides, null);
							validar.returnMessagesGP(gp, tool_no_crea_centroides.getToolName());
						} catch (Exception e){
							System.out.println("Mensaje de error: " + e.getMessage());
							e.printStackTrace();								
							validar.returnMessagesGP(gp, tool_no_crea_centroides.getToolName());	
						}														
					}else if (creoCentroides == 1){
						PasarAmenazaALineas tool_crea_centroides = new PasarAmenazaALineas();
						tool_crea_centroides.setRutaFCLassLV(rutaFCLassLV);
						tool_crea_centroides.setNuevoCampoAmenazaFC(nuevoCampoAmenazaFC);
						tool_crea_centroides.setCampoAmenazaFCACalcular(campoAmenazaFCACalcular);
						tool_crea_centroides.setRutaNuevoFClassCentroides(rutaNuevoFClassCentroides);
						tool_crea_centroides.setListaCamposEliminarFCCentroides(listaCamposEliminarFCCentroides);
						tool_crea_centroides.setRutaNuevoFCLassCentroidesAmenaza(rutaNuevoFCLassCentroidesAmenaza);
						tool_crea_centroides.setRutaRasterAmenaza(rutaRasterAmenaza);				
						tool_crea_centroides.setExpresionCalculoCampoAmenaza(expresionCalculoCampoAmenaza);					

						if (validar.Validar(gp, tool_crea_centroides) == true){
							System.out.println("  **** Error: El registro actual no se procesa por tener los errores descritos con anterioridad!");
							continue;
						}
						try {
							gp.execute(tool_crea_centroides, null);
							validar.returnMessagesGP(gp, tool_crea_centroides.getToolName());
						} catch (Exception e){
							System.out.println("Mensaje de error: " + e.getMessage());
							e.printStackTrace();
							validar.returnMessagesGP(gp, tool_crea_centroides.getToolName());
						}
						
					}else{
						throw new IllegalStateException(" '9- creoCentroides' solo acepta '0', o '1' como valor!");
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
