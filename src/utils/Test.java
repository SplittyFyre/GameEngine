package utils;

public class Test {
	
	public static void main(String[] args) {

		float f = 22.542069f;
		
		System.out.println(SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_LEFT, 67.5f, f));
		System.out.println(SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_LEFT, 67.5f, -f));
		
		
		System.exit(0);
		
		char[] togen = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '.'};
		
		for (int i = 0; i < togen.length; i++) {
			char c = togen[i];
			System.out.println("case Keyboard.KEY_" + c + ":"
					+ "\n	return '" + c + "';");
		}
		
	}
	
	public static float binom(float p, int k, int n) {
		
		int choose = fac(n) / (fac(k) * fac(n - k));
		
		float var = (float) Math.pow((1 - p), (n - k));
		
		return (float) (choose * Math.pow(p, k) * var);
		
	}
	
	public static double binomd(float p, int k, int n) {
		
		int choose = fac(n) / (fac(k) * fac(n - k));
		
		double var = Math.pow((1 - p), (n - k));
		
		return choose * Math.pow(p, k) * var;
		
	}
	
	public static int fac(int i) {
		
		int ret = 1;
		
		for (; i > 0; i--) {
			ret *= i;
		}
		
		return ret;
		
	}

}
