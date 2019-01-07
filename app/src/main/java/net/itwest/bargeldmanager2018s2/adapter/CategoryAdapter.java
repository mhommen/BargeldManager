package net.itwest.bargeldmanager2018s2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import net.itwest.bargeldmanager2018s2.database.dto.Category;

public class CategoryAdapter extends ArrayAdapter<Category> {

    private List<Category> objects;
    private int textViewResourceId;
    private LayoutInflater inflater;

    public CategoryAdapter(Context context, List<Category> objects) {
        super(context, 0, objects);
        this.objects = objects;
        this.textViewResourceId = android.R.layout.simple_list_item_1;
        inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(textViewResourceId, null);
        }

        Category category = objects.get(position);
        if (category != null) {
            TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
            if (textView != null) {
                textView.setText(category.getTitle());
            }
        }

        return convertView;

    }
}