package fessmax.postvkcreator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import fessmax.postvkcreator.data.Sticker;

public class StickersActivity extends Activity {

    private final int COLUMN_COUNT = 4;
    public static final String SELECTED_STICKER_ID = "SELECTED_STICKER_ID";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stickers);

        mRecyclerView = findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(this, COLUMN_COUNT);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(Sticker.generateStickers());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        animateFinish();
    }

    void OnStickerSelected(int resId){
        Intent resultIntent = new Intent();
        resultIntent.putExtra(SELECTED_STICKER_ID, resId);
        setResult(Activity.RESULT_OK, resultIntent);
        animateFinish();
    }

    private void animateFinish(){
        finish();
        overridePendingTransition( R.anim.slide_stay, R.anim.slide_out_up );
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private Sticker[] mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public ImageView mImageView;

            public MyViewHolder(ImageView v) {
                super(v);
                mImageView = v;
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(Sticker[] myDataset) {
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            // create a new view
            ImageView view = new ImageView(parent.getContext());

            MyViewHolder vh = new MyViewHolder(view);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
/*
            try {
                holder.mImageView.setImageDrawable(Drawable.createFromStream(getAssets().open(mDataset[position].imageName), null));
            } catch (IOException e) {
                Log.e("onBindViewHolder", e.getMessage(), e);
            }
*/
            holder.mImageView.setImageResource(mDataset[position].imageName);
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OnStickerSelected(mDataset[holder.getAdapterPosition()].imageName);
                }
            });
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.length;
        }
    }
}
