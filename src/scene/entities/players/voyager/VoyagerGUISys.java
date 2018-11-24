package scene.entities.players.voyager;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import box.TM;
import renderEngine.Loader;
import renderEngine.guis.IButton;
import renderEngine.guis.IGUI;
import renderEngine.guis.ISlider;
import renderEngine.guis.SFAbstractButton;
import renderEngine.guis.SFVerticalSlider;
import renderEngine.textures.GUITexture;

public class VoyagerGUISys {
	
	private static Vector2f panelpos = PlayerVoyager.panelpos;
	private static Vector2f schmpos = PlayerVoyager.schmpos;
	//schmpos = [0.4045, -0.3f]
	
	private static GUITexture gui_panel = new GUITexture(Loader.loadTexture("LCARSpanel"), panelpos, new Vector2f(0.35f, 0.7f));
	private static GUITexture schematic = new GUITexture(Loader.loadTexture("schematic1"), schmpos, new Vector2f(0.233f, 0.466f));
	
	
	private static List<IGUI> tacticalElements;
	private static List<IGUI> opsElements;
	private static List<IGUI> helmElements;
	private static List<IGUI> miscElements;
	
	private static PlayerVoyager player;
	
	public VoyagerGUISys(PlayerVoyager player) {
		tacticalElements = player.tacticalElements;
		opsElements = player.opsElements;
		helmElements = player.helmElements;
		miscElements = player.miscElements;
		VoyagerGUISys.player = player;
		setupTactical();
	}
	
	SFAbstractButton buttonFrontPhasers;
	
	SFAbstractButton button_port_front_phaser;
	SFAbstractButton button_center_front_phaser;
	SFAbstractButton button_starb_front_phaser;
	
	SFAbstractButton buttonFrontPhotons;
	
	SFAbstractButton button_port_front_photon;
	SFAbstractButton button_starb_front_photon;
	
	SFAbstractButton buttonFrontQuantums;
	
	SFAbstractButton button_port_front_quantum;
	SFAbstractButton button_starb_front_quantum;
	
	SFAbstractButton buttonPhaserSpray;
	SFAbstractButton buttonPortArrays1;
	SFAbstractButton buttonPortArrays2;
	SFAbstractButton buttonBackPortArrays;
	SFAbstractButton buttonStarbArrays1;
	SFAbstractButton buttonStarbArrays2;
	SFAbstractButton buttonBackStarbArrays;
	
	SFVerticalSlider sliderBackPortArrays;
	SFVerticalSlider sliderBackStarbArrays;
	
	SFAbstractButton toggleshields;
	SFAbstractButton buttonBackMountedPhaser;
	SFAbstractButton buttonBackEndPhaser;
	
	SFAbstractButton buttonBackMountedTorpedo;
	SFAbstractButton buttonBackEndTorpedo;
	
