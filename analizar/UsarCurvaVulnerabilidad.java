package analizar;

import com.esri.arcgis.geodatabase.Cursor;
import com.esri.arcgis.geodatabase.Field;
import com.esri.arcgis.geodatabase.IField;
import com.esri.arcgis.geodatabase.IRow;
import com.esri.arcgis.geodatabase.QueryFilter;
import com.esri.arcgis.geodatabase.Table;
import com.esri.arcgis.geodatabase.TableSort;
import com.esri.arcgis.geodatabase.Workspace;
import com.esri.arcgis.geodatabase.esriFieldType;

import conectar.geodatabase.AbrirFile_o_Personal_Geodatabase;
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
 * <i><b>Uso de la clase 'UsarCurvaVulnerabilidad.java':</b></i>
 * -----------------
 * <i><b>Recomendación: Utilícela mejor (mas fácil) siempre a través de 'CalcularDanio.java'!!!</b></i>
 * Desde el código fuente:         
 *           <b>import analizar.UsarCurvaVulnerabilidad;</b>
 *           <b>UsarCurvaVulnerabilidad ucv = new UsarCurvaVulnerabilidad();</b>
 *           <b>ucv.InterpolarCurvaAsignarMapa(rutaGDB, idCurva, tablaValoresUnicos, campoTVU_X, campoTVU_Y);</b>
 * Desde línea de comandos (uso de binarios):
 *      Usando al archivo binario UsarCurvaVulnerabilidad.class: 
 *           <b>java analizar.UsarCurvaVulnerabilidad.InterpolarCurvaAsignarMapa(rutaGDB, idCurva, tablaValoresUnicos, campoTVU_X, campoTVU_Y);</b>
 *      Apuntando el archivo rslv.jar:
 *           <b>java -cp ./rslv.jar analizar.UsarCurvaVulnerabilidad.InterpolarCurvaAsignarMapa(rutaGDB, idCurva, tablaValoresUnicos, campoTVU_X, campoTVU_Y);</b>
 * -----------------
 * <b><i>Descripción de la clase 'UsarCurvaVulnerabilidad.java':</b></i>
 * -----------------
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
 *  <b><i>Descripción de la clase 'CalcularDanio.java':</b></i>
 *  Lee los parámetros de configuración del archivo ParametrosRepairRateCurvasVulnerabilidad.txt para
 *  ejecutar la clase UsarCurvaVulnerabilidad.java. Hace un ciclo recorriendo cada línea del archivo mencionado
 *  para ejecutar la clase UsarCurvaVulnerabilidad.java una vez por cada línea. 
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
 *            (***no tener en cuenta) 8- campoFC_Y: rr_pvmaxs_250_cv28 cambiado a danio_pvmaxs_250_cv28
 *  Nota 3: Asegúrese que la base de datos (parámetro 1) exista con las tablas correspondientes: 'PAV_PARAMETROS_VULN', 'DAV_DATOS_VULN', y la tabla de valores únicos (parámetro 3)
 *  Nota 4: Asegúrese que la tabla 'PAV_PARAMETROS_VULN', tenga para el identificador de la curva a usar: PAV_ID = '(parámetro 2)', los valores de 'pav_formula', 'pav_minx', 'pav_maxx'
 *  Nota 5: Si el campo 'pav_formula' en 'PAV_PARAMETROS_VULN' no almacena formula, se usarán los puntos de la curva en 'DAV_DATOS_VULN' para interpolar el daño. Verifique que los valores DAV_X, DAV_Y para la curva PAV_ID = '(parámetro 2)' estén almacenados en esta tabla
 *  Nota 6: Los valores de 'pav_minx', 'pav_maxx' son obligatorios en 'PAV_PARAMETROS_VULN' para la curva PAV_ID = '(parámetro 2)'
 *  Nota 7: Verifique correctamente los puntos de la curva almacenados en 'DAV_DATOS_VULN' para que cumplan con los valores mínimos ('pav_minx') y máximos ('pav_maxx') y así poder interpolar
 *  Nota 8: La tabla de valores únicos 'prueba_pvmaxs_250' debe poseer dos campos tipo FLOAT, el primero lleno con valores de amenaza: '(parámetro 4)', y el segundo para llenar con el daño: '(parámetro 5)'
 *  Nota 9: Los valores de daño corresponden el eje Y de las curvas de vulnerabilidad almacenados, calculados por fórmula o interpolados por los puntos de la curva almacenados en 'DAV_DATOS_VULN'
 *  Nota 10: Utilicela mejor siempre a través de 'CalcularDanio.java'!
 *  Nota 11: Asegúrese que la base de datos no esté bloqueada por ArcGIS!
 * </pre>
 *  @version 1.0
 *  @author Msc. Esp. Ing. Alexys H Rodríguez Avellaneda, <a href="mailto:alexyshr@gmail.com">alexyshr@gmail.com</a> - Nov 2009 - Maestría en Geomática (Universidad Nacional de Colombia)
 ***************************************************************************/
