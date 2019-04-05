package engine.renderEngine.guis;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import engine.fontMeshCreator.FontType;
import engine.fontMeshCreator.GUIText;
import engine.renderEngine.TRDisplayManager;
import engine.renderEngine.Loader;
import engine.utils.SFMath;
import engine.utils.TRUtils;

public abstract class SFVerticalSlider implements ISlider, IGUI {

	private SFVerticalSlider that = this;
	private SFAbstractButton slide;
	private GUITexture background;
	private boolean isHidden = true;
	
	private List<IGUI> marks;
	private List<GUIText> markTexts;
	
	private GUIText counter;
	private DecimalFormat df;
	private Callable<String> run;
	
	public Vector2f getPosition() {
		return background.getPosition();
	}
	
	public float getPosX() {
		return background.getPosition().x;
	}
	
	public float getTopY() {
		return background.getPosition().y + background.getScale().y + slide.getTexture().getScale().y;
	}
	
	@Override
	public void move(float dx, float dy) {
		background.move(dx, dy);
		if (counter != null)
			counter.move(dx, dy);
		slide.move(dx, dy);
		for (IGUI el : marks) {
			el.move(dx, dy);
		}
		for (GUIText el : markTexts) {
			el.move(dx, dy);
		}
	}
	
	public SFVerticalSlider(float sliderLength, Vector2f position, Vector2f scale, String slidetex, String backgroundtex) {
		
		marks = new ArrayList<IGUI>();
		markTexts = new ArrayList<GUIText>();
		
		slide = new SFAbstractButton(slidetex, position, scale, 0, 0.1f) {
			
			@Override
			public void whileHovering(IButton button) {
				
			}
			
			@Override
			public void whileHolding(IButton button) {
				//this.move(0, (float) (Mouse.getDY() * 0.001));
				this.getTexture().getPosition().y = Mouse.getY();
			}
			
			@Override
			public void onStopHover(IButton button) {
				sliderStopHover(that);
				
			}
			
			@Override
			public void onStartHover(IButton button) {
				sliderStartHover(that);
				
			}
			
			@Override
			public void onClick(IButton button) {
				
			}
		};
		background = new GUITexture(Loader.loadTexture(backgroundtex), new Vector2f(position), new Vector2f(0.04f, scale.y * sliderLength * TRDisplayManager.getAspectRatio()));
	}
	
	public SFVerticalSlider(List<IGUI> list, float sliderLength, float xoff, float yoff, Vector2f position, Vector2f scale, String slidetex, String backgroundtex) {
		
		marks = new ArrayList<IGUI>();
		markTexts = new ArrayList<GUIText>();
		
		slide = new SFAbstractButton(slidetex, position, scale, xoff, yoff) {
			
			@Override
			public void whileHovering(IButton button) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void whileHolding(IButton button) {
				//this.move(0, (float) (Mouse.getDY() * 0.00125));
				this.getTexture().getPosition().y = Mouse.getY();
			}
			
			@Override
			public void onStopHover(IButton button) {
				sliderStopHover(that);
				
			}
			
			@Override
			public void onStartHover(IButton button) {
				sliderStartHover(that);
				
			}
			
			@Override
			public void onClick(IButton button) {
				
			}
		};
		background = new GUITexture(Loader.loadTexture(backgroundtex), new Vector2f(position), new Vector2f(0.02f, sliderLength));
		list.add(this);
	}
	
	public SFVerticalSlider(GUIStruct struct, float sliderLength, float xoff, float yoff, Vector2f position, Vector2f scale, String slidetex, String backgroundtex) {
		
		marks = new ArrayList<IGUI>();
		markTexts = new ArrayList<GUIText>();
		
		slide = new SFAbstractButton(slidetex, position, scale, xoff, yoff) {
			
			@Override
			public void whileHovering(IButton button) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void whileHolding(IButton button) {
				//this.move(0, (float) (Mouse.getDY() * 0.00125));
				this.getTexture().getPosition().y = -1.0f + 2.0f * Mouse.getY() / Display.getHeight();
			}
			
			@Override
			public void onStopHover(IButton button) {
				sliderStopHover(that);
				
			}
			
			@Override
			public void onStartHover(IButton button) {
				sliderStartHover(that);
				
			}
			
			@Override
			public void onClick(IButton button) {
				
			}
		};
		background = new GUITexture(Loader.loadTexture(backgroundtex), new Vector2f(position), new Vector2f(0.02f, sliderLength));
		struct.addChild(this);
	}
	
	public void addMark(List<GUITexture> guis, String texture, Vector2f scale, float xoffset, float value) {
		
		float distance = background.getScale().y * 2;
		float floor = background.getPosition().y - background.getScale().y;
		
		SFAbstractButton mark = new SFAbstractButton(this.marks, texture, 
				new Vector2f(background.getPosition().x + xoffset, floor + (distance * value)), scale, 0, 0) {
			
			@Override
			public void whileHovering(IButton button) {
				
			}
			
			@Override
			public void whileHolding(IButton button) {
				
			}
			
			@Override
			public void onStopHover(IButton button) {
				
			}
			
			@Override
			public void onStartHover(IButton button) {
				
			}
			
			@Override
			public void onClick(IButton button) {
				slide.getTexture().getPosition().y = floor + (distance * value);
			}
		};
		
		if (!isHidden) {
			mark.show(guis);
		}
				
	}
	
