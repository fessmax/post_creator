package fessmax.postvkcreator.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import fessmax.postvkcreator.CommonHelper;
import fessmax.postvkcreator.R;
import fessmax.postvkcreator.data.Background;

public class BackgroundAdapter extends RecyclerView.Adapter<BackgroundAdapter.BackgroundViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Background item);
    }

    private Background[] mDataset;
    private Context mContext;
    private OnItemClickListener mListener;
    private int selectedId = 0;

    public static class BackgroundViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ImageView mImageView;

        public BackgroundViewHolder(View view) {
            super(view);
            this.mView = view;
            mImageView = view.findViewById(R.id.image);
        }

        public void onSelect(boolean isSelected, boolean isGradient){
            mView.setSelected(isSelected);
            mImageView.setSelected(isSelected);
            mImageView.setScaleX(isSelected ? 0.8f : 1f);
            mImageView.setScaleY(isSelected ? 0.8f : 1f);

            if (!isGradient) mImageView.setImageBitmap(CommonHelper.getRoundedCornerBitmap(mImageView.getDrawable(), isSelected ? 0 : 15));
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
        holder.mImageView.setImageDrawable(mContext.getResources().getDrawable(mDataset[position].smallImageResId));
        holder.onSelect(selectedId == position, mDataset[position].isGradient);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(mDataset[holder.getAdapterPosition()]);
                setSelected(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public void setSelected(int position){
        if (position != getItemCount() - 1) {
            selectedId = position;
            notifyDataSetChanged();
        }
    }
}
