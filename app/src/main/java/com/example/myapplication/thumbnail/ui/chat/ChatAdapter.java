package com.example.myapplication.thumbnail.ui.chat;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.myapplication.thumbnail.R;
import com.example.myapplication.thumbnail.utill.GlideNoTokenUrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {
    Context context;
    private final Handler handler = new Handler(Looper.getMainLooper());
    public List<ChatData> chatList;
    private final Map<Integer, String> urlMap = new HashMap<>();

    public ChatAdapter(Context context, List<ChatData> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @Override
    @NonNull
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_row, parent, false);
        return new ChatViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.titleView.setText(chatList.get(position).getTitle());
        holder.detailView.setText(chatList.get(position).getDetail());
        holder.thumbnail.setImageResource(R.drawable.ic_menu_camera);
        /*既にURLを発行済の場合、Glideへ発行済URLを渡す。
          GlideはURLをキーとしてキャッシュ内検査を行うため、キャッシュを消すと同一URLでDLしにいく。
          S3の場合、同一URLはエラーとなるため、Glideでリスナーを用意する。
         */
        if (urlMap.containsKey(position)) {
            loadImage(holder, urlMap.get(position), position);
            Log.d("GlideT", "[URL]skip Position: " + position);
            return;
        }
        try {
            chatList.get(holder.getAdapterPosition()).setImageUrlFuture(loadImageUrlAsync(position));
            chatList.get(holder.getAdapterPosition()).getImageUrlFuture()
                    .whenComplete((url, throwable) -> {
                        loadImage(holder, url, position);
                        //再描画時にurlを発行しないようにするため、初回発行後にマップに格納する。
                        urlMap.put(position, url);
                    });
        } catch (InterruptedException e) {
            Log.w("Thumbnail", "Url can't get");
        } catch (NullPointerException e) {
            Log.w("Thumbnail", "Url is null");
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    private CompletableFuture<String> loadImageUrlAsync(int position) throws InterruptedException {
        return CompletableFuture.supplyAsync(() -> {
            Log.i("URL", "URL position: " + position);
            return "https://placehold.jp/150x150.png?" + position;
        });
    }

    private void loadImage(ChatViewHolder holder, String url, int position) {
        handler.post(() -> {
            if (url.isEmpty()) {
                Log.i("GlideT", "[Url]Url is Empty!" + holder.getAdapterPosition());
                return;
            }
            if (holder.getAdapterPosition() == RecyclerView.NO_POSITION) {
                Log.i("GlideT", "[image]Position no position!" + holder.getAdapterPosition());
                return;
            }
            if (position != holder.getAdapterPosition()) {
                Log.i("GlideT", "[image]Position wrong position!" + holder.getAdapterPosition());
                return;
            }
            Log.i("GlideT", "[image]Start Glide: " + holder.getAdapterPosition());
            Glide.with(context)
                    .load(new GlideNoTokenUrl(url))
                    .thumbnail(0.05f)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.i("GlideT", "[image]error " + holder.getAdapterPosition());
                            urlMap.remove(holder.getAdapterPosition());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.thumbnail.setImageDrawable(resource);
                            Log.i("GlideT", "[image]ResourceReady: " + holder.getAdapterPosition());
                            return false;
                        }
                    })
                    .into(holder.thumbnail);
        });
    }
}