public class UsarCurvaVulnerabilidad {
	public void InterpolarCurvaAsignarMapa 
		(String rutaGdb, int curvaId, 
		 String tablaValoresUnicos, 
		 String campoTVU_X, String campoTVU_Y, String campoCorreccionMedia
		 //String nombreFeatureClass, 
		 //String campoFC_X, String campoFC_y

											) 
	{
		try{
			System.out.println("  Ingresó a UsarCurvaVulnerabilidad");
			AbrirFile_o_Personal_Geodatabase fgdb = new AbrirFile_o_Personal_Geodatabase();
			//RutaGdb = "D:/geo/geo.gdb"
			Workspace workspace = fgdb.openFile_o_PersonalWorkspace(rutaGdb);
			
			Table pTablePAV = new Table(workspace.openTable("PAV_PARAMETROS_VULN"));
			QueryFilter pQueryFilterPAV = new QueryFilter();
			//ClausulaWhere = "PAV_ID = 12"
			pQueryFilterPAV.setWhereClause("PAV_ID = " + curvaId);
			pQueryFilterPAV.setSubFields("pav_formula, pav_minx, pav_maxx, pav_miny, pav_maxy, pav_tipoformula");	    
			Cursor pCursorPAV = new Cursor(pTablePAV.ITable_search(pQueryFilterPAV, false));

		
			Table pTableDAV = new Table(workspace.openTable("DAV_DATOS_VULN"));
			//IFeatureWorkspace pFeatureWorkspace =  fgdb.openFileWorkspace("D:/geo/geo.gdb");
		    //.NET: Dim pTableDAVSort As ITableSort
		    //.NET: Set pTableDAVSort = New esriGeoDatabase.TableSort			
			TableSort pTableDAVSort = new TableSort();
			QueryFilter pQueryFilterDAV = new QueryFilter();
			//ClausulaWhere = "PAV_ID = 12"
			//ClausulaWhere = "PAV_ID = " + curvaId
			
			//En el anterior programa leia cv_id de la tabla XY_Curvas_Vulnerabilidad
			//que está en E:\\alexyshr2007\\dpae\\Redes\\EAAB\\redes.gdb
			//E:\alexyshr2007\dpae\Informacion_Geografica\redes.gdb
			//campo "cv_id = " + curvaId
				
			pQueryFilterDAV.setWhereClause("PAV_ID = " + curvaId);
			//CamposTablaXYCurva = "DAV_X, DAV_Y"
			pTableDAVSort.setFields("DAV_X, DAV_Y");
			//CampoOrdenarTablaXYCurva = "DAV_X"
			pTableDAVSort.setAscending("DAV_X", true);
			pTableDAVSort.setQueryFilterByRef(pQueryFilterDAV);
			pTableDAVSort.setTableByRef(pTableDAV);

			//.NET: pTableDAVSort.Sort Nothing
		    pTableDAVSort.sort(null);
		    Cursor pCursorDAV = new Cursor(pTableDAVSort.getRows());
		    //Otra forma de obtener el cursor sin ordenar
		    //ICursor cursor = table.ITable_search(qf, false);
		    
		    //nombreFeatureClass = "RPA_Red_Primaria_Acueducto"
		    //FeatureClass pFeatureClass = new FeatureClass(workspace.openFeatureClass(nombreFeatureClass));
		    //IFeatureClass fc = workspace.openFeatureClass(nombreFeatureClass);
		    //Table pTableFC = new Table(pFeatureClass);
		    //Table pTableFC = (Table) pFeatureClass;
		    //ITable pTableFC = (ITable) pFeatureClass;
		    
		    //Tabla con los valores unicos
		    //TablaValoresUnicos = prueba_pvmaxs_250
		    Table pTableVU = new Table(workspace.openTable(tablaValoresUnicos));
		    if (pTableVU.findField(campoTVU_Y)!= -1){
		    	System.out.println("  ***Se sobreescribirá el campo '" + campoTVU_Y + "' en la tabla '" + tablaValoresUnicos + "'");
		    	//Borro el campo
		    }else{
		    	Field campo = new Field();
		    	campo.setName(campoTVU_Y);
		    	campo.setType(esriFieldType.esriFieldTypeSingle);
		    	campo.setPrecision(4);
		    	campo.setScale(2);
		    	pTableVU.addField((IField)campo);
		    }
		    
		    if (campoCorreccionMedia.length() != 0) {
			    if (pTableVU.findField(campoCorreccionMedia)== -1){
			    	throw new IllegalStateException("      Si se pasa el parámetro 4, campo: " + campoCorreccionMedia + " , este debe existir con valores!");
			    }	
		    }
		    
		    
		    TableSort pTableVUSort = new TableSort();
		    //CampoTVU_X = "pvmaxs_250"
		    //CampoTVU_Y = "pvmaxs_250_cv28"
		    if (campoCorreccionMedia.length() != 0) {
			    pTableVUSort.setFields(campoTVU_X + ", " + campoTVU_Y+ ", " + campoCorreccionMedia);
		    }else{
		    	pTableVUSort.setFields(campoTVU_X + ", " + campoTVU_Y);
		    }


		    //campoOrdenarTablaFC = "f250"
		    //El campo por el que ordeno siempre será el X de la curva
		    pTableVUSort.setAscending(campoTVU_X, true);
		    //pTableFCSort.setQueryFilterByRef(pQueryFilterDAV);
		    pTableVUSort.setTableByRef(pTableVU);
		    pTableVUSort.sort(null);

		    //Cursor pCursorFC = (Cursor)pTableFCSort.getRows();		  
		    Cursor pCursorVU = new Cursor(pTableVUSort.getRows());
		    IRow pRowVU = null;
		    //campoOrdenarTablaFC = "f250"
		    int indexcampoTVU_X = (int) pCursorVU.findField(campoTVU_X);		    
		    int indexcampoTVU_Y = (int) pCursorVU.findField(campoTVU_Y);
		    
			Curva curva = new Curva(curvaId, pCursorDAV, pCursorPAV);
			//curva.setCurvaId();
			//curva.loadPoints();
						
			do{
				//pRowFC = new Row (pCursorFC.nextRow());
				//pRowFC = (Row) pCursorFC.nextRow();
				//pFeatureFC = (Feature) pCursorFC.nextRow();
				
				//Row pRowVU = null;
				//pRowVU = (Row) pCursorVU.nextRow(); IRowProxy cannot be cast Row
				//Vea la derecha consulta la clase proxy				
				
				//pRowVU = new Row (pCursorVU.nextRow()); el constructor row object es obsoleto
				//if ()
				//IRow pRowTemp = (IRow) pCursorVU.nextRow();
				//if (pRowVU instanceof IRow) 
				//System.out.println("pRowVU instaneceof IRow = " + (pRowVU instanceof IRow));
				pRowVU = (IRow) pCursorVU.nextRow();				
				
				//pRowVU = (Row) pRowTemp;
				
				if (pRowVU == null)
					break;
				//Double xValue = (Double) pFeatureFC.getValue(indexcampoFC_X);
				//Float xValue = (Float) pRowVU.getValue(indexcampoTVU_X);
				if (pRowVU.getValue(indexcampoTVU_X) != null) {
					Float xValue = Float.valueOf((String) pRowVU.getValue(indexcampoTVU_X)).floatValue() ;
			        /*if (xValue > 13.97) {
			        	System.out.println("hola");
			        }*/
					float danio;
					
				    if (campoCorreccionMedia.length() != 0) {
				    	int indexcampoCorreccionMedia = (int) pCursorVU.findField(campoCorreccionMedia);
				    	Float FactorCorreccionMedia = (Float) pRowVU.getValue(indexcampoCorreccionMedia);
				    	danio = curva.despejarCurva(xValue, FactorCorreccionMedia);
				    }else{
				    	danio = curva.despejarCurva(xValue);
				    }
			        
			        //System.out.println("  UsarCurvaVulnerabilidad - x = " + xValue + " y = " + reparation_rate);
			        pRowVU.setValue(indexcampoTVU_Y, danio);
			        pRowVU.store();
				}
			}while (pRowVU != null);		    
			
		}	
		catch(Exception e){
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}			
		
	}
		
}
