package com.rahuljanagouda.statusstories.glideProgressBar;

import android.support.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Created by rahuljanagouda on 30/09/17.
 */

public final class NoOpRequestListener<R> implements RequestListener<R> {
    private static final RequestListener INSTANCE = new NoOpRequestListener();

    @SuppressWarnings("unchecked")
    public static <R> RequestListener<R> get() {
        return INSTANCE;
    }

    private NoOpRequestListener() {
    }

    @Override public boolean onResourceReady(R resource, Object model, Target<R> target, DataSource dataSource,
                                             boolean isFirstResource) {
        return false;
    }
    @Override public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<R> target,
                                          boolean isFirstResource) {
        return false;
    }
}
