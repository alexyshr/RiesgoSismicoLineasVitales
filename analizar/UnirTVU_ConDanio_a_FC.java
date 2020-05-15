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
 * <i><b>Uso de la clase 'UnirTVU_ConDanio_a_FC.java':</b></i>
 * -----------------
 * Desde el c�digo fuente:         
 *           <b>import analizar.UnirTVU_ConDanio_a_FC;</b>
 *           <b>UnirTVU_ConDanio_a_FC.main(null);</b>
 * Desde l�nea de comandos (uso de binarios):
 *      Usando al archivo binario UnirTVU_ConDanio_a_FC.class: 
 *           <b>java analizar.UnirTVU_ConDanio_a_FC</b>
 *      Apuntando el archivo rslv.jar:
 *           <b>java -cp ./rslv.jar analizar.UnirTVU_ConDanio_a_FC</b>
 * -----------------
 * <b><i>Descripci�n de la clase 'UnirTVU_ConDanio_a_FC.java':</b></i>
 * -----------------
 *  A partir de un modelo de geoprocesamiento construido con Model Builder, se une (Join) temporalmente una tabla de valores �nicos de amenaza que contiene 
 *  los datos de da�o, a un Feature Class de l�neas vitales para calcular en este �ltimo los valores de da�o provenientes de esta tabla. 
 *  La tabla y el Feature Class deben tener un campo com�n con el mismo nombre, para hacer el 'Join'.
 *  El modelo crea un �ndice en el campo llave para el Join, residente en el Feature Class.
 *  La tabla de valores �nicos de amenaza debe tener los valores de da�o almacenados en un campo. Esta tabla puede crearse previamente con la clase 'CrearTablaValoresUnicosCampo.java'
 *  El campo que contiene los datos de da�o en la tabla, servir� para crear uno id�ntico en el Feature Class y ah� se calcular�n los datos de da�o.
 *  Utiliza el modelo de geoprocesamiento llamado 'UnirTablaValoresUnicosaFC' y lee los par�metros del modelo de una l�nea del archivo 'parametros_UnirTablaValoresUnicosaFC.txt'
 * -----------------
 * <b><i>Notas para un correcto funcionamiento:</b></i>
 * -----------------
 *  Nota 1: La variable de sistema 'RiesgoSismicoLineasVitales' debe definir la ruta a la carpeta que contiene el aplicativo. Ejemplo:
 *            RiesgoSismicoLineasVitales = D:\geo\
 *  Nota 2: El Toolbox con el modelo debe estar en %RiesgoSismicoLineasVitales%\modelos_geoprocesamieto\Modelos_RSLV.tbx
 *  Nota 3: El modelo de geoprocesamiento usado por esta clase es:
 *            - UnirTablaValoresUnicosaFC: 
 *              Une la tabla (par�metro 3) al Feature Class (par�metro 1) a partir de campos comunes (par�metro 2). 
 *              Crea un �ndice en un campo (par�metro 2) del Feature Class, para que la uni�n ('Join') sea m�s r�pida
 *              Calcula un campo nuevo (par�metro 4) en el Feature Class para almacenar los datos de da�o provenientes de un campo de la tabla (par�metro 4). 
 *              El campo en la tabla con los datos de da�o y campo nuevo en el FC tendr�n el mismo nombre (par�metro 4)
 *              Usa internamente la clase 'UnirTablaValoresUnicosaFC.java'
 *  Nota 4: El archivo de texto de par�metros debe estar en %RiesgoSismicoLineasVitales%\modelos_geoprocesamieto\parametros_UnirTablaValoresUnicosaFC.txt
 *            Toda l�nea en ese archivo de texto que inicie por (quitando las comillas) ' *', '/*' o '//' es un comentario
 *            No puede existir una l�nea vac�a al final del archivo de texto
 *            Todos los par�metros de la ejecuci�n de los modelos de geoprocesamiento van en una sola l�nea
 *            Coloque tantas l�neas como corridas o ejecuciones del modelo desee (la clase itera leyendo los par�metros de cada l�nea y ejecutando el modelo por cada l�nea)
 *            Cada par�metro en la l�nea va separado por punto y coma y un espacio. Ejemplo: '; '
 *            Espacios adicionales o sobrantes entre par�metros causan que el modelo falle.
 *          Los par�metros en cada l�nea del archivo de par�metros son los siguientes:
 *            1-fClass: Ruta al Feature Class que se desea unir los valores de amenaza.
 *            2-campoFCJoin, campoIndexFC y campoTablaUnirJoin: Nombre del campo residente en el Feature Class y en la tabla de amenaza con el cu�l se har� el 'Join'. En este campo existente para el FC se har� un �ndice.
 *            3-tablaUnir: Ruta a la tabla con los valores �nicos de amenaza. Debe tener el campo com�n con el FC para hacer el 'Join' y otro campo con los valores de amenaza.
 *            4-CampoDaniosTablaUnir (campo nuevo en el FC): Nombre del campo en la tabla de valores �nicos que contiene los valores de da�o. Con este nombre de campo se crear� uno id�ntico en el FC
 *  Nota 5: El campo 'CampoDaniosTablaUnir' (par�metro 4) no debe existir en el 'fClass' (par�metro 1)
 *  Nota 6: El �ndice en el campo 'campoIndexFC' (par�metro 2) del 'fClass' (par�metro 1) no debe existir!
 *  Nota 7: El campo 'CampoDaniosTablaUnir' (par�metro 4) debe existir con datos de da�o en la 'tablaUnir' (par�metro 3)
 *  Nota 8: El 'fClass' (par�metro 1) y la 'tablaUnir' (par�metro 3) pueden existir en diferentes bases de datos
 *  Nota 9: Aseg�rese que no haya bloqueo en las bases de datos donde reside la Tabla y el Feature Class (Cierre ArcGIS)!
 * </pre>
 *  @version 1.0
 *  @author Msc. Esp. Ing. Alexys H Rodr�guez Avellaneda, <a href="mailto:alexyshr@gmail.com">alexyshr@gmail.com</a> - Nov 2009 - Maestr�a en Geom�tica (Universidad Nacional de Colombia)
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
					if (tokens.length != 3){
						System.out.println("  Error en la l�nea : '" + i + "'. Deber�a tener 3 par�metros y tiene '" + tokens.length + "'" );
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
					System.out.println("  La tabla '" + tablaUnir + "' se unir� (JOIN) al Feature Class: '" + fClass + "'");				
					//System.out.println("  3- tablaUnir = '" + tablaUnir + "'");
					String campoNuevoFC = tokens[2];
					System.out.println("  3- CampoDaniosTablaUnir (campo a crear en el FC) = '" + campoNuevoFC + "'"); 				
					String campoNuevoFC2 = fClass.substring(fClass.lastIndexOf("/")+1) + "." + campoNuevoFC;
					System.out.println("  *** Campo nuevo en el Feature Class (campoNuevoFC2) para almacenar datos de da�os, formateado para el Calculate = '" + campoNuevoFC2 + "'");						
					String expresionCalculoFC = "[" + tablaUnir.substring(tablaUnir.lastIndexOf("/")+1) + "." + campoNuevoFC + "]";
					System.out.println("  *** Expresi�n de Calculo en el FC = '" + expresionCalculoFC + "'");
									
					System.out.println("  Nota 1: El campo '" + campoFCJoin + "' debe existir en el Feature Class '" + fClass + "' y en la tabla '" + tablaUnir + "'");
					System.out.println("  Nota 1: El campo '" + campoNuevoFC + "' no debe existir en el Feature Class '" + fClass + "'");
					System.out.println("  Nota 2: El campo '" + campoNuevoFC + "' debe existir con datos de da�o en la tabla '" + tablaUnir + "'");
					System.out.println("  Nota 3: Es recomendable que cree primero (antes de correr este proceso) un �ndice en el campo '" + campoFCJoin + "' de '" + fClass);
					System.out.println("  Nota 4: Aseg�rese que no haya bloqueo en las bases de datos donde reside la Tabla y el Feature Class (cierre ArcGIS)!");
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
					System.out.println("  //////////El proceso se ha corrido para la l�nea: '" + i + "'");
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


