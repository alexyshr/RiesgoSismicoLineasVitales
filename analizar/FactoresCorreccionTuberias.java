package analizar;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import com.esri.arcgis.geodatabase.ICursor;
import com.esri.arcgis.geodatabase.IRow;
import com.esri.arcgis.geodatabase.ITable;
import com.esri.arcgis.geodatabase.QueryFilter;
import com.esri.arcgis.geodatabase.Workspace;
import conectar.geodatabase.AbrirFile_o_Personal_Geodatabase;

/**
 * @author     user
 */
public class FactoresCorreccionTuberias {
	private Integer factoresCorreccionTuberiasId;
	/**
	 */
	private String nombre;	
	/**
	 */
	private String descripcion;
	/**
	 */
	private Short tipoFactorCorreccion;
	private Workspace workspace;
	//private TreeSet<Point> pointSet;
	//private TreeSet<String[]> factoresCoreccionSet;
	//private String[][] unArreglo;
	/**
	 */
	private ArrayList<String[]> factoresCoreccionSet; 
	public FactoresCorreccionTuberias(){	
	}
	public FactoresCorreccionTuberias(String RutaGDB, Integer fct_id1){
		factoresCorreccionTuberiasId = fct_id1;
		AbrirFile_o_Personal_Geodatabase fgdb = new AbrirFile_o_Personal_Geodatabase();
		//RutaGdb = "D:/geo/geo.gdb"
		workspace = fgdb.openFile_o_PersonalWorkspace(RutaGDB);		
		//Leo los datos de la tabla factores_correccion_tuberias filtrados por el id anterior
		loadFactorCorreccion();
		//Cargo los valores del factor de corrección en un Array List
		loadValoresFactorCorreccion();
	}

	public float despejarFactorCorreccion(int codigo)throws IllegalArgumentException{
		//Iterator<String[]> i = factoresCoreccionSet.iterator();
		//int cuentaColumnas = factoresCoreccionSet.size();	    	    
		boolean codigovalido = false;
	    float factorCorreccionFlotante = 0;
			for (String[] a: factoresCoreccionSet){
				//int columna=0; columna < cuentaColumnas; columna++) {
				//int cuentaFilas = unArreglo[columna].length;
				//for (int j=0; j < cuentaFilas; j++) {
					//Creamos un objeto del tipo entero 
					Integer codigoSet = new Integer (a[0]);
					//Extraemos el valor y lo asignamos a una variable entera
					int codigoSetEntero = codigoSet.intValue();
					if (codigoSetEntero == codigo){	
						codigovalido = true;
						//leo el factor de correccion que esta en la posicion 1
						Float factorCorreccion = new Float (a[1]);
						factorCorreccionFlotante = factorCorreccion.floatValue();
						break;						
					}				
			     //}
			}
			//while(i.hasNext()){
				//String[] unArreglo;
				//unArreglo = i.next();
				//El codigo esta en la posicion 0
				//Cast
				//Creamos un objeto del tipo entero 
				//Integer codigoSet = new Integer (unArreglo[0]);
				
				//Extraemos el valor y lo asignamos a una variable entera
				//int codigoSetEntero = codigoSet.intValue();
			
				//castign de integer  a stting
				//String a = "" + valorEntero;
				//Casting de string a integer
				//int 5 = new Integer(valorString);

				//if (codigoSetEntero == codigo){	
				//	codigovalido = true;
					//leo el factor de correccion que esta en la posicion 1
				//	Float factorCorreccion = new Float (unArreglo[1]);
				//	factorCorreccionFlotante = factorCorreccion.floatValue();															
				//}
			//}
			
			if (codigovalido == false)
				throw new IllegalArgumentException("El valor de X: '" + codigo + "' no está en el dominio de Factores de Corrección.");

		return factorCorreccionFlotante;
	}

	public void loadFactorCorreccion(){
		try {								
			ITable table =  workspace.openTable("factores_correccion_tuberias");
			QueryFilter qf = new QueryFilter();
			qf.setWhereClause("fct_id = " + factoresCorreccionTuberiasId);
			ICursor cursor = table.ITable_search(qf, false);
			setNombre("");
			setDescripcion("");
			setTipoFactorCorreccion(null);
			IRow row = null;
			//pointSet = new TreeSet<Point> ();
			do{
				row = cursor.nextRow();
				if (row == null)
					break;
				int indiceCampo = cursor.findField("fct_nombre");
				setNombre((String)row.getValue(indiceCampo));
				
				indiceCampo = cursor.findField("fct_descripcion2");
				setDescripcion((String)row.getValue(indiceCampo));
				
				indiceCampo = cursor.findField("fct_tipo");
				setTipoFactorCorreccion((Short) row.getValue(indiceCampo));				
			}while (row != null);						
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void loadValoresFactorCorreccion(){
		try {						
			ITable table =  workspace.openTable("factores_correccion_valor");
			QueryFilter qf = new QueryFilter();
			String a = "fct_id = " + factoresCorreccionTuberiasId;			
			qf.setWhereClause(a);
			ICursor cursor = table.ITable_search(qf,false);
			IRow row = null;
			factoresCoreccionSet = new ArrayList<String[]>();			
			do{
				row = cursor.nextRow();
				if (row == null)
					break;				                
				String [] unArreglo = new String[3];				
				int indiceCampo = cursor.findField("fcv_codigo");
				String valor = "" + row.getValue(indiceCampo);
				
				unArreglo[0] = valor;
				
				indiceCampo = cursor.findField("fcv_factor");
				valor = "" + row.getValue(indiceCampo);
				unArreglo[1] = valor;
				
				indiceCampo = cursor.findField("fcv_descripcion2");
				valor = "" + row.getValue(indiceCampo);				
				unArreglo[2] = valor;				
				factoresCoreccionSet.add(unArreglo);							
			}while (row != null);						
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	/**
	 * @return
	 * @uml.property  name="factoresCoreccionSet"
	 */
	public ArrayList<String[]> getFactoresCoreccionSet() {
		return factoresCoreccionSet;
	}
	/**
	 * @param  factoresCoreccionSet
	 * @uml.property  name="factoresCoreccionSet"
	 */
	public void setFactoresCoreccionSet(ArrayList<String[]> factoresCoreccionSet) {
		this.factoresCoreccionSet = factoresCoreccionSet;
	}
	/**
	 * @param  tipoFactorCorreccion
	 * @uml.property  name="tipoFactorCorreccion"
	 */
	public void setTipoFactorCorreccion(Short tipoFactorCorreccion) {
		this.tipoFactorCorreccion = tipoFactorCorreccion;
	}
	/**
	 * @return
	 * @uml.property  name="tipoFactorCorreccion"
	 */
	public Short getTipoFactorCorreccion() {
		return tipoFactorCorreccion;
	}
	/**
	 * @param  descripcion
	 * @uml.property  name="descripcion"
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @return
	 * @uml.property  name="descripcion"
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param  nombre
	 * @uml.property  name="nombre"
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * @return
	 * @uml.property  name="nombre"
	 */
	public String getNombre() {
		return nombre;
	}
}
