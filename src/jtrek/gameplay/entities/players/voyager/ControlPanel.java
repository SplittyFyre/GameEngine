package jtrek.gameplay.entities.players.voyager;

import java.util.ArrayList;
import java.util.List;

import engine.renderEngine.guis.IGUI;

public abstract class ControlPanel {
	
	private List<IGUI> contents = new ArrayList<IGUI>();
	
	public String name;
	
	public ControlPanel(String name) {
		this.name = name;
	}
	
	public void update() {
		for (IGUI el : contents) {
			el.update();
		}
	}
	
	public abstract void init();

}
