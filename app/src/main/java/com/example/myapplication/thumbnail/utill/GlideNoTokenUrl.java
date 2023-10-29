package com.example.myapplication.thumbnail.utill;

import com.bumptech.glide.load.model.GlideUrl;

import java.net.URL;

/*
画像ファイル取得にS3を利用するため、URLにトークンが付与される。
GlideのキャッシュキーはURLを使用するため、トークン部分を削除する。
 */
public class GlideNoTokenUrl extends GlideUrl {
    public GlideNoTokenUrl(URL url) {
        super(url);
    }

    public GlideNoTokenUrl(String url) {
        super(url);
    }

    @Override
    public String getCacheKey() {
        String url = toStringUrl();
        if (url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }
        return url;
    }
}
