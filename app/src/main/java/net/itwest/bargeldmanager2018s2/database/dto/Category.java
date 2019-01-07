package net.itwest.bargeldmanager2018s2.database.dto;

public class Category {

    private long _id;
    private String _title;

    public Category() { }

    public long getId() { return this._id; }
    public void setId(long id) { this._id = id; }

    public String getTitle() { return this._title; }
    public void setTitle(String title) { this._title = title; }

    @Override
    public String toString() {
        return getTitle();
    }
}
