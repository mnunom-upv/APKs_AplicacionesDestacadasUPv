package com.example.skinchange;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.content.ContentValues;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final int REQ_CAMERA   = 7;
    private static final int REQ_STORAGE  = 8; // para API <= 28

    private JavaCameraView cameraView;
    private Switch switchFilter;
    private SeekBar seekLuz;
    private TextView txtLuz;
    private Button btnSwitchCam, btnCapture;

    private boolean applyFilter = true;

    // Mats
    private Mat mRgba, mTmp, mHSV, mYCrCb, mMaskHSV, mMaskYcc, mMask, mMaskFeather;

    // Umbrales HSV (piel) — endurecidos para reducir amarillos/beige planos
    private final Scalar HSV_MIN = new Scalar(0, 70, 40);     // antes 0,58,30
    private final Scalar HSV_MAX = new Scalar(33, 255, 255);

    // Umbrales YCrCb (piel) — Cr max un poco más bajo para ser más selectivo
    private final Scalar YCC_MIN = new Scalar(0, 133, 77);
    private final Scalar YCC_MAX = new Scalar(235, 170, 127); // antes 235,173,127

    private int luzSlider = 0; // -100..+100
    private int currentCameraIndex = CameraBridgeViewBase.CAMERA_ID_FRONT;
    private volatile boolean captureRequested = false;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraView   = findViewById(R.id.camera_view);
        switchFilter = findViewById(R.id.switchFilter);
        seekLuz      = findViewById(R.id.seekLuz);
        txtLuz       = findViewById(R.id.txtLuz);
        btnSwitchCam = findViewById(R.id.btnSwitchCam);
        btnCapture   = findViewById(R.id.btnCapture);

        cameraView.setVisibility(SurfaceView.VISIBLE);
        cameraView.setCvCameraViewListener(this);
        cameraView.setCameraIndex(currentCameraIndex);
        cameraView.setKeepScreenOn(true);

        switchFilter.setChecked(true);
        switchFilter.setOnCheckedChangeListener((b, checked) -> applyFilter = checked);

        seekLuz.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                luzSlider = progress - 100;
                txtLuz.setText("Luz piel: " + luzSlider);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnSwitchCam.setOnClickListener(v -> switchCamera());
        btnCapture.setOnClickListener(v -> {
            // Si API <= 28, aseguramos permiso de escritura antes
            if (Build.VERSION.SDK_INT <= 28 && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQ_STORAGE);
            } else {
                captureRequested = true;
            }
        });

        ensureCameraPermission();
    }

    /** Arranca la cámara si ya hay permiso y OpenCV está listo */
    private void startCameraIfReady() {
        if (cameraView != null) {
            cameraView.setCameraPermissionGranted();
            if (OpenCVLoader.initDebug()) {
                Log.d("OpenCV", "OpenCV cargado, iniciando cámara...");
                cameraView.enableView();
            } else {
                Log.e("OpenCV", "No se pudo inicializar OpenCV");
            }
        }
    }

    private void ensureCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQ_CAMERA);
        } else {
            startCameraIfReady();
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraIfReady();
            } else {
                finish();
            }
            return;
        }

        if (requestCode == REQ_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                captureRequested = true; // dispara tras conceder
            }
            return;
        }
    }

    @Override protected void onResume() {
        super.onResume();
        startCameraIfReady();
    }

    @Override protected void onPause() {
        super.onPause();
        if (cameraView != null) cameraView.disableView();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (cameraView != null) cameraView.disableView();
    }

    @Override public void onCameraViewStarted(int width, int height) {
        Log.d("OpenCV", "Camera started: " + width + "x" + height);
        mRgba        = new Mat(height, width, CvType.CV_8UC4);
        mTmp         = new Mat(height, width, CvType.CV_8UC4);
        mHSV         = new Mat(height, width, CvType.CV_8UC3);
        mYCrCb       = new Mat(height, width, CvType.CV_8UC3);
        mMaskHSV     = new Mat(height, width, CvType.CV_8UC1);
        mMaskYcc     = new Mat(height, width, CvType.CV_8UC1);
        mMask        = new Mat(height, width, CvType.CV_8UC1);
        mMaskFeather = new Mat(height, width, CvType.CV_8UC1);
    }

    @Override public void onCameraViewStopped() {
        if (mRgba != null)        mRgba.release();
        if (mTmp  != null)        mTmp.release();
        if (mHSV  != null)        mHSV.release();
        if (mYCrCb!= null)        mYCrCb.release();
        if (mMaskHSV != null)     mMaskHSV.release();
        if (mMaskYcc != null)     mMaskYcc.release();
        if (mMask != null)        mMask.release();
        if (mMaskFeather != null) mMaskFeather.release();
    }

    @Override public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        if (!applyFilter) {
            handleCaptureIfRequested(mRgba);
            return mRgba;
        }

        // --- Máscara HSV ---
        Imgproc.cvtColor(mRgba, mHSV, Imgproc.COLOR_RGBA2RGB);
        Imgproc.cvtColor(mHSV, mHSV, Imgproc.COLOR_RGB2HSV);
        Core.inRange(mHSV, HSV_MIN, HSV_MAX, mMaskHSV);

        // --- Máscara YCrCb ---
        Imgproc.cvtColor(mRgba, mYCrCb, Imgproc.COLOR_RGBA2RGB);
        Imgproc.cvtColor(mYCrCb, mYCrCb, Imgproc.COLOR_RGB2YCrCb);
        Core.inRange(mYCrCb, YCC_MIN, YCC_MAX, mMaskYcc);

        // --- Intersección para reducir falsos positivos ---
        Core.bitwise_and(mMaskHSV, mMaskYcc, mMask);

        // --- Suavizado / feather ---
        Imgproc.medianBlur(mMask, mMask, 5);
        Imgproc.GaussianBlur(mMask, mMaskFeather, new Size(21, 21), 0);

        // --- Separar H, S, V ---
        List<Mat> hsv = new ArrayList<>(3);
        Core.split(mHSV, hsv);
        Mat H = hsv.get(0);
        Mat S = hsv.get(1);
        Mat V = hsv.get(2);

        // --- Aclarar/oscurecer V solo en piel ---
        double gain = 1.0 + (luzSlider * 0.006); // 0.4..1.6 aprox
        Mat Vfloat = new Mat();
        V.convertTo(Vfloat, CvType.CV_32F);
        Core.multiply(Vfloat, new Scalar(gain), Vfloat);
        Core.min(Vfloat, new Scalar(255.0), Vfloat);
        Core.max(Vfloat, new Scalar(0.0), Vfloat);
        Mat V8 = new Mat();
        Vfloat.convertTo(V8, CvType.CV_8U);

        // Mantener V original fuera de la piel (usar máscara invertida)
        Mat invMask = new Mat();
        Core.bitwise_not(mMaskFeather, invMask);
        V.copyTo(V8, invMask);
        V = V8;
        invMask.release();

        // Opcional: subir saturación cuando aclaras
        if (luzSlider > 0) {
            Mat Sfloat = new Mat();
            S.convertTo(Sfloat, CvType.CV_32F);
            double satGain = 1.0 + (luzSlider * 0.004);
            Core.multiply(Sfloat, new Scalar(satGain), Sfloat);
            Core.min(Sfloat, new Scalar(255.0), Sfloat);
            Sfloat.convertTo(S, CvType.CV_8U);
            Sfloat.release();
        }

        // --- Reconstruir y volver a RGBA ---
        hsv.set(0, H); hsv.set(1, S); hsv.set(2, V);
        Core.merge(hsv, mHSV);
        Imgproc.cvtColor(mHSV, mTmp, Imgproc.COLOR_HSV2RGB);
        Imgproc.cvtColor(mTmp, mTmp, Imgproc.COLOR_RGB2RGBA);

        // --- Mezcla suave con la imagen original ---
        Mat inv = new Mat();
        Core.bitwise_not(mMaskFeather, inv);
        Mat base = new Mat();
        mRgba.copyTo(base, inv);          // original fuera de piel
        mTmp.copyTo(mRgba, mMaskFeather); // modificado en piel
        Core.add(base, mRgba, mRgba);

        Vfloat.release(); V8.release(); inv.release(); base.release();

        // Captura si fue solicitada (con filtro aplicado)
        handleCaptureIfRequested(mRgba);
        return mRgba;
    }

    /** Alterna frontal/trasera y reinicia vista */
    private void switchCamera() {
        if (cameraView == null) return;
        cameraView.disableView();
        currentCameraIndex = (currentCameraIndex == CameraBridgeViewBase.CAMERA_ID_FRONT)
                ? CameraBridgeViewBase.CAMERA_ID_BACK
                : CameraBridgeViewBase.CAMERA_ID_FRONT;
        cameraView.setCameraIndex(currentCameraIndex);
        startCameraIfReady();
    }

    /** Guarda el frame mostrado en galería (Pictures/SkinChange) */
    private void handleCaptureIfRequested(Mat rgba) {
        if (!captureRequested) return;
        captureRequested = false;

        Mat frameToSave = rgba.clone();
        new Thread(() -> {
            try {
                saveMatToGallery(frameToSave);
            } finally {
                frameToSave.release();
            }
        }).start();
    }

    private void saveMatToGallery(Mat rgba) {
        try {
            Bitmap bmp = Bitmap.createBitmap(rgba.cols(), rgba.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(rgba, bmp);

            String displayName = "skinchange_" + System.currentTimeMillis() + ".jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, displayName);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

            // API 29+ usa RELATIVE_PATH (scoped storage)
            if (Build.VERSION.SDK_INT >= 29) {
                values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/SkinChange");
            }

            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (uri != null) {
                try (OutputStream out = getContentResolver().openOutputStream(uri)) {
                    bmp.compress(Bitmap.CompressFormat.JPEG, 95, out);
                }
            }
        } catch (Exception e) {
            Log.e("OpenCV", "Error guardando foto: " + e.getMessage());
        }
    }
}
