package app.com.hss.cooking.jp.co.tegaraashi.Appcooking464;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


public class AsyncFileDownload extends AsyncTask<String, Void, Boolean> {

    final private String TAG = "AsyncFileDownload";

    final private int TIMEOUT_READ = 5000;
    final private int TIMEOUT_CONNECT = 30000;
    private final int BUFFER_SIZE = 1024 * 1024;

    public Activity owner;

    private String urlString;
    private File outputFile;
    private FileOutputStream fileOutputStream;
    private InputStream inputStream;
    private BufferedInputStream bufferdInputStream;

    private int totalByte = 0;
    private int currentByte = 0;

    private byte[] buffer = new byte[this.BUFFER_SIZE];

    private URL url;
    private URLConnection urlConnection;

    /**
     * コンストラクタ
     *
     * @param activity
     * @param url
     * @param oFile
     */
    public AsyncFileDownload(Activity activity, String url, File oFile) {
        this.owner = activity;
        this.urlString = url;
        this.outputFile = oFile;
    }

    /**
     *
     */
    @Override
    protected Boolean doInBackground(String... params) {
        try {
            this.connect();

        } catch (IOException e) {
            Log.d(this.TAG, "Connect Error: " + e.toString());
            cancel(true);

        }

        if (isCancelled()) {
            return false;
        }

        if (this.bufferdInputStream != null) {
            try {
                int len;

                while ((len = this.bufferdInputStream.read(this.buffer)) != -1) {
                    this.fileOutputStream.write(this.buffer, 0, len);
                    this.currentByte += len;

                    if (isCancelled()) {
                        break;
                    }
                }

            } catch (IOException e) {
                Log.d(this.TAG, e.toString());
                return false;

            }

        } else {
            Log.d(this.TAG, "bufferedInputStream == null");

        }

        try {
            close();

        } catch (IOException e) {
            Log.d(this.TAG, "Close Error: " + e.toString());

        }

        return true;
    }

    /**
     *
     */
    @Override
    protected void onPreExecute() {
    }

    /**
     *
     */
    @Override
    protected void onPostExecute(Boolean result) {
    }

    /**
     *
     */
    @Override
    protected void onProgressUpdate(Void... progress) {
    }

    /**
     * @throws IOException
     */
    private void connect() throws IOException {
        this.url = new URL(this.urlString);
        this.urlConnection = this.url.openConnection();
        this.urlConnection.setReadTimeout(this.TIMEOUT_READ);
        this.urlConnection.setConnectTimeout(this.TIMEOUT_CONNECT);

        this.inputStream = this.urlConnection.getInputStream();

        this.bufferdInputStream = new BufferedInputStream(this.inputStream, this.BUFFER_SIZE);
        this.fileOutputStream = new FileOutputStream(this.outputFile);

        this.totalByte = this.urlConnection.getContentLength();
        this.currentByte = 0;
    }

    /**
     * @throws IOException
     */
    private void close() throws IOException {
        this.fileOutputStream.flush();
        this.fileOutputStream.close();
        this.bufferdInputStream.close();
    }

    /**
     * @return int
     */
    public int getLoadedBytePercent() {
        if (this.totalByte <= 0) {
            return 0;
        }
        return (int) Math.floor(100 * this.currentByte / this.totalByte);
    }

}
