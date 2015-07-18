package it.jaschke.alexandria;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ScannerActivity extends ActionBarActivity implements ZBarScannerView.ResultHandler {
    private static final String CAMERA_ID = "CAMERA_ID";
    private static final String LOG_TAG = "ScannerActivity";
    public static final String EXTRA_CODE = "extra_code";
    private ZBarScannerView scannerView;
    private int cameraId = -1;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        if (state != null) {
            cameraId = state.getInt(CAMERA_ID, -1);
        } else {
            cameraId = -1;
        }

        scannerView = new ZBarScannerView(this);
        setupFormats();
        setContentView(scannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera(cameraId);
        scannerView.setAutoFocus(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CAMERA_ID, cameraId);
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.e(LOG_TAG, "Contents = " + rawResult.getContents() + ", Format = " + rawResult.getBarcodeFormat().getName());
        if (!rawResult.getBarcodeFormat().getName().equals("ISBN13") && !rawResult.getBarcodeFormat().getName().equals("ISBN10")) {
            scannerView.startCamera(cameraId);
            return;
        }
        String code = rawResult.getContents();
        if (rawResult.getBarcodeFormat().getName().equals("ISBN10")) {
            code = "978" + code;
        }
        try {
            Long.parseLong(code);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.error_parse_isbn), Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
            finish();
            return;
        }
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_CODE, code);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    public void setupFormats() {
        if (scannerView != null) {
            scannerView.setFormats(BarcodeFormat.ALL_FORMATS);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }
}