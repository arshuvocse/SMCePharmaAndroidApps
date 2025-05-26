package com.creatrix.salessolution.Manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.creatrix.salessolution.BuildConfig;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.DateTimeUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class PhotoManager {


    private static String TAG = PhotoManager.class.getName();

    private Context context;
    private Fragment mFragment;
    private PhotoManagerListener photoManagerListener;

    public static final int REQUEST_CAPTURE_PHOTO = 901;
    public static final int REQUEST_BROWSE_PHOTO = 902;
    public static final int REQUEST_CAPTURE_MULTIPLE_PHOTO = 903;

    private int type;
    private Bitmap imageBitmap;

    public PhotoManager(Context context, PhotoManagerListener photoManagerListener) {
        this.context = context;
        this.photoManagerListener = photoManagerListener;
    }

    public PhotoManager(Context context, Fragment fragment, PhotoManagerListener photoManagerListener) {
        this.context = context;
        this.mFragment = fragment;
        this.photoManagerListener = photoManagerListener;
    }

    public void setPhotoManagerListener(PhotoManagerListener photoManagerListener) {
        this.photoManagerListener = photoManagerListener;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void capturePhoto() {
        dispatchTakePictureIntent();
    }

    public void capturePhoto(PhotoManagerListener photoManagerListener) {
        this.setPhotoManagerListener(photoManagerListener);
        dispatchTakePictureIntent();
    }

    public void browsePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (mFragment != null) {
            mFragment.startActivityForResult(intent, REQUEST_BROWSE_PHOTO);
        } else {
            ((Activity) context).startActivityForResult(intent, REQUEST_BROWSE_PHOTO);
        }
    }

    public void browsePhoto(PhotoManagerListener photoManagerListener) {
        this.setPhotoManagerListener(photoManagerListener);
        browsePhoto();
    }

    public String getImagePath() {
        return ShareprefManager.getImagePath(context);
    }

    public void handleCapturePhotoResult() {
        if (context == null) {
            return;
        }
        String currentImagePath = ShareprefManager.getImagePath(context);
        loadPhotoFromPath(currentImagePath);
    }

    public void loadPhotoFromPath(String imagePath) {
        Log.d(TAG, "ImagePath:  " + imagePath);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inSampleSize = 4;

        imageBitmap = BitmapFactory.decodeFile(imagePath, options);

        if (imageBitmap == null) {
            return;
        }

        int nh = (int) (imageBitmap.getHeight() * (800.0 / imageBitmap.getWidth()));
        imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 800, nh, true);

        try {
            ExifInterface exifInterface = new ExifInterface(imagePath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    imageBitmap = rotateBitmap(imageBitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    imageBitmap = rotateBitmap(imageBitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    imageBitmap = rotateBitmap(imageBitmap, 270);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);

        if (photoManagerListener != null) {
            photoManagerListener.onImageViewSet(imageBitmap);
            photoManagerListener.onImageSet(
                    Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT),
                    Constants.IMAGE_FORMAT_SERVER,
                    imagePath);
        }
    }

    public void handleBrowsePhotoResult(Intent data) {
        try {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            if (selectedImage == null) {
                throw new Exception();
            }

            Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn,
                    null, null, null);
            String imagePath = "";
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imagePath = cursor.getString(columnIndex);
                cursor.close();
            }

            Log.d(TAG, "Image selected: " + imagePath);
            ShareprefManager.setImagePath(context, imagePath);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inSampleSize = 4;

            imageBitmap = BitmapFactory.decodeFile(imagePath, options);

            if (imageBitmap == null) {
                return;
            }

            int nh = (int) (imageBitmap.getHeight() * (800.0 / imageBitmap.getWidth()));
            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 800, nh, true);

            try {
                ExifInterface exifInterface = new ExifInterface(imagePath);
                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);

                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        imageBitmap = rotateBitmap(imageBitmap, 90);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        imageBitmap = rotateBitmap(imageBitmap, 180);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        imageBitmap = rotateBitmap(imageBitmap, 270);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);

            if (photoManagerListener != null) {
                photoManagerListener.onImageViewSet(imageBitmap);
                photoManagerListener.onImageSet(
                        Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT),
                        Constants.IMAGE_FORMAT_SERVER,
                        imagePath);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent capturePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (capturePhotoIntent.resolveActivity(context.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile != null) {
                Uri photoUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    photoUri = FileProvider.getUriForFile(context,
                            BuildConfig.APPLICATION_ID + ".provider", photoFile);
                } else {
                    photoUri = Uri.fromFile(photoFile);
                }
                capturePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                if (mFragment != null) {
                    mFragment.startActivityForResult(capturePhotoIntent, REQUEST_CAPTURE_PHOTO);
                } else {
                    ((Activity) context).startActivityForResult(capturePhotoIntent, REQUEST_CAPTURE_PHOTO);
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        String imageFileName = Constants.IMAGE_FILE_PREFIX + DateTimeUtil.getFormattedDateAsFileName();
        File storageDirectory;
        if (isExternalStorageWritable()) {
            storageDirectory = context.getExternalFilesDir(null);
        } else {
            storageDirectory = context.getFilesDir();
        }
        File image = File.createTempFile(imageFileName, Constants.IMAGE_FORMAT, storageDirectory);
        String currentImagePath = image.getAbsolutePath();
        ShareprefManager.setImagePath(context, currentImagePath);

        return image;
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private Bitmap rotateBitmap(Bitmap bitmap, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap getBitmapFromBase64ImageData(String image) {
        try {
            byte[] encodedByte = image.getBytes();
            byte[] decodeByte = Base64.decode(encodedByte, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodeByte, 0, decodeByte.length);
        } catch (Exception e) {
        }
        return null;
    }
}
