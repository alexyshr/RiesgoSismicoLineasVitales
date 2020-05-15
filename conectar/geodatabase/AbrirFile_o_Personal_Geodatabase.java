package conectar.geodatabase;

import java.io.IOException;
import java.net.UnknownHostException;

import com.esri.arcgis.datasourcesGDB.AccessWorkspaceFactory;
import com.esri.arcgis.datasourcesGDB.FileGDBWorkspaceFactory;
import com.esri.arcgis.geodatabase.Workspace;
/*import com.esri.arcgis.system.AoInitialize;
import com.esri.arcgis.system.EngineInitializer;
import com.esri.arcgis.system.esriLicenseProductCode;
import com.esri.arcgis.system.esriLicenseStatus;*/


public class AbrirFile_o_Personal_Geodatabase {

	//Constructor
	/*public AbrirFileGeodatabase(){
		initializeArcGISLicenses();
	}*/
	
	public Workspace openFile_o_PersonalWorkspace(String connString){
		try {
			
			AbrirFile_o_Personal_Geodatabase app = new AbrirFile_o_Personal_Geodatabase();
			Workspace workspace = app.getWorkspace(connString);
			//De la clase paso a la interfaz
			//IWorkspace iworkspace = (Workspace) workspace;			
			//return (Workspace) app.getWorkspace(connString);
			return workspace;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*try{
			new AoInitialize().shutdown();
		}catch(Exception ex){
				ex.printStackTrace();
		}*/		
		return null;
	}
	private Workspace getWorkspace(String connString) throws UnknownHostException, IOException {		
		//VBA: Dim pWorkspaceFactory As IWorkspaceFactory
		//VBA: Set pWorkspaceFactory = New FileGDBWorkspaceFactory
		if (connString.contains(".gdb")){
			FileGDBWorkspaceFactory pWorkspaceFactory = new FileGDBWorkspaceFactory();
			//VBA: Dim pWS As IWorkspace
			//VBA: Set pWS = pWorkspaceFactory.OpenFromFile(connString, 0)
			return (Workspace) pWorkspaceFactory.openFromFile(connString, 0);
		}else if (connString.contains(".mdb")){
			AccessWorkspaceFactory pWorkspaceFactory = new AccessWorkspaceFactory();
			return (Workspace) pWorkspaceFactory.openFromFile(connString, 0);
		}else{
	    	throw new IllegalStateException("Este proceso solo funciona con datos en Personal o File Geodatabase!!");
	    }			    					 				
	}	

	/*static void initializeArcGISLicenses() {
		try {
			EngineInitializer.initializeEngine();
			AoInitialize ao = new AoInitialize();
			if (ao.isProductCodeAvailable(esriLicenseProductCode.esriLicenseProductCodeArcInfo) == esriLicenseStatus.esriLicenseAvailable)
				ao.initialize(esriLicenseProductCode.esriLicenseProductCodeArcInfo);
			//ao.checkOutExtension(com.esri.arcgis.system.esriLicenseExtensionCode.esriLicenseExtensionCodeSpatialAnalyst);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}
