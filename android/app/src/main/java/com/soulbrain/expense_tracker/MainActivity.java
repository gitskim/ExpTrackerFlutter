package com.soulbrain.expense_tracker;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "samples.flutter.dev/sms";

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler(
                        (call, result) -> {
                            // Note: this method is invoked on the main thread.
                            if (call.method.equals("getMessages")) {
                                getBatteryLevel((String) call.argument("text"));

                            } else {
                                result.notImplemented();
                            }
                        }
                );
    }

    private void getBatteryLevel(String text) {
        if (!hasReadSmsPermission()) {
            showRequestPermissionsInfoAlertDialog();
        } else {
            // do nothing
        }

        Cursor cursor = getContentResolver().query(
                Uri.parse("content://sms/inbox"),
                null,
                null,
                null,
                null);

        int totalSMS = cursor.getCount();

        List<String> numbers = new ArrayList();
        List<String> messages = new ArrayList();
        List<String> results = new ArrayList();

        while (cursor != null && cursor.moveToNext()) {
            String smsDate = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE));
            String number = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
            String body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY));
            if (number.equals("692639")) {
                numbers.add(number);
            }
        }
        // else {
        // throw new RuntimeException("You have no SMS");
        // }
        cursor.close();
    }

    private static final int SMS_PERMISSION_CODE = 0;
    private static final String TAG = "MainActivity";
    private static final String WRITE_KEY = "25Uy8pcW3tbwO54Z0nCv3tL8CQF";
    private String DATA_PLANE_URL = "https://protonmailnhuc.dataplane.rudderstack.com";

    /**
     * Runtime permission shenanigans
     */
    private boolean hasReadSmsPermission() {
        return ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestReadAndSendSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_SMS)) {
            Log.d(TAG, "shouldShowRequestPermissionRationale(), no permission requested");
            return;
        }
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
                SMS_PERMISSION_CODE);
    }

    /**
     * Optional informative alert dialog to explain the user why the app needs the Read/Send SMS permission
     */
    private void showRequestPermissionsInfoAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.permission_alert_dialog_title);
        builder.setMessage(R.string.permission_dialog_message);
        builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                requestReadAndSendSmsPermission();
            }
        });
        builder.show();
    }

}