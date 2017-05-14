package com.example.enkhturbadamsaikhan.completesudoku;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;


/**
 * Handles OCR portion of application-- uses tess-two API to recognize digits
 *
 */
public class TessOCR {

    private TessBaseAPI tessAPI;
    private boolean isInit = false;
    private boolean isEnded = false;

    private static String TAG = "TessOCR";

    public final String TRAINED_DATA_DIRECTORY = "tessdata";
    public final String TRAINED_DATA_FILENAME = "eng.traineddata";
    private String DATA_PATH;
    public static final String TAG_DIR_CREATE_SUCCESS = "directory created success";
    public static final String TAG_DIR_CREATE_FAIL = "directory failed create";

    /**
     * constructor to obtain context+bitmap and initializes DATA_PATH needed for
     * class methods
     **/
    public TessOCR(Context context) {
        DATA_PATH = Environment.getExternalStorageDirectory()
                + "/Android/data/" + context.getPackageName() + "/Files/";
    }

    /**
     * initializes OCR-- copies traineddata file from assets to external storage
     * (which is required by tess-two API) and accesses tess API
     **/
    public void initOCR(Context context) {
        tessAPI = new TessBaseAPI();
        // automate this
        if (!new File(DATA_PATH+TRAINED_DATA_DIRECTORY + "/" + TRAINED_DATA_FILENAME).exists()) {
            copyAssetFolder(context.getAssets(), TRAINED_DATA_DIRECTORY, DATA_PATH + TRAINED_DATA_DIRECTORY);
        }

        // datapath is in parent directory of tessdata
        tessAPI.init(DATA_PATH, "eng");
        tessAPI.setVariable("tessedit_char_whitelist", "123456789");
        isInit = true;

    }

    public boolean isInit() {
        return isInit;
    }

    public boolean isEnded() {
        return isEnded;
    }

    public String doOCR(Bitmap bmp) {
        tessAPI.setImage(bmp);
        String result = tessAPI.getUTF8Text();
        return result;
    }

    public String doOCR(byte[][] byteArray) {
        Log.d("doing OCR", "byte array");
        byte[] stream = convertToByteStream(byteArray);
        tessAPI.setImage(stream, byteArray[0].length, byteArray.length, 1, 1);
        String result = tessAPI.getUTF8Text();
        return result;
    }

    public void endTessOCR() {
        tessAPI.end();
        isEnded = true;
    }

    public static byte[] convertToByteStream(byte[][] byteArray) {
        int index = 0;
        byte[] stream = new byte[byteArray.length * byteArray[0].length];
        for (int i = 0; i < byteArray.length; i++) {
            for (int j = 0; j < byteArray[0].length; j++) {
                stream[index] = byteArray[i][j];
                index++;
            }
        }
        return stream;
    }

    private static boolean copyAssetFolder(AssetManager assetManager,
                                           String fromAssetPath, String toPath) {
        try {
            String[] files = assetManager.list(fromAssetPath);
            new File(toPath).mkdirs();
            boolean res = true;
            for (String file : files)
                if (file.contains("."))
                    res &= copyAsset(assetManager,
                            fromAssetPath + "/" + file,
                            toPath + "/" + file);
                else
                    res &= copyAssetFolder(assetManager,
                            fromAssetPath + "/" + file,
                            toPath + "/" + file);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
            return false;
        }
    }

    private static boolean copyAsset(AssetManager assetManager,
                                     String fromAssetPath, String toPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(fromAssetPath);
            new File(toPath).createNewFile();
            out = new FileOutputStream(toPath);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
            return true;
        } catch(Exception e) {
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
}
