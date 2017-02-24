package net.gusakov.ddatestapp.framents;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.gusakov.ddatestapp.R;
import net.gusakov.ddatestapp.classes.CourseUtil;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class DetailFragment extends DialogFragment {
    private Map<Integer, Integer> marks;
    private static final String AVERANGE_MARK_TEXT = "Averange mark: ";


    public DetailFragment() {
        // Required empty public constructor
    }

    public void setMarks(Map<Integer, Integer> marks) {
        this.marks = marks;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.detail_fragment, null);
        initViews(v);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
        return builder.create();
    }

    private void initViews(View v) {
        ViewGroup[] rootViewGroups = new ViewGroup[]{(ViewGroup) v.findViewById(R.id.firstCourseRootId), (ViewGroup) v.findViewById(R.id.secondCourseRootId),
                (ViewGroup) v.findViewById(R.id.thirdCourseRootId), (ViewGroup) v.findViewById(R.id.fourthsCourseRootId)};
        TextView averangeMarkTextView = (TextView) v.findViewById(R.id.averangeMarkTxtViewId);
        Button okButton = (Button) v.findViewById(R.id.OkButtonId);
        int i = 0;
        int markSum = 0;
        if (marks != null && !marks.keySet().isEmpty()) {
            SortedSet<Integer> sortedKeySet = new TreeSet<Integer>(marks.keySet());
            for (Integer key : sortedKeySet) {
                ((TextView) rootViewGroups[i].getChildAt(0)).setText(CourseUtil.getCourseString(key));
                ((TextView) rootViewGroups[i].getChildAt(1)).setText(marks.get(key) + "");
                markSum += marks.get(key);
                i++;
            }
            float averangeMark = (float) markSum / marks.keySet().size();
            averangeMarkTextView.setText(AVERANGE_MARK_TEXT + averangeMark);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().dismiss();
                }
            });
        }

    }

}
