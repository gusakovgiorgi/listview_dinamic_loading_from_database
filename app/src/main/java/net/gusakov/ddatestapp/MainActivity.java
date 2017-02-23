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

        ImageButton imageButton = (ImageButton) findViewById(R.id.filterId);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterDialogFragment dialog = new FilterDialogFragment();
                dialog.setAdapter(adapter);
                dialog.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog);
                dialog.show(getFragmentManager(), "MyDialogFragment");

            }
        });
        listView = (ListView) findViewById(R.id.listview);
//  SELECT  _id,first_name,last_name,birthday FROM studentsWHERE (_id=1 OR _id=2 OR _id=7 OR _id=8 OR _id=9 OR _id=17 OR _id=19 OR _id=20 OR _id=24 OR _id=37 OR _id=41 OR _id=47 OR _id=48 OR _id=52 OR _id=61 OR _id=62 OR _id=63 OR _id=64 OR _id=68 OR _id=78) LIMIT 20

        if (firsttime()) {
            initListViewWhenDataBaseFilled();
        } else {
            initialListView();

        }
    }

    public void initialListView() {
        adapter = StudentCursorAdapter.newInstance(this);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        adapter.closeAdapterConnectionToDataBase();
        super.onDestroy();
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
