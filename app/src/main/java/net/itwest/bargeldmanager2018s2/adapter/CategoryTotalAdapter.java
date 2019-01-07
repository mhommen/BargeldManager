package net.itwest.bargeldmanager2018s2.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CategoryTotalAdapter extends ArrayAdapter<CategoryTotalEntry>
{
    private List<CategoryTotalEntry> objects;
    private int textViewResourceId;
    private LayoutInflater inflater;

    public CategoryTotalAdapter(Context context, List<CategoryTotalEntry> objects)
    {
        super(context, 0, objects);
        this.objects = objects;
        this.textViewResourceId = android.R.layout.simple_list_item_2;
        inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
            convertView = inflater.inflate(textViewResourceId, null);

        CategoryTotalEntry entry = objects.get(position);
        if (entry != null) {
            TextView textView1 = (TextView) convertView.findViewById(android.R.id.text1);
            TextView textView2 = (TextView) convertView.findViewById(android.R.id.text2);
            if (textView1 != null && textView2 != null) {
                textView1.setText(entry.getTitel());
                textView2.setText(entry.getTotal());
            }
        }

        return convertView;
    }
}
