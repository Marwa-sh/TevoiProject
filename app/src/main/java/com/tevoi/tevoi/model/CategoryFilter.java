package com.tevoi.tevoi.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryFilter implements Parcelable {

    private int id;
    private String name;
    private boolean isFavorite;

    public CategoryFilter(int id,String name, boolean isFavorite) {
        this.id = id;
        this.name = name;
        this.isFavorite = isFavorite;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    protected CategoryFilter(Parcel in) {
        name = in.readString();
    }

    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
    public boolean isFavorite() {
        return isFavorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryFilter)) return false;

        CategoryFilter artist = (CategoryFilter) o;

        if (isFavorite() != artist.isFavorite()) return false;
        return getName() != null ? getName().equals(artist.getName()) : artist.getName() == null;

    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (isFavorite() ? 1 : 0);
        return result;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoryFilter> CREATOR = new Creator<CategoryFilter>() {
        @Override
        public CategoryFilter createFromParcel(Parcel in) {
            return new CategoryFilter(in);
        }

        @Override
        public CategoryFilter[] newArray(int size) {
            return new CategoryFilter[size];
        }
    };
}
