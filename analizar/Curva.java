package analizar;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Iterator;
import com.esri.arcgis.geodatabase.Cursor;
import com.esri.arcgis.geodatabase.IRow;
import java.awt.geom.Point2D;
import com.esri.arcgis.interop.AutomationException;
import java.lang.Math;

/**
 * @author     user
 */
public class Curva {
	/**
	 */
	private Integer curvaId;
	//private TreeSet<Point2D.Float> pointSet;
	private List<Point2D.Float> miListaPuntos;
	//miListaPuntos.
	private String formula;
	private float minx;
	private float maxx;	
	private float miny;
	private float maxy;
	private Short tipoformula;
    
	
	public Curva (int id, Cursor pCursorDAV, Cursor pCursorPAV)
	{
		System.out.println("    Ingresó a Curva");
		System.out.println("     Identificador PAV_ID de la curva: '" + id + "'");
		curvaId = id;
		loadPoints(pCursorDAV);
		loadCurva(pCursorPAV);
	}

	public void loadPoints(Cursor pCursorDAV)
	{
		try {
		    //Deprecated: Row pRowDAV = new Row(pCursorDAV.nextRow());
		    //Usar Casting normal de Java		    
		    //Row pRowDAV = (Row) pCursorDAV.nextRow();
		    IRow pRowDAV = null;
		    
		    //.NET: Dim indexcampoDAV_X As Single
		    //.NET: indexcampoDAV_X = pRowDAV.Fields.FindField("DAV_X") 'pFlds.Field(i).Name)
		    //indexcampoTablaXY_X = "DAV_X"
		    //int indexcampoDAV_X = (int) pRowDAV.getFields().findField(indexcampoTablaXY_X);
		    int indexcampoDAV_X = (int) pCursorDAV.findField("dav_x");
		    //indexcampoTablaXY_Y = "DAV_Y"
		    //int indexcampoDAV_Y = (int) pRowDAV.getFields().findField(indexcampoTablaXY_Y);
		    int indexcampoDAV_Y = (int) pCursorDAV.findField("dav_y");
		    
		    //Coloco el contenido del cursor en un ???
		    //pointSet = new TreeSet<Point2D.Float> ();
		    miListaPuntos = new ArrayList<Point2D.Float>();
		    
			do{
				//Asi devuelve la interfaz IRow:   pRowDAV = pCursorDAV.nextRow();
				//Y así devuelve el objeto Row
				//pRowDAV = (Row) pCursorDAV.nextRow();
				//Row row = new Row(table.createRow());
				pRowDAV = pCursorDAV.nextRow();
				if (pRowDAV == null)
					break;				
				Float x = (Float) pRowDAV.getValue(indexcampoDAV_X);
				//Float  x = new Float(fx.FloatValue());
				Float y = (Float) pRowDAV.getValue(indexcampoDAV_Y);
				//Float  y = new Float(fy.FloatValue());				
				//punto.setX((Float)pRowDAV.getValue(indexcampoDAV_X));
				Point2D.Float punto = new Point2D.Float(x,y);
				//punto.setY((Float)pRowDAV.getValue(indexcampoDAV_Y));
				//punto.setY(y);
				//pointSet.add(punto);
				miListaPuntos.add(punto);
			}while (pRowDAV != null);
						
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	public void loadCurva(Cursor pCursorPAV)
	{
		try {
		    IRow pRowPAV = null;
		    int indexcampoPAV_formula = (int) pCursorPAV.findField("pav_formula");
		    int indexcampoPAV_minx = (int) pCursorPAV.findField("pav_minx");
		    int indexcampoPAV_maxx = (int) pCursorPAV.findField("pav_maxx");
		    int indexcampoPAV_miny = (int) pCursorPAV.findField("pav_miny");
		    int indexcampoPAV_maxy = (int) pCursorPAV.findField("pav_maxy");
		    int indexcampoPAV_tipoformula = (int) pCursorPAV.findField("pav_tipoformula");
		 		    
			do{
				//pRowPAV = (Row) pCursorPAV.nextRow(); //com.esri.arcgis.geodatabase.IRowProxy cannot be cast to com.esri.arcgis.geodatabase.Row
				pRowPAV = (IRow) pCursorPAV.nextRow();
				
				if (pRowPAV == null)
					break;				
				formula = (String)pRowPAV.getValue(indexcampoPAV_formula);
				if (formula != null && formula.isEmpty() != true) 
					formula = formula.replace(" ", "");				
				System.out.println("     Formula de la curva (quitando posibles espacios!) = '" + formula + "'");
				minx = (Float) pRowPAV.getValue(indexcampoPAV_minx);
				//minx = new Float(fx.FloatValue());
				System.out.println("     Valor mínimo en X, permitido por la curva = '" + minx + "'");
				maxx = (Float) pRowPAV.getValue(indexcampoPAV_maxx);
				//maxx  = new Float(fy.FloatValue());
				System.out.println("     Valor máximo en X, permitido por la curva = '" + maxx+ "'");

				miny = (Float) pRowPAV.getValue(indexcampoPAV_miny);
				//minx = new Float(fx.FloatValue());
				System.out.println("     Valor mínimo en Y, permitido por la curva = '" + miny+ "'");
				maxy = (Float) pRowPAV.getValue(indexcampoPAV_maxy);
				//maxx  = new Float(fy.FloatValue());
				System.out.println("     Valor máximo en Y, permitido por la curva = '" + maxy+ "'");
				
				//tipoformula (0: Ecuación, 1: Distribución de Probabilidad, 2: No Aplica)
				tipoformula = (Short) pRowPAV.getValue(indexcampoPAV_tipoformula);
				System.out.println("     El tipo formula de la curva de vulnerabilidad es  = '" + tipoformula+ "'");
				
				//minx = (Float)pRowPAV.getValue(indexcampoPAV_minx);
				//maxx = (Float)pRowPAV.getValue(indexcampoPAV_maxx);
			}while (pRowPAV != null);
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	public float interpolarPunto (float xValue) {//throws IllegalStateException, AutomationException, IOException{
		if (miListaPuntos == null || miListaPuntos.isEmpty())
			throw new IllegalStateException("      No existe conjunto de puntos para interpolar");
		//Point minorPoint = null;
		//Point majorPoint = null;
		//Point p;
		//Point2D.Float majorPoint = pointSet.first();
		
		//Iterator<Point2D.Float> n = miListaPuntos.iterator();
		//int contador = 0;
		//System.out.println("      XXXXXX -interpolarPunto - xValue = '" + xValue + "'");		
		//System.out.println("       Incio Primer Iterador");
			//while(n.hasNext()){				
				//Point2D.Float p2 = n.next();							
				//System.out.println("        contador: '" + contador + "' - indice '" + miListaPuntos.indexOf(p2) + "' - px = '" + p2.x + "' - py = '" + p2.y + "'");
				//contador++;
			//}			
		//System.out.println("       Fin Primer Iterador");
		
		int h = 0;
		Point2D.Float majorPoint = miListaPuntos.get(0);				
		Point2D.Float minorPoint = miListaPuntos.get(0);;		
		Double  yInterpoladoDouble = null;
		Float yInterpolado = null;				
		//System.out.println("       Incio Segundo Iterador");		
		Vector <Point2D.Float> puntos_a_remover = new Vector<Point2D.Float>();
		Iterator<Point2D.Float> i = miListaPuntos.iterator();
		try{
			while(i.hasNext()){
				//System.out.println("        contador (h): '" + h + "'");
				minorPoint = majorPoint;
				majorPoint = i.next();
				if (xValue < majorPoint.getX()){
					if (h == 0){
						yInterpoladoDouble = majorPoint.getY();
						yInterpolado = new Float (yInterpoladoDouble.floatValue());						
					}else{
						yInterpoladoDouble = Interpolador.singleInterpolation(minorPoint.getX(), minorPoint.getY(), majorPoint.getX(), majorPoint.getY(), xValue);
						yInterpolado = new Float (yInterpoladoDouble.floatValue());
					}
				break;	
				}else if (xValue == majorPoint.getX()) {
					if (h != 0){
						//pointSet.remove(minorPoint);
						//miListaPuntos.remove(h - 1);
						puntos_a_remover.add(minorPoint);
					}
					yInterpoladoDouble = majorPoint.getY();
					yInterpolado = new Float (yInterpoladoDouble.floatValue());
				break;
				}else if (xValue > majorPoint.getX()) {
					if (h != 0){
						//pointSet.remove(minorPoint);
						//miListaPuntos.remove(h - 1);
						puntos_a_remover.add(minorPoint);
					}
					yInterpoladoDouble = majorPoint.getY();
					yInterpolado = new Float (yInterpoladoDouble.floatValue());
				}
				h++;
			}
			
		}catch (RuntimeException e){
			e.printStackTrace();
			throw new IllegalStateException("Error en el proceso de interpolación utilizando los puntos");			
		}
		Iterator<Point2D.Float> puntoaremover = puntos_a_remover.iterator();
		try{
			while(puntoaremover.hasNext())				
			miListaPuntos.remove(puntoaremover.next());						
		}catch (RuntimeException e){
				e.printStackTrace();
				throw new IllegalStateException("Error en el proceso de eliminación de los puntos!");			
		}

		//System.out.println(miListaPuntos);
		//System.out.println("        yInterpolado: " + yInterpolado);
		
		//System.out.println("       Fin Segundo Iterador");
		return yInterpolado;		
	}

	public Float despejarCurva (Float xValue)throws IllegalArgumentException, IllegalStateException, AutomationException, IOException{
		return despejarCurva(xValue, 1f);
	}
	public Float despejarCurva(Float xValue, Float FactorCorreccionMedia)throws IllegalArgumentException, IllegalStateException, AutomationException, IOException{
		//if (xValue < minx || xValue > maxx){
		
		if (xValue < minx){
			//throw new IllegalArgumentException("El valor de X no es admitido");
			xValue = minx;
		}else if  (xValue > maxx){
			xValue = maxx;
		}
		if (formula != null && formula.isEmpty() != true && tipoformula == 0){ //Tengo una ecuación: tipoformula == 0
			//Ojo que en formula debe estar escrita la variable x como "x"
			//System.out.println("     Está utilizando la formula para interpotar!");
			MathEvaluator m = new MathEvaluator(formula);						
			m.addVariable("x", xValue);
			if (m.getValue() == null )throw new IllegalStateException("La formula está mal escrita para la curva de vulnerabilidad!!. Valor almacenado para la formula = '"+ formula + "'");
			Double mDouble = m.getValue();
			Float mFloat = mDouble.floatValue();
			if (mFloat < miny){
				//throw new IllegalArgumentException("El valor de X no es admitido");
				mFloat = miny;
			}else if  (mFloat > maxy){
				mFloat = maxy;
			}
			
			return mFloat;
		}else if (formula != null && formula.isEmpty() != true && tipoformula == 1){ //Tengo una distribución de probabilidad lognormal: tipoformula == 1
			CalcularProbabilidades probabilidades = new CalcularProbabilidades();
			//formula: media=13,desviacion=15
			String [] parametroscurva = formula.split(",");
			if (parametroscurva.length != 2)
				throw new IllegalStateException("      La formula '" + formula + " está mal almacenada!");
			if (parametroscurva[0].split("=").length != 2)
				throw new IllegalStateException("      La formula '" + formula + " está mal almacenada (revisar parámetro de la media!");
			if (parametroscurva[1].split("=").length != 2)
				throw new IllegalStateException("      La formula '" + formula + " está mal almacenada (revisar parámetro de la desviación!");			
			Float mediadistribucion = Float.valueOf(parametroscurva[0].split("=")[1]);
			mediadistribucion = mediadistribucion * FactorCorreccionMedia;
			Float desviaciondistribucion = Float.valueOf(parametroscurva[1].split("=")[1]);
			Double valor;
			valor = Math.log(xValue.doubleValue()/mediadistribucion.doubleValue());			
			xValue = (1/desviaciondistribucion) * valor.floatValue();
			return probabilidades.DistribucionNormal(xValue, 0f, 1f);
		}else if (formula != null && formula.isEmpty() != true && tipoformula == 3){ //Tengo una distribución de probabilidad normal: tipoformula == 3
			CalcularProbabilidades probabilidades = new CalcularProbabilidades();
			//formula: media=13,desviacion=15
			String [] parametroscurva = formula.split(",");
			if (parametroscurva.length != 2)
				throw new IllegalStateException("      La formula '" + formula + " está mal almacenada!");
			if (parametroscurva[0].split("=").length != 2)
				throw new IllegalStateException("      La formula '" + formula + " está mal almacenada (revisar parámetro de la media!");
			if (parametroscurva[1].split("=").length != 2)
				throw new IllegalStateException("      La formula '" + formula + " está mal almacenada (revisar parámetro de la desviación!");			
			Float mediadistribucion = Float.valueOf(parametroscurva[0].split("=")[1]);
			mediadistribucion = mediadistribucion * FactorCorreccionMedia;
			Float desviaciondistribucion = Float.valueOf(parametroscurva[1].split("=")[1]);
			return probabilidades.DistribucionNormal(xValue, mediadistribucion, desviaciondistribucion);			
		}else{ //No tengo formula: tipoformula == 2
			//System.out.println("     Está utilizando los puntos para interpolar!");
			float puntointerpolado = interpolarPunto(xValue);
			if (puntointerpolado < miny){
				//throw new IllegalArgumentException("El valor de X no es admitido");
				puntointerpolado = miny;
			}else if  (puntointerpolado > maxy){
				puntointerpolado = maxy;
			}			
			return puntointerpolado;
		}
	}
	

	/**
	 * @return
	 * @uml.property  name="curvaId"
	 */
	public Integer getCurvaId() {
		return curvaId;
	}

	/**
	 * @param  curvaId
	 * @uml.property  name="curvaId"
	 */
	public void setCurvaId(Integer curvaId) {
		this.curvaId = curvaId;
	}

	//public TreeSet<Point2D.Float> getPointSet() {
	public List<Point2D.Float> getPointList() {
		return miListaPuntos;
	}

	//public void setPointSet(TreeSet<Point2D.Float> pointSet) {
	public void setPointList(List<Point2D.Float> miListaPuntos) {
		//this.pointSet = pointSet;
		this.miListaPuntos = miListaPuntos;
	}
	
	
}
