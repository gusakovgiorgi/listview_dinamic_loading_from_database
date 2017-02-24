package net.gusakov.ddatestapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import net.gusakov.ddatestapp.adapters.StudentCursorAdapter;
import net.gusakov.ddatestapp.asyntasks.FillDataBaseFromWebAsynTask;
import net.gusakov.ddatestapp.framents.FilterDialogFragment;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    public static final String SHARED_PREF_NAME = "net.gusakov.ddatestapp.SHARED_PREF";
    public static final String SHARED_PREF_FIRST_TIME_KEY = "first_time";
    StudentCursorAdapter adapter;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFilter();
        listView = (ListView) findViewById(R.id.listview);

        if (firsttime()) {
            initListViewWhenDataBaseFilled();
        } else {
            initialListView();

        }
    }

    private void initFilter() {
        ImageButton imageButton = (ImageButton) findViewById(R.id.filterId);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterDialogFragment dialog = new FilterDialogFragment();
                dialog.setAdapter(adapter);
                dialog.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog);
                dialog.show(getFragmentManager(), "FilterDialogFragment");

            }
        });
    }

    public void initialListView() {
        adapter = StudentCursorAdapter.newInstanceWithConnectionToDataBase(this);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        adapter.closeAdapterConnectionToDataBase();
        clearFilterState();
        super.onDestroy();
    }

    private void clearFilterState() {
        SharedPreferences shared=getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        SharedPreferences.Editor ed=shared.edit();
        ed.remove(FilterDialogFragment.SHARED_PREF_INT_SPINNER_ID);
        ed.remove(FilterDialogFragment.SHARED_PREF_STRING_MARK);
        ed.commit();
    }

    private void initListViewWhenDataBaseFilled() {
        try {
            URL url = new URL("https://ddapp-sfa-api-dev.azurewebsites.net/api/test/students");
            new FillDataBaseFromWebAsynTask(this, findViewById(R.id.progressBarFrameId)).execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private boolean firsttime() {
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        return sharedPref.getBoolean(SHARED_PREF_FIRST_TIME_KEY, true);
    }


}
