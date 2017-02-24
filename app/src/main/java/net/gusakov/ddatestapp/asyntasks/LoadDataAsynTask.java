package net.gusakov.ddatestapp.asyntasks;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import net.gusakov.ddatestapp.adapters.StudentCursorAdapter;
import net.gusakov.ddatestapp.classes.Filter;
import net.gusakov.ddatestapp.database.Database;

/**
 * Created by gusakov on 2/23/2017.
 * dinamycally load data when it's needed and pass it in cursor adapter
 */

public class LoadDataAsynTask extends AsyncTask<Filter, Void, Cursor> {
    StudentCursorAdapter adapter;
    Context ctx;

    public LoadDataAsynTask(Context ctx, StudentCursorAdapter adapter) {
        this.ctx = ctx;
        this.adapter = adapter;
    }

    @Override
    protected Cursor doInBackground(Filter... params) {
        if (params.length == 0) {
            return null;
        }
        Database db = adapter.getDb();
        return db.getStudentsInfo(params[0]);
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        super.onPostExecute(cursor);
        if (cursor == null) {
            return;
        }
        adapter.changeCursor(cursor);
    }
}