	private void setupTactical() {
		
		int a0 = Loader.loadTexture("stdbutton");
		int b0 = Loader.loadTexture("stdbuttonfilled");
		
		int a1 = Loader.loadTexture("voyphaserdiag2");
		int b1 = Loader.loadTexture("voyphaserdiagactive");
		//BOOKMARK front phaser shoot button 
		buttonFrontPhasers = new SFAbstractButton(tacticalElements, "voyphaserdiag2", schematic.getPosition(), new Vector2f(-0.0045f, 0.375f), TM.sqr8) {
			
			@Override
			public void whileHovering(IButton button) {
		
			}
			
			@Override
			public void onStopHover(IButton button) {
				this.getTexture().setTexture(a1);
			}
			
			@Override
			public void onStartHover(IButton button) {
				this.getTexture().setTexture(b1);
			}
			
			@Override
			public void onClick(IButton button) {
				
			}

			@Override
			public void whileHolding(IButton button) {
				player.fireFrontPhasers();
			}
		};
		
		//BOOKMARK fire individual phaser cannons
		button_port_front_phaser = new SFAbstractButton(tacticalElements, "stdbutton", schematic.getPosition(), new Vector2f(0.09f, 0.435f), TM.sqr2) {
			
			@Override
			public void whileHovering(IButton button) {
		
			}
			
			@Override
			public void onStopHover(IButton button) {
				this.getTexture().setTexture(a0);
			}
			
			@Override
			public void onStartHover(IButton button) {
				this.getTexture().setTexture(b0);
			}
			
			@Override
			public void onClick(IButton button) {
				
			}

			@Override
			public void whileHolding(IButton button) {
				player.fire_port_front_phaser();
			}
		};
		
		button_center_front_phaser = new SFAbstractButton(tacticalElements, "stdbutton", schematic.getPosition(), new Vector2f(0.09f, 0.385f), TM.sqr2) {
			
			@Override
			public void whileHovering(IButton button) {
		
			}
			
			@Override
			public void onStopHover(IButton button) {
				this.getTexture().setTexture(a0);
			}
			
			@Override
			public void onStartHover(IButton button) {
				this.getTexture().setTexture(b0);
			}
			
			@Override
			public void onClick(IButton button) {
				
			}

			@Override
			public void whileHolding(IButton button) {
				player.fire_center_front_phaser();
			}
		};
		
		button_starb_front_phaser = new SFAbstractButton(tacticalElements, "stdbutton", schematic.getPosition(), new Vector2f(0.09f, 0.335f), TM.sqr2) {
			
			@Override
			public void whileHovering(IButton button) {
		
			}
			
			@Override
			public void onStopHover(IButton button) {
				this.getTexture().setTexture(a0);
			}
			
			@Override
			public void onStartHover(IButton button) {
				this.getTexture().setTexture(b0);
			}
			
			@Override
			public void onClick(IButton button) {
				
			}

			@Override
			public void whileHolding(IButton button) {
				player.fire_starb_front_phaser();
			}
		};
		
		int a2 = Loader.loadTexture("guisys");
		int b2 = Loader.loadTexture("guisysfilled");
		//BOOKMARK front double photon shots
		buttonFrontPhotons = new SFAbstractButton(tacticalElements, "guisys", schmpos, new Vector2f(-0.0545f, 0.25f), TM.sqr8) {
			
			@Override
			public void whileHovering(IButton button) {
				
			}
			
			@Override
			public void whileHolding(IButton button) {
				
			}
			
			@Override
			public void onStopHover(IButton button) {
				this.getTexture().setTexture(a2);
				
			}
			
			@Override
			public void onStartHover(IButton button) {
				this.getTexture().setTexture(b2);
				
			}
			
			@Override
			public void onClick(IButton button) {
				player.fireFrontPhotons();
			}
		};
		
		//BOOKMARK fire individual front photon tubes
		button_port_front_photon = new SFAbstractButton(tacticalElements, "stdbutton", schmpos, new Vector2f(0.1f, 0.275f), TM.sqr2) {
			
			@Override
			public void whileHovering(IButton button) {
				
			}
			
			@Override
			public void whileHolding(IButton button) {
				
			}
			
			@Override
			public void onStopHover(IButton button) {
				this.getTexture().setTexture(a0);
				
			}
			
			@Override
			public void onStartHover(IButton button) {
				this.getTexture().setTexture(b0);
				
			}
			
			@Override
			public void onClick(IButton button) {
				player.fire_port_front_photon();
			}
		};
		
		button_starb_front_photon = new SFAbstractButton(tacticalElements, "stdbutton", schmpos, new Vector2f(0.125f, 0.275f), TM.sqr2) {
			
			@Override
			public void whileHovering(IButton button) {
				
			}
			
			@Override
			public void whileHolding(IButton button) {
				
			}
			
			@Override
			public void onStopHover(IButton button) {
				this.getTexture().setTexture(a0);
				
			}
			
			@Override
			public void onStartHover(IButton button) {
				this.getTexture().setTexture(b0);
				
			}
			
			@Override
			public void onClick(IButton button) {
				player.fire_starb_front_photon();
			}
		};
		
		//BOOKMARK fire individual front quantum tubes
		button_port_front_quantum = new SFAbstractButton(tacticalElements, "stdbutton", schmpos, new Vector2f(0.1f, 0.225f), TM.sqr2) {
			
			@Override
			public void whileHovering(IButton button) {
				
			}
			
			@Override
			public void whileHolding(IButton button) {
				
			}
			
			@Override
			public void onStopHover(IButton button) {
				this.getTexture().setTexture(a0);
				
			}
			
			@Override
			public void onStartHover(IButton button) {
				this.getTexture().setTexture(b0);
				
			}
			
			@Override
			public void onClick(IButton button) {
				player.fire_port_front_quantum();
			}
		};
		
		button_starb_front_quantum = new SFAbstractButton(tacticalElements, "stdbutton", schmpos, new Vector2f(0.125f, 0.225f), TM.sqr2) {
			
			@Override
			public void whileHovering(IButton button) {
				
			}
			
			@Override
			public void whileHolding(IButton button) {
				
			}
			
			@Override
			public void onStopHover(IButton button) {
				this.getTexture().setTexture(a0);
				
			}
			
			@Override
			public void onStartHover(IButton button) {
				this.getTexture().setTexture(b0);
				
			}
			
			@Override
			public void onClick(IButton button) {
				player.fire_starb_front_quantum();
			}
		};
		
		//BOOKMARK front double quantum shots
		buttonFrontQuantums = new SFAbstractButton(tacticalElements, "guisys", schmpos, new Vector2f(0.0455f, 0.25f), TM.sqr8) {
			
			@Override
			public void whileHovering(IButton button) {
					
			}
			
			@Override
			public void whileHolding(IButton button) {
					
			}
				
			@Override
			public void onStopHover(IButton button) {
				this.getTexture().setTexture(a2);
				
			}
				
			@Override
			public void onStartHover(IButton button) {
				this.getTexture().setTexture(b2);
				
			}
				
			@Override
			public void onClick(IButton button) {
				player.fireFrontQuantums();
			}
		};
		
		buttonFrontPhotons.getTexture().setRotation(-15);
		buttonFrontQuantums.getTexture().setFlipped(true);
		buttonFrontQuantums.getTexture().setRotation(-15);
		
		int a3 = Loader.loadTexture("subsec");
		int b3 = Loader.loadTexture("subsecfilled");
		//BOOKMARK phaser spray
		buttonPhaserSpray = new SFAbstractButton(tacticalElements, "subsec", schmpos, new Vector2f(-0.0045f, 0.175f), TM.sqr4) {
			
			@Override
			public void whileHovering(IButton button) {
				
			}
			
			@Override
			public void whileHolding(IButton button) {
				player.firePhaserSpray();
			}
			
			@Override
			public void onStopHover(IButton button) {
				this.getTexture().setTexture(a3);
			}
			
			@Override
			public void onStartHover(IButton button) {
				this.getTexture().setTexture(b3);
			}
			
			@Override
			public void onClick(IButton button) {
				
			}
		};
		
		buttonPhaserSpray.getTexture().setRotation(180);
		
		int a4 = Loader.loadTexture("sqgui");
		int b4 = Loader.loadTexture("sqguifilled");
		Vector2f varl = new Vector2f(0.03f / TM.GUI_SCALE_DIV, 0.02f);
		//BOOKMARK shoot port arrays
		buttonPortArrays1 = new SFAbstractButton(tacticalElements, "sqgui", schmpos, new Vector2f(-0.055f, 0.15f), varl) {
			
			@Override
			public void whileHovering(IButton button) {
				
			}
			
			@Override
			public void whileHolding(IButton button) {
				player.firePortArrays(true);
			}
			
			@Override
			public void onStopHover(IButton button) {
				this.getTexture().setTexture(a4);
			}
			
			@Override
			public void onStartHover(IButton button) {
				this.getTexture().setTexture(b4);
			}
			
			@Override
			public void onClick(IButton button) {
				
			}
		};

		buttonPortArrays2 = new SFAbstractButton(tacticalElements, "sqgui", schmpos, new Vector2f(-0.055f, 0.1f), varl) {
			
			@Override
			public void whileHovering(IButton button) {
				
			}
			
			@Override
			public void whileHolding(IButton button) {
				player.firePortArrays(false); 
			}
			
			@Override
			public void onStopHover(IButton button) {
				this.getTexture().setTexture(a4);
			}
			
			@Override
			public void onStartHover(IButton button) {
				this.getTexture().setTexture(b4);
			}
			
			@Override
			public void onClick(IButton button) {
				
			}
		};
		
		buttonBackPortArrays = new SFAbstractButton(tacticalElements, "sqgui", schmpos, new Vector2f(-0.055f, 0.05f), varl) {
			
			@Override
			public void whileHovering(IButton button) {
				
			}
			
			@Override
			public void whileHolding(IButton button) {
				player.fireBackPortArrays(sliderBackPortArrays.getSliderValue() * 22.5f);
			}
			
			@Override
			public void onStopHover(IButton button) {
				this.getTexture().setTexture(a4);
			}
			
			@Override
			public void onStartHover(IButton button) {
				this.getTexture().setTexture(b4);
			}
			
			@Override
			public void onClick(IButton button) {
				 
			}
		};
		
		//BOOKMARK fire starboard arrays
		buttonStarbArrays1 = new SFAbstractButton(tacticalElements, "sqgui", schmpos, new Vector2f(0.0455f, 0.15f), varl) {
			
			@Override
			public void whileHovering(IButton button) {
				
			}
			
			@Override
			public void whileHolding(IButton button) {
				player.fireStarbArrays(true);
			}
			
			@Override
			public void onStopHover(IButton button) {
				this.getTexture().setTexture(a4);
			}
			
			@Override
			public void onStartHover(IButton button) {
				this.getTexture().setTexture(b4);
			}
			
			@Override
			public void onClick(IButton button) {
				
			}
		};
		
		buttonStarbArrays2 = new SFAbstractButton(tacticalElements, "sqgui", schmpos, new Vector2f(0.0455f, 0.1f), varl) {
			
			@Override
			public void whileHovering(IButton button) {
				
			}
			
			@Override
			public void whileHolding(IButton button) {
				player.fireStarbArrays(false); 
			}
			
			@Override
			public void onStopHover(IButton button) {
				this.getTexture().setTexture(a4);
			}
			
			@Override
			public void onStartHover(IButton button) {
				this.getTexture().setTexture(b4);
			}
			
			@Override
			public void onClick(IButton button) {
				
			}
		};
		
		buttonBackStarbArrays = new SFAbstractButton(tacticalElements, "sqgui", schmpos, new Vector2f(0.0455f, 0.05f), varl) {
			
			@Override
			public void whileHovering(IButton button) {
				
			}
			
			@Override
			public void whileHolding(IButton button) {
				player.fireBackStarbArrays(sliderBackStarbArrays.getSliderValue() * 22.5f);
			}
			
			@Override
			public void onStopHover(IButton button) {
				this.getTexture().setTexture(a4);
			}
			
			@Override
			public void onStartHover(IButton button) {
				this.getTexture().setTexture(b4);
			}
			
			@Override
			public void onClick(IButton button) {
				
			}
		};
		
		//BOOKMARK sliders for back array angles
		sliderBackPortArrays = new SFVerticalSlider(tacticalElements, 0.12f, -0.01f, 0, new Vector2f(0.325f, -0.345f), TM.sqr4, "knob", "tramp") {
			
			@Override
			public void sliderStopHover(ISlider slider) {
				
			}
			
			@Override
			public void sliderStartHover(ISlider slider) {
				
			}
		};
		
		//BOOKMARK starboard phaser angle slider
		sliderBackStarbArrays = new SFVerticalSlider(tacticalElements, 0.12f, -0.01f, 0, new Vector2f(0.48f, -0.345f), TM.sqr4, "knob", "tramp") {
			
			@Override
			public void sliderStopHover(ISlider slider) {
				
			}
			
			@Override
			public void sliderStartHover(ISlider slider) {
				
			}
		};
		
		int a5 = Loader.loadTexture("shieldiconfilled");
		int b5 = Loader.loadTexture("shieldicon");
		//BOOKMARK toggle shields
		toggleshields = new SFAbstractButton(tacticalElements, "shieldiconfilled", schmpos, new Vector2f(0, 0.51f), TM.sqr4) {
			
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
				boolean flag = player.checkShields();
				if (flag)
					this.getTexture().setTexture(a5);
				else 
					this.getTexture().setTexture(b5);
			}
		};
		
