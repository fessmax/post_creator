package fessmax.postvkcreator;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class BackgroundAdapter extends RecyclerView.Adapter<BackgroundAdapter.BackgroundViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Background item);
    }

    private Background[] mDataset;
    private Context mContext;
    private OnItemClickListener mListener;

    public static class BackgroundViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ImageView mImageView;

        public BackgroundViewHolder(View view) {
            super(view);
            this.mView = view;
            mImageView = view.findViewById(R.id.image);
        }
    }

    public BackgroundAdapter(Context context, Background[] myDataset, OnItemClickListener listener) {
        mContext = context;
        mDataset = myDataset;
        mListener = listener;
    }

    @Override
    public BackgroundAdapter.BackgroundViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.background_card, parent, false);

        return new BackgroundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BackgroundViewHolder holder, int position) {
        holder.mView.setBackgroundResource(R.drawable.rounded_corner);
        holder.mImageView.setImageDrawable(mContext.getResources().getDrawable(mDataset[position].smallImageResId));
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(mDataset[holder.getAdapterPosition()]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
