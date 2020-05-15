/*
 * Copyright (c) 2008 ESRI
 *
 * All rights reserved under the copyright laws of the United States
 * and applicable international laws, treaties, and conventions.
 *
 * You may freely redistribute and use this sample code, with or
 * without modification, provided you include the original copyright
 * notice and use restrictions.
 * See use restrictions at /arcgis/java/samples/userestrictions.
 */
/*
 * ArcGIS Engine Developer Sample
 * Application Name: SpatialRelation
 */
package analizar;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import com.esri.arcgis.carto.FeatureLayer;
import com.esri.arcgis.carto.esriSelectionResultEnum;
import com.esri.arcgis.geodatabase.FeatureClass;
import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geodatabase.IFeatureClass;
import com.esri.arcgis.geodatabase.IFeatureCursor;
import com.esri.arcgis.geodatabase.IRowBuffer;
import com.esri.arcgis.geodatabase.QueryFilter;
import com.esri.arcgis.geodatabase.SelectionSet;
import com.esri.arcgis.geodatabase.SpatialFilter;
import com.esri.arcgis.geodatabase.Table;
import com.esri.arcgis.geodatabase.Workspace;
import com.esri.arcgis.geodatabase.esriSelectionOption;
import com.esri.arcgis.geodatabase.esriSelectionType;
import com.esri.arcgis.geodatabase.esriSpatialRelEnum;
import com.esri.arcgis.geometry.IGeometry;
import com.esri.arcgis.system.AoInitialize;
import com.esri.arcgis.system.EngineInitializer;
import com.esri.arcgis.system.esriLicenseProductCode;

import conectar.geodatabase.AbrirFile_o_Personal_Geodatabase;

/**
 * This sample demonstrates how to use a spatial relation description string
 * spatial filter to process the relationships between a geometric object and
 * members of a featureclass.
 */
public class SpatialRelation {
	/**
	 * Process features using a spatial relation description string as a spatial filter.
	 *
	 * @param path shapefile path
	 * @param name shapefile name
	 * @throws IOException if cannot get shapefile related information.
	 */
	private Workspace ws;
	
	private void processFeatures(String path, String name) throws IOException {
		System.out.println("Using shapefile " + name);
		try {
			//
			// Get the feature class and create a feature layer for this shapefile
			// to add selected features to.
			//
			FeatureClass featureClass = getShapefileFeatureClass(path, name);
			
			
			//
			// Create a spatial filter to use for selecting features from this feature class.
			//
			SpatialFilter spatialFilter = createSpatialFilter(featureClass);
			//
			// Perform the search/query/comparison using the spatial filter,
			// creating a feature cursor representing all features passing it,
			// and add those features to the layer.
			//
			
			int featureCtr = 0;
			//FeatureClass featureClassLineas = getShapefileFeatureClass(path, "prueba");
			FeatureClass featureClassPoligonos = getShapefileFeatureClass(path, "Poligonos2");
			//Usa el spatialFilter, si pasa null devuelve todo
			SelectionSet selection = new SelectionSet(featureClassPoligonos.select(spatialFilter, esriSelectionType.esriSelectionTypeHybrid, esriSelectionOption.esriSelectionOptionNormal, null));
			System.out.println("Number of selected features = " + selection.getCount());

			int campoDescripcion = featureClassPoligonos.findField("DESCRIPCION");
			//spatialFilter.setGeometryField(featureClassPoligonos.getShapeFieldName());
			IFeatureCursor featureCursorPoligonos = featureClassPoligonos.search(spatialFilter, false);
			
			//ITable pTablePoligonos = (ITable) featureClassPoligonos;
			Table pTablePoligonos2 = new Table(featureClassPoligonos);
			
			IRowBuffer irb = pTablePoligonos2.createRowBuffer();
			irb.setValue(irb.getFields().findField("DDD"), "2");
			QueryFilter qf = new QueryFilter();
			qf.setSubFields("DDD");
			featureClassPoligonos.updateSearchedRows(qf, irb); //Importantisimo: Si no definio un QueryFilter con los campos a actualizar, el row buffer daña los otros campos colocandolos a null!
			
			IFeature feature = featureCursorPoligonos.nextFeature();
			FeatureLayer featureLayer = new FeatureLayer();		
			featureLayer.setFeatureClassByRef(featureClassPoligonos);
			featureLayer.selectFeatures(spatialFilter,esriSelectionResultEnum.esriSelectionResultNew, false);			
			System.out.println("Number of selected features del layer = " + featureLayer.getSelectionSet().getCount());
			
			while (feature != null) {
				//int campoDescripcion = feature.getFields().findField("DESRIPCION"));
				System.out.println("Linea: " + feature.getValue(campoDescripcion));				
				featureLayer.add(feature);
				feature = featureCursorPoligonos.nextFeature();
				featureCtr++;
			}
			System.out.println("Features added to layer: " + featureCtr + " out of " + featureClassPoligonos.featureCount(null));
		}
		catch (IOException e) {
			System.out.println("Could not process shapefile.");
			throw e;
		}
	}
	/**
	 * Create a spatial filter based on the first feature of a feature class.
	 *
	 * @param featureClass the feature class containing the feature to use to query against.
	 * @return SpatialFilter the spatial filter based on the feature and certain relationships.
	 * @throws IOException if couldn't create the spatial filter.
	 */
	private SpatialFilter createSpatialFilter(IFeatureClass featureClass) throws IOException {
		try {
			//
			// Create an envelope polygon from the first feature.
			//
			//IEnvelopeGEN polygon = (IEnvelopeGEN) featureClass.getFeature(0).getExtent();
			
			IFeatureCursor cursorPoligonos = (IFeatureCursor) featureClass.search(null, false);
			IFeature featurePoligonos = cursorPoligonos.nextFeature();
			//IEnvelopeGEN polygon = (IEnvelopeGEN) featurePoligonos.getShape().getEnvelope();
			
			IGeometry forma = featurePoligonos.getShape();
			/*
			 * Create the spatial relation description string containing "T|F|*" codes
			 * to define the relationship between two geometries and their
			 * geometric interior, boundary, and exterior relationships, such that
			 * it would find those features that intersect with the envelope polygon.
			 */
			final String spatialRelationDescription = "TT*TT****";
			//
			// Create a spatial filter based on the spatial relation description
			// and the envelope polygon.
			//
			SpatialFilter spatialFilter = new SpatialFilter();
			//spatialFilter.setGeometryByRef((IGeometry) polygon);
			spatialFilter.setGeometryByRef(forma);
			spatialFilter.setSpatialRel(esriSpatialRelEnum.esriSpatialRelRelation);
			spatialFilter.setSpatialRelDescription(spatialRelationDescription);
			//spatialFilter.setGeometryField(featureClass.getShapeFieldName());
			return spatialFilter;
		}
		catch (IOException e) {
			System.out.println("Couldn't create the spatial filter.");
			throw e;
		}
	}

