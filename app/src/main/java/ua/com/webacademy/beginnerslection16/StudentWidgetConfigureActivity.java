package ua.com.webacademy.beginnerslection16;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class StudentWidgetConfigureActivity extends Activity implements LoaderManager.LoaderCallbacks<ArrayList<Student>> {

    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private ListView mListView;
    private ProgressDialog mDialog;

    private ArrayList<Student> mStudents;

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            int position = mListView.getCheckedItemPosition();

            if (position >= 0) {
                Student student = mStudents.get(position);
                Widget widget = new Widget(student.id, mAppWidgetId);

                new DataBaseHelper(StudentWidgetConfigureActivity.this).insertWidget(widget);

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(StudentWidgetConfigureActivity.this);
                StudentWidget.updateAppWidget(StudentWidgetConfigureActivity.this, appWidgetManager, mAppWidgetId);

                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_widget_configure);

        setResult(RESULT_CANCELED);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);
        mListView = (ListView) findViewById(R.id.listView);

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Wait...");
        mDialog.setCancelable(false);
        mDialog.show();

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<ArrayList<Student>> onCreateLoader(int i, Bundle bundle) {
        return new StudentsLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Student>> loader, ArrayList<Student> students) {
        mStudents = students;

        ArrayAdapter<Student> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_single_choice,
                android.R.id.text1,
                mStudents);

        mListView.setAdapter(adapter);

        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Student>> loader) {

    }
}

