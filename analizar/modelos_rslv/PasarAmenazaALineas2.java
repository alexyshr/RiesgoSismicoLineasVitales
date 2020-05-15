package analizar.modelos_rslv;

import com.esri.arcgis.geoprocessing.AbstractGPTool;

/**
 * The PasarAmenazaALineas2 tool is contained in the Modelos_RSLV tool box.
 * Ojo: ojo: Este NO crea puntos con centroides!!
 */
public class PasarAmenazaALineas2 extends AbstractGPTool {

	/**
	 * Creates the PasarAmenazaALineas2 tool with defaults.
	 * <p>
	 * Initializes the array of tool parameters with the default values specified when the tool was created.
	 */
	public PasarAmenazaALineas2() {
		vals = new Object[7];
		vals[2] = "pvmaxs_200"; //nuevoCampoAmenazaFC
	}

	/**
	 * Creates the PasarAmenazaALineas2 tool with the required parameters.
	 * <p>
	 * Initializes the array of tool parameters with the values as specified for the required parameters and with the default values for the other parameters.
	 * @param rutaFCLassLV null
	 * @param rutaRasterAmenaza null
	 * @param nuevoCampoAmenazaFC null
	 * @param rutaNuevoFCLassCentroidesAmenaza null
	 * @param expresionCalculoCampoAmenaza null
	 * @param campoAmenazaFCACalcular null
	 * @param rutaFClassCentroidesLV null
	 */
	public PasarAmenazaALineas2(Object rutaFCLassLV, Object rutaRasterAmenaza, String nuevoCampoAmenazaFC, Object rutaNuevoFCLassCentroidesAmenaza, Object expresionCalculoCampoAmenaza, Object campoAmenazaFCACalcular, Object rutaFClassCentroidesLV) {
		this();
		vals[0] = rutaFCLassLV; //ruta_FCLassLV
		vals[1] = rutaRasterAmenaza; //rutaRaster_Amenaza
		vals[2] = nuevoCampoAmenazaFC; //nuevoCampoAmenazaFC
		vals[3] = rutaNuevoFCLassCentroidesAmenaza; //rutaNuevoFCLassCentroides_Amenaza
		vals[4] = expresionCalculoCampoAmenaza; //Expresion_CalculoCampoAmenaza
		vals[5] = campoAmenazaFCACalcular; //campoAmenazaFC_aCalcular
		vals[6] = rutaFClassCentroidesLV; //ruta_FClassCentroidesLV
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
	 * Returns the rutaRaster_Amenaza parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @return	the rutaRaster_Amenaza
	 */
	public Object getRutaRasterAmenaza() {
		return vals[1];
	}

	/**
	 * Sets the rutaRaster_Amenaza parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @param rutaRasterAmenaza null
	 */
	public void setRutaRasterAmenaza(Object rutaRasterAmenaza) {
		vals[1] = rutaRasterAmenaza;
	}

	/**
	 * Returns the nuevoCampoAmenazaFC parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @return	the nuevoCampoAmenazaFC
	 */
	public String getNuevoCampoAmenazaFC() {
		return vals[2] == null ? null : (String)vals[2];
	}

	/**
	 * Sets the nuevoCampoAmenazaFC parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @param nuevoCampoAmenazaFC null
	 */
	public void setNuevoCampoAmenazaFC(String nuevoCampoAmenazaFC) {
		vals[2] = nuevoCampoAmenazaFC;
	}

	/**
	 * Returns the rutaNuevoFCLassCentroides_Amenaza parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @return	the rutaNuevoFCLassCentroides_Amenaza
	 */
	public Object getRutaNuevoFCLassCentroidesAmenaza() {
		return vals[3];
	}

	/**
	 * Sets the rutaNuevoFCLassCentroides_Amenaza parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @param rutaNuevoFCLassCentroidesAmenaza null
	 */
	public void setRutaNuevoFCLassCentroidesAmenaza(Object rutaNuevoFCLassCentroidesAmenaza) {
		vals[3] = rutaNuevoFCLassCentroidesAmenaza;
	}

	/**
	 * Returns the Expresion_CalculoCampoAmenaza parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @return	the Expresion_CalculoCampoAmenaza
	 */
	public Object getExpresionCalculoCampoAmenaza() {
		return vals[4];
	}

	/**
	 * Sets the Expresion_CalculoCampoAmenaza parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @param expresionCalculoCampoAmenaza null
	 */
	public void setExpresionCalculoCampoAmenaza(Object expresionCalculoCampoAmenaza) {
		vals[4] = expresionCalculoCampoAmenaza;
	}

	/**
	 * Returns the campoAmenazaFC_aCalcular parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @return	the campoAmenazaFC_aCalcular
	 */
	public Object getCampoAmenazaFCACalcular() {
		return vals[5];
	}

	/**
	 * Sets the campoAmenazaFC_aCalcular parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @param campoAmenazaFCACalcular null
	 */
	public void setCampoAmenazaFCACalcular(Object campoAmenazaFCACalcular) {
		vals[5] = campoAmenazaFCACalcular;
	}

	/**
	 * Returns the ruta_FClassCentroidesLV parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @return	the ruta_FClassCentroidesLV
	 */
	public Object getRutaFClassCentroidesLV() {
		return vals[6];
	}

	/**
	 * Sets the ruta_FClassCentroidesLV parameter of this tool .
	 * This is a required parameter.
	 * 
	 * @param rutaFClassCentroidesLV null
	 */
	public void setRutaFClassCentroidesLV(Object rutaFClassCentroidesLV) {
		vals[6] = rutaFClassCentroidesLV;
	}

	/**
	 * Returns the name of this tool.
	 * 
	 * @return	the tool name
	 */
	public String getToolName() {
		return "PasarAmenazaALineas2";
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
