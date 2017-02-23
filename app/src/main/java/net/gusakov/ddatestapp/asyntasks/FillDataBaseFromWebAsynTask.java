package net.gusakov.ddatestapp.asyntasks;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import net.gusakov.ddatestapp.MainActivity;
import net.gusakov.ddatestapp.adapters.StudentCursorAdapter;
import net.gusakov.ddatestapp.classes.JSONParser;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

/**
 * Created by hasana on 2/23/2017.
 */

public class FillDataBaseFromWebAsynTask extends AsyncTask<URL, Void, Void> {
    private final static String TAG = "FillDataBaseFromWebAT";
    private View progressBarFrameView;
    private Context ctx;

    public FillDataBaseFromWebAsynTask(Context ctx, View progressBarFrameView) {
        this.progressBarFrameView = progressBarFrameView;
        this.ctx=ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBarFrameView.setVisibility(View.VISIBLE);
    }


    @Override
    protected Void doInBackground(URL... params) {
        if (params.length == 0) {
            return null;
        }
        try {
            URL url = params[0];
            new JSONParser(ctx).parse(getString(url.openStream()));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressBarFrameView.setVisibility(View.GONE);
        try {
            MainActivity mainActivity = (MainActivity) ctx;
            mainActivity.initialListView();
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }


    private String getString(InputStream is) {
        Reader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        char[] buf = new char[3000];
        int readNum;
        try {
            while ((readNum = reader.read(buf)) != -1) {
                Log.v(TAG, new String(buf, 0, readNum));
                sb.append(buf, 0, readNum);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(reader);
        }
        return sb.toString();
    }

    private void close(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
                reader = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