	public void addMark(List<GUITexture> guis, String texture, Vector2f scale, float xoffset, float value, String text, float txtsize, float 
		txtXOff, float r, float g, float b) {
		
		float distance = background.getScale().y * 2;
		float floor = background.getPosition().y - background.getScale().y;
		
		SFAbstractButton mark = new SFAbstractButton(this.marks, texture, 
				new Vector2f(background.getPosition().x + xoffset, floor + (distance * value)), scale, 0, 0) {
			
			@Override
			public void whileHovering(IButton button) {
				
			}
			
			@Override
			public void whileHolding(IButton button) {
				
			}
			
			@Override
			public void onStopHover(IButton button) {
				
			}
			
			@Override
			public void onStartHover(IButton button) {
				
			}
			
			@Override
			public void onClick(IButton button) {
				slide.getTexture().getPosition().y = floor + (distance * value);
			}
		};
		
		if (!isHidden) {
			mark.show(guis);
		}
		
		//-1.0f + 2.0f * background.getPosition().x + xoffset + txtXOff / Display.getWidth()
		//, 1.0f - 2.0f * floor + (distance * value) / Display.getHeight()
		
		float bgX = background.getPosition().x + xoffset + txtXOff;
		float bgY = floor + (distance * value);
		//System.out.println(bgY);
		
		/*GUIText o = new GUIText(text, txtsize, TaskManager.font, 
				new Vector2f((bgX + xoffset), bgY), 1, false);*/
		
		GUIText o = new GUIText(text, txtsize, TRUtils.trFont, 
				new Vector2f(((bgX / 2) + 0.5f), -(bgY / 2) + 0.5f - (scale.y / 4)), 1, false);
		
		o.setColour(r, g, b);
		markTexts.add(o);
		
		if (isHidden) {
			o.hide();
		}
				
	}
	
	@Override
	public void show(List<GUITexture> textures) {
		if (isHidden) {
			textures.add(background);
			slide.show(textures);
			for (IGUI el : marks) {
				el.show(textures);
			}
			for (GUIText el : markTexts) {
				el.show();
			}
			isHidden = false;
			if (this.counter != null)
				this.counter.show();
		}
	}

	@Override
	public void update() {
		slide.update();
		
		for (IGUI el : marks) {
			el.update();
		}
			
			/*if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
				markTexts.get(0).getPosition().x += 0.01;
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
				markTexts.get(0).getPosition().x -= 0.01;
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
				markTexts.get(0).getPosition().y += 0.01;
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
				markTexts.get(0).getPosition().y -= 0.01;
			}
			
		System.out.println(markTexts.get(0).getPosition());*/
		
		float pos = slide.getTexture().getPosition().y;
		float scale = slide.getTexture().getScale().y;
		float bpos = background.getPosition().y;
		float bscale = background.getScale().y;
		if (pos + scale > bpos + bscale + scale) {
			slide.getTexture().getPosition().y = bpos + bscale; 
		}
		else if (pos - scale < bpos - bscale - scale) {
			slide.getTexture().getPosition().y = bpos - bscale;
		}
		
		if (this.counter != null) {
			if (this.run != null) {
				try {
					this.counter.setText(run.call());
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
			else
				this.counter.setText(df.format(getSliderValue()));
		}
		
	}
	
	@Override
	public void hide(List<GUITexture> textures) {
		if (!isHidden) {
			textures.remove(background);
			slide.hide(textures);
			for (IGUI el : marks) {
				el.hide(textures);
			}
			for (GUIText el : markTexts) {
				el.hide();
			}
			isHidden = true;
			if (this.counter != null)
				this.counter.hide();
		}
	}
	
	public float getSliderValue() {
		float floor = background.getPosition().y - background.getScale().y;
		//return (slide.getTexture().getPosition().y - floor) / (background.getPosition().y + background.getScale().y - floor);
		/*System.out.println(slide.getTexture().getPosition().y);
		System.out.println(floor);
		System.out.println(background.getScale().y * 2 + "\nResult:");*/
		return (slide.getTexture().getPosition().y - floor) / (background.getScale().y * 2);
	}
	
	public void setCounter(float fontSize, FontType font, float yoffset,  float r, float g, float b) {
		this.counter = new GUIText("", fontSize, font, SFMath.coordtext(this.getPosX() - 0.01f, this.getTopY() + yoffset), 0.25f, false);
		this.counter.setColour(r, g, b);
		this.df = new DecimalFormat("#.###");
	}
	
	public void setCounter(float fontSize, FontType font, float yoffset, float r, float g, float b, Callable<String> run) {
		this.counter = new GUIText("", fontSize, font, SFMath.coordtext(this.getPosX() - 0.01f, this.getTopY() + yoffset), 0.25f, false);
		this.counter.setColour(r, g, b);
		this.run = run;
	}
	
}
