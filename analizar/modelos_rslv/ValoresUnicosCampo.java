package analizar.modelos_rslv;

import com.esri.arcgis.geoprocessing.AbstractGPTool;

/**
 * The ValoresUnicosCampo tool is contained in the Modelos_RSLV tool box.
 */
public class ValoresUnicosCampo extends AbstractGPTool {

	/**
	 * Creates the ValoresUnicosCampo tool with defaults.
	 * <p>
	 * Initializes the array of tool parameters with the default values specified when the tool was created.
	 */
	public ValoresUnicosCampo() {
		vals = new Object[3];
	}

	/**
	 * Creates the ValoresUnicosCampo tool with the required parameters.
	 * <p>
	 * Initializes the array of tool parameters with the values as specified for the required parameters and with the default values for the other parameters.
	 * @param campoFCX null
	 * @param fClass null
	 * @param tablaValoresUnicos null
	 */
	public ValoresUnicosCampo(Object campoFCX, Object fClass, Object tablaValoresUnicos) {
		this();
		vals[0] = campoFCX; //CampoFC_X
		vals[1] = fClass; //FClass
		vals[2] = tablaValoresUnicos; //TablaValoresUnicos
	}

	/**
	 * Returns the CampoFC_X parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @return	the CampoFC_X
	 */
	public Object getCampoFCX() {
		return vals[0];
	}

	/**
	 * Sets the CampoFC_X parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @param campoFCX null
	 */
	public void setCampoFCX(Object campoFCX) {
		vals[0] = campoFCX;
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
	 * Returns the TablaValoresUnicos parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @return	the TablaValoresUnicos
	 */
	public Object getTablaValoresUnicos() {
		return vals[2];
	}

	/**
	 * Sets the TablaValoresUnicos parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @param tablaValoresUnicos null
	 */
	public void setTablaValoresUnicos(Object tablaValoresUnicos) {
		vals[2] = tablaValoresUnicos;
	}

	/**
	 * Returns the name of this tool.
	 * 
	 * @return	the tool name
	 */
	public String getToolName() {
		return "ValoresUnicosCampo";
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
