package engine.renderEngine;

import org.lwjgl.opengl.GL11;

import engine.postProcessing.Fbo;
import engine.scene.TRScene;
import engine.scene.particles.ParticleWatcher;

public class TRRenderEngine {
	
	public static TRRenderEngine activeInstance = null;
	
	public static final int RENDER_ENTITIES_BIT = 1;
	public static final int RENDER_TERRAIN_BIT = 2;
	public static final int RENDER_SKYBOX_BIT = 4;
	public static final int RENDER_DUDVWATER_BIT = 8;
	
	private MasterRenderSystem renderer;
		
	private final TRFrustumCuller frustumCuller;
	
	private TRProjectionCtx defaultProjection, activeProjection;
	
	public TRRenderEngine(int renderAvailableMasks, TRProjectionCtx projection) {
		this.defaultProjection = projection;
		this.activeProjection = defaultProjection;
		this.renderer = new MasterRenderSystem(renderAvailableMasks, defaultProjection.getMatrix());
		this.frustumCuller = new TRFrustumCuller(projection);
		TRRenderEngine.activeInstance = this;
		ParticleWatcher.init(this.activeProjection.getMatrix());
	}
	
	public void renderScene(TRScene scene, Fbo fbo) {
		this.frustumCuller.updateViewMatrix(scene.getCamera().getViewMatrix());
		renderer.renderMainPass(scene, fbo, this.frustumCuller);
	}
	
	public void cleanUp() {
		renderer.cleanUp();
		ParticleWatcher.cleanUp();
	}
	
	
	public void setProjection(TRProjectionCtx pctx) {
		this.activeProjection = pctx;
		updateSubProjections();
	}
	
	public void restoreDefaultProjection() {
		this.activeProjection = this.defaultProjection;
		updateSubProjections();
	}
	
	public void updateSubProjections() {
		this.frustumCuller.setProjection(this.activeProjection);
		this.renderer.setProjectionMatrix(this.activeProjection.getMatrix());
		ParticleWatcher.setProjectionMatrix(this.activeProjection.getMatrix());
	}
	
	public TRProjectionCtx getActiveProjection() {
		return this.activeProjection;
	}

}
