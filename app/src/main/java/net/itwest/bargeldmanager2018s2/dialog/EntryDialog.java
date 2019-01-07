package net.itwest.bargeldmanager2018s2.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.List;

import net.itwest.bargeldmanager2018s2.database.dto.Category;

public class EntryDialog extends DialogBase
{
    private List<Category> _categories;
    private EntryDialog.IInputDialog _positiveButton;

    public EntryDialog(Context context, int titleResId, int messageResId, List<Category> kategorien, IInputDialog positiveButton)
    {
        super(context, titleResId, messageResId);
        _categories = kategorien;
        _positiveButton = positiveButton;
    }

    // TODO Layout laden
    public void show()
    {
        LinearLayout container = new LinearLayout(_context);
        container.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = params.rightMargin = 50;

        final EditText input = new EditText(_context);
        input.setLayoutParams(params);
        input.setSingleLine();
        input.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
        input.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        container.addView(input);

        final Spinner spinner = new Spinner(_context);
        spinner.setLayoutParams(params);
        spinner.setAdapter(new ArrayAdapter<>(_context, android.R.layout.simple_list_item_1, _categories));
        container.addView(spinner);

        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        builder.setTitle(_title);
        builder.setIcon(android.R.drawable.ic_input_add);
        builder.setMessage(_message);
        builder.setView(container);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                _positiveButton.onClick(dialog, which, getInputCents(input), (Category) spinner.getSelectedItem());
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private int getInputCents(EditText input)
    {
        int inputInt;

        try
        {
            double inputDbl = Double.parseDouble(input.getText().toString());
            inputInt = (int) (Math.round(inputDbl * 100));
        }
        catch (Exception ex)
        {
            inputInt = 0;
        }

        return inputInt;
    }

    public interface IInputDialog {
        void onClick(DialogInterface dialog, int which, int value, Category category);
    }
}
