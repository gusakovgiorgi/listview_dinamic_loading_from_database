package net.gusakov.ddatestapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.gusakov.ddatestapp.R;
import net.gusakov.ddatestapp.asyntasks.LoadDataAsynTask;
import net.gusakov.ddatestapp.classes.Filter;
import net.gusakov.ddatestapp.database.Database;

import java.text.SimpleDateFormat;
import java.util.Date;


import static net.gusakov.ddatestapp.classes.CourseUtil.UNINITIALIZED;

/**
 * Created by hasana on 2/23/2017.
 */

public class StudentCursorAdapter extends CursorAdapter {
    private static final String TAG="StudentAdapter";
    private SimpleDateFormat dateFormat;
    private Drawable defaultImage;
    private Drawable infoImage;
    private Context context;
    private static int studentsIncrease =20;
    private int studentsReaded;
    private Database db;
    private Filter filter;

    private StudentCursorAdapter(Context context, Cursor cursor,Database db) {
        super(context, cursor, 0);
        this.context = context;
        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        defaultImage = context.getResources().getDrawable(R.drawable.ic_perm_contact_calendar_black_36dp, null);
        infoImage = context.getResources().getDrawable(R.drawable.ic_info_outline_black_24dp, null);
        this.db=db;
        studentsReaded=cursor.getCount();
        filter=new Filter();
    }
    public static StudentCursorAdapter newInstance(Context ctx) {
        Database database=new Database(ctx);
        database.openSqlConnection();
        Cursor cursor=database.getStudentsInfo(new Filter(studentsIncrease));
        return new StudentCursorAdapter(ctx,cursor,database);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.listview_item, parent, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView = (ImageView) view.findViewById(R.id.imageId);
        TextView name = (TextView) view.findViewById(R.id.nameId);
        TextView date = (TextView) view.findViewById(R.id.dateId);
        ImageView info = (ImageView) view.findViewById(R.id.infoImageId);

        imageView.setImageDrawable(defaultImage);
        name.setText(cursor.getString(cursor.getColumnIndex(Database.ST_TB_FIRST_NAME)) + " " + cursor.getString(cursor.getColumnIndex(Database.ST_TB_LAST_NAME)));
        date.setText(dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Database.ST_TB_BIRTHDAY)))));
        info.setImageDrawable(infoImage);
        info.setTag(cursor.getInt(cursor.getColumnIndex(Database.ST_TB_ID)));
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG,"image clicked");
            }
        });
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position>studentsReaded-5){
            filter.setReadNumber(studentsReaded+studentsIncrease);
            loadData(filter);

        }
        return super.getView(position, convertView, parent);
    }

    public void closeAdapterConnectionToDataBase(){
        db.closeSqlConnection();
    }

    //can be called from outside
    public void loadData(Filter filter){
        this.filter=filter;
        if(filter.getReadNumber()==UNINITIALIZED) {
            filter.setReadNumber(studentsReaded);
        }
            new LoadDataAsynTask(context, this).execute(filter);

    }

    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);
        studentsReaded=cursor.getCount();
    }

    public Database getDb() {
        return db;
    }

}
