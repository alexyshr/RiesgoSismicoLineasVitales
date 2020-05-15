package analizar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
 * <i><b>Uso de la clase 'CalcularDanio.java':</b></i>
 * -----------------
 * Desde el c�digo fuente:         
 *           <b>import analizar.CalcularDanio;</b>
 *           <b>CalcularDanio.main(null);</b>
 * Desde l�nea de comandos (uso de binarios):
 *      Usando al archivo binario CalcularDanio.class: 
 *           <b>java analizar.CalcularDanio</b>
 *      Apuntando el archivo rslv.jar:
 *           <b>java -cp ./rslv.jar analizar.CalcularDanio</b>
 * -----------------
 * <b><i>Descripci�n de la clase 'CalcularDanio.java':</b></i>
 * -----------------
 *  Lee los par�metros de configuraci�n del archivo ParametrosRepairRateCurvasVulnerabilidad.txt para
 *  ejecutar la clase UsarCurvaVulnerabilidad.java. Hace un ciclo recorriendo cada l�nea del archivo mencionado
 *  para ejecutar la clase UsarCurvaVulnerabilidad.java una vez por cada l�nea. 
 *  <b><i>Descripci�n de la clase 'UsarCurvaVulnerabilidad.java':</b></i>
 *  Es inicializada por la clase 'CalcularDanio.java', pas�ndole cinco par�metros contenidos en cada l�nea
 *  del archivo ParametrosRepairRateCurvasVulnerabilidad.txt. Ver los par�metros mas adelante.
 *  <b><i>Su objetivo</b></i> es poblar los valores de da�o -- eje Y de una curva de vulnerabilidad espec�fica 
 *  seleccionada a partir de su identificador <b><i>(par�metro 2)</b></i>-- en el campo correspondiente <b><i>(par�metro 5)</b></i> 
 *  de una tabla con valores �nicos de amenaza <b><i>(par�metro 3)</b></i>, a partir de los valores �nicos de amenaza 
 *  almacenados en otro campo de esa tabla <b><i>(par�metro 4)</b></i>. 
 *  En la <b><i>File Geodatabase</b></i> descrita por otro par�metro (par�metro 1) deben existir:
 *    - Una tabla llamada <b><i>'PAV_PARAMETROS_VULN'</b></i> con los par�metros de las curvas de vulnerabilidad de acuerdo con el modelo de datos
 *    - La tabla <b><i>'PAV_PARAMETROS_VULN'</b></i> debe tener los campos: <b><i>PAV_ID, pav_formula, pav_minx, pav_maxx</b></i>. El �nico campo no obligatorio es 'formula'
 *    - Otra tabla llamada <b><i>'DAV_DATOS_VULN'</b></i> con los datos X, Y de la correspondiente curva de vulnerabilidad identificada en la tabla 'PAV_PARAMETROS_VULN'
 *    - La tabla 'DAV_DATOS_VULN' debe tener los campos: <b><i>PAV_ID, DAV_X, DAV_Y</b></i>. No son obligatorios solo si existe valor para 'formula' en la tabla 'PAV_PARAMETROS_VULN'.
 *  <b><i>Importante:</b></i>
 *    - El identificador de la curva <b><i>(par�metro 2)</b></i> corresponde al campo <b><i>PAV_ID</b></i> de las tablas 'PAV_PARAMETROS_VULN', 'DAV_DATOS_VULN'
 *    - La tabla de valores �nicos de amenaza (par�metro 3) debe existir en <b><i>la base de datos</b></i> descrita por el par�metro 1
 *    - Los valores �nicos de amenaza (par�metro 4) almacenados en la tabla (par�metro 3) deben servir para la curva de vulnerabilidad seleccionada
 *    - En esta rutina no se lleva el da�o al Feature Class de l�neas vitales. Solo se almacena el da�o en la tabla de valores �nicos (par�metro 3)
 *    - La curva se arma y manipula en otra clase llamada <b><i>'Curva.java'</b></i>. Si <b><i>hay 'formula'</b></i> esta se utiliza para calcular el da�o con la clase <b><i>'MathEvaluator.java'</b></i>
 *    - Si <b><i>no hay 'formula'</b></i> la clase 'Curva.java' llama a la clase </b></i>'Interpolador.java'</b></i> para usar los puntos de la tabla 'DAV_DATOS_VULN' e interpolar el da�o.
 *    - Esta clase no utiliza modelos de geoprocesamiento
 * -----------------
 * <b><i>Notas para un correcto funcionamiento:</b></i>
 * -----------------
 *  Nota 1: La variable de sistema 'RiesgoSismicoLineasVitales' debe definir la ruta a la carpeta que contiene el aplicativo. Ejemplo:
 *            RiesgoSismicoLineasVitales = D:\geo\
 *  Nota 2: El archivo de texto de par�metros debe estar en %RiesgoSismicoLineasVitales%\archivos_configuracion_clases_java\ParametrosRepairRateCurvasVulnerabilidad.txt
 *            Toda l�nea en ese archivo de texto que inicie por (quitando las comillas) ' *', '/*' o '//' es un comentario
 *            No puede existir una l�nea vac�a al final del archivo de texto
 *            Todos los par�metros de la ejecuci�n de la clase van en una sola l�nea
 *            Coloque tantas l�neas como corridas o ejecuciones de la clase UsarCurvaVulnerabilidad.java desee (la clase CalcularDanio.java itera leyendo los par�metros de cada l�nea y ejecutando UsarCurvaVulnerabilidad.java una vez por cada l�nea)
 *            Cada par�metro en la l�nea va separado por punto y coma y un espacio. Ejemplo: '; '
 *            Espacios adicionales o sobrantes entre par�metros causan que el modelo falle
 *            Los par�metros que son lista se separan por espacios sencillos. Ejemplo: '; campo1 campo2 campo3'
 *          Los par�metros en cada l�nea del archivo de par�metros son los siguientes:
 *            1- rutaGDB: Ruta a la File Geodatabase conteniendo las tablas 'PAV_PARAMETROS_VULN' y 'DAV_DATOS_VULN'
 *            2- idCurva: Identificador de una curva correspondiente el campo 'PAV_ID' de las tablas anteriores
 *            3- tablaValoresUnicos: Una tabla almacenada en la File Geodatabase del par�metro 1. Con dos campos tipo FLOAT, uno lleno con valores de amenaza, y otro vac�o para almacenar valores de da�o. Esta tabla se puede crear con 'CrearTablaValoresUnicosCampo.java'
 *            4- campoTVU_X: Nombre de campo existente (tipo FLOAT) almacenando en la tabla del par�metro 3, los valores de amenaza (debe estar lleno, es decir con valores)
 *            5- campoTVU_Y: Nombre de campo existente (tipo FLOAT) para almacenar en la tabla del par�metro 3, los valores de da�o (si est� lleno estos valores se recalculan)
 *            (***no tener en cuenta) 6- nombreFetureClass: prueba
 *            (***no tener en cuenta) 7- campoFC_X: pvmaxs_250
 *            (***no tener en cuenta) 8- campoFC_Y: rr_pvmaxs_250_cv28
 *  Nota 3: Aseg�rese que la base de datos (par�metro 1) exista con las tablas correspondientes: 'PAV_PARAMETROS_VULN', 'DAV_DATOS_VULN', y la tabla de valores �nicos (par�metro 3)
 *  Nota 4: Aseg�rese que la tabla 'PAV_PARAMETROS_VULN', tenga para el identificador de la curva a usar: PAV_ID = '(par�metro 2)', los valores de 'pav_formula', 'pav_minx', 'pav_maxx'
 *  Nota 5: Si el campo 'pav_formula' en 'PAV_PARAMETROS_VULN' no almacena formula, se usar�n los puntos de la curva en 'DAV_DATOS_VULN' para interpolar el da�o. Verifique que los valores DAV_X, DAV_Y para la curva PAV_ID = '(par�metro 2)' est�n almacenados en esta tabla
 *  Nota 6: Los valores de 'pav_minx', 'pav_maxx' son obligatorios en 'PAV_PARAMETROS_VULN' para la curva PAV_ID = '(par�metro 2)'
 *  Nota 7: Verifique correctamente los puntos de la curva almacenados en 'DAV_DATOS_VULN' para que cumplan con los valores m�nimos ('pav_minx') y m�ximos ('pav_maxx') y as� poder interpolar
 *  Nota 8: La tabla de valores �nicos 'prueba_pvmaxs_250' debe poseer dos campos tipo FLOAT, el primero lleno con valores de amenaza: '(par�metro 4)', y el segundo para llenar con el da�o: '(par�metro 5)'
 *  Nota 9: Los valores de da�o corresponden el eje Y de las curvas de vulnerabilidad almacenados, calculados por f�rmula o interpolados por los puntos de la curva almacenados en 'DAV_DATOS_VULN'
 *  Nota 10: Aseg�rese que la base de datos no est� bloqueada por ArcGIS!
 * </pre>
 *  @version 1.0
 *  @author Msc. Esp. Ing. Alexys H Rodr�guez Avellaneda, <a href="mailto:alexyshr@gmail.com">alexyshr@gmail.com</a> - Nov 2009 - Maestr�a en Geom�tica (Universidad Nacional de Colombia)
 ***************************************************************************/
