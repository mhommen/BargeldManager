package net.itwest.bargeldmanager2018s2.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

public class InputDialog extends DialogBase
{
    private IInputDialog _positiveButton;

    public InputDialog(Context context, int titleResId, int messageResId, IInputDialog positiveButton)
    {
        super(context, titleResId, messageResId);
        _positiveButton = positiveButton;
    }

    // TODO Layout laden
    public void show()
    {
        FrameLayout container = new FrameLayout(_context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText input = new EditText(_context);
        params.leftMargin = params.rightMargin = 50;
        input.setLayoutParams(params);
        input.setSingleLine();
        container.addView(input);

        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        builder.setTitle(_title);
        builder.setIcon(android.R.drawable.ic_input_add);
        builder.setMessage(_message);
        builder.setView(container);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                _positiveButton.onClick(dialog, which, input.getText().toString());
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

    public interface IInputDialog
    {
        void onClick(DialogInterface dialog, int which, String input);
    }
}
