package engine.scene.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import engine.collision.BoundingBox;
import engine.renderEngine.TRAddtlGeom;
import engine.renderEngine.models.TexturedModel;
import engine.utils.SFMath;

public abstract class TREntity {

	private TexturedModel model;
	private Vector3f position;
	private float rotX, rotY, rotZ;
	private float scaleX, scaleY, scaleZ;
	private Vector3f worldScale = new Vector3f(0, 0, 0);
	private BoundingBox boundingBox;
	private final BoundingBox staticBoundingBox;
	
	public float bbyoffset = 0;
	
	public boolean canHaveChildren = false;
	public final boolean isOrganizationNode;
	
	private TREntity parent = null;
	private List<TREntity> children = null;
	
	private Vector3f worldPosition = new Vector3f(0, 0, 0);
	
	public Vector3f getWorldPosition() {
		return worldPosition;
	}

	public void setWorldPosition(Vector3f worldPosition) {
		this.worldPosition = worldPosition;
	}
	
	public Vector3f getWorldScale() {
		return this.worldScale;
	}
	
	
	public TREntity getParent() {
		return this.parent;
	}
	public void setParent(TREntity parent) {
		this.parent = parent;
	}
	
	
	
	
	public void attachChild(TREntity child) {
		if (!this.canHaveChildren) {
			throw new RuntimeException("this entity can not have children (it got kicked in the balls really hard and I am mature)");
		}
		children.add(child);
		child.setParent(this);
	}
	
	public void detachChild(TREntity child) {
		if (!this.canHaveChildren) {
			throw new RuntimeException("this entity can not have children and therefore you cannot remove any");
		}
		children.remove(child);
		child.setParent(null);
	}
	
	public void detachChildAt(int i) {
		if (!this.canHaveChildren) {
			throw new RuntimeException("this entity can not have children");
		}
		children.get(i).setParent(null);
		children.remove(i);
	}
	
	public TREntity childAt(int i) {
		if (!this.canHaveChildren) {
			throw new RuntimeException("this entity can not have children");
		}
		return children.get(i);
	}
	
	public void setChildren(List<TREntity> childrens) {
		this.children = childrens;
	}
	
	public List<TREntity> getChildren() {
		return this.children;
	}
	
	public boolean hasChildren() {
		return this.canHaveChildren && this.children.size() != 0; 
	}
	
	
	
	
	public TREntity enableChildren() {
		this.canHaveChildren = true;
		this.children = new ArrayList<TREntity>();
		return this;
	}
	
																				// mat is for internal use, renderer pass in null
	public void updateSceneGraph(Map<TexturedModel, List<TRAddtlGeom>> mapPtr, Matrix4f parentTransformMat) {
		
		Matrix4f m_transformationMatrix = null;
		
		// if this is not merely an organization node
		if (!this.isOrganizationNode) {
			
			m_transformationMatrix = SFMath.createTransformationMatrix(this.position, this.getRotX(), this.getRotY(), this.getRotZ(), this.getScale());
			
			TRAddtlGeom additionalGeom = null;
			
			if (this.parent.isOrganizationNode) {
				this.worldPosition.set(this.position);
				this.worldScale.set(this.scaleX, this.scaleY, this.scaleZ);
			}
			else { // if parent is not root and therefore 'valid'
				// actually apply parent transform				
				Matrix4f.mul(parentTransformMat, m_transformationMatrix, m_transformationMatrix);
				
				Vector3f parentScale = this.parent.getWorldScale();
				this.worldScale.set(parentScale.x * this.scaleX, parentScale.y * this.scaleY, parentScale.z * this.scaleZ);
				// calc world pos and stuff
				SFMath.transformAndSet_inplace(m_transformationMatrix, this.position, this.worldPosition);
			}
			
			additionalGeom = new TRAddtlGeom(m_transformationMatrix, this.getTextureXOffset(), this.getTextureYOffset());	
			
			TexturedModel model = this.model;
			List<TRAddtlGeom> batch = mapPtr.get(model);
			if (batch == null) {
				List<TRAddtlGeom> newBatch = new ArrayList<TRAddtlGeom>();
				newBatch.add(additionalGeom);
				mapPtr.put(model, newBatch);
			}
			else {
				batch.add(additionalGeom);
			}
			
			// after having calculated world pos and scale, update bounding box
			this.updateBoundingBox();
			
		}
		
		if (this.canHaveChildren) {
			for (TREntity child : children) {
				child.updateSceneGraph(mapPtr, m_transformationMatrix);
			}
		}
	}
	