	/**
	 * Get the shapefile feature class for a data path and feature class name.
	 *
	 * @param path path to the shapefile
	 * @param name the feature class name
	 * @return IFeatureClass object representing the shapefile feature class
	 * @throws IOException if feature class could not be obtained
	 */
	private FeatureClass getShapefileFeatureClass(String path, String name) throws IOException {
		FeatureClass featureClass = null;
		try {
			AbrirFile_o_Personal_Geodatabase fgdb = new AbrirFile_o_Personal_Geodatabase();
			//RutaGdb = "D:/geo/geo.gdb"
			ws = fgdb.openFile_o_PersonalWorkspace(path);
			featureClass = new FeatureClass(ws.openFeatureClass(name));								
		}
		catch (IOException e) {
			System.out.println("Couldn't access feature class :" + name + " in " + path);
			throw e;
		}
		return featureClass;
	}
	/**
	 * Initialize ArcObjects for Engine, call the method to process the features, and then shutdown.
	 *
	 * @param args shapefile path and name
	 */
	public static void main(String[] args) {
		System.out.println("Starting SpatialRelation - An ArcObjects Java SDK Developer Sample");
		String arcGISHome = null;
		try {
			arcGISHome = System.getenv("ARCGISHOME");
		}
		catch (Error error) { // for Java 1.4
			BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Please enter the ArcGIS installation directory: ");
			try {
				arcGISHome = console.readLine();
			}
			catch (IOException exception) {
				exception.printStackTrace();
			}
		}
		if (!(new File(arcGISHome).exists())) {
			System.out.println(arcGISHome + " does not exist.\nExiting...");
			System.exit(-1);
		}
		//
		// Change the following lines if you want to use different data
		//
		//String inPath = arcGISHome + "/java/samples/data/usa/";
		String inPath = "D:/geo/geo.gdb";

		//String name = "states.shp";
		String name = "Poligonos";
		try {
			EngineInitializer.initializeEngine();
			AoInitialize aoInitializer = new AoInitialize();
			aoInitializer.initialize(esriLicenseProductCode.esriLicenseProductCodeAdvanced);
			SpatialRelation thisSampleApp = new SpatialRelation();
			thisSampleApp.processFeatures(inPath, name);
			aoInitializer.shutdown();
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
			System.out.println("Sample failed.  Exiting...");
			System.exit(-1);
		}
		System.out.println("Sample finished.  No files modified.");
	}
}
