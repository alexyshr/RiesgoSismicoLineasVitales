package analizar;

import java.io.IOException;
import java.util.ArrayList;
import com.esri.arcgis.geodatabase.Field;
import com.esri.arcgis.geodatabase.IRowBuffer;
import com.esri.arcgis.geodatabase.QueryFilter;
import com.esri.arcgis.geodatabase.Table;
import com.esri.arcgis.geodatabase.Workspace;
import com.esri.arcgis.geodatabase.esriFieldType;
import com.esri.arcgis.interop.AutomationException;

import conectar.geodatabase.AbrirFile_o_Personal_Geodatabase;

public class ConvertirCodigo_a_FactorCorreccion {
	public void PasarAtributo(String rutaGDB, String nombreFC_o_T, String CampoCodigoFC_o_T, String CampoFactorCorreccion, int codigoTablaFactorCorreccion, int valorCodigoFactorCorrecionCastigo) throws AutomationException, IOException{
		try {
			AbrirFile_o_Personal_Geodatabase fgdb = new AbrirFile_o_Personal_Geodatabase();
			Workspace workspace = fgdb.openFile_o_PersonalWorkspace(rutaGDB);	
			Table tablaFC = new Table(workspace.openTable(nombreFC_o_T));
			/*
			 * if (tablaFC == null){ throw new IllegalStateException("La tabla o FC: '" +
			 * nombreFC_o_T + "' no existe!!!"); }
			 */
	
			//Verificar si existe el campo CampoCodigoFC_o_T en tablaFC		
			if (tablaFC.findField(CampoCodigoFC_o_T)== -1){
				throw new IllegalStateException("El campo '" + CampoCodigoFC_o_T + "' no existe en el FClass '" + nombreFC_o_T);
			}
			if (tablaFC.findField(CampoFactorCorreccion)== -1){
				System.out.println("  Se creará el campo '" + CampoFactorCorreccion + "' (tipo FLOAT) en '" + nombreFC_o_T + "'");
				Field campo_codigo_lineas = new Field();
				campo_codigo_lineas.setName(CampoFactorCorreccion);
				campo_codigo_lineas.setType(esriFieldType.esriFieldTypeSingle);
				campo_codigo_lineas.setPrecision(2);
				campo_codigo_lineas.setScale(2);
				tablaFC.addField(campo_codigo_lineas);				
			}else{
				System.out.println("  **Se sobreescribirá el contenido del campo '" + CampoCodigoFC_o_T + "' existente en '" + nombreFC_o_T + "'");
			}
			
			ArregloValoresUnicos avu_mapa = new ArregloValoresUnicos(rutaGDB,nombreFC_o_T,CampoCodigoFC_o_T);
			FactoresCorreccionTuberias fct = new FactoresCorreccionTuberias(rutaGDB,codigoTablaFactorCorreccion);
			//ArrayList<String []> factoresCorreccionSet = fct.getFactoresCoreccionSet();
			ArrayList<String> valoresUnicosSet = avu_mapa.getvaloresUnicosSet();
			for (String a: valoresUnicosSet){
				//En nuestro caso los valores de CampoCodigoLineas deben ser enteros
				Integer codigoLinea = null;
				if (a.equals("null")){
					codigoLinea = valorCodigoFactorCorrecionCastigo;
					System.out.println("  ***Los valores nulos en '"+ CampoCodigoFC_o_T + "' de ' "+ nombreFC_o_T + "' serán tomados con el código de castigo: 'factores_correccion_valor.fcv_codigo' = '"+ valorCodigoFactorCorrecionCastigo + "'");
				}else{
					codigoLinea = new Integer (a);	
				}
				
				float factorCorreccion = fct.despejarFactorCorreccion(codigoLinea);
				QueryFilter qf = new QueryFilter();
				if (a.equals("null")){
					qf.setWhereClause(CampoCodigoFC_o_T + " IS NULL");
				} else{
					qf.setWhereClause(CampoCodigoFC_o_T + " = " + codigoLinea);
				}
								
				qf.setSubFields(CampoFactorCorreccion);
				IRowBuffer irb = tablaFC.createRowBuffer();			
				irb.setValue(irb.getFields().findField(CampoFactorCorreccion), factorCorreccion);
				tablaFC.updateSearchedRows(qf, irb);
			}
		}catch (AutomationException ax) {
			System.out.println(ax.getMessage());
		}
		
	}

}
