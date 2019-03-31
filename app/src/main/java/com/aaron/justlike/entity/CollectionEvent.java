package com.aaron.justlike.entity;

import com.aaron.justlike.http.unsplash.entity.collection.Collection;

import java.util.List;

/**
 * For online-search module's ElementActivity
 */
public class CollectionEvent {

    private List<Collection> collections;

    public CollectionEvent(List<Collection> collections) {
        this.collections = collections;
    }

    public List<Collection> getCollections() {
        return collections;
    }

    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }
}
