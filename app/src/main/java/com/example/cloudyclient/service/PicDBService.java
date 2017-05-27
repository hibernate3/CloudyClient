package com.example.cloudyclient.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.widget.Switch;

import com.example.cloudyclient.model.biz.PicEntityDBManager;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class PicDBService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_INSERT_MUL = "com.example.cloudyclient.service.action.INSERT_MUL";
    private static final String ACTION_INSERT_ONE = "com.example.cloudyclient.service.action.INSERT_ONE";
    private static final String ACTION_DELETE_ONE = "com.example.cloudyclient.service.action.DELETE_ONE";

    // TODO: Rename parameters
    private static final String PICS_PATH_PARAM = "com.example.cloudyclient.service.extra.PICS_PATH_PARAM";
    private static final String PIC_PATH_PARAM = "com.example.cloudyclient.service.extra.PIC_PATH_PARAM";

    public PicDBService() {
        super("PicDBService");
    }

    /**
     * Starts this service to perform action Insert with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionInsert(Context context, List<String> paths) {
        Intent intent = new Intent(context, PicDBService.class);
        intent.setAction(ACTION_INSERT_MUL);
        intent.putStringArrayListExtra(PICS_PATH_PARAM, (ArrayList<String>) paths);
        context.startService(intent);
    }

    public static void startActionInsert(Context context, String path) {
        Intent intent = new Intent(context, PicDBService.class);
        intent.setAction(ACTION_INSERT_ONE);
        intent.putExtra(PIC_PATH_PARAM, path);
        context.startService(intent);
    }

    public static void startActionDelete(Context context, String path) {
        Intent intent = new Intent(context, PicDBService.class);
        intent.setAction(ACTION_DELETE_ONE);
        intent.putExtra(PIC_PATH_PARAM, path);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();

            switch (action) {
                case ACTION_INSERT_MUL:
                    handleActionInsert(intent.getStringArrayListExtra(PICS_PATH_PARAM));
                    break;
                case ACTION_INSERT_ONE:
                    handleActionInsert(intent.getStringExtra(PIC_PATH_PARAM));
                    break;
                case ACTION_DELETE_ONE:
                    handleActionDelete(intent.getStringExtra(PIC_PATH_PARAM));
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Handle action Insert in the provided background thread with the provided
     * parameters.
     */
    private void handleActionInsert(List<String> paths) {
        PicEntityDBManager.getInstance().insert(paths);
    }

    private void handleActionInsert(String path) {
        PicEntityDBManager.getInstance().insert(path);
    }

    private void handleActionDelete(String path) {
        PicEntityDBManager.getInstance().delete(path);
    }
}
