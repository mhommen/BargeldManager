package net.itwest.bargeldmanager2018s2.database.dto;

import java.util.Date;

public class Entry {

    private long _id;
    private long _amount;
    private Date _date = null;
    private String _text;
    private long _categoryId;

    public long getId() { return this._id; }
    public void setId(long id) { this._id = id; }

    public long getAmount() { return this._amount; }
    public void setAmount(long amount) { this._amount = amount; }

    public Date getDate() { return this._date; }
    public void setDate(Date date) { this._date = date; }

    public String getText() { return this._text; }
    public void setText(String text) { this._text = text; }

    public long getCategoryId() { return this._categoryId; }
    public void setCategoryId(long categoryId) { this._categoryId = categoryId; }
}
