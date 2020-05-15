package analizar;

import umontreal.iro.lecuyer.probdist.LognormalDist;
import umontreal.iro.lecuyer.probdist.NormalDist;

public class CalcularProbabilidades {

	public Float DensidadNormal(Float x, Float media, Float desviacion){
		NormalDist normal = new NormalDist(media,desviacion);
		//En R: dnorm(x, 0 ,1)
		Double probabilidad = normal.density(x);
		return probabilidad.floatValue();
		//System.out.println("prob (X = " + x + "): " + normal.density(x));
	}
	
	public Float DensidadComplementariaNormal(Float x, Float media, Float desviacion){
		NormalDist normal = new NormalDist(media,desviacion);
		//En R: 1 - pnorm(x, 0 ,1)
		Double probabilidad = normal.barF(x);
		return probabilidad.floatValue();
		//System.out.println("prob (X > " + x + "): " + normal.barF(x));
	}
	
	public Float DistribucionNormal(Float x, Float media, Float desviacion){
		NormalDist normal = new NormalDist(media,desviacion);
		//En R: pnorm(x, 0 ,1)
		Double probabilidad = normal.cdf(x);
		return probabilidad.floatValue();
		//System.out.println("prob (X <= " + x + "): " + normal.cdf(x));
	}
	
	public Float DensidadLogNormal(Float x, Float media, Float desviacion){
		LognormalDist lognormal = new LognormalDist(media,desviacion);
		//En R: dlnorm(x, 0 ,1)
		Double probabilidad = lognormal.density(x);
		return probabilidad.floatValue();
		//System.out.println("prob (X = " + x + "): " + lognormal.density(x));
	}
	
	public Float DensidadComplementariaLogNormal(Float x, Float media, Float desviacion){
		LognormalDist lognormal = new LognormalDist(media,desviacion);
		//En R: 1 - plnorm(x, 0 ,1)
		Double probabilidad = lognormal.barF(x);
		return probabilidad.floatValue();
		//System.out.println("prob (X > " + x + "): " + lognormal.barF(x));
	}
	
	public Float DistribucionLogNormal(Float x, Float media, Float desviacion){
		LognormalDist lognormal = new LognormalDist(media,desviacion);
		//En R: plnorm(x, 0 ,1)		
		Double probabilidad = lognormal.cdf(x);
		return probabilidad.floatValue();
		//System.out.println("prob (X <= " + x + "): " + lognormal.cdf(x));
	}	
}
