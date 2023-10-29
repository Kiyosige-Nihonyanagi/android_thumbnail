package com.example.myapplication.thumbnail.ui.chat;

import java.util.concurrent.CompletableFuture;

public class ChatData {
    private final String title;
    private final String detail;
    private CompletableFuture<String> imageUrlFuture;

    public ChatData(String title, String detail, CompletableFuture<String> imageUrlFuture) {
        this.title = title;
        this.detail = detail;
        this.imageUrlFuture = imageUrlFuture;
    }

    public String getTitle(){
        return this.title;
    }
    public String getDetail(){
        return this.detail;
    }
    public CompletableFuture<String> getImageUrlFuture() { return this.imageUrlFuture;}
    public void setImageUrlFuture(CompletableFuture<String> imageUrlFuture){
        this.imageUrlFuture = imageUrlFuture;
    }
}
