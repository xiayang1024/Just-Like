package com.aaron.justlike.http;

import com.aaron.justlike.http.entity.Photo;

import java.util.List;

public interface PhotosCallback {

    void onSuccess(List<Photo> photos);

    void onFailure(String error);
}
