package com.applica.applicaandroid.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.applica.applicaandroid.ApplicaFont;
import com.applica.applicaandroid.R;
import com.applica.applicaandroid.controllers.AcademicFormSubjectsAdapter;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;

public class AcademicsFormActivity extends NavigationDrawerActivity {

    private IconicsImageView topBarSchoolList;
    private ListView academicFormSubjects;
    private AcademicFormSubjectsAdapter adapter;
    private static LayoutInflater inflater = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup the top bar
//        setupTopBar();

        String[][] data = new String[1][3];
        academicFormSubjects = (ListView) findViewById(R.id.academicFormSubjects);

        adapter = new AcademicFormSubjectsAdapter(this, data);

        academicFormSubjects.setAdapter(adapter);
        inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_academics_form;
    }

    protected void setupTopBar() {
//        super.setupTopBar();
//        topBarSchoolList = (IconicsImageView) findViewById(R.id.topBarSchoolList);
//        topBarSchoolList.setIcon(new IconicsDrawable(this, ApplicaFont.Icon.school_list_icon)
//                .color(Color.parseColor("#2dafd5")).sizeDp(72));
    }

    public void addAnotherSubject(View button) {
//        View footerView =  inflater.inflate(R.layout.activity_academics_form_subject, null, false);
//        academicFormSubjects.addView(footerView);
        String[][] data = new String[2][3];
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }

}