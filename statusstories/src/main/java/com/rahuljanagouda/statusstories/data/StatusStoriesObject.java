package com.rahuljanagouda.statusstories.data;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class StatusStoriesObject implements Serializable {
    private List<String> resources = new ArrayList<>();
    private Long duration;
    private Boolean isImmersive = false;
    private Boolean isCachingEnabled = false;
    private Boolean isTextProgressEnabled = false;
    //private StatusStoriesListener listener;


    public List<String> getResources() {
        return resources;
    }

    public void setResources(List<String> resources) {
        this.resources = resources;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Boolean getImmersive() {
        return isImmersive;
    }

    public void setImmersive(Boolean immersive) {
        isImmersive = immersive;
    }

    public Boolean getCachingEnabled() {
        return isCachingEnabled;
    }

    public void setCachingEnabled(Boolean cachingEnabled) {
        isCachingEnabled = cachingEnabled;
    }

    public Boolean getTextProgressEnabled() {
        return isTextProgressEnabled;
    }

    public void setTextProgressEnabled(Boolean textProgressEnabled) {
        isTextProgressEnabled = textProgressEnabled;
    }

//    public StatusStoriesListener getListener() {
//        return listener;
//    }
//
//    public void setListener(StatusStoriesListener listener) {
//        this.listener = listener;
//    }
}
