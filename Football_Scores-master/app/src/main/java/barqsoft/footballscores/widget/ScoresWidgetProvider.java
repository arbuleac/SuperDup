package barqsoft.footballscores.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import barqsoft.footballscores.R;

/**
 * @since 7/25/15.
 */
public class ScoresWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = updateWidgetListView(context,
                    appWidgetId);
            appWidgetManager.updateAppWidget(appWidgetId,
                    remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {
        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(), R.layout.score_appwidget);

        Intent serviceIntent = new Intent(context, ScoresWidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        remoteViews.setRemoteAdapter(R.id.widget_lv, serviceIntent);
        remoteViews.setEmptyView(R.id.widget_lv, R.id.empty_view);
        return remoteViews;
    }
}
