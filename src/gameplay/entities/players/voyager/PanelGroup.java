package gameplay.entities.players.voyager;

import java.util.ArrayList;
import java.util.List;

import renderEngine.guis.IGUI;

public class PanelGroup {
	
	public List<ControlPanel> list;
	public List<IGUI> tabbuttons;
	
	public PanelGroup() {
		list = new ArrayList<ControlPanel>();
		tabbuttons = new ArrayList<IGUI>();
	}

}
