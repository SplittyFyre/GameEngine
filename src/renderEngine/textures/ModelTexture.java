package renderEngine.textures;

public class ModelTexture {
	
	private int textureID;
	private int specularMap;
	private int numRows = 1;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	private boolean transparent = false;
	private boolean useFakeLighting = false;
	private boolean hasSpecularMap = false;
	private float brightDamper = 0;
	
	public void setBrightDamper(float f) {
		this.brightDamper = f;
	}
	
	public float getBrightDamper() {
		return this.brightDamper;
	}
	
	public ModelTexture(int texture){
		this.textureID = texture;
	}
	
	public void setSpecularMap(int specMap) {
		this.specularMap = specMap;
		this.hasSpecularMap = true;
	}
	
	public boolean hasSpecularMap() {
		return hasSpecularMap;
	}
	
	public int getSpecularMap() {
		return specularMap;
	}
	
	public int getNumRows() {
		return numRows;
	}

	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}

	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}

	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}

	public boolean isTransparent() {
		return transparent;
	}
	
	public int getID(){
		return textureID;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
	
}
