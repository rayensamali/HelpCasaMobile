package com.example.helpcasamobile;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
public class ImageUtil {
    private static final String PREFS_NAME = "image_prefs";
    private static final String IMAGE_KEY = "image_data";

    public static void saveImageToPreferences(Context context, Bitmap bitmap) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convert Bitmap to Base64 String
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        // Save encoded string to SharedPreferences
        editor.putString(IMAGE_KEY, encodedImage);
        editor.apply();
    }

    public static Bitmap getImageFromPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String encodedImage = sharedPreferences.getString(IMAGE_KEY, null);

        if (encodedImage != null) {
            byte[] byteArray = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        }

        return null;
    }
}
