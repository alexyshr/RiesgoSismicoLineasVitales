package analizar;
public class Interpolador {
	public static double singleInterpolation (double minorX, double minorY, double majorX,  double majorY, double xValue){		
		double factor = (majorY - minorY) / (majorX - minorX);
		double sum = (xValue * factor) - (minorX * factor) + minorY;
		return  sum;
	}
}
