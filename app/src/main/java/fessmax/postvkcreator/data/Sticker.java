package fessmax.postvkcreator.data;

import java.util.ArrayList;

import fessmax.postvkcreator.R;

public class Sticker {
    public int imageName;
    public Sticker(int imageName){
        this.imageName = imageName;
    }

    public static Sticker[] generateStickers(){
        ArrayList<Sticker> stickers = new ArrayList<>();
/*
        for(int i=1;i<=24;i++){
            stickers.add(new Sticker("Stickers/"+i+".png"));
        }
*/
        stickers.add(new Sticker(R.drawable.sticker_1));
        stickers.add(new Sticker(R.drawable.sticker_2));
        stickers.add(new Sticker(R.drawable.sticker_3));
        stickers.add(new Sticker(R.drawable.sticker_4));
        stickers.add(new Sticker(R.drawable.sticker_5));
        stickers.add(new Sticker(R.drawable.sticker_6));
        stickers.add(new Sticker(R.drawable.sticker_7));
        stickers.add(new Sticker(R.drawable.sticker_8));
        stickers.add(new Sticker(R.drawable.sticker_9));
        stickers.add(new Sticker(R.drawable.sticker_10));
        stickers.add(new Sticker(R.drawable.sticker_11));
        stickers.add(new Sticker(R.drawable.sticker_12));
        stickers.add(new Sticker(R.drawable.sticker_13));
        stickers.add(new Sticker(R.drawable.sticker_14));
        stickers.add(new Sticker(R.drawable.sticker_15));
        stickers.add(new Sticker(R.drawable.sticker_16));
        stickers.add(new Sticker(R.drawable.sticker_17));
        stickers.add(new Sticker(R.drawable.sticker_18));
        stickers.add(new Sticker(R.drawable.sticker_19));
        stickers.add(new Sticker(R.drawable.sticker_20));
        stickers.add(new Sticker(R.drawable.sticker_21));
        stickers.add(new Sticker(R.drawable.sticker_22));
        stickers.add(new Sticker(R.drawable.sticker_23));
        stickers.add(new Sticker(R.drawable.sticker_24));
        return stickers.toArray(new Sticker[stickers.size()]);

    }
}
