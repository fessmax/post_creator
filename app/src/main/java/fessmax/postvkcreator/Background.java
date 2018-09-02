package fessmax.postvkcreator;

import java.util.ArrayList;

public class Background {
    public boolean fromGallery;
    public int bigImageResId;
    public int smallImageResId;

    public Background(){
        this.fromGallery = true;
        this.smallImageResId = R.drawable.ic_toolbar_new;
    }

    public Background(int bigImageResId, int smallImageResId) {
        this.fromGallery = false;
        this.bigImageResId = bigImageResId;
        this.smallImageResId = smallImageResId;
    }

    public static Background[] generateBackgrounds(){
        ArrayList<Background> list = new ArrayList<>();
        list.add(new Background(R.drawable.bg_gradient_white, R.drawable.bg_gradient_white));
        list.add(new Background(R.drawable.bg_gradient_blue, R.drawable.bg_gradient_blue));
        list.add(new Background(R.drawable.bg_gradient_green, R.drawable.bg_gradient_green));
        list.add(new Background(R.drawable.bg_gradient_orange, R.drawable.bg_gradient_orange));
        list.add(new Background(R.drawable.bg_gradient_purple, R.drawable.bg_gradient_purple));
        list.add(new Background(R.drawable.bg_gradient_red, R.drawable.bg_gradient_red));
        list.add(new Background(R.drawable.bg_stars_center, R.drawable.thumb_stars));
        list.add(new Background(R.drawable.bg_beach, R.drawable.thumb_beach));
        list.add(new Background());

        return list.toArray(new Background[list.size()]);
    }

}
