package com.mproduction.watchplaces.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.mproduction.watchplaces.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.os.Environment.DIRECTORY_PICTURES;

public class Utils {
    public static final String EXTRA = "extra";
    public static final String EXTRA_SEC = "extra_sec";

    public static final int TARGET_WIDTH_LARGE = 900;
    public static final int TARGET_WIDTH_NORMAL = 500;
    public static final int TARGET_WIDTH_SMALL = 200;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static final long TIME_SECOND_IN_MILLISECONDS = 1000;
    public static final long TIME_MINUTE_IN_MILLISECONDS = TIME_SECOND_IN_MILLISECONDS * 60;
    public static final long TIME_HOUR_IN_MILLISECONDS = TIME_MINUTE_IN_MILLISECONDS * 60;
    public static final long TIME_DAY_IN_MILLISECONDS = TIME_HOUR_IN_MILLISECONDS * 24;

    public static final String TMP_PICTURE_PATH = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES)+"/"+"tmp_picture"+".jpg";

    public static final String TMP_PICTURE_FILE_PATH = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES)+"/"+"tmp_picture";

    public static final int PICK_IMAGE = 1001;
    public static final int PICK_VIDEO = 1002;

    public static String getLargeCountString(int points) {
        if (points < 100000) {
            return String.valueOf(points);
        } else {
            float result = ((float) points) / 1000.0f;
            return String.format("%.2f", result) + "k";
        }
    }

    public static String getCountString(int points) {
        if (points < 1000) {
            return String.valueOf(points);
        } else {
            float result = ((float) points) / 1000.0f;
            return String.format("%.2f", result) + "k";
        }
    }


    public static interface EditBoxDialogCallback {
        public void onPositiveClick(String message);
    }

    public static void showEditBoxDialog(Context context, int titleId, int hintId, final EditBoxDialogCallback listener) {

        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_text, null);
        final EditText etComments = (EditText) view.findViewById(R.id.edit_text);
        etComments.setHint(hintId);
        etComments.setSingleLine(true);

        new AlertDialog.Builder(context)
                .setTitle(titleId)
                .setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String location = etComments.getText().toString();

                        listener.onPositiveClick(location);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create().show();
    }

    public static void showEditBoxDialog(Context context, String title, String hint, final EditBoxDialogCallback listener) {

        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_text, null);
        final EditText etComments = (EditText) view.findViewById(R.id.edit_text);
        etComments.setHint(hint);
        etComments.setSingleLine(true);

        new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String location = etComments.getText().toString();

                        listener.onPositiveClick(location);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create().show();
    }

    public static void showEditBoxDialog(Context context, String title, String hint, String placeholder, final EditBoxDialogCallback listener) {

        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_text, null);
        final EditText etComments = (EditText) view.findViewById(R.id.edit_text);
        etComments.setHint(hint);
        etComments.setText(placeholder);
        etComments.setSingleLine(true);

        new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String location = etComments.getText().toString();

                        listener.onPositiveClick(location);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create().show();
    }

    public static int compareLongValue(long o1, long o2) {
        if (o1 == o2) {
            return 0;
        } else if (o1 < o2) {
            return -1;
        } else {
            return 1;
        }
    }

    public static void showIntegerEditBoxDialog(Context context, String title, String hint, final EditBoxDialogCallback listener) {

        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_text, null);
        final EditText etComments = (EditText) view.findViewById(R.id.edit_text);
        etComments.setHint(hint);
        etComments.setSingleLine(true);
        etComments.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);

        new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String location = etComments.getText().toString();

                        listener.onPositiveClick(location);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create().show();
    }

    public static void showDialog(Context context, String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(R.string.ok, null)
                .create().show();
    }

    public static void showDialog(Context context, String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, null)
                .create().show();
    }

    public static void showDialog(Context context, int message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(R.string.ok, null)
                .create().show();
    }

    public static void showDialog(Context context, int message, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(R.string.ok, listener)
                .create().show();
    }

    public static void showQueryDialog(Context context, String message, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(R.string.yes, listener)
                .setNegativeButton(R.string.no, null)
                .create().show();
    }

    public static void showProgressDialog(ProgressDialog progressDialog) {
        progressDialog.setMessage("Please wait..");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

    }

    public static void hideProgressDialog(ProgressDialog progressDialog)
    {
        if(progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }

    public static void hideProgressDialog(Activity activity, final ProgressDialog progressDialog)
    {
        if(progressDialog != null && progressDialog.isShowing())
        {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.hide();
                }
            });
        }
    }

    public static String getSecondsString(long time) {
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        return format.format(new Date(time));
    }

    public static String getPaceString(double speed) {
        double seconds = speed > 0 ? 1000 / speed : 0;

        int hour = (int) (seconds / 3600);
        int min = (int) ((seconds % 3600) / 60);
        int sec = (int) (seconds % 60);
        if (hour > 0) {
            return String.format("%dh%d'%d\"", hour, min, sec);
        } else {
            return String.format("%d'%d\"", min, sec);
        }
    }

    public static String getPaceStr(long time) {
        double seconds = time / 1000;

        int hour = (int) (seconds / 3600);
        int min = (int) ((seconds % 3600) / 60);
        int sec = (int) (seconds % 60);
        if (hour > 0) {
            return String.format("%dh%d'%d\"", hour, min, sec);
        } else {
            return String.format("%d'%d\"", min, sec);
        }
    }

    public static String getSimpleDateString(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy");
        return format.format(new Date(time));
    }

    public static String getDateTimeString(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy, hh:mm aa");
        return format.format(new Date(time));
    }

    public static String getTimeString(long time) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss aa");
        return format.format(new Date(time));
    }

    public static String getDateTimeString2Line(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy\nhh:mm aa");
        return format.format(new Date(time));
    }

    public static String getDayString(long j) {
        return new SimpleDateFormat("dd").format(new Date(j));
    }

    public static String getMonthString(long j) {
        return new SimpleDateFormat("MMM").format(new Date(j));
    }

    public static String getDurationString(long start, long end) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
        return simpleDateFormat.format(new Date(start)) + " ~ " + simpleDateFormat.format(new Date(end));
    }

    public static void showImageDialog(final Activity activity, final int requestCode) {
        new AlertDialog.Builder(activity)
                .setItems(R.array.pick_image, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();

                        if (which == 0) {
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode);
                        } else {
                            /*intent.setClass(activity, TakePictureActivity.class);
                            activity.startActivityForResult(intent, requestCode);*/
                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                                activity.startActivityForResult(intent, requestCode);
                            }
                        }
                    }
                }).create().show();
    }

    public static void showImageDialog(final Activity activity, final Uri uri, final int requestCode) {
        new AlertDialog.Builder(activity)
                .setItems(R.array.pick_image, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();

                        if (which == 0) {
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode);
                        } else {
                            /*intent.setClass(activity, TakePictureActivity.class);
                            activity.startActivityForResult(intent, requestCode);*/
                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                                activity.startActivityForResult(intent, requestCode);
                            }
                        }
                    }
                }).create().show();
    }

    public static void showVideoDialog(final Activity activity, final Uri uri, final int requestCode) {
        new AlertDialog.Builder(activity)
                .setItems(R.array.pick_video, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();

                        if (which == 0) {
                            intent.setType("video/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode);
                        } else {
                            /*intent.setClass(activity, TakePictureActivity.class);
                            activity.startActivityForResult(intent, requestCode);*/
                            intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                                activity.startActivityForResult(intent, requestCode);
                            }
                        }
                    }
                }).create().show();
    }

    public static void performCrop(Activity context, Uri picUri, int requetCode) {
        // take care of exceptions
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);

            cropIntent.putExtra("return-data", true);

            context.startActivityForResult(cropIntent, requetCode);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(context, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public static Matrix configureTransform(int viewWidth, int viewHeight, Size mPreviewSize, Activity context) {

        int rotation = context.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        return matrix;
    }

    public static Size chooseOptimalSize(Size[] choices, int textureViewWidth,
                                         int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {

        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        // Collect the supported resolutions that are smaller than the preview Surface
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth &&
                        option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new Utils.CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new Utils.CompareSizesByArea());
        } else {
            Log.e("tag", "Couldn't find any suitable preview size");
            return choices[0];
        }
    }

    public static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {

            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }


    public static class ImageSaver implements Runnable {

        private final Image mImage;

        private final File mFile;

        public ImageSaver(Image image, File file) {
            mImage = image;
            mFile = file;
        }

        @Override
        public void run() {
            ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            FileOutputStream output = null;
            try {
                output = new FileOutputStream(mFile);
                output.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mImage.close();
                if (null != output) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /*public static void showFacebookShareDialog(final Activity activity, final Uri videoUri) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ShareDialog.canShow(ShareContent.class)) {
                    ShareVideo video = new ShareVideo.Builder()
                            .setLocalUrl(videoUri)
                            .build();
                    ShareVideoContent content = new ShareVideoContent.Builder()
                            .setVideo(video)
                            .build();
                    ShareDialog.show(activity, content);
                } else {
                    new AlertDialog.Builder(activity)
                            .setMessage(R.string.no_facebook)
                            .setPositiveButton(R.string.ok, null)
                            .create().show();
                }
            }
        });
    }*/

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public static void verifyCameraStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public static long timeDifference = 0;

    public static void checkServerTime() {

        FirebaseDatabase.getInstance().getReference("curTimestamp").setValue(ServerValue.TIMESTAMP).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference("curTimestamp").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            long now = (Long) dataSnapshot.getValue();

                            timeDifference = System.currentTimeMillis() - now;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            timeDifference = 0;
                        }
                    });
                } else {
                    timeDifference = 0;
                }
            }
        });
    }

    public static long getCurTime() {
        if (timeDifference < Utils.TIME_MINUTE_IN_MILLISECONDS * 10) {
            return System.currentTimeMillis();
        } else {
            return System.currentTimeMillis() - timeDifference;
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static String round(double value) {
        int places = 0;
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return String.valueOf(tmp / factor);
    }

    public static int getIntegerFromMap(HashMap<String, Integer> map, String uid) {
        if (map != null && map.containsKey(uid)) {
            return map.get(uid);
        }
        return 0;
    }

    public static double getDoubleFromMap(HashMap<String, Double> map, String uid) {
        if (map != null && map.containsKey(uid)) {
            return map.get(uid);
        }
        return 0;
    }

    public static long getLongFromMap(HashMap<String, Long> map, String uid) {
        if (map != null && map.containsKey(uid)) {
            return map.get(uid);
        }
        return 0;
    }

    public static void setImageUri(Context context, ImageView view, String uri, int resId) {
        if (!TextUtils.isEmpty(uri)) {
            Picasso.with(context).load(uri).resize(TARGET_WIDTH_NORMAL, 0).placeholder(resId).into(view);
        } else {
            view.setImageResource(resId);
        }
    }

    public static void setLargeImageUri(Context context, ImageView view, String uri, int resId) {
        if (!TextUtils.isEmpty(uri)) {
            Picasso.with(context).load(uri).resize(TARGET_WIDTH_LARGE, 0).placeholder(resId).into(view);
        } else {
            view.setImageResource(resId);
        }
    }

    public static void setSmallImageUri(Context context, ImageView view, String uri, int resId) {
        if (!TextUtils.isEmpty(uri)) {
            Picasso.with(context).load(uri).resize(TARGET_WIDTH_SMALL, 0).placeholder(resId).into(view);
        } else {
            view.setImageResource(resId);
        }
    }

    public static void updateTabLayout(TabLayout tabLayout, int index, int count) {
        if (count == 0) {
            tabLayout.getTabAt(index).removeBadge();
        } else {
            tabLayout.getTabAt(index).getOrCreateBadge().setNumber(count);
        }
    }

    public static String getDistanceStr(double distance) {
        if (distance < 1000) {
            return String.format("%.2f", distance) + "m";
        } else {
            return String.format("%.2f", distance / 1000) + "km";
        }
    }

    public static interface DialogListener {
        public void onStart();
        public void onSuccess();
        public void onFailure(String error);
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
