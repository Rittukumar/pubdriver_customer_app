package com.oceanstyxx.pubdriver.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mohsin on 29/10/16.
 */

public class Utility {

    /** Returns a Drawable object containing the image located at 'imageWebAddress' if successful, and null otherwise.
     * (Pre: 'imageWebAddress' is non-null and non-empty;
     * method should not be called from the main/ui thread.)*/
    public static Drawable createDrawableFromUrl(String imageWebAddress)
    {
        Drawable drawable = null;

        try
        {
            InputStream inputStream = new URL(imageWebAddress).openStream();
            drawable = Drawable.createFromStream(inputStream, null);
            inputStream.close();
        }
        catch (MalformedURLException ex) { }
        catch (IOException ex) { }

        return drawable;
    }

    /** Returns a Bitmap object containing the image located at 'imageWebAddress'
     * if successful, and null otherwise.
     * (Pre: 'imageWebAddress' is non-null and non-empty;
     * method should not be called from the main/ui thread.)*/
    public static Bitmap createBitmapFromUrl(String imageWebAddress)
    {
        Bitmap bitmap = null;

        try
        {
            InputStream inputStream = new URL(imageWebAddress).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        }
        catch (MalformedURLException ex) { }
        catch (IOException ex) { }

        return bitmap;
    }

}
