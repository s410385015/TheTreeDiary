package com.example.nako.thetreediary.myClass;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.nako.thetreediary.EditActivity;
import com.example.nako.thetreediary.MainActivity;
import com.example.nako.thetreediary.R;


/**
 * Created by Yo on 2016/12/22.
 */

public class MoodSelecter extends BottomSheetDialogFragment implements View.OnClickListener {

    public class ListViewAdapter extends BaseExpandableListAdapter {

        private String[] EMOTION_STR = getResources().getStringArray(R.array.Emotions);
        private String[][] MOODS_STR;

        public ListViewAdapter() {
            MOODS_STR = new String[EMOTION_STR.length][];
            MOODS_STR[0] = getResources().getStringArray(R.array.HAPPY);
            MOODS_STR[1] = getResources().getStringArray(R.array.SAD);
            MOODS_STR[2] = getResources().getStringArray(R.array.ANGRY);
            MOODS_STR[3] = getResources().getStringArray(R.array.WRETCHED);
        }

        @Override
        public int getGroupCount() {
            return EMOTION_STR.length;
        }

        @Override
        public int getChildrenCount(int i) {
            return MOODS_STR[i].length;
        }

        @Override
        public Object getGroup(int i) {
            return EMOTION_STR[i];
        }

        @Override
        public Object getChild(int i, int i1) {
            return MOODS_STR[i][i1];
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(MoodSelecter.this.getActivity());
            textView.setPadding(70, 20, 20, 20);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(layoutParams);
            textView.setText(getGroup(i).toString());
            textView.setTextSize(20.0f);
            return textView;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(MoodSelecter.this.getActivity());
            textView.setText(getChild(i, i1).toString());
            textView.setPadding(20, 20, 20, 20);
            textView.setId(100*i + i1);
            textView.setOnClickListener(MoodSelecter.this);
            return textView;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }

    private Context context;
    private View rootView;
    private ExpandableListView expandableListView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        this.getDialog().setCanceledOnTouchOutside(true);

        rootView = inflater.inflate(R.layout.botdialog_selectmood, container);

        expandableListView = (ExpandableListView) rootView.findViewById(R.id.mood_pick);
        expandableListView.setAdapter(new ListViewAdapter());

        return rootView;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        String s=(String)new ListViewAdapter().getChild(i/100, i%100) ;
        EditText editText = (EditText) (((EditActivity)getActivity()).findViewById(R.id.editText));
        for(int n=0;n<s.length();n++)
            editText.append(s.substring(n,n+1));
        dismiss();
    }

}
