package analizar.modelos_rslv;

import com.esri.arcgis.geoprocessing.AbstractGPTool;

/**
 * The UnirTablaValoresUnicosaFC tool is contained in the Modelos_RSLV tool box.
 */
public class UnirTablaValoresUnicosaFC extends AbstractGPTool {

	/**
	 * Creates the UnirTablaValoresUnicosaFC tool with defaults.
	 * <p>
	 * Initializes the array of tool parameters with the default values specified when the tool was created.
	 */
	public UnirTablaValoresUnicosaFC() {
		vals = new Object[7];
		vals[0] = "pvmaxs_250_cv28"; //campoNuevoFC
	}

	/**
	 * Creates the UnirTablaValoresUnicosaFC tool with the required parameters.
	 * <p>
	 * Initializes the array of tool parameters with the values as specified for the required parameters and with the default values for the other parameters.
	 * @param campoNuevoFC null
	 * @param fClass null
	 * @param tablaUnir null
	 * @param campoFCJoin null
	 * @param campoTablaUnirJoin null
	 * @param expresionCalculoFC null
	 * @param campoNuevoFC2 null
	 */
	public UnirTablaValoresUnicosaFC(String campoNuevoFC, Object fClass, Object tablaUnir, Object campoFCJoin, Object campoTablaUnirJoin, Object expresionCalculoFC, Object campoNuevoFC2) {
		this();
		vals[0] = campoNuevoFC; //campoNuevoFC
		vals[1] = fClass; //FClass
		vals[2] = tablaUnir; //tablaUnir
		vals[3] = campoFCJoin; //campoFCJoin
		vals[4] = campoTablaUnirJoin; //campoTablaUnirJoin
		vals[5] = expresionCalculoFC; //ExpresionCalculoFC
		vals[6] = campoNuevoFC2; //campoNuevoFC2
	}

	/**
	 * Returns the campoNuevoFC parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @return	the campoNuevoFC
	 */
	public String getCampoNuevoFC() {
		return vals[0] == null ? null : (String)vals[0];
	}

	/**
	 * Sets the campoNuevoFC parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @param campoNuevoFC null
	 */
	public void setCampoNuevoFC(String campoNuevoFC) {
		vals[0] = campoNuevoFC;
	}

	/**
	 * Returns the FClass parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @return	the FClass
	 */
	public Object getFClass() {
		return vals[1];
	}

	/**
	 * Sets the FClass parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @param fClass null
	 */
	public void setFClass(Object fClass) {
		vals[1] = fClass;
	}

	/**
	 * Returns the tablaUnir parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @return	the tablaUnir
	 */
	public Object getTablaUnir() {
		return vals[2];
	}

	/**
	 * Sets the tablaUnir parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @param tablaUnir null
	 */
	public void setTablaUnir(Object tablaUnir) {
		vals[2] = tablaUnir;
	}

	/**
	 * Returns the campoFCJoin parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @return	the campoFCJoin
	 */
	public Object getCampoFCJoin() {
		return vals[3];
	}

	/**
	 * Sets the campoFCJoin parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @param campoFCJoin null
	 */
	public void setCampoFCJoin(Object campoFCJoin) {
		vals[3] = campoFCJoin;
	}

	/**
	 * Returns the campoTablaUnirJoin parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @return	the campoTablaUnirJoin
	 */
	public Object getCampoTablaUnirJoin() {
		return vals[4];
	}

	/**
	 * Sets the campoTablaUnirJoin parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @param campoTablaUnirJoin null
	 */
	public void setCampoTablaUnirJoin(Object campoTablaUnirJoin) {
		vals[4] = campoTablaUnirJoin;
	}

	/**
	 * Returns the ExpresionCalculoFC parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @return	the ExpresionCalculoFC
	 */
	public Object getExpresionCalculoFC() {
		return vals[5];
	}

	/**
	 * Sets the ExpresionCalculoFC parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @param expresionCalculoFC null
	 */
	public void setExpresionCalculoFC(Object expresionCalculoFC) {
		vals[5] = expresionCalculoFC;
	}

	/**
	 * Returns the campoNuevoFC2 parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @return	the campoNuevoFC2
	 */
	public Object getCampoNuevoFC2() {
		return vals[6];
	}

	/**
	 * Sets the campoNuevoFC2 parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @param campoNuevoFC2 null
	 */
	public void setCampoNuevoFC2(Object campoNuevoFC2) {
		vals[6] = campoNuevoFC2;
	}

	/**
	 * Returns the name of this tool.
	 * 
	 * @return	the tool name
	 */
	public String getToolName() {
		return "UnirTablaValoresUnicosaFC";
	}

	/**
	 * Returns the name of the tool box containing this tool.
	 * 
	 * @return	the tool box name
	 */
	public String getToolboxName() {
		return "Modelos_RSLV";
	}

	/**
	 * Returns the alias of the tool box containing this tool.
	 * 
	 * @return	the tool box alias
	 */
	public String getToolboxAlias() {
		return "";
	}
}
