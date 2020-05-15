package analizar;

import java.io.IOException;

import com.esri.arcgis.geodatabase.IGPMessages;
import com.esri.arcgis.geoprocessing.GPTool;
import com.esri.arcgis.geoprocessing.GeoProcessor;

public class ValidarGP_MensajesGP {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public boolean Validar (GeoProcessor gp, GPTool tool) throws IOException{
		IGPMessages gpm = gp.validate(tool, false);
		return devolverMensagesValidacion (gpm, tool.getToolName());
	}
	
	private static boolean devolverMensagesValidacion(IGPMessages gpm, String nombreTool){
		boolean eserror = false;
		try{
			System.out.println("--Inicio Validación del Geoprocesador: '" + nombreTool + "'");
			//Loop through the messages returned by the GeoProcessor			
			for(int i = 0; i < gpm.getCount(); i++){
				//Get the message at its index position
				String descripcion_error = gpm.getMessage(i).getDescription();
				if ( descripcion_error.length() != 0)
				System.out.println(gpm.getMessage(i).getDescription());
				if ( gpm.getMessage(i).isError()== true)eserror = true;
			}
			System.out.println("--Fin Validación del Geoprocesador: '" + nombreTool + "'");
		}catch(Exception e){e.printStackTrace();}
		return eserror;
	}
	public void returnMessagesGP(GeoProcessor gp, String nombreTool){
		try{
			//Loop through the messages returned by the GeoProcessor
			System.out.println("--Inicio Mensajes del Geoprocesador: '" + nombreTool + "'");
			for(int i = 0; i < gp.getMessageCount(); i++){
				//Get the message at its index position
				System.out.println((gp.getMessage(i)));
			}
			System.out.println("--Fin Mensajes del Geoprocesador: '" + nombreTool + "'");
		}catch(Exception e){e.printStackTrace();}
	}
}
