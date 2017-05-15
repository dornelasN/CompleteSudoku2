
package com.example.enkhturbadamsaikhan.completesudoku;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;
import org.opencv.utils.Converters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivityGallery extends AppCompatActivity {
    private int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "MainActivity";

    private static final int SELECTED_PIC=1;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gallery);

        imageView = (ImageView)findViewById(R.id.imageView);
    }

    public void btnClick(View v){
        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageBitmap(bitmap);
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(width,height);
                imageView.setLayoutParams(params);
                AsyncTaskRunner proc = new AsyncTaskRunner();
                proc.execute(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class AsyncTaskRunner extends AsyncTask<Bitmap, Bitmap, Mat> {

        private Mat pic;
        ProgressDialog progressDialog;

        @Override
        protected Mat doInBackground(Bitmap... params) {
            pic = new Mat();
            Utils.bitmapToMat(params[0], pic);
            Mat input = pic.clone();

            Imgproc.GaussianBlur(input, input, new Size(11, 11), 0);

            Imgproc.cvtColor(input, input, Imgproc.COLOR_BGR2GRAY);

            Imgproc.adaptiveThreshold(input, input, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 5, 2);

            Core.bitwise_not(input, input);

            Imgproc.dilate(input, input, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2)));

            List<MatOfPoint> contourList = new ArrayList<MatOfPoint>();
            Mat hierarchy = new Mat();
            Imgproc.findContours(input, contourList, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
            int max = 0;
            int index = 0;
            for (int i = 0; i < contourList.size(); i++) {
                Rect bounding_rect = Imgproc.boundingRect(contourList.get(i));
                int area = bounding_rect.width*bounding_rect.height;
                if (area > max) {
                    max = area;
                    index = i;
                }
            }

            double newPoints[][] = null;
            if (contourList.size() > 0) {
                MatOfPoint2f contour2f = new MatOfPoint2f();
                MatOfPoint2f approxContour2f = new MatOfPoint2f();
                contourList.get(index).convertTo(contour2f, CvType.CV_32FC2);
                Imgproc.approxPolyDP(contour2f, approxContour2f, Imgproc.arcLength(contour2f, true) * 0.02, true);
                if (approxContour2f.total() == 4) {
                    newPoints = new double[4][2];
                    List<Point> dots = approxContour2f.toList();
                    for (int i = 0; i < 4; i++) {
                        newPoints[i][0] = dots.get(i).x;
                        newPoints[i][1] = dots.get(i).y;
                    }
                }

                contour2f.release();
                approxContour2f.release();
            }
            hierarchy.release();
            input.release();

            //leave if failed
            if (newPoints == null) {
                pic.release();
                //update user on failure
                publishProgress(null);
                return null;
            }

            Mat overlay = pic.clone();
            Mat res = pic.clone();
            Imgproc.fillConvexPoly(overlay, new MatOfPoint(new Point(newPoints[0]), new Point(newPoints[1]),
                    new Point(newPoints[2]), new Point(newPoints[3])), new Scalar(64, 128, 255));
            double opacity = 0.4;
            Core.addWeighted(overlay, opacity, res, 1 - opacity, 0, res);
            overlay.release();

            //Bitmap bitMap = Bitmap.createBitmap(res.cols(), res.rows(),Bitmap.Config.RGB_565);
            //Utils.matToBitmap(res, bitMap);
            //publishProgress(bitMap);
            res.release();
            /////

            List<Point> flexCorners = new ArrayList<>();
            for (double[] point : newPoints) {
                flexCorners.add(new Point(point[0], point[1]));
            }
            Point centroid = new Point(0,0);

            for(Point point : flexCorners)
            {
                Log.i(TAG, "Point x: "+ point.x+  " Point y: "+ point.y);
                centroid.x+=point.x;
                centroid.y+=point.y;
            }
            centroid.x/=((double)flexCorners.size());
            centroid.y/=((double)flexCorners.size());

            sortCorners(flexCorners,centroid);

            int length = (int)getMaxEdgeLength(flexCorners);
            Mat correctedImage = new Mat(length, length, pic.type());
            Mat srcPoints= Converters.vector_Point2f_to_Mat(flexCorners);

            int offset = 0;
            Mat destPoints=Converters.vector_Point2f_to_Mat(Arrays.asList(
                    new Point(offset, offset),
                    new Point(correctedImage.cols()-offset, offset),
                    new Point(correctedImage.cols()-offset,correctedImage.rows()-offset),
                    new Point(offset,correctedImage.rows()-offset)));

            Log.d(TAG, "rows: "+correctedImage.rows()+" cols: "+correctedImage.cols());
            Mat transformation = Imgproc.getPerspectiveTransform(srcPoints, destPoints);
            Imgproc.warpPerspective(pic, correctedImage, transformation, correctedImage.size());
            pic.release();

            return transformation;
        }

        private void sortCorners(List<Point> corners, Point center)
        {
            ArrayList<Point> top=new ArrayList<Point>();
            ArrayList<Point> bottom=new ArrayList<Point>();

            for (int i = 0; i < corners.size(); i++)
            {
                if (corners.get(i).y < center.y)
                    top.add(corners.get(i));
                else
                    bottom.add(corners.get(i));
            }

            double topLeft=top.get(0).x;
            int topLeftIndex=0;
            for(int i=1;i<top.size();i++)
            {
                if(top.get(i).x<topLeft)
                {
                    topLeft=top.get(i).x;
                    topLeftIndex=i;
                }
            }

            double topRight=0;
            int topRightIndex=0;
            for(int i=0;i<top.size();i++)
            {
                if(top.get(i).x>topRight)
                {
                    topRight=top.get(i).x;
                    topRightIndex=i;
                }
            }

            double bottomLeft=bottom.get(0).x;
            int bottomLeftIndex=0;
            for(int i=1;i<bottom.size();i++)
            {
                if(bottom.get(i).x<bottomLeft)
                {
                    bottomLeft=bottom.get(i).x;
                    bottomLeftIndex=i;
                }
            }

            double bottomRight=bottom.get(0).x;
            int bottomRightIndex=0;
            for(int i=1;i<bottom.size();i++)
            {
                if(bottom.get(i).x>bottomRight)
                {
                    bottomRight=bottom.get(i).x;
                    bottomRightIndex=i;
                }
            }

            Point topLeftPoint = top.get(topLeftIndex);
            Point topRightPoint = top.get(topRightIndex);
            Point bottomLeftPoint = bottom.get(bottomLeftIndex);
            Point bottomRightPoint = bottom.get(bottomRightIndex);

            corners.clear();
            corners.add(topLeftPoint);
            corners.add(topRightPoint);
            corners.add(bottomRightPoint);
            corners.add(bottomLeftPoint);
        }

        private double getMaxEdgeLength(List<Point> corners) {
            double max = 0;
            for (int i = 0; i < corners.size()-1; i++) {
                for (int j = i+1; j < corners.size(); j++) {
                    Point p1 = corners.get(i);
                    Point p2 = corners.get(j);
                    double dist = Math.sqrt((p1.x-p2.x)*(p1.x-p2.x)+(p1.y-p2.y)*(p1.y-p2.y));
                    if (dist > max) {
                        max = dist;
                    }
                }
            }
            return max;
        }

        @Override
        protected void onPostExecute(Mat result) {
            // execution of result of Long time consuming operation
            if (result == null) {
                Toast.makeText(MainActivityGallery.this, "No puzzle detected", Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();
            long addr = result.getNativeObjAddr();
            Intent intent = new Intent(MainActivityGallery.this, Edit_Sudoku_Activity.class);
            intent.putExtra( "myImg", addr );
            startActivity( intent );
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivityGallery.this,
                    "Complete Sudoku",
                    "Processing image");
        }


        @Override
        protected void onProgressUpdate(Bitmap... update) {
            //show progress;
            if (update != null) {
                Log.d(TAG, "4 corners found");
                imageView.setImageBitmap(update[0]);
            }
        }
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
