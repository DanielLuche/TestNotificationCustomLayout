package util;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.dluche.testnotificationcustomlayout.R;

import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * Created by d.luche on 23/10/2017.
 */

public class Utils {
    public static final String NOTIFICATION_TYPE_IN_PROGRESS = "NOTIFICATION_TYPE_IN_PROGRESS";
    public static final String NOTIFICATION_TYPE_DONE = "NOTIFICATION_TYPE_DONE";

    public static void createNotification(Context context, int id, String download_name, String type, Integer... values) {

        NotificationManager nm = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);

        //
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.notification_layout);

        switch (type) {
            case NOTIFICATION_TYPE_IN_PROGRESS:
                view.setImageViewResource(R.id.notfication_icon, R.drawable.ic_cloud_download_black_24dp);
                view.setTextViewText(R.id.notification_tv_name, download_name);
                view.setProgressBar(R.id.notification_progress_bar, values[0], values[1], false);
                view.setTextViewText(R.id.notification_tv_proress, String.valueOf(values[1]));
                break;

            case NOTIFICATION_TYPE_DONE:
                view.setImageViewResource(R.id.notfication_icon, R.drawable.ic_cloud_done_black_24dp);
                view.setTextViewText(R.id.notification_tv_name, "Download Concluido!");
                view.setViewVisibility(R.id.notification_progress_bar, View.GONE);
                view.setViewVisibility(R.id.notification_tv_proress, View.GONE);
                break;
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_cloud_download_black_24dp);
        builder.setAutoCancel(true);
        builder.setContent(view);

        Notification notification = builder.build();
        //
        nm.notify(id, notification);
    }

    public static void cancelNotification(Context context, int notification_id) {
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(notification_id);
    }


}
