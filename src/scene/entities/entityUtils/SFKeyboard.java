package scene.entities.entityUtils;

import org.lwjgl.input.Keyboard;

public class SFKeyboard {
	
	private static boolean numkey_allow = true;
	
	public static char getNumKeys() {
		
		if (!Keyboard.getEventKeyState()) {
			numkey_allow = true;
			return ' ';	
		}
		else {
			numkey_allow = false;
		}
		
		if (numkey_allow) {
			return ' ';
		}
		
		switch (Keyboard.getEventKey()) {
		
		case Keyboard.KEY_0:
			return '0';
		case Keyboard.KEY_1:
			return '1';
		case Keyboard.KEY_2:
			return '2';
		case Keyboard.KEY_3:
			return '3';
		case Keyboard.KEY_4:
			return '4';
		case Keyboard.KEY_5:
			return '5';
		case Keyboard.KEY_6:
			return '6';
		case Keyboard.KEY_7:
			return '7';
		case Keyboard.KEY_8:
			return '8';
		case Keyboard.KEY_9:
			return '9';
		case Keyboard.KEY_MINUS:
			return '-';
		case Keyboard.KEY_PERIOD:
			return '.';
		default:
			return ' ';
			
		}
		
	}

}
