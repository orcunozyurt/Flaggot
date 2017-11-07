package com.nerdz.flaggot.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.caverock.androidsvg.SVG;
import com.nerdz.flaggot.R;
import com.nerdz.flaggot.models.Country;
import com.nerdz.flaggot.utils.AnimationFactory;
import com.nerdz.flaggot.utils.SvgDecoder;
import com.nerdz.flaggot.utils.SvgDrawableTranscoder;
import com.nerdz.flaggot.utils.SvgSoftwareLayerSetter;

import java.io.InputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class CardsRecyclerAdapter extends RecyclerView.Adapter<CardsRecyclerAdapter.ViewHolder> {
    private static final String TAG = "CardsRecyclerAdapter";
    private final Context context;
    private List<Country> mDataSet;
    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;



    public CardsRecyclerAdapter(List<Country> data, Context context) {
        this.mDataSet = data;
        this.context = context;
    }

    public void updateCountries(List<Country> items) {
        mDataSet = items;
        notifyDataSetChanged();
    }

    private Country getItem(int adapterPosition) {
        return mDataSet.get(adapterPosition);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_card_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        getFlagImage(holder.firstChildImageView, holder.secondChildImageView,
                getItem(position).getFlag());

        holder.firstChildTitle.setText(getItem(position).getName());
        holder.firstChildDesc.setText(getItem(position).getCapital());
        holder.secondChildTitle.setText(getItem(position).getName());

        Resources res = context.getResources();
        String secondChildDesc = String.format(res.getString(R.string.second_child_desc_schema),
                getItem(position).getNativeName(), getItem(position).getRegion(),
                getItem(position).getSubregion(),getItem(position).getCioc(),
                getItem(position).getLanguages().get(0).getName());
        holder.secondChildDesc.setText(secondChildDesc);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*holder.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.card_flip_left_in));
                holder.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.card_flip_left_out));*/
                AnimationFactory.flipTransition(holder.viewFlipper, AnimationFactory.FlipDirection.LEFT_RIGHT);
                //holder.viewFlipper.showNext();
            }
        });




    }

    @Override
    public int getItemCount() {
        if (mDataSet == null ) {
            return 0;
        }else {
            return mDataSet.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private ViewFlipper viewFlipper;
        private CardView firstChildCardView;
        private ImageView firstChildImageView;
        private TextView firstChildTitle;
        private TextView firstChildDesc;
        private CardView secondChildCardView;
        private CircleImageView secondChildImageView;
        private TextView secondChildTitle;
        private TextView secondChildDesc;
        private CardView thirdChildCardView;
        private ImageView thirdChildImageView;
        private TextView thirdChildTitle;
        private TextView thirdChildDesc;

        public ViewHolder(View view) {
            super(view);

            viewFlipper = (ViewFlipper) view.findViewById(R.id.view_flipper);
            firstChildCardView = (CardView) view.findViewById(R.id.first_child_card_view);
            firstChildImageView = (ImageView) view.findViewById(R.id.first_child_image_view);
            firstChildTitle = (TextView) view.findViewById(R.id.first_child_title);
            firstChildDesc = (TextView) view.findViewById(R.id.first_child_desc);
            secondChildCardView = (CardView) view.findViewById(R.id.second_child_card_view);
            secondChildImageView = (CircleImageView) view.findViewById(R.id.second_child_image_view);
            secondChildTitle = (TextView) view.findViewById(R.id.second_child_title);
            secondChildDesc = (TextView) view.findViewById(R.id.second_child_desc);
            thirdChildCardView = (CardView) view.findViewById(R.id.third_child_card_view);
            thirdChildImageView = (ImageView) view.findViewById(R.id.third_child_image_view);
            thirdChildTitle = (TextView) view.findViewById(R.id.third_child_title);
            thirdChildDesc = (TextView) view.findViewById(R.id.third_child_desc);
        }


    }

    private void getFlagImage(ImageView flagImageView, final ImageView infoImageView, String url) {
        requestBuilder = Glide.with(context)
                .using(Glide.buildStreamModelLoader(Uri.class, context), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder()))
                .decoder(new SvgDecoder())
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_error)
                .animate(android.R.anim.fade_in)
                .listener(new SvgSoftwareLayerSetter<Uri>());

        Uri uri = Uri.parse(url);
        requestBuilder
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                // SVG cannot be serialized so it's not worth to cache it
                .load(uri)
                .into(flagImageView);

        requestBuilder
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                // SVG cannot be serialized so it's not worth to cache it
                .load(uri)
                .into(infoImageView);

    }


}
