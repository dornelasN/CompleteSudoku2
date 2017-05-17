package com.example.enkhturbadamsaikhan.completesudoku;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.david.completesudoku.Sudoku;
import com.david.completesudoku.SudokuGame;
import com.david.completesudoku.SudokuModel;
import com.david.completesudoku.SudokuSolver;
import com.example.david.testsudoku.DataResult;
import com.example.david.testsudoku.GameActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;
import org.w3c.dom.Text;

import java.io.IOException;

public class Edit_Sudoku_Activity extends AppCompatActivity {

    private static final String TAG = "EditSudokuActivity";

    private int[][] array;
    private TessOCR mOCR;

    private Handler saveHandler;
    public static final int SAVE_SUCCESS = 0;
    public static final int SAVE_FAILURE = 1;

    private SudokuGame sudokuGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        long addr = intent.getLongExtra("myImg", 0);
        Mat tempImg = new Mat( addr );
        Mat img = tempImg.clone();

        mOCR = new TessOCR(getApplicationContext());

        setContentView(R.layout.activity_edit__sudoku);

        Bitmap bitMap = Bitmap.createBitmap(img.cols(), img.rows(),Bitmap.Config.RGB_565);
        Utils.matToBitmap(img, bitMap);

        Button ok = (Button) findViewById(R.id.bt_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Edit_Sudoku_Activity.this, SinglePlayerActivity.class);
                startActivity(i);
            }
        });
        Button retake = (Button) findViewById(R.id.bt_retake);
        retake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //prepare handler
        saveHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if (msg.what == SAVE_SUCCESS){
                    Toast toast = Toast.makeText(Edit_Sudoku_Activity.this, String.format(getString(com.example.david.testsudoku.R.string.save_success),
                            sudokuGame.getName()), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (msg.what == SAVE_FAILURE) {
                    Toast toast = Toast.makeText(Edit_Sudoku_Activity.this, String.format(getString(com.example.david.testsudoku.R.string.save_failure),
                            sudokuGame.getName()), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        };

        // find the imageview and draw it!
        ImageView iv = (ImageView) findViewById(R.id.imageView);
        iv.setImageBitmap(bitMap);
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(width,height);
        iv.setLayoutParams(params);
        AsyncTaskRunner ocr = new AsyncTaskRunner();
        ocr.execute(img);

        array = new int[9][9];
    }

    private class AsyncTaskRunner extends AsyncTask<Mat, Integer, Bitmap> {

        private Mat input;
        ProgressDialog progressDialog;
        //private DigitRecognizer mnist;

        @Override
        protected Bitmap doInBackground(Mat... params) {
            mOCR.initOCR(getApplicationContext());
            publishProgress(-1); // Calls onProgressUpdate()
            input = params[0].clone();


            //Imgproc.GaussianBlur(input, input, new Size(11, 11), 0);

            Imgproc.dilate(input, input, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
            //Imgproc.resize(input, input, new Size(mnist.getWidth()*9,+mnist.getHeight()*9));
            Imgproc.cvtColor(input, input, Imgproc.COLOR_BGR2GRAY);

            //Imgproc.Canny(input, input, 10, 100);
            //Imgproc.Canny(input, input, 50, 100);

            Imgproc.adaptiveThreshold(input, input, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 101, 30);

            //Imgproc.adaptiveThreshold(input, input,255,Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV,15, 2);

            /*input.convertTo(input, CvType.CV_32FC1, 1.0 / 255.0);
            Mat res = CalcBlockMeanVariance(input, 21);
            Core.subtract(new MatOfDouble(1.0), res, res);
            Imgproc.cvtColor(input, input, Imgproc.COLOR_BGRA2BGR);
            Core.add(input, res, res);
            Imgproc.threshold(res, res, 0.85, 1, Imgproc.THRESH_BINARY);
            res.convertTo(res, CvType.CV_8UC1, 255.0);*/

            //remove grid lines
            /*Mat lines = new Mat();
            int threshold = 10;
            int minLineSize = input.width()/2;
            int lineGap = 2;
            Imgproc.HoughLinesP(input, lines, 1, Math.PI/180, threshold, minLineSize, lineGap);
            for(int i = 0; i < lines.cols(); i++) {
                double[] vec = lines.get(0,i);
                Imgproc.line(input, new Point(vec[0], vec[1]), new Point(vec[2],vec[3]), new Scalar(0), 10, Core.LINE_AA, 0);
            }*/

            //fill lines
            /*double Cellwidth = input.cols()/9;
            Mat mask = Mat.zeros(input.rows() + 2, input.cols() + 2, CvType.CV_8U);
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    Imgproc.floodFill(input, mask, new Point(0,0), new Scalar( 255, 255, 255)) ;
                }
            }*/

            //Imgproc.resize(input, input, new Size(mnist.getWidth()*9,+mnist.getHeight()*9));

            double height = input.rows()/9;
            double width = input.cols()/9;
            publishProgress(-2);
            for (int i = 0; i < 9; i++) {
                int col = (int)Math.round(i*height);
                for (int j = 0; j < 9; j++) {
                    int row = (int)Math.round(j*width);
                    Mat digit = input.submat(row, row+(int)Math.round(width), col, col+(int)Math.round(height));
                    //flood fill edges
                    /*Mat mask = Mat.zeros(digit.rows() + 2, digit.cols() + 2, CvType.CV_8U);
                    for (int k = 0; k < mnist.getHeight(); k++) {
                        if (digit.get(k, 0)[0] != 0) {
                            Imgproc.floodFill(digit, mask, new Point(k, 0), new Scalar(0));
                        }
                        if (digit.get(k, digit.cols()-1)[0] != 0) {
                            Imgproc.floodFill(digit, mask, new Point(k, digit.cols()-1), new Scalar(0));
                        }
                    }
                    for (int k = 0; k < mnist.getWidth(); k++) {
                        if (digit.get(0, k)[0] != 0) {
                            Imgproc.floodFill(digit, mask, new Point(0, k), new Scalar(0));
                        }
                        if (digit.get(digit.rows()-1, k)[0] != 0) {
                            Imgproc.floodFill(digit, mask, new Point(digit.rows()-1, k), new Scalar(0));
                        }
                    }*/
                    //flood fill out everything but what is in the center
                    for (int k = 0; k < (int)Math.round(height); k++) {
                        for (int l = 0; l < (int)Math.round(width); l++) {
                            if (digit.get(k, l)[0] < 128) {
                                digit.put(k, l, new double[]{0});
                            } else {
                                digit.put(k, l, new double[]{255});
                            }
                        }
                    }
                    int count = 0;
                    for (int k = (int)Math.round(height/4.0); k < (int)Math.round(height*3.0/4.0); k++) {
                        for (int l = (int)Math.round(width/4.0); l <(int)Math.round(width*3.0/4.0); l++) {
                            if (digit.get(k, l)[0] == 255) {
                                Mat mask = digit.clone();
                                Rect border = new Rect(new Point(0, 0), mask.size());
                                int thickness = 1;
                                Imgproc.rectangle(mask, new Point(0, 0), new Point(mask.rows()-1, mask.cols()-1),
                                        new Scalar(0), thickness);
                                Core.bitwise_not(mask, mask);
                                Core.copyMakeBorder(mask, mask, 1, 1, 1, 1, Core.BORDER_CONSTANT, new Scalar(0));
                                Imgproc.floodFill(digit, mask, new Point(k, l), new Scalar(100));
                            }
                            if (digit.get(k, l)[0] == 100) {
                                count++;
                            }
                        }
                    }
                    if (count < 7) {
                        publishProgress(j, i, 0);
                        continue;
                    }
                    Imgproc.threshold(digit, digit, 254, 255, Imgproc.THRESH_TOZERO_INV);
                    Imgproc.threshold(digit, digit, 99, 255, Imgproc.THRESH_BINARY);

                    //Mat test = new Mat();
                    //Imgproc.resize(digit, test, new Size(mnist.getWidth(),mnist.getHeight()));
                    //int value = mnist.findValue(test);

                    Bitmap bmp = Bitmap.createBitmap(digit.cols(), digit.rows(),
                            Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(digit, bmp);

                    String bmpInfo = String.format("width: %d, height %d", bmp.getWidth(), bmp.getHeight());
                    Log.d(TAG, bmpInfo);
                    int ans = 0;
                    try {
                        ans = Integer.parseInt(mOCR.doOCR(bmp));
                    } catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                    }
                    if (ans < 1) {
                        ans = 0;
                    }
                    if (ans > 9) {
                        ans = trimNum(ans);
                    }

                    publishProgress(j, i, ans);
                }
            }

            Mat res = input;
            Bitmap bitMap = Bitmap.createBitmap(res.cols(), res.rows(),Bitmap.Config.RGB_565);
            Utils.matToBitmap(res, bitMap);
            return bitMap;
        }

        private int trimNum(int n) {
            while (n > 9) {
                n = n / 10;
            }
            return n;
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            // execution of result of Long time consuming operation
            ImageView iv = (ImageView) findViewById(R.id.imageView);
            iv.setImageBitmap(result);

            TextView difficulty = (TextView) findViewById(R.id.difficulty_view);
            Sudoku sudoku = new Sudoku(array);
            SudokuSolver s = new SudokuSolver(new Sudoku(sudoku));
            if (s.solve()) {
                difficulty.setText(s.getDifficulty());

                sudokuGame = new SudokuGame(sudoku);
                sudokuGame.setDifficulty(s.getDifficulty());
                DataResult.getInstance().setSudokuGame(sudokuGame);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    SudokuModel sudokuModel = new FirebaseModel("/Users/" + user.getUid());
                    DataResult.getInstance().setSudokuModel(sudokuModel);
                    sudokuGame.setName("Captured Image");
                    try {
                        sudokuModel.saveGame(sudokuGame, saveHandler);
                    } catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                        saveHandler.sendEmptyMessage(SAVE_FAILURE);
                    }
                } else {
                    Toast toast = Toast.makeText(Edit_Sudoku_Activity.this, R.string.not_logged_in, Toast.LENGTH_SHORT);
                    toast.show();
                }
            } else {
                difficulty.setText("No Solution");
            }
            progressDialog.dismiss();

        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(Edit_Sudoku_Activity.this,
                    "Complete Sudoku",
                    "Training Classifier");
        }


        @Override
        protected void onProgressUpdate(Integer... update) {
            //show progress;
            if (update[0] == -1) {
                progressDialog.setMessage("Processing Image");
            } else if (update[0] == -2) {
                progressDialog.setMessage("Classifying Digits");
            } else {
                TextView textView = (TextView) findViewById(R.id.textView);
                array[update[0]][update[1]] = update[2];
                textView.setText(getMatrix(array));
                Log.d(TAG, "i: "+update[0]+" j: "+update[1]+" value: "+update[2]);
            }
        }
    }

    private String getMatrix(int[][] a) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 9; ++i) {
            if (i % 3 == 0)
                sb.append(" -----------------------").append("\n");
            for (int j = 0; j < 9; ++j) {
                if (j % 3 == 0) sb.append("| ");
                sb.append(a[i][j] == 0 ? " " : Integer.toString(a[i][j]));

                sb.append(' ');
            }
            sb.append("|").append("\n");
        }
        sb.append(" -----------------------").append("\n");

        return sb.toString();
    }

    static {
        //System.loadLibrary("native-lib");

        if(!OpenCVLoader.initDebug()){
            Log.d(TAG, "OpenCV not loaded");
        } else {
            Log.d(TAG, "OpenCV loaded");
        }
    }
}