	private int textureIndex = 0;
	
	public abstract void respondToCollision();
	
	public TREntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scaleX = scale;
		this.scaleY = scale;
		this.scaleZ = scale;
		this.boundingBox = new BoundingBox(this.getModel().getRawModel().getBoundingBox());
		this.staticBoundingBox = new BoundingBox(boundingBox);
		
		this.isOrganizationNode = false;
	}
	
	public TREntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scaleX, float scaleY, float scaleZ) {
		
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.scaleZ = scaleZ;
		this.boundingBox = new BoundingBox(this.getModel().getRawModel().getBoundingBox());
		this.staticBoundingBox = new BoundingBox(boundingBox);
		
		this.isOrganizationNode = false;
	}
	
	public TREntity(TexturedModel model, int index, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scaleX = scale;
		this.scaleY = scale;
		this.scaleZ = scale;
		this.boundingBox = new BoundingBox(this.getModel().getRawModel().getBoundingBox());
		this.staticBoundingBox = new BoundingBox(boundingBox);
		
		this.isOrganizationNode = false;
	}
	/**
	 * protected constructor reserved for construction of organization nodes (including root nodes)
	 */
	protected TREntity() {
		this.staticBoundingBox = null;
		this.isOrganizationNode = true;
		this.enableChildren();
	}


	public float getTextureXOffset() {
		int column = textureIndex % model.getTexture().getNumRows();
		
		return (float) column / (float) model.getTexture().getID();
	}
	
	public float getTextureYOffset() {
		int row = textureIndex / model.getTexture().getNumRows();
		
		return (float) row / (float) model.getTexture().getNumRows();
	}
	
	public void move(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void rotate(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}
	
	public void setRotVec(Vector3f rot) {
		this.rotX = rot.x;
		this.rotY = rot.y;
		this.rotZ = rot.z;
	}

	public Vector3f getScale() {
		return new Vector3f(scaleX, scaleY, scaleZ);
	}

	public void setScale(float scaleX, float scaleY, float scaleZ) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.scaleZ = scaleZ;
	}
	
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}
	
	public BoundingBox getStaticBoundingBox() {
		return new BoundingBox(staticBoundingBox);
	}
	
	
	private void updateBoundingBox() {
		
		BoundingBox bb = this.boundingBox;
		
		bb.minX = this.worldPosition.x + this.staticBoundingBox.minX;
		bb.minY = this.worldPosition.y + this.staticBoundingBox.minY + this.bbyoffset;
		bb.minZ = this.worldPosition.z + this.staticBoundingBox.minZ;
		bb.maxX = this.worldPosition.x + this.staticBoundingBox.maxX;
		bb.maxY = this.worldPosition.y + this.staticBoundingBox.maxY + this.bbyoffset;
		bb.maxZ = this.worldPosition.z + this.staticBoundingBox.maxZ;
		
		Vector3f vec = this.getWorldScale();
		
		if (vec.x > 1) {
			float modX = (vec.x - 1) * (bb.maxX - bb.minX) / 2.f;
			bb.minX -= modX;
			bb.maxX += modX;
		}
		else if (vec.x < 1) {
			float modX = (1 - vec.x) * (bb.maxX - bb.minX) / 2.f;
			bb.minX -= modX;
			bb.maxX += modX;
		}
		
		if (vec.y > 1) {
			float modY = (vec.y - 1) * (bb.maxY - bb.minY) / 2.f;
			bb.minY -= modY;
			bb.maxY += modY;
		}
		else if (vec.y < 1) {
			float modY = (1 - vec.y) * (bb.maxY - bb.minY) / 2.f;
			bb.minY -= modY;
			bb.maxY += modY;
		}
		
		if (vec.z > 1) {
			float modZ = (vec.z - 1) * (bb.maxZ - bb.minZ) / 2.f;
			bb.minZ -= modZ;
			bb.maxZ += modZ;
		}
		else if (vec.z < 1) {
			float modZ = (1 - vec.z) * (bb.maxZ - bb.minZ) / 2.f;
			bb.minZ -= modZ;
			bb.maxZ += modZ;
		}
		
		bb.sphereRadius = this.staticBoundingBox.sphereRadius * Math.max(Math.max(vec.x, vec.y), vec.z);
		
	}

}
