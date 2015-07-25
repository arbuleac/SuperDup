package barqsoft.footballscores.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;
import barqsoft.footballscores.ViewHolder;
import barqsoft.footballscores.service.MyFetchService;

/**
 * @since 7/25/15.
 */
public class ScoresWidgetFactory implements RemoteViewsService.RemoteViewsFactory, Loader.OnLoadCompleteListener<Cursor> {

    private static final long WIDGET_UPDATE_THROTTLE = 1000;
    private final int appWidgetId;
    private Context context;
    private CursorLoader loader;
    private MatrixCursor matrixCursor;

    public ScoresWidgetFactory(Context applicationContext, Intent intent) {
        context = applicationContext;
        appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        Intent serviceStart = new Intent(context, MyFetchService.class);
        context.startService(serviceStart);
        initLoader();
    }

    private void initLoader() {
        Date today = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        loader = new CursorLoader(context, DatabaseContract.ScoresTable.buildScoreWithDate(),
                null, null, new String[]{dateFormat.format(today)}, null);
        loader.setUpdateThrottle(WIDGET_UPDATE_THROTTLE);
        loader.registerListener(appWidgetId, this);
        loader.startLoading();
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
        context = null;
        if (loader != null) {
            loader.reset();
        }
        if (matrixCursor != null && !matrixCursor.isClosed()) {
            matrixCursor.close();
            matrixCursor = null;
        }
    }

    @Override
    public RemoteViews getLoadingView() {
        //use the defaul loading
        return null;
    }

    @Override
    public int getCount() {
        return 2; //TODO put back matrixCursor == null ? 0 : matrixCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position < 0 || position >= getCount()) {
            return null;
        }
        if (matrixCursor == null || matrixCursor.getCount() == 0) {
            return null;
        }
        matrixCursor.moveToFirst(); //TODO put back matrixCursor.moveToPosition(position);
        RemoteViews views = newView();
        bindView(views, matrixCursor);
        return views;
    }


    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
        matrixCursor = matrixCursorFromCursor(data);
        data.close();
    }

    public static MatrixCursor matrixCursorFromCursor(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        String[] columnNames = cursor.getColumnNames();
        if (columnNames == null) {
            columnNames = new String[]{};
        }
        MatrixCursor newCursor = new MatrixCursor(columnNames);
        int numColumns = cursor.getColumnCount();
        String data[] = new String[numColumns];
        cursor.moveToPosition(-1);
        while (cursor.moveToNext()) {
            for (int i = 0; i < numColumns; i++) {
                data[i] = cursor.getString(i);
            }
            newCursor.addRow(data);
        }
        return newCursor;
    }

    public RemoteViews newView() {
        return new RemoteViews(context.getPackageName(), R.layout.scores_widget_list_item);
    }

    public static final int COL_MATCHTIME = 2;
    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;

    public void bindView(RemoteViews view, Cursor cursor) {
        view.setTextViewText(R.id.home_name, cursor.getString(COL_HOME));
        view.setTextViewText(R.id.away_name, cursor.getString(COL_AWAY));
        view.setTextViewText(R.id.data_textview, cursor.getString(COL_MATCHTIME));
        view.setTextViewText(R.id.score_textview, Utilies.getScores(cursor.getInt(COL_HOME_GOALS), cursor.getInt(COL_AWAY_GOALS)));
        view.setImageViewResource(R.id.home_crest, Utilies.getTeamCrestByTeamName(context, cursor.getString(COL_HOME)));
        view.setImageViewResource(R.id.away_crest, Utilies.getTeamCrestByTeamName(context, cursor.getString(COL_AWAY)));
    }
}
