package net.itwest.bargeldmanager2018s2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnClicked(View view)
    {
        Intent in = new Intent(this, CategoryActivity.class);
        startActivity(in);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_test1)
        {
            Log.d("BM2018", "Test1");
            return true;
        }
        else if (item.getItemId() == R.id.action_test2)
        {
            Log.d("BM2018", "Test2");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}