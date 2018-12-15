package gameplay.entities.players.voyager;

import java.util.HashMap;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import box.TM;
import fontMeshCreator.GUIText;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.guis.GUIStruct;
import renderEngine.guis.GUITexture;
import renderEngine.guis.IButton;
import renderEngine.guis.IGUI;
import renderEngine.guis.ISlider;
import renderEngine.guis.SFAbstractButton;
import renderEngine.guis.SFVerticalSlider;

public class VoyagerGUISys {
		
	private static List<IGUI> guiElements;
	
	private static List<ControlPanel> tacticalPanelGroup;
	private static List<ControlPanel> helmPanelGroup;
	private static List<ControlPanel> opsPanelGroup;
	private static ControlPanel indexPanel;
	private static List<IGUI> tabs;
	private static List<GUIText> tabTexts;
	
	static HashMap<Integer, List<ControlPanel>> hashref = new HashMap<Integer, List<ControlPanel>>();
	
	static final int INDEX_GROUP = 0;
	static final int TACTICAL_GROUP = 1;
	static final int HELM_GROUP = 2;
	static final int OPS_GROUP = 3;
	
	static int activeGroup = INDEX_GROUP;
	static int tabIndex = 0;
	
	private static PlayerVoyager player;
		
	private static int bluerect = Loader.loadTexture("sqgui");
	private static int filledrect = Loader.loadTexture("sqguifilled");
	
	private static final Vector2f WEAPON_ARRAY_POS_STD = new Vector2f(0.55f, -0.2f);
	private static final Vector2f WEAPON_ARRAY_POS_RIGHT = new Vector2f(0.7f, -0.2f);
	GUIStruct struct = new GUIStruct(new Vector2f(WEAPON_ARRAY_POS_STD));
	GUIStruct indexChanges = new GUIStruct(new Vector2f(0.5f, -1 + TM.sqr4.y));
	//GUIStruct indexChanges = new GUIStruct(new Vector2f(0.5f, 0.2f));
	
	GUITexture schematic = new GUITexture(Loader.loadTexture("schematic1"), new Vector2f(0, 0), new Vector2f(0.233f, 0.466f));
	
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
	
	//Sliders
	SFVerticalSlider sliderBackPortArrays;
	SFVerticalSlider sliderBackStarbArrays;
	
	SFAbstractButton toggleshields;
	SFAbstractButton buttonBackMountedPhaser;
	SFAbstractButton buttonBackEndPhaser;
	
	SFAbstractButton buttonBackMountedTorpedo;
	SFAbstractButton buttonBackEndTorpedo;
	
	public VoyagerGUISys(PlayerVoyager player) {
		
		guiElements = player.guiElements;
		
		tacticalPanelGroup = player.tacticalPanelGroup;
		helmPanelGroup = player.helmPanelGroup;
		opsPanelGroup = player.opsPanelGroup;
		tabs = player.tabs;
		tabTexts = player.tabTexts;
		hashref.put(TACTICAL_GROUP, tacticalPanelGroup);
		hashref.put(HELM_GROUP, helmPanelGroup);
		hashref.put(OPS_GROUP, opsPanelGroup);
		
		VoyagerGUISys.player = player;
		setupTactical();
				
		player.indexPanel = new ControlPanel("index") {
			
			@Override
			public void init() {
				struct.show(player.getGuis());
				indexChanges.show(player.getGuis());
				struct.setPosition(WEAPON_ARRAY_POS_STD);
			}
		};
		
		indexPanel = player.indexPanel;
				
	}
	
