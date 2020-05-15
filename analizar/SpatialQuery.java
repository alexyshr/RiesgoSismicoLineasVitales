package analizar;
import java.io.IOException;
import java.net.UnknownHostException;
import com.esri.arcgis.geodatabase.FeatureClass;
import com.esri.arcgis.geodatabase.IQueryFilter;
import com.esri.arcgis.geodatabase.SpatialFilter;
import com.esri.arcgis.geometry.IGeometry;

public class SpatialQuery {
	public IQueryFilter Spatial_Query(FeatureClass pFeatureClassIN, 
			IGeometry searchGeometry, int spatialRelation, String whereClause) 
     {
		//ISpatialFilter pSpatialFilter;

		try {
			// create a spatial query filter
			SpatialFilter pSpatialFilter = new SpatialFilter(); 
						
			//specify the geometry to query with
			pSpatialFilter.setGeometryByRef(searchGeometry);
			
			//specify what the geometry file is called on the Feature Class that we will be querying against
			String strShpFld = pFeatureClassIN.getShapeFieldName();
			pSpatialFilter.setGeometryField(strShpFld);

			// specify the type of spatial operation to use
			pSpatialFilter.setSpatialRel(spatialRelation);

			// create the where statement
			if (whereClause == null) {whereClause = "";}
			
			pSpatialFilter.setWhereClause(whereClause);

			// perform the query and use a cursor to hold the results
			IQueryFilter pQueryFilter = pSpatialFilter;
						
			return pQueryFilter;
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;		
	}

}
