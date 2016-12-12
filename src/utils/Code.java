package utils;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;

public class Code {
	public static Map<Character, Color> codeColor;

	static {
		codeColor = new HashMap<>();
		codeColor.put('A', Color.valueOf("#4d83fd"));
		codeColor.put('B', Color.valueOf("#69cb54"));
		codeColor.put('C', Color.valueOf("#dc7364"));
		codeColor.put('D', Color.valueOf("#9413e0"));
		codeColor.put('E', Color.valueOf("#393d73"));
		codeColor.put('F', Color.valueOf("#561703"));
		codeColor.put('G', Color.valueOf("#601ba4"));
		codeColor.put('H', Color.valueOf("#f0c7e5"));
		codeColor.put('I', Color.valueOf("#0b9f65"));
		codeColor.put('J', Color.valueOf("#c33550"));
		codeColor.put('K', Color.valueOf("#acfc72"));
		codeColor.put('L', Color.valueOf("#e9b318"));
		codeColor.put('M', Color.valueOf("#03ceea"));
		codeColor.put('N', Color.valueOf("#7060fa"));
		codeColor.put('O', Color.valueOf("#9e642e"));
		codeColor.put('P', Color.valueOf("#283c85"));
		codeColor.put('Q', Color.valueOf("#ff714e"));
		codeColor.put('R', Color.valueOf("#a1826c"));
		codeColor.put('S', Color.valueOf("#2d2cc3"));
		codeColor.put('T', Color.valueOf("#385597"));
		codeColor.put('U', Color.valueOf("#800dbb"));
		codeColor.put('V', Color.valueOf("#038c1e"));
		codeColor.put('W', Color.valueOf("#e61464"));
		codeColor.put('X', Color.valueOf("#f6f475"));
		codeColor.put('Y', Color.valueOf("#eb223b"));
		codeColor.put('Z', Color.valueOf("#605070"));
	}

	public static Color getCodeColor(char code) {
		return codeColor.get(code);
	}
}
