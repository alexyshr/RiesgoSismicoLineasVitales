package analizar;

import java.io.IOException;

import com.esri.arcgis.geodatabase.Feature;
import com.esri.arcgis.geodatabase.FeatureClass;
import com.esri.arcgis.geodatabase.Field;
import com.esri.arcgis.geodatabase.IFeatureCursor;
import com.esri.arcgis.geodatabase.IQueryFilter;
import com.esri.arcgis.geodatabase.IRowBuffer;
import com.esri.arcgis.geodatabase.QueryFilter;
import com.esri.arcgis.geodatabase.Table;
import com.esri.arcgis.geodatabase.esriFeatureType;
import com.esri.arcgis.geodatabase.esriFieldType;
import com.esri.arcgis.geodatabase.esriSpatialRelEnum;
import com.esri.arcgis.geometry.IArea;
import com.esri.arcgis.geometry.IEnvelopeGEN;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.geometry.esriGeometryType;
import com.esri.arcgis.interop.AutomationException;

public class PasarAtributoPoligonos_a_FClass {
	public void PasarAtributo(FeatureClass fClassLV, String CampoCodigoLV, FeatureClass fClassPoligonos, String CampoCodigoPoligonos, int codigoPoligonosCastigo) throws AutomationException, IOException{
		//fClassLV puede ser esriFeatureType.esriFTSimple, y el feature puede ser de puntos, líneas o polígonos u otros soportados menos esriGeometryType.esriGeometryNull
		//Verificar que exista el campo en el fClassPoligonos
		if (fClassPoligonos.findField(CampoCodigoPoligonos)== -1){
			throw new IllegalStateException("El campo '" + CampoCodigoPoligonos + "'(tipo SHORT) debe existir con valores en el FC de polígonos '" + fClassPoligonos.getName()+ "'");
		}		
		//Verificar si existe el campo CampoCodigoLV en fClassLV		
		if (fClassLV.findField(CampoCodigoLV)== -1){
			System.out.println("  **Se creará el campo '" + CampoCodigoLV + "' en el FC de líneas '" + fClassLV.getName()+ "'");
			Field campo_codigo_lv = new Field();
			campo_codigo_lv.setName(CampoCodigoLV);
			campo_codigo_lv.setType(esriFieldType.esriFieldTypeSmallInteger);
			campo_codigo_lv.setPrecision(2);
			fClassLV.addField(campo_codigo_lv);				
		}else{
			System.out.println("  **Se sobreescribirá el contenido del campo '" + CampoCodigoLV + "' existente en el FC de líneas '" + fClassLV.getName()+ "'");
		}
		Table pTablelv = new Table(fClassLV);
		IFeatureCursor cursorPoligonos = (IFeatureCursor) fClassPoligonos.search(null, false);
		Feature featurePoligonos = (Feature) cursorPoligonos.nextFeature();;
		Integer cuenta_poligonos = 0;
		while (featurePoligonos != null){ //Calcula el codigo para todas las líneas que están dentro!
			cuenta_poligonos ++;
			if (featurePoligonos.getValue(cursorPoligonos.getFields().findField(CampoCodigoPoligonos)) == null) {
				throw new IllegalStateException("El FClass '" + fClassPoligonos.getName() + "' tiene valores nulos en el campo '" + CampoCodigoPoligonos + "'");
			}
			int codigoPoligonos = ((Short)featurePoligonos.getValue(cursorPoligonos.getFields().findField(CampoCodigoPoligonos))).shortValue();				
			IQueryFilter pQueryFilterlv =  new SpatialQuery().Spatial_Query
			(fClassLV, featurePoligonos.getShape(), esriSpatialRelEnum.esriSpatialRelContains, null);			
			//FeatureCursor cursorlv = (FeatureCursor) fClassLV.search(pQueryFilter, false);								
			IRowBuffer irb = pTablelv.createRowBuffer();
			irb.setValue(irb.getFields().findField(CampoCodigoLV), codigoPoligonos);
			//Importantisimo: Si no definio un QueryFilter con los campos a actualizar, el row buffer daña los otros campos colocandolos a null, daña el Fclass!
			pQueryFilterlv.setSubFields(CampoCodigoLV);
			fClassLV.updateSearchedRows(pQueryFilterlv, irb);
			featurePoligonos = (Feature) cursorPoligonos.nextFeature();
		}
		
		QueryFilter pQueryFilterlv2 = new QueryFilter();
		pQueryFilterlv2.setWhereClause(CampoCodigoLV + " is null");
		
		IFeatureCursor cursorlv2 = (IFeatureCursor) fClassLV.search(pQueryFilterlv2, false);
		Feature featurelv2 = (Feature) cursorlv2.nextFeature();
		Integer cuenta_lv2 = 0;
		
		int tipoFeature =  fClassLV.getFeatureType();
		if (tipoFeature != esriFeatureType.esriFTSimple) {
			System.out.println("  **Error (tipo de feature class de líneas vitales no permitido): El FeatureClass '" + fClassLV.getName() + "' no es de tipo esriFeatureType.esriFTSimple'");
			return;
		}
		
		while (featurelv2 != null){ //(featurelv != null); //Este ciclo es para las lv que quedaron null por no estar dentro de ningun poligono (interseptaban o estaban fuera)
			cuenta_lv2 ++;
			int tipoShape = fClassLV.getShapeType();
			Point punto = null;
			if (tipoShape != esriGeometryType.esriGeometryPoint && tipoShape != esriGeometryType.esriGeometryNull) {
				IEnvelopeGEN envelope = (IEnvelopeGEN) featurelv2.getShape().getEnvelope();
				//double xCentroide = (envelope.getXMax()+envelope.getXMin())/2;; 
				//double yCentroide = (envelope.getYMax()+envelope.getYMin())/2;;
				IArea area = (IArea) envelope;				
				//Point punto = new Point();
				//punto.setX(xCentroide);
				//punto.setY(yCentroide);
				punto = (Point) area.getCentroid();															
			} else if (tipoShape == esriGeometryType.esriGeometryPoint){
				punto = (Point) featurelv2.getShape();
			} else if (tipoShape == esriGeometryType.esriGeometryNull){
				System.out.println("  **Error (hay un Feature nulo en el FeatureClass). FeatureClass: '" + fClassLV.getName() + "'. Feature: '" + Integer.toString(featurelv2.getOID()) + "'");
				continue;
			}
						
			IQueryFilter pQueryFilterPoligonos =  new SpatialQuery().Spatial_Query
			(fClassPoligonos, punto, esriSpatialRelEnum.esriSpatialRelWithin, null);									
			
			IFeatureCursor cursorPoligonos2 = (IFeatureCursor) fClassPoligonos.search(pQueryFilterPoligonos, false);
			
			featurePoligonos = (Feature) cursorPoligonos2.nextFeature(); //El primer poligono que contenga el punto, pueden haber varios (poligonos unos sobre otros)
			
			if (featurePoligonos != null) {
				int codigoPoligonos = ((Short)featurePoligonos.getValue(cursorPoligonos2.getFields().findField(CampoCodigoPoligonos))).shortValue();
				featurelv2.setValue(cursorlv2.getFields().findField(CampoCodigoLV), codigoPoligonos);
				featurelv2.store();
			}
			featurelv2 = (Feature) cursorlv2.nextFeature();
		} 					

		//Todo el resto de líneas que quedaron sin asignar (su centroide no tiene poligono encima). Valor castigo!!
		QueryFilter pQueryFilterlv3 = new QueryFilter();
		pQueryFilterlv3.setWhereClause(CampoCodigoLV + " is null");
		
		IRowBuffer irb3 = pTablelv.createRowBuffer();
		irb3.setValue(irb3.getFields().findField(CampoCodigoLV), codigoPoligonosCastigo);
		//Importantisimo: Si no definio un QueryFilter con los campos a actualizar, el row buffer daña los otros campos colocandolos a null, daña el Fclass!
		pQueryFilterlv3.setSubFields(CampoCodigoLV);
		fClassLV.updateSearchedRows(pQueryFilterlv3, irb3);										
	}

}
