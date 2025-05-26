package com.creatrix.salessolution.Manager;

import android.graphics.Bitmap;

public interface PhotoManagerListener {
    void onImageViewSet(Bitmap imageBitmap);

    void onImageSet(String image, String imageType, String imagePath);
}