	void setActiveGroup(int group) {
		
		tabIndex = 0;
		
		for (IGUI el : guiElements) {
			el.hide(player.getGuis());
		}
		
		for (IGUI el : tabs) {
			el.hide(player.getGuis());
		}
		for (GUIText el : tabTexts) {
			el.hide();
		}
		
		tabs.clear();
		tabTexts.clear();
		
		activeGroup = group;
		
		if (activeGroup != INDEX_GROUP) {
			
			float stride = -1 + TM.sqr4.y;
			
			Vector2f scale = new Vector2f(TM.sqr4);
			scale.x *= 2f;
		
			List<ControlPanel> list = hashref.get(activeGroup);
			
			int i;
			for (i = 0; i < list.size(); i++) {
				int a = i;
				ControlPanel el = list.get(i);
				Vector2f pos = new Vector2f(0.5f, stride);
				tabs.add(new SFAbstractButton("sqgui", pos, scale) {
					
					@Override
					public void whileHovering(IButton button) {
						
					}
					
					@Override
					public void whileHolding(IButton button) {
						
					}
					
					@Override
					public void onStopHover(IButton button) {
						this.getTexture().setTexture(bluerect);
						tabTexts.get(a).setColour(1, 1, 1);
					}
					
					@Override
					public void onStartHover(IButton button) {
						this.getTexture().setTexture(filledrect);
						tabTexts.get(a).setColour(0, 0, 0);
					}
					
					@Override
					public void onClick(IButton button) {
						tabIndex = a;
						setUpPanel();
					}
				});
				
				tabTexts.add(new GUIText(el.name, 1.2f, TM.font, TM.coordtextcenter(pos, scale.x, scale.y), scale.x, true).setColourret(1, 1, 1));
				stride += TM.sqr4.y * 2;
			}
			
			
			int a = i;
			Vector2f pos = new Vector2f(0.5f, stride);
			tabs.add(new SFAbstractButton("sqgui", pos, scale) {
				
				@Override
				public void whileHovering(IButton button) {
					
				}
				
				@Override
				public void whileHolding(IButton button) {
					
				}
				
				@Override
				public void onStopHover(IButton button) {
					this.getTexture().setTexture(bluerect);
					tabTexts.get(a).setColour(1, 1, 1);
				}
				
				@Override
				public void onStartHover(IButton button) {
					this.getTexture().setTexture(filledrect);
					tabTexts.get(a).setColour(0, 0, 0);
				}
				
				@Override
				public void onClick(IButton button) {
					setActiveGroup(INDEX_GROUP);
				}
			});
			
			tabTexts.add(new GUIText("index", 1.2f, TM.font, TM.coordtextcenter(pos, scale.x, scale.y), scale.x, true).setColourret(1, 1, 1));
			stride += TM.sqr4.y * 2;
			
			
			setUpPanel();
		}
		else {
			indexPanel.init();
		}
		
		for (IGUI el : tabs) {
			el.show(player.getGuis());
		}
		
	}
	
	void setUpPanel() {
		
		for (IGUI el : guiElements) {
			el.hide(player.getGuis());
		}
		
		ControlPanel panel = hashref.get(activeGroup).get(tabIndex);
		panel.init();
		
	}
	
