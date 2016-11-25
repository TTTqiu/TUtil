package com.tttqiu.tutil;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tttqiu.library.TUtil;
import com.tttqiu.library.network.RequestQueue;
import com.tttqiu.library.request.BitmapRequest;
import com.tttqiu.library.request.Request;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

    private String[] images;
    private Context context;

    public MyRecyclerAdapter(Context context, String[] images) {
        super();
        this.context = context;
        this.images = images;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
//        TUtil.loadImageInto(context, images[position], holder.imageView, TUtil.CACHE_DISABLE, TUtil.CACHE_DISABLE);
        RequestQueue mRequestQueue=TUtil.startRequestQueue(RequestQueue.DEFAULT_THREAD_NUM);
        BitmapRequest request=new BitmapRequest(Request.GET,images[position], new Request.RequestListener<Bitmap>() {
            @Override
            public void onComplete(Bitmap result) {
                holder.imageView.setImageBitmap(result);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
        mRequestQueue.addRequest(request);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        MyViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.recycler_title_image);
        }
    }
}