		int a8 = Loader.loadTexture("rect");
		int b8 = Loader.loadTexture("rectfilled");
		//BOOKMARK fire aft mounted phaser gun
		buttonBackMountedPhaser = new SFAbstractButton(tacticalElements, "rect", schmpos, new Vector2f(-0.0045f, 0.075f), TM.sqr4) {

			@Override
			public void onClick(IButton button) {
				
			}

			@Override
			public void whileHolding(IButton button) {
				player.fireBackMountedPhaser();
			}

			@Override
			public void onStartHover(IButton button) {
				this.getTexture().setTexture(b8);
			}

			@Override
			public void onStopHover(IButton button) {
				this.getTexture().setTexture(a8);
			}

			@Override
			public void whileHovering(IButton button) {
				
			}
			
		};
		
		//BOOKMARK fire backmost phaser gun
		buttonBackEndPhaser = new SFAbstractButton(tacticalElements, "rect", schmpos, new Vector2f(-0.0045f, -0.34f), TM.sqr4) {

			@Override
			public void onClick(IButton button) {
					
			}

			@Override
			public void whileHolding(IButton button) {
				player.fireBackEndPhaser();
			}
			
			@Override
			public void onStartHover(IButton button) {
				this.getTexture().setTexture(b8);
			}

			@Override
			public void onStopHover(IButton button) {
				this.getTexture().setTexture(a8);
			}

			@Override
			public void whileHovering(IButton button) {
					
			}
				
		};
		
