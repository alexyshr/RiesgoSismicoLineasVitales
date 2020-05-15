


package conectar;

import com.esri.arcgis.system.AoInitialize;
import com.esri.arcgis.system.EngineInitializer;
import com.esri.arcgis.system.esriLicenseProductCode;
import com.esri.arcgis.system.esriLicenseStatus;


public class InicioLicenciasVariables {
     private AoInitialize ao;
	/**
	 * @param args
	 * @return 
	 */	    
	public void initializeArcGISLicenses() {
		try {
			EngineInitializer.initializeEngine();
			ao = new AoInitialize();			
			if (ao.isProductCodeAvailable(esriLicenseProductCode.esriLicenseProductCodeAdvanced) 
					== esriLicenseStatus.esriLicenseAvailable)
				ao.initialize(esriLicenseProductCode.esriLicenseProductCodeAdvanced);
				ao.checkOutExtension(com.esri.arcgis.system.esriLicenseExtensionCode.esriLicenseExtensionCodeSpatialAnalyst);
		} catch (Exception e) {
			System.out.println("Mensaje de error: No hay licencia de esriLicenseProductCodeAdvanced disponible!");
			e.printStackTrace();
		}
	}
	
	public String iniciarRSLV() {
		try{
			return System.getenv("RiesgoSismicoLineasVitales");
		}			
		catch (Error e){
			System.out.println("Mensaje de error: Debe definir primero la variable de sistema 'RiesgoSismicoLineasVitales'");
		}
		return null;
	}
	public void finalizarUsoLicencias(){
		try{
			ao.shutdown();
			System.out.println("---Aplicación 'RiesgoSismicoLineasVitales' finalizada!");
		}catch(Exception ex){			
			ex.printStackTrace();
		}		
	}


}
