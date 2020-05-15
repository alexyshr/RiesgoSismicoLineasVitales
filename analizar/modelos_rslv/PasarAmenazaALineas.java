package analizar.modelos_rslv;

import com.esri.arcgis.geoprocessing.AbstractGPTool;

/**
 * The PasarAmenazaALineas tool is contained in the Modelos_RSLV tool box.
 * ojo: Este crea primero puntos con centroides!!
 */
public class PasarAmenazaALineas extends AbstractGPTool {

	/**
	 * Creates the PasarAmenazaALineas tool with defaults.
	 * <p>
	 * Initializes the array of tool parameters with the default values specified when the tool was created.
	 */
	public PasarAmenazaALineas() {
		vals = new Object[8];
		vals[4] = "pvmaxs_200"; //nuevoCampoAmenazaFC
	}

	/**
	 * Creates the PasarAmenazaALineas tool with the required parameters.
	 * <p>
	 * Initializes the array of tool parameters with the values as specified for the required parameters and with the default values for the other parameters.
	 * @param rutaFCLassLV null
	 * @param rutaNuevoFClassCentroides null
	 * @param listaCamposEliminarFCCentroides null
	 * @param rutaRasterAmenaza null
	 * @param nuevoCampoAmenazaFC null
	 * @param rutaNuevoFCLassCentroidesAmenaza null
	 * @param expresionCalculoCampoAmenaza null
	 * @param campoAmenazaFCACalcular null
	 */
	public PasarAmenazaALineas(Object rutaFCLassLV, Object rutaNuevoFClassCentroides, Object listaCamposEliminarFCCentroides, Object rutaRasterAmenaza, String nuevoCampoAmenazaFC, Object rutaNuevoFCLassCentroidesAmenaza, Object expresionCalculoCampoAmenaza, Object campoAmenazaFCACalcular) {
		this();
		vals[0] = rutaFCLassLV; //ruta_FCLassLV
		vals[1] = rutaNuevoFClassCentroides; //rutaNuevo_FClassCentroides
		vals[2] = listaCamposEliminarFCCentroides; //ListaCamposEliminarFCCentroides
		vals[3] = rutaRasterAmenaza; //rutaRaster_Amenaza
		vals[4] = nuevoCampoAmenazaFC; //nuevoCampoAmenazaFC
		vals[5] = rutaNuevoFCLassCentroidesAmenaza; //rutaNuevoFCLassCentroides_Amenaza
		vals[6] = expresionCalculoCampoAmenaza; //Expresion_CalculoCampoAmenaza
		vals[7] = campoAmenazaFCACalcular; //campoAmenazaFC_aCalcular
	}

	/**
	 * Returns the ruta_FCLassLV parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @return	the ruta_FCLassLV
	 */
	public Object getRutaFCLassLV() {
		return vals[0];
	}

	/**
	 * Sets the ruta_FCLassLV parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @param rutaFCLassLV null
	 */
	public void setRutaFCLassLV(Object rutaFCLassLV) {
		vals[0] = rutaFCLassLV;
	}

	/**
	 * Returns the rutaNuevo_FClassCentroides parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @return	the rutaNuevo_FClassCentroides
	 */
	public Object getRutaNuevoFClassCentroides() {
		return vals[1];
	}

	/**
	 * Sets the rutaNuevo_FClassCentroides parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @param rutaNuevoFClassCentroides null
	 */
	public void setRutaNuevoFClassCentroides(Object rutaNuevoFClassCentroides) {
		vals[1] = rutaNuevoFClassCentroides;
	}

	/**
	 * Returns the ListaCamposEliminarFCCentroides parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @return	the ListaCamposEliminarFCCentroides
	 */
	public Object getListaCamposEliminarFCCentroides() {
		return vals[2];
	}

	/**
	 * Sets the ListaCamposEliminarFCCentroides parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @param listaCamposEliminarFCCentroides null
	 */
	public void setListaCamposEliminarFCCentroides(Object listaCamposEliminarFCCentroides) {
		vals[2] = listaCamposEliminarFCCentroides;
	}

	/**
	 * Returns the rutaRaster_Amenaza parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @return	the rutaRaster_Amenaza
	 */
	public Object getRutaRasterAmenaza() {
		return vals[3];
	}

	/**
	 * Sets the rutaRaster_Amenaza parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @param rutaRasterAmenaza null
	 */
	public void setRutaRasterAmenaza(Object rutaRasterAmenaza) {
		vals[3] = rutaRasterAmenaza;
	}

	/**
	 * Returns the nuevoCampoAmenazaFC parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @return	the nuevoCampoAmenazaFC
	 */
	public String getNuevoCampoAmenazaFC() {
		return vals[4] == null ? null : (String)vals[4];
	}

	/**
	 * Sets the nuevoCampoAmenazaFC parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @param nuevoCampoAmenazaFC null
	 */
	public void setNuevoCampoAmenazaFC(String nuevoCampoAmenazaFC) {
		vals[4] = nuevoCampoAmenazaFC;
	}

	/**
	 * Returns the rutaNuevoFCLassCentroides_Amenaza parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @return	the rutaNuevoFCLassCentroides_Amenaza
	 */
	public Object getRutaNuevoFCLassCentroidesAmenaza() {
		return vals[5];
	}

	/**
	 * Sets the rutaNuevoFCLassCentroides_Amenaza parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @param rutaNuevoFCLassCentroidesAmenaza null
	 */
	public void setRutaNuevoFCLassCentroidesAmenaza(Object rutaNuevoFCLassCentroidesAmenaza) {
		vals[5] = rutaNuevoFCLassCentroidesAmenaza;
	}

	/**
	 * Returns the Expresion_CalculoCampoAmenaza parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @return	the Expresion_CalculoCampoAmenaza
	 */
	public Object getExpresionCalculoCampoAmenaza() {
		return vals[6];
	}

	/**
	 * Sets the Expresion_CalculoCampoAmenaza parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @param expresionCalculoCampoAmenaza null
	 */
	public void setExpresionCalculoCampoAmenaza(Object expresionCalculoCampoAmenaza) {
		vals[6] = expresionCalculoCampoAmenaza;
	}

	/**
	 * Returns the campoAmenazaFC_aCalcular parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @return	the campoAmenazaFC_aCalcular
	 */
	public Object getCampoAmenazaFCACalcular() {
		return vals[7];
	}

	/**
	 * Sets the campoAmenazaFC_aCalcular parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @param campoAmenazaFCACalcular null
	 */
	public void setCampoAmenazaFCACalcular(Object campoAmenazaFCACalcular) {
		vals[7] = campoAmenazaFCACalcular;
	}

	/**
	 * Returns the name of this tool.
	 * 
	 * @return	the tool name
	 */
	public String getToolName() {
		return "PasarAmenazaALineas";
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
