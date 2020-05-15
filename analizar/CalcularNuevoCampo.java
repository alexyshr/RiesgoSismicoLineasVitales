package analizar;


import com.esri.arcgis.geodatabase.Field;
import com.esri.arcgis.geodatabase.IDataset;
import com.esri.arcgis.geodatabase.IDatasetProxy;
import com.esri.arcgis.geodatabase.IFeatureClass;
import com.esri.arcgis.geodatabase.ITable;
import com.esri.arcgis.geodatabase.Workspace;
import com.esri.arcgis.geodatabase.esriDatasetType;
import com.esri.arcgis.geoprocessing.GeoProcessor;
import com.esri.arcgis.geoprocessing.tools.datamanagementtools.CalculateField;
import com.esri.arcgis.geoprocessing.tools.datamanagementtools.MakeTableView;


import conectar.geodatabase.AbrirFile_o_Personal_Geodatabase;

public class CalcularNuevoCampo {
	private GeoProcessor gp = null;	
	public CalcularNuevoCampo(){
		try {
			gp = new GeoProcessor();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public String Calcular (String rutaGDB, String tabla_o_FC, 
			String campoMMI, int tipoCampo, int precisionCampo, int scaleCampo, int longitudCampo, 
			String codeBlock, String expression, String expressionType, String ClausulaWhereFiltro ){
		//El objeto GP se crea en el constructor
		try {
			AbrirFile_o_Personal_Geodatabase fgdb = new AbrirFile_o_Personal_Geodatabase();
			Workspace workspace = fgdb.openFile_o_PersonalWorkspace(rutaGDB);	
			ITable tablaFC = (ITable) workspace.openTable(tabla_o_FC);			
			if (tablaFC == null){
				throw new IllegalStateException("La tabla o FC: '" + tabla_o_FC + "' no existe!!!");
			}
			//Verificar si existe el campo campoMMI en nombreFC_o_T		
			if (tablaFC.findField(campoMMI)== -1){
				System.out.println("  Se creará el campo '" + campoMMI + "' en '" + tabla_o_FC + "'");
				Field campo_MMI = new Field();
				campo_MMI.setName(campoMMI);
				if (tipoCampo == -1) {
					throw new IllegalStateException("  No está definido correctamente el parámetro Tipo de Campo : '" + tipoCampo + "'");
				}
				campo_MMI.setType(tipoCampo);
				//
				if (tipoCampo == 0 || tipoCampo == 1 ||  tipoCampo == 2  || tipoCampo == 3 ){
					if (precisionCampo == -1) {
						throw new IllegalStateException("  No está definido correctamente el parámetro Precisión de Campo : '" + precisionCampo + "'");
					} else if (scaleCampo == -1){
						throw new IllegalStateException("  No está definido correctamente el parámetro Escala de Campo : '" + scaleCampo + "'");
					}
					campo_MMI.setPrecision(precisionCampo);
					campo_MMI.setScale(scaleCampo);
				} else if (tipoCampo == 4){
					if (longitudCampo == -1) {
						throw new IllegalStateException("  No está definido correctamente el parámetro Logitud de Campo : '" + longitudCampo + "'");
					}					
					campo_MMI.setLength(longitudCampo);
				} else if (tipoCampo == 4){
					
				}else{
					throw new IllegalStateException("  Solo se soportan campos tipo 'SmallInteger: 0', 'Integer: 1', 'Single: 2', 'Double: 3', 'String: 4', 'Date: 5' (esriFieldType)");
				}				
				tablaFC.addField(campo_MMI);				
			}else{
				System.out.println("  **Se sobreescribirá el contenido del campo (el tipo debe ser compatible con el Cálculo a realizar) '" + campoMMI + "' existente en '" + tabla_o_FC + "'");
			}
			
			IDataset ds = new IDatasetProxy(tablaFC);
			
			String rutatabla_o_FC =  rutaGDB + "/" + tabla_o_FC;
			
			if (ds.getType() == esriDatasetType.esriDTFeatureClass){
				IFeatureClass fClass =  (IFeatureClass) workspace.openFeatureClass(tabla_o_FC);
				if (fClass.getFeatureDataset() != null){
					String nombre_fd = "" + fClass.getFeatureDataset().getName();
					rutatabla_o_FC =  rutaGDB + "/" + nombre_fd + "/" + tabla_o_FC;
				}
			}			 			
			   
			//IName a = tablaFC.getFullName();
			//a.getNameString();
			//FeatureDatasetName fdn = (FeatureDatasetName) a;
			//fdn.getName();
						
			// Overwrite any previous output data
			gp.setOverwriteOutput(true);
			MakeTableView tablaView = new MakeTableView();
			tablaView.setInTable(rutatabla_o_FC);
			tablaView.setOutView("tabla_o_FC_View");
			
			if (!ClausulaWhereFiltro.equals("")){
				tablaView.setWhereClause(ClausulaWhereFiltro);
			}			
			
			
			ValidarGP_MensajesGP validar = new ValidarGP_MensajesGP();			
			if (validar.Validar(gp, tablaView) == true){
				System.out.println("  **** El registro actual no se procesa por tener los errores descritos con anterioridad!");
				return "  *** Proceso no exitoso!";
			}							
						
			try {
				gp.execute(tablaView, null);
				validar.returnMessagesGP(gp, tablaView.getToolName());
			} catch (Exception e){
				System.out.println("Mensaje de error: " + e.getMessage());
				e.printStackTrace();								
				validar.returnMessagesGP(gp, tablaView.getToolName());
				return "  *** Error: Proceso no exitoso!";
			}
														
			CalculateField cf = new CalculateField();
			cf.setInTable("tabla_o_FC_View");	
//			cf.setInTable("\"" + rutatabla_o_FC + "\"");
			//cf.setInTable("D:/geo/borrar.mdb/prueba");
			
			cf.setField("" + campoMMI + "");
			cf.setExpression("" + expression+ "");

			if (!expressionType.equals("")){
				cf.setExpressionType("" + expressionType+ "");
			}

			if (!codeBlock.equals("")){
				cf.setCodeBlock(codeBlock);
			}

			/*for (Object a: cf.getParameterValues()){
				if (a != null){
					System.out.println("cf = " + a.toString());	
				}				
			}*/		
			if (validar.Validar(gp, cf) == true){
				System.out.println("  **** El registro actual no se procesa por tener los errores descritos con anterioridad!");
				return "  *** Error: Proceso no exitoso!";
			}
			
			try {
				gp.execute(cf, null);			
				validar.returnMessagesGP(gp, cf.getToolName());
			} catch (Exception e){
				e.printStackTrace();								
				validar.returnMessagesGP(gp, cf.getToolName());
				return "  *** Error: Proceso no exitoso!";
			}			
		} catch (Exception e){			
			e.printStackTrace();
			return "  *** Error: Proceso no exitoso!";
		}
		// if everything is fine then return true
		return "  *** Proceso exitoso!";
	}

}

	

