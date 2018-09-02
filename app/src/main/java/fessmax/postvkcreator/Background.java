package fessmax.postvkcreator;

import java.util.ArrayList;

public class Background {
    public boolean fromGallery;
    public int bigImageResId;
    public int smallImageResId;
    public boolean isGradient;

    public Background(){
        this.fromGallery = true;
        this.smallImageResId = R.drawable.bg_plus_tumb;
        this.isGradient = true;
    }

    public Background(int bigImageResId, int smallImageResId, boolean isGradient) {
        this.fromGallery = false;
        this.bigImageResId = bigImageResId;
        this.smallImageResId = smallImageResId;
        this.isGradient = isGradient;
    }

    public static Background[] generateBackgrounds(){
        ArrayList<Background> list = new ArrayList<>();
        list.add(new Background(R.drawable.bg_gradient_white_big, R.drawable.bg_gradient_white, true));
        list.add(new Background(R.drawable.bg_gradient_blue_big, R.drawable.bg_gradient_blue, true));
        list.add(new Background(R.drawable.bg_gradient_green_big, R.drawable.bg_gradient_green, true));
        list.add(new Background(R.drawable.bg_gradient_orange_big, R.drawable.bg_gradient_orange, true));
        list.add(new Background(R.drawable.bg_gradient_purple_big, R.drawable.bg_gradient_purple, true));
        list.add(new Background(R.drawable.bg_gradient_red_big, R.drawable.bg_gradient_red, true));
        list.add(new Background(R.drawable.bg_stars_center, R.drawable.bg_stars_tumb, false));
        list.add(new Background(R.drawable.bg_beach, R.drawable.thumb_beach, false));
        list.add(new Background());

        return list.toArray(new Background[list.size()]);
    }

}
