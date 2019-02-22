package com.aaron.justlike.http;

import com.aaron.justlike.http.entity.Photo;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Unsplash {

    private static final String BASE_URL = "https://api.unsplash.com/";
    private static final String CLIENT_ID = "18db24a3d59a1b2633897fa63f3f49455c2cbfa8a22e5b8520141cb2660fa816";

    private PhotoService mPhotoService;

    private Unsplash() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HeaderInterceptor(CLIENT_ID))
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mPhotoService = retrofit.create(PhotoService.class);
    }

    public void getPhotos(int page, int perPage, Order order, PhotosCallback callback) {
        mPhotoService.getPhotos(page, perPage, order.getOrder())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Photo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Photo> photos) {
                        callback.onSuccess(photos);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getPhoto(String id, PhotoCallback callback) {
        mPhotoService.getPhoto(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Photo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Photo photo) {
                        callback.onSuccess(photo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static Unsplash getInstance() {
        return Singleton.instance;
    }

    private static class Singleton {

        private static final Unsplash instance = new Unsplash();
    }
}