public class CalcularDanio {
	/**
	 * @param args
	 */

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
								+"parametrosCalcularDanioCurvasVulnerabilidad.txt";
			System.out.println("___________________________");
			System.out.println("Archivo de Par�metros = ");
			System.out.println("  " + RutaArchivoParametros);
			System.out.println("___________________________");
			System.out.println("___________________________");
			
			BufferedReader bReader = new BufferedReader(new FileReader(RutaArchivoParametros));  	    
					
			//Loop through the text file that contains the parameters needed to execute
			//the analysis with the custom model
			int i = 0;
			UsarCurvaVulnerabilidad ucv = new UsarCurvaVulnerabilidad();
			for(String line = bReader.readLine(); line != null; line = bReader.readLine()){
				i++;
				if (line.startsWith("//") || line.startsWith(" *") || line.startsWith("/*")) {
					//System.out.println("La l�nea '" + i + "' es un comentario. No se tiene en cuenta!");
					continue;
				}
				System.out.println("***Inicia Ejecuci�n (NO GP) de la l�nea: '"+ i + "'");
				System.out.println("  " + line);					
				String [] tokens = line.split("; ");
				String rutaGDB = tokens[0];
				if (tokens.length != 5){
					System.out.println("  Error en la l�nea : '" + i + "'. Deber�a tener 5 par�metros y tiene '" + tokens.length + "'" );
					continue;
				}
				System.out.println("  1- rutaGDB = '" + rutaGDB + "'");				
				int idCurva = Integer.parseInt(tokens[1]);
				System.out.println("  2- idCurva = '" + idCurva + "'");
				String tablaValoresUnicos = tokens[2];
				System.out.println("  3- tablaValoresUnicos = '" + tablaValoresUnicos + "'");

				String campoCorreccionMedia = tokens[3];
				System.out.println("  4- campoCorreccionMedia = '" + campoCorreccionMedia + "'");
			
				
				String campoTVU_X = tokens[4];
				System.out.println("  5- campoTVU_X = '" + campoTVU_X + "'");
				String campoTVU_Y = "danio_" +  campoTVU_X + "_cv" + idCurva;
				
				System.out.println("  Nombre del campo existente o a crear en '" + tablaValoresUnicos + "': '" + campoTVU_Y + "'");
				System.out.println("  Nota 1: Aseg�rese que '" + rutaGDB + "' exista con las tablas correspondientes: 'PAV_PARAMETROS_VULN', 'DAV_DATOS_VULN', y la tabla de valores �nicos '" + tablaValoresUnicos + "'");
				System.out.println("  Nota 2: Aseg�rese que la tabla 'PAV_PARAMETROS_VULN', tenga para el identificador de la curva a usar: PAV_ID = '" + idCurva + "', los valores de 'pav_formula', 'pav_minx', 'pav_maxx', 'pav_miny', 'pav_maxy'");
				System.out.println("  Nota 3: Si el campo 'pav_formula' en 'PAV_PARAMETROS_VULN' no almacena una formula, se usar�n los puntos de la curva en 'DAV_DATOS_VULN' para interpolar el da�o. Verifique que los valores DAV_X, DAV_Y para la curva PAV_ID = '" + idCurva + "' est�n almacenados en esta tabla");
				System.out.println("  Nota 4: Los valores de 'pav_minx', 'pav_maxx', 'pav_miny', 'pav_maxy' son obligatorios en 'PAV_PARAMETROS_VULN' para la curva PAV_ID = '"+ idCurva+"'");
				System.out.println("  Nota 5: Verifique correctamente los puntos de la curva almacenados en 'DAV_DATOS_VULN' para que cumplan con los valores m�nimos ('pav_minx', 'pav_miny') y m�ximos ('pav_maxx', 'pav_maxy') y as� poder interpolar");
				System.out.println("  Nota 6: La tabla de valores �nicos '" + tablaValoresUnicos + "' debe poseer el siguiente campo tipo FLOAT 'lleno' con valores de amenaza: '" + campoTVU_X +"'. Si en esta tabla el campo '" + campoTVU_Y+ "' existe se sobreescribir�, si no se crear�");
				System.out.println("  Nota 7: Importante VERIFIQUE que la curva de vulnerabilidd con PAV_ID = '" + idCurva + "', tenga las mismas unidades en X, que las unidades de la amenaza almacenada en el campo '" + campoTVU_X + "' de la tabla '" + tablaValoresUnicos + "'");
				System.out.println("  Nota 8: Los valores de da�o corresponden el eje Y de las curvas de vulnerabilidad almacenados, calculados por f�rmula o interpolados por los puntos de la curva almacenados en 'DAV_DATOS_VULN'");
				System.out.println("  Nota 9: Aseg�rese que la base de datos no est� bloqueada por ArcGIS!");

				//String nombreFetureClass = tokens[5];
				//System.out.println("  6- nombreFetureClass = '" + nombreFetureClass + "'");
				//String campoFC_X = tokens[6];
				//System.out.println("  7- campoFC_X = '" + campoFC_X + "'");
				//String campoFC_Y = tokens[7];
				//System.out.println("  8- campoFC_Y = '" + campoFC_Y + "'");
				ucv.InterpolarCurvaAsignarMapa(rutaGDB, idCurva, tablaValoresUnicos, campoTVU_X, 
						campoTVU_Y, campoCorreccionMedia);
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
