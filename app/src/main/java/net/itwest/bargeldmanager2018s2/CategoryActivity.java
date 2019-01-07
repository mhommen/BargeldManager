package net.itwest.bargeldmanager2018s2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.itwest.bargeldmanager2018s2.adapter.CategoryAdapter;
import net.itwest.bargeldmanager2018s2.database.DbContract;
import net.itwest.bargeldmanager2018s2.database.dto.Category;
import net.itwest.bargeldmanager2018s2.dialog.InputDialog;

import java.util.List;

public class CategoryActivity extends Activity {

    private List<Category> categories;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        final DbContract.DbHelper db = new DbContract.DbHelper(this);
        categories = db.getCategories();
        adapter = new CategoryAdapter(this, categories);

        final ListView listViewCategories = (ListView) findViewById(R.id.LvCategories);
        listViewCategories.setAdapter(adapter);
        listViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category category = categories.get(position);
                if (db.deleteCategory(category)) {
                    refreshCategories();
                }
            }
        });
    }

    private void refreshCategories() {
        DbContract.DbHelper db = new DbContract.DbHelper(this);
        categories.clear();
        categories.addAll(db.getCategories());
        adapter.notifyDataSetChanged();
    }

    public void btnAddCategoryClicked(View view) {
        new InputDialog(this, R.string.add_category, R.string.msg_add_category, new InputDialog.IInputDialog() {
            @Override
            public void onClick(DialogInterface dialog, int which, String input) {
                DbContract.DbHelper db = new DbContract.DbHelper(CategoryActivity.this);
                Category category = new Category();
                category.setTitle(input);
                db.addCategory(category);
                dialog.dismiss();
                refreshCategories();
            }
        }).show();
    }
}
