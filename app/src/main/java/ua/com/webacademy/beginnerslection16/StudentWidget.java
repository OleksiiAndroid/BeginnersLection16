package ua.com.webacademy.beginnerslection16;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.Random;

public class StudentWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        DataBaseHelper helper = new DataBaseHelper(context);
        Widget widget = helper.getWidget(appWidgetId);

        if (widget != null) {
            Student student = helper.getStudent(widget.idStudent);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.student_widget);
            views.setTextViewText(R.id.textViewFirstName, student.FirstName);
            views.setTextViewText(R.id.textViewLastName, student.LastName);
            views.setTextViewText(R.id.textViewAge, String.valueOf(student.Age));

            Intent intent = new Intent(context, EditActivity.class);
            intent.putExtra(EditActivity.EXTRA_ID, student.id);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, new Random().nextInt(), intent, 0);

            views.setOnClickPendingIntent(R.id.viewWidget, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        DataBaseHelper helper = new DataBaseHelper(context);

        for (int appWidgetId : appWidgetIds) {
            helper.deleteWidget(appWidgetId);
        }
    }
}

