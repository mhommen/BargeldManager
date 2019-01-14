package net.itwest.bargeldmanager2018s2;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.LongSparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.itwest.bargeldmanager2018s2.adapter.CategoryTotalAdapter;
import net.itwest.bargeldmanager2018s2.adapter.CategoryTotalEntry;
import net.itwest.bargeldmanager2018s2.database.DbContract;
import net.itwest.bargeldmanager2018s2.database.dto.Category;
import net.itwest.bargeldmanager2018s2.database.dto.Entry;
import net.itwest.bargeldmanager2018s2.dialog.EntryDialog;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity
{
    private TextView tvEarning;
    private TextView tvIssue;
    private TextView tvTotal;

    private CategoryTotalAdapter adapter;
    private List<CategoryTotalEntry> categoryEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvEarning = findViewById(R.id.TvEarning);
        tvIssue = findViewById(R.id.TvIssue);
        tvTotal = findViewById(R.id.TvTotal);

        categoryEntries = new ArrayList<>();

        adapter = new CategoryTotalAdapter(this, categoryEntries);
        ListView listView = findViewById(R.id.LvTotals);
        listView.setAdapter(adapter);

        refreshEntries();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_categories)
        {
            Intent in = new Intent(this, CategoryActivity.class);
            startActivity(in);
            return true;
        }
        else if (item.getItemId() == R.id.action_clear)
        {
            DbContract.DbHelper db = new DbContract.DbHelper(this);
            db.clear();
            refreshEntries();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshEntries()
    {
        DbContract.DbHelper db = new DbContract.DbHelper(this);
        List<Entry> entries = db.getEntries();

        refreshListView(entries);
        refreshTotals(entries);
    }

    private void refreshListView(List<Entry> entries)
    {
        NumberFormat format = getNumberFormat();
        DbContract.DbHelper db = new DbContract.DbHelper(this);
        LongSparseArray<Long> categoryVals = getCategoryTotals(entries);

        categoryEntries.clear();

        for (int i = 0; i < categoryVals.size(); i++) {
            long key = categoryVals.keyAt(i);
            long value = categoryVals.get(key);

            Category category = db.getCategory(key);

            categoryEntries.add(
                    new CategoryTotalEntry(
                            category.getTitle(),
                            format.format(value / 100f)
                    )
            );
        }

        adapter.notifyDataSetChanged();
    }

    private void refreshTotals(List<Entry> entries)
    {
        NumberFormat format = getNumberFormat();
        long totalEarning = getTotalEarning(entries);
        long totalIssue = getTotalIssue(entries);

        tvEarning.setText(format.format(totalEarning / 100f));
        tvIssue.setText(format.format(totalIssue / 100f));
        tvTotal.setText(format.format((totalEarning - totalIssue) / 100f));
    }

    private long getTotalEarning(List<Entry> entries)
    {
        long amount = 0;
        for (Entry entry : entries)
        {
            if (entry.getAmount() >= 0)
                amount += entry.getAmount();
        }
        return amount;
    }

    private long getTotalIssue(List<Entry> entries)
    {
        long amount = 0;
        for (Entry entry : entries)
        {
            if (entry.getAmount() < 0)
                amount += -entry.getAmount();
        }
        return amount;
    }

    private NumberFormat getNumberFormat()
    {
        return NumberFormat.getCurrencyInstance(Locale.GERMANY);
    }

    private LongSparseArray<Long> getCategoryTotals(List<Entry> entries)
    {
        LongSparseArray<Long> categoryVals = new LongSparseArray<>();

        for (Entry entry : entries)
        {
            long betrag = entry.getAmount();

            if (categoryVals.indexOfKey(entry.getCategoryId()) >= 0)
                betrag += categoryVals.get(entry.getCategoryId());

            categoryVals.put(entry.getCategoryId(), betrag);
        }

        return  categoryVals;
    }

    public boolean CategoriesCheck ()
    {
        final DbContract.DbHelper db = new DbContract.DbHelper(this);
        List<Category> categories = db.getCategories();
        // wenn keine Kategorien vorhanden, dann toast anzeigen und aus Methode rausgehen
        return categories.size() != 0;
    }

    public void btnEarningClicked(View view) {
        if (CategoriesCheck())
            EntryDialog(true);
        else
            Toast.makeText(MainActivity.this, "Bitte Kategorien anlegen", Toast.LENGTH_LONG).show();
    }

    public void btnIssueClicked(View view) {
        if (CategoriesCheck())
            EntryDialog(false);
        else
            Toast.makeText(MainActivity.this, "Bitte Kategorien anlegen", Toast.LENGTH_LONG).show();
    }

    private void EntryDialog(final boolean isEarning)
    {
        final DbContract.DbHelper db = new DbContract.DbHelper(this);
        List<Category> categories = db.getCategories();

         int idTitle = isEarning ? R.string.earning : R.string.issue;
        int idMessage = isEarning ? R.string.msg_add_earning : R.string.msg_add_issue;

        new EntryDialog(this, idTitle, idMessage, categories, new EntryDialog.IInputDialog() {
            @Override
            public void onClick(DialogInterface dialog, int which, int value, Category category) {

                if (value == 0)
                    Toast.makeText(MainActivity.this, getString(R.string.msg_invalid_input), Toast.LENGTH_SHORT).show();
                else {
                    Entry b = new Entry();
                    b.setCategoryId(category.getId());
                    b.setAmount(isEarning ? value : -value);
                    db.addEntry(b);

                    refreshEntries();
                }

            }
        }).show();
    }
}