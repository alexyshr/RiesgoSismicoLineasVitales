package analizar;


import umontreal.iro.lecuyer.probdist.LognormalDist;
import umontreal.iro.lecuyer.probdist.NormalDist;

public class DistrubucionesProbabilidad {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NormalDist normal = new NormalDist(0,1);
		LognormalDist lognormal = new LognormalDist(0,1);

		double x = 0.2334455;
		//En R: dnorm(x, 0 ,1)
		double densidad = normal.density(x);
		
		//En R: 1 - pnorm(x, 0 ,1)
		double distribucionComplementaria = normal.barF(x);
		
		//En R: pnorm(x, 0 ,1)
		double distrubucion = normal.cdf(x);
		System.out.println("---Distribución Normal");
		System.out.println("prob (X = " + x + "): " + densidad);
		System.out.println("prob (X > " + x + "): " + distribucionComplementaria);
		System.out.println("prob (X <= " + x + "): " + distrubucion);
		
		
		//En R: dlnorm(x, 0 ,1)
		densidad = lognormal.density(x);
		
		//En R: 1 - plnorm(x, 0 ,1)
		distribucionComplementaria = lognormal.barF(x);
		
		//En R: plnorm(x, 0 ,1)		
		distrubucion = lognormal.cdf(x);
		
		System.out.println("---Distribución LogNormal");
		System.out.println("prob (X = " + x + "): " + densidad);
		System.out.println("prob (X > " + x + "): " + distribucionComplementaria);
		System.out.println("prob (X <= " + x + "): " + distrubucion);
		
	}

}
