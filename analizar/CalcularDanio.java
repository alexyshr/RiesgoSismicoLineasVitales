package analizar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
 * <i><b>Uso de la clase 'CalcularDanio.java':</b></i>
 * -----------------
 * Desde el código fuente:         
 *           <b>import analizar.CalcularDanio;</b>
 *           <b>CalcularDanio.main(null);</b>
 * Desde línea de comandos (uso de binarios):
 *      Usando al archivo binario CalcularDanio.class: 
 *           <b>java analizar.CalcularDanio</b>
 *      Apuntando el archivo rslv.jar:
 *           <b>java -cp ./rslv.jar analizar.CalcularDanio</b>
 * -----------------
 * <b><i>Descripción de la clase 'CalcularDanio.java':</b></i>
 * -----------------
 *  Lee los parámetros de configuración del archivo ParametrosRepairRateCurvasVulnerabilidad.txt para
 *  ejecutar la clase UsarCurvaVulnerabilidad.java. Hace un ciclo recorriendo cada línea del archivo mencionado
 *  para ejecutar la clase UsarCurvaVulnerabilidad.java una vez por cada línea. 
 *  <b><i>Descripción de la clase 'UsarCurvaVulnerabilidad.java':</b></i>
 *  Es inicializada por la clase 'CalcularDanio.java', pasándole cinco parámetros contenidos en cada línea
 *  del archivo ParametrosRepairRateCurvasVulnerabilidad.txt. Ver los parámetros mas adelante.
 *  <b><i>Su objetivo</b></i> es poblar los valores de daño -- eje Y de una curva de vulnerabilidad específica 
 *  seleccionada a partir de su identificador <b><i>(parámetro 2)</b></i>-- en el campo correspondiente <b><i>(parámetro 5)</b></i> 
 *  de una tabla con valores únicos de amenaza <b><i>(parámetro 3)</b></i>, a partir de los valores únicos de amenaza 
 *  almacenados en otro campo de esa tabla <b><i>(parámetro 4)</b></i>. 
 *  En la <b><i>File Geodatabase</b></i> descrita por otro parámetro (parámetro 1) deben existir:
 *    - Una tabla llamada <b><i>'PAV_PARAMETROS_VULN'</b></i> con los parámetros de las curvas de vulnerabilidad de acuerdo con el modelo de datos
 *    - La tabla <b><i>'PAV_PARAMETROS_VULN'</b></i> debe tener los campos: <b><i>PAV_ID, pav_formula, pav_minx, pav_maxx</b></i>. El único campo no obligatorio es 'formula'
 *    - Otra tabla llamada <b><i>'DAV_DATOS_VULN'</b></i> con los datos X, Y de la correspondiente curva de vulnerabilidad identificada en la tabla 'PAV_PARAMETROS_VULN'
 *    - La tabla 'DAV_DATOS_VULN' debe tener los campos: <b><i>PAV_ID, DAV_X, DAV_Y</b></i>. No son obligatorios solo si existe valor para 'formula' en la tabla 'PAV_PARAMETROS_VULN'.
 *  <b><i>Importante:</b></i>
 *    - El identificador de la curva <b><i>(parámetro 2)</b></i> corresponde al campo <b><i>PAV_ID</b></i> de las tablas 'PAV_PARAMETROS_VULN', 'DAV_DATOS_VULN'
 *    - La tabla de valores únicos de amenaza (parámetro 3) debe existir en <b><i>la base de datos</b></i> descrita por el parámetro 1
 *    - Los valores únicos de amenaza (parámetro 4) almacenados en la tabla (parámetro 3) deben servir para la curva de vulnerabilidad seleccionada
 *    - En esta rutina no se lleva el daño al Feature Class de líneas vitales. Solo se almacena el daño en la tabla de valores únicos (parámetro 3)
 *    - La curva se arma y manipula en otra clase llamada <b><i>'Curva.java'</b></i>. Si <b><i>hay 'formula'</b></i> esta se utiliza para calcular el daño con la clase <b><i>'MathEvaluator.java'</b></i>
 *    - Si <b><i>no hay 'formula'</b></i> la clase 'Curva.java' llama a la clase </b></i>'Interpolador.java'</b></i> para usar los puntos de la tabla 'DAV_DATOS_VULN' e interpolar el daño.
 *    - Esta clase no utiliza modelos de geoprocesamiento
 * -----------------
 * <b><i>Notas para un correcto funcionamiento:</b></i>
 * -----------------
 *  Nota 1: La variable de sistema 'RiesgoSismicoLineasVitales' debe definir la ruta a la carpeta que contiene el aplicativo. Ejemplo:
 *            RiesgoSismicoLineasVitales = D:\geo\
 *  Nota 2: El archivo de texto de parámetros debe estar en %RiesgoSismicoLineasVitales%\archivos_configuracion_clases_java\ParametrosRepairRateCurvasVulnerabilidad.txt
 *            Toda línea en ese archivo de texto que inicie por (quitando las comillas) ' *', '/*' o '//' es un comentario
 *            No puede existir una línea vacía al final del archivo de texto
 *            Todos los parámetros de la ejecución de la clase van en una sola línea
 *            Coloque tantas líneas como corridas o ejecuciones de la clase UsarCurvaVulnerabilidad.java desee (la clase CalcularDanio.java itera leyendo los parámetros de cada línea y ejecutando UsarCurvaVulnerabilidad.java una vez por cada línea)
 *            Cada parámetro en la línea va separado por punto y coma y un espacio. Ejemplo: '; '
 *            Espacios adicionales o sobrantes entre parámetros causan que el modelo falle
 *            Los parámetros que son lista se separan por espacios sencillos. Ejemplo: '; campo1 campo2 campo3'
 *          Los parámetros en cada línea del archivo de parámetros son los siguientes:
 *            1- rutaGDB: Ruta a la File Geodatabase conteniendo las tablas 'PAV_PARAMETROS_VULN' y 'DAV_DATOS_VULN'
 *            2- idCurva: Identificador de una curva correspondiente el campo 'PAV_ID' de las tablas anteriores
 *            3- tablaValoresUnicos: Una tabla almacenada en la File Geodatabase del parámetro 1. Con dos campos tipo FLOAT, uno lleno con valores de amenaza, y otro vacío para almacenar valores de daño. Esta tabla se puede crear con 'CrearTablaValoresUnicosCampo.java'
 *            4- campoTVU_X: Nombre de campo existente (tipo FLOAT) almacenando en la tabla del parámetro 3, los valores de amenaza (debe estar lleno, es decir con valores)
 *            5- campoTVU_Y: Nombre de campo existente (tipo FLOAT) para almacenar en la tabla del parámetro 3, los valores de daño (si está lleno estos valores se recalculan)
 *            (***no tener en cuenta) 6- nombreFetureClass: prueba
 *            (***no tener en cuenta) 7- campoFC_X: pvmaxs_250
 *            (***no tener en cuenta) 8- campoFC_Y: rr_pvmaxs_250_cv28
 *  Nota 3: Asegúrese que la base de datos (parámetro 1) exista con las tablas correspondientes: 'PAV_PARAMETROS_VULN', 'DAV_DATOS_VULN', y la tabla de valores únicos (parámetro 3)
 *  Nota 4: Asegúrese que la tabla 'PAV_PARAMETROS_VULN', tenga para el identificador de la curva a usar: PAV_ID = '(parámetro 2)', los valores de 'pav_formula', 'pav_minx', 'pav_maxx'
 *  Nota 5: Si el campo 'pav_formula' en 'PAV_PARAMETROS_VULN' no almacena formula, se usarán los puntos de la curva en 'DAV_DATOS_VULN' para interpolar el daño. Verifique que los valores DAV_X, DAV_Y para la curva PAV_ID = '(parámetro 2)' estén almacenados en esta tabla
 *  Nota 6: Los valores de 'pav_minx', 'pav_maxx' son obligatorios en 'PAV_PARAMETROS_VULN' para la curva PAV_ID = '(parámetro 2)'
 *  Nota 7: Verifique correctamente los puntos de la curva almacenados en 'DAV_DATOS_VULN' para que cumplan con los valores mínimos ('pav_minx') y máximos ('pav_maxx') y así poder interpolar
 *  Nota 8: La tabla de valores únicos 'prueba_pvmaxs_250' debe poseer dos campos tipo FLOAT, el primero lleno con valores de amenaza: '(parámetro 4)', y el segundo para llenar con el daño: '(parámetro 5)'
 *  Nota 9: Los valores de daño corresponden el eje Y de las curvas de vulnerabilidad almacenados, calculados por fórmula o interpolados por los puntos de la curva almacenados en 'DAV_DATOS_VULN'
 *  Nota 10: Asegúrese que la base de datos no esté bloqueada por ArcGIS!
 * </pre>
 *  @version 1.0
 *  @author Msc. Esp. Ing. Alexys H Rodríguez Avellaneda, <a href="mailto:alexyshr@gmail.com">alexyshr@gmail.com</a> - Nov 2009 - Maestría en Geomática (Universidad Nacional de Colombia)
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
			System.out.println("Archivo de Parámetros = ");
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
					//System.out.println("La línea '" + i + "' es un comentario. No se tiene en cuenta!");
					continue;
				}
				System.out.println("***Inicia Ejecución (NO GP) de la línea: '"+ i + "'");
				System.out.println("  " + line);					
				String [] tokens = line.split("; ");
				String rutaGDB = tokens[0];
				if (tokens.length != 5){
					System.out.println("  Error en la línea : '" + i + "'. Debería tener 5 parámetros y tiene '" + tokens.length + "'" );
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
				System.out.println("  Nota 1: Asegúrese que '" + rutaGDB + "' exista con las tablas correspondientes: 'PAV_PARAMETROS_VULN', 'DAV_DATOS_VULN', y la tabla de valores únicos '" + tablaValoresUnicos + "'");
				System.out.println("  Nota 2: Asegúrese que la tabla 'PAV_PARAMETROS_VULN', tenga para el identificador de la curva a usar: PAV_ID = '" + idCurva + "', los valores de 'pav_formula', 'pav_minx', 'pav_maxx', 'pav_miny', 'pav_maxy'");
				System.out.println("  Nota 3: Si el campo 'pav_formula' en 'PAV_PARAMETROS_VULN' no almacena una formula, se usarán los puntos de la curva en 'DAV_DATOS_VULN' para interpolar el daño. Verifique que los valores DAV_X, DAV_Y para la curva PAV_ID = '" + idCurva + "' estén almacenados en esta tabla");
				System.out.println("  Nota 4: Los valores de 'pav_minx', 'pav_maxx', 'pav_miny', 'pav_maxy' son obligatorios en 'PAV_PARAMETROS_VULN' para la curva PAV_ID = '"+ idCurva+"'");
				System.out.println("  Nota 5: Verifique correctamente los puntos de la curva almacenados en 'DAV_DATOS_VULN' para que cumplan con los valores mínimos ('pav_minx', 'pav_miny') y máximos ('pav_maxx', 'pav_maxy') y así poder interpolar");
				System.out.println("  Nota 6: La tabla de valores únicos '" + tablaValoresUnicos + "' debe poseer el siguiente campo tipo FLOAT 'lleno' con valores de amenaza: '" + campoTVU_X +"'. Si en esta tabla el campo '" + campoTVU_Y+ "' existe se sobreescribirá, si no se creará");
				System.out.println("  Nota 7: Importante VERIFIQUE que la curva de vulnerabilidd con PAV_ID = '" + idCurva + "', tenga las mismas unidades en X, que las unidades de la amenaza almacenada en el campo '" + campoTVU_X + "' de la tabla '" + tablaValoresUnicos + "'");
				System.out.println("  Nota 8: Los valores de daño corresponden el eje Y de las curvas de vulnerabilidad almacenados, calculados por fórmula o interpolados por los puntos de la curva almacenados en 'DAV_DATOS_VULN'");
				System.out.println("  Nota 9: Asegúrese que la base de datos no esté bloqueada por ArcGIS!");

				//String nombreFetureClass = tokens[5];
				//System.out.println("  6- nombreFetureClass = '" + nombreFetureClass + "'");
				//String campoFC_X = tokens[6];
				//System.out.println("  7- campoFC_X = '" + campoFC_X + "'");
				//String campoFC_Y = tokens[7];
				//System.out.println("  8- campoFC_Y = '" + campoFC_Y + "'");
				ucv.InterpolarCurvaAsignarMapa(rutaGDB, idCurva, tablaValoresUnicos, campoTVU_X, 
						campoTVU_Y, campoCorreccionMedia);
				System.out.println("  //////////El proceso ha terminado para la línea: '" + i + "'");
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