		int a9 = Loader.loadTexture("junction");
		int b9 = Loader.loadTexture("junctionfilled");
		//BOOKMARK fire aft mounted torpedo
		buttonBackMountedTorpedo = new SFAbstractButton(tacticalElements, "junction", schmpos, new Vector2f(-0.0045f, 0), TM.sqr4) {
			
			@Override
			public void whileHovering(IButton button) {
				
			}
			
			@Override
			public void whileHolding(IButton button) {
				
			}
			
			@Override
			public void onStopHover(IButton button) {
				this.getTexture().setTexture(a9);
			}
			
			@Override
			public void onStartHover(IButton button) {
				this.getTexture().setTexture(b9);
			}
			
			@Override
			public void onClick(IButton button) {
				player.fireBackMountedTorpedo();
			}
		};
		
		//BOOKMARK fire backmost torpedo
		buttonBackEndTorpedo = new SFAbstractButton(tacticalElements, "junction", schmpos, new Vector2f(-0.0045f, -0.1f), TM.sqr4) {
			
			@Override
			public void whileHovering(IButton button) {
				
			}
			
			@Override
			public void whileHolding(IButton button) {
				
			}
			
			@Override
			public void onStopHover(IButton button) {
				this.getTexture().setTexture(a9);
			}
			
			@Override
			public void onStartHover(IButton button) {
				this.getTexture().setTexture(b9);
			}
			
			@Override
			public void onClick(IButton button) {
				player.fireBackEndTorpedo();
			}
		};
		
	}
	
}
