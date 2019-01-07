package net.itwest.bargeldmanager2018s2.dialog;

import android.content.Context;

abstract class DialogBase
{
    Context _context;
    String _title;
    String _message;

    DialogBase(Context context, int titleResId, int messageResId)
    {
        this._context = context;
        this._title = context.getString(titleResId);
        this._message = context.getString(messageResId);
    }

    abstract void show();
}
