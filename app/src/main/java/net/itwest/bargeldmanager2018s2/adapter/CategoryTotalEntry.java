package net.itwest.bargeldmanager2018s2.adapter;

public class CategoryTotalEntry
{
    private String _title;
    private String _total;

    public CategoryTotalEntry(String title, String total)
    {
        this._title = title;
        this._total = total;
    }

    public String getTitel() { return this._title; }
    public String getTotal() { return this._total; }
}
