package fessmax.postvkcreator;

import java.util.ArrayList;

public class TextStyle {
    public int styleId;
    public int hintColor;
    public int cursorColor;
    public int spanColor;

    public TextStyle(int styleId, int hintColor, int cursorColor, int spanColor) {
        this.styleId = styleId;
        this.hintColor = hintColor;
        this.cursorColor = cursorColor;
        this.spanColor = spanColor;
    }

    public static ArrayList<TextStyle> generateSyles() {
        ArrayList<TextStyle> styles = new ArrayList<>();
        styles.add(new TextStyle(R.style.TextViewStyleSimple, 0x55000000, 0xff000000, 0xffffffff));
        styles.add(new TextStyle(R.style.TextViewStyleBlack, 0x55ffffff, 0xffffffff, 0xff000000));
        styles.add(new TextStyle(R.style.TextViewStylePink, 0x55e6ff, 0xffe6ff, 0xaaff1aff));
        return styles;
    }
}