	private void setupTactical() {
		
		struct.addChild(schematic);
		
		int a0 = Loader.loadTexture("stdbutton");
		int b0 = Loader.loadTexture("stdbuttonfilled");
		
		int a1 = Loader.loadTexture("voyphaserdiag2");
		int b1 = Loader.loadTexture("voyphaserdiagactive");
		//BOOKMARK front phaser shoot button 
		buttonFrontPhasers = new SFAbstractButton(struct, "voyphaserdiag2", new Vector2f(-0.0045f, 0.375f), TM.sqr8) {
			
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
		button_port_front_phaser = new SFAbstractButton(struct, "stdbutton", new Vector2f(0.09f, 0.435f), TM.sqr2) {
			
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
		
		button_center_front_phaser = new SFAbstractButton(struct, "stdbutton", new Vector2f(0.09f, 0.385f), TM.sqr2) {
			
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
		
		button_starb_front_phaser = new SFAbstractButton(struct, "stdbutton", new Vector2f(0.09f, 0.335f), TM.sqr2) {
			
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
		buttonFrontPhotons = new SFAbstractButton(struct, "guisys", new Vector2f(-0.0545f, 0.25f), TM.sqr8) {
			
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
		button_port_front_photon = new SFAbstractButton(struct, "stdbutton", new Vector2f(0.1f, 0.275f), TM.sqr2) {
			
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
		
		button_starb_front_photon = new SFAbstractButton(struct, "stdbutton", new Vector2f(0.125f, 0.275f), TM.sqr2) {
			
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
		button_port_front_quantum = new SFAbstractButton(struct, "stdbutton", new Vector2f(0.1f, 0.225f), TM.sqr2) {
			
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
		
		button_starb_front_quantum = new SFAbstractButton(struct, "stdbutton", new Vector2f(0.125f, 0.225f), TM.sqr2) {
			
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
		buttonFrontQuantums = new SFAbstractButton(struct, "guisys", new Vector2f(0.0455f, 0.25f), TM.sqr8) {
			
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
		buttonPhaserSpray = new SFAbstractButton(struct, "subsec", new Vector2f(-0.0045f, 0.175f), TM.sqr4) {
			
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
		Vector2f varl = new Vector2f(0.015f, 0.01f * DisplayManager.getAspectRatio());
		//BOOKMARK shoot port arrays
		buttonPortArrays1 = new SFAbstractButton(struct, "sqgui", new Vector2f(-0.055f, 0.15f), varl) {
			
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

		buttonPortArrays2 = new SFAbstractButton(struct, "sqgui", new Vector2f(-0.055f, 0.1f), varl) {
			
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
		
		buttonBackPortArrays = new SFAbstractButton(struct, "sqgui", new Vector2f(-0.055f, 0.05f), varl) {
			
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
		buttonStarbArrays1 = new SFAbstractButton(struct, "sqgui", new Vector2f(0.0455f, 0.15f), varl) {
			
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
		
		buttonStarbArrays2 = new SFAbstractButton(struct, "sqgui", new Vector2f(0.0455f, 0.1f), varl) {
			
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
		
		buttonBackStarbArrays = new SFAbstractButton(struct, "sqgui", new Vector2f(0.0455f, 0.05f), varl) {
			
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
		sliderBackPortArrays = new SFVerticalSlider(struct, 0.12f, -0.01f, 0, new Vector2f(-0.075f, -0.045f), TM.sqr4, "knob", "tramp") {
			
			@Override
			public void sliderStopHover(ISlider slider) {
				
			}
			
			@Override
			public void sliderStartHover(ISlider slider) {
				
			}
		};
		
		//BOOKMARK starboard phaser angle slider
		sliderBackStarbArrays = new SFVerticalSlider(struct, 0.12f, -0.01f, 0, new Vector2f(0.075f, -0.045f), TM.sqr4, "knob", "tramp") {
			
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
		toggleshields = new SFAbstractButton(struct, "shieldiconfilled", new Vector2f(0, 0.51f), TM.sqr4) {
			
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
		buttonBackMountedPhaser = new SFAbstractButton(struct, "rect", new Vector2f(-0.0045f, 0.075f), TM.sqr4) {

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
		buttonBackEndPhaser = new SFAbstractButton(struct, "rect", new Vector2f(-0.0045f, -0.34f), TM.sqr4) {

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
		buttonBackMountedTorpedo = new SFAbstractButton(struct, "junction", new Vector2f(-0.0045f, 0), TM.sqr4) {
			
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
		buttonBackEndTorpedo = new SFAbstractButton(struct, "junction", new Vector2f(-0.0045f, -0.1f), TM.sqr4) {
			
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
		
		Vector2f beta = new Vector2f(TM.sqr4);
		beta.x *= 2;
		SFAbstractButton changeToTactical = new SFAbstractButton(indexChanges, "sqgui", new Vector2f(0, 0), beta) {
			
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
				setActiveGroup(TACTICAL_GROUP);
			}
		};
		
		indexChanges.addChildWithoutTransform(new GUIText("tactical", 1.2f, TM.font, TM.coordtextcenter(new Vector2f(0.5f, -1 + TM.sqr4.y), beta.x, beta.y), beta.x, true));
		
		guiElements.add(struct);
		guiElements.add(indexChanges);
		struct.show(player.getGuis());
		indexChanges.show(player.getGuis());
		
		tacticalPanelGroup.add(new ControlPanel("normal") {
			
			@Override
			public void init() {
				struct.show(player.getGuis());
				struct.setPosition(new Vector2f(WEAPON_ARRAY_POS_RIGHT));
			}
		});
		
		tacticalPanelGroup.add(new ControlPanel("hai") {
			
			@Override
			public void init() {
				
			}
		});
		
		
	}
	
}
