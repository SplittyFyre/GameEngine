package engine.scene.lensFlare;
 
import org.lwjgl.util.vector.Vector2f;
 
public class FlareTexture {
     
    private int texture;
    private float scale;
     
    private Vector2f position = new Vector2f();
 
    public FlareTexture(int texture, float scale){
        this.texture = texture;
        this.scale = scale;
    }
     
    public void setPosition(Vector2f pos){
        this.position.set(pos);
    }
     
    public int getTextureID() {
        return texture;
    }
 
    public float getScale() {
        return scale;
    }
 
    public Vector2f getPosition() {
        return position;
    }
     
}