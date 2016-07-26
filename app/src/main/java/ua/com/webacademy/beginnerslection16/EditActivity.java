package ua.com.webacademy.beginnerslection16;

import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "ua.com.webacademy.beginnerslection16.extra.ID";

    private UpdateTask mUpdateTask;
    private GetTask mGetTask;

    private long mId;
    private Student mStudent;


    private EditText mEditTextFirstName;
    private EditText mEditTextLasttName;
    private EditText mEditTextAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mEditTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        mEditTextLasttName = (EditText) findViewById(R.id.editTextLastName);
        mEditTextAge = (EditText) findViewById(R.id.editTextAge);

        mId = getIntent().getExtras().getLong(EXTRA_ID, 0);
        mGetTask = new GetTask();
        mGetTask.execute(mId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mUpdateTask != null) {
            mUpdateTask.cancel(true);
        }
        if (mGetTask != null) {
            mGetTask.cancel(true);
        }
    }

    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSave:
                mStudent.FirstName = mEditTextFirstName.getText().toString();
                mStudent.LastName = mEditTextLasttName.getText().toString();
                mStudent.Age = Integer.parseInt(mEditTextAge.getText().toString());

                mUpdateTask = new UpdateTask();
                mUpdateTask.execute(mStudent);
                break;
            case R.id.buttonCancel:
                finish();
                break;
        }
    }

    class UpdateTask extends AsyncTask<Student, Void, Integer> {

        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDialog = new ProgressDialog(EditActivity.this);
            mDialog.setMessage("Wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected Integer doInBackground(Student... params) {
            int count = 0;

            try {
                Student student = params[0];

                DataBaseHelper helper = new DataBaseHelper(EditActivity.this);
                count = helper.updateStudent(student);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return count;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }

            DataBaseHelper helper = new DataBaseHelper(EditActivity.this);
            ArrayList<Widget> widgets = helper.getWidgetsByStudent(mStudent.id);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(EditActivity.this);
            for (Widget w : widgets) {
                StudentWidget.updateAppWidget(EditActivity.this, appWidgetManager, (int) w.idWidget);
            }

            finish();
        }
    }

    class GetTask extends AsyncTask<Long, Void, Student> {

        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDialog = new ProgressDialog(EditActivity.this);
            mDialog.setMessage("Wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected Student doInBackground(Long... params) {
            Student student = null;

            try {
                long id = params[0];

                DataBaseHelper helper = new DataBaseHelper(EditActivity.this);
                student = helper.getStudent(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return student;
        }

        @Override
        protected void onPostExecute(Student result) {
            super.onPostExecute(result);

            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }

            mStudent = result;

            if (mStudent == null) {
                Toast.makeText(EditActivity.this, "Student not found", Toast.LENGTH_SHORT).show();
            } else {
                mEditTextFirstName.setText(mStudent.FirstName);
                mEditTextLasttName.setText(mStudent.LastName);
                mEditTextAge.setText(String.valueOf(mStudent.Age));
            }
        }
    }
}
