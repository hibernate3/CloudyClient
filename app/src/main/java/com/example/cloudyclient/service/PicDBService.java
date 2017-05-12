package com.example.cloudyclient.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.example.cloudyclient.MainApplication;
import com.example.cloudyclient.model.biz.PicEntityDBManager;
import com.example.cloudyclient.util.LocalStorageUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import permissions.dispatcher.RuntimePermissions;

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
    private static final String ACTION_INSERT = "com.example.cloudyclient.service.action.INSERT";

    // TODO: Rename parameters
    private static final String PICS_PATH_PARAM = "com.example.cloudyclient.service.extra.PICS_PATH_PARAM";

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
        intent.setAction(ACTION_INSERT);
        intent.putStringArrayListExtra(PICS_PATH_PARAM, (ArrayList<String>) paths);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INSERT.equals(action)) {
                final List<String> pics_path_param = intent.getStringArrayListExtra(PICS_PATH_PARAM);
                handleActionInsert(pics_path_param);
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
}
