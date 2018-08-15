package ca.aequilibrium.weather.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.InputStream;

public class ImageLoader {

    private static ImageLoader INSTANCE;

    private LruCache<String, Bitmap> mMemoryCache;

    private ImageLoader() {
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public static ImageLoader getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ImageLoader();
        }

        return INSTANCE;
    }

    public void loadImageFromUrl(ImageView imageView, String url) {
        Bitmap bitmap = getBitmapFromMemCache(url);
        if (bitmap == null) {
            new DownloadImageTask(this, imageView).execute(url);
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageLoader imageLoader;
        ImageView imageView;
        public DownloadImageTask(ImageLoader imageLoaderIn, ImageView imageViewIn) {
            imageLoader = imageLoaderIn;
            imageView = imageViewIn;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            imageLoader.addBitmapToMemoryCache(url, bmp);
            return bmp;
        }
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
