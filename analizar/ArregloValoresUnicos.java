package analizar;

import java.io.IOException;
import java.util.ArrayList;
import com.esri.arcgis.geodatabase.ICursor;
import com.esri.arcgis.geodatabase.IRow;
import com.esri.arcgis.geodatabase.Table;
import com.esri.arcgis.geodatabase.Workspace;
import com.esri.arcgis.interop.AutomationException;

import conectar.geodatabase.AbrirFile_o_Personal_Geodatabase;
//Agrega el null también al arreglo de valores únicos!
public class ArregloValoresUnicos {
	private ArrayList<String> valoresUnicosSet;
	public ArregloValoresUnicos(String rutaGDB, String nombre_tabla_o_fc, String nombre_campo) throws AutomationException, IOException{
		try{
			AbrirFile_o_Personal_Geodatabase fgdb = new AbrirFile_o_Personal_Geodatabase();
			Workspace workspace = fgdb.openFile_o_PersonalWorkspace(rutaGDB);		
			Table tabla = new Table(workspace.openTable(nombre_tabla_o_fc));
			/*
			 * if (tabla == null){ throw new IllegalArgumentException("La tabla o FC '" +
			 * nombre_tabla_o_fc + "' no existe!"); }
			 */
			if (tabla.findField(nombre_campo)== -1){
				throw new IllegalArgumentException("El campo '" + nombre_campo + "' en '" + nombre_tabla_o_fc + "' no existe!");						
			}
			ICursor cursorTabla = (ICursor) tabla.ITable_search(null, false);
			IRow fila = (IRow) cursorTabla.nextRow();
			Integer numero_fila = 0;
			valoresUnicosSet = new ArrayList<String>();  //Si hay nulos el el campo también lo agrega
			while (fila != null){
				numero_fila ++;
				String valor = "" + fila.getValue(cursorTabla.getFields().findField(nombre_campo));
				boolean existe = false;
				if (numero_fila != 1 && !valor.isEmpty()){
					for (String a: valoresUnicosSet){
						if (a.equals(valor)){	
							existe = true;
							break;						
						}
					}
				}
				if (!existe && !valor.isEmpty())
				valoresUnicosSet.add(valor);
				fila = (IRow) cursorTabla.nextRow();
			}
		} catch (AutomationException ax) {
			System.out.println(ax.getMessage());
		}
	}
	public ArrayList<String> getvaloresUnicosSet() {
		return valoresUnicosSet;
	}	
}
