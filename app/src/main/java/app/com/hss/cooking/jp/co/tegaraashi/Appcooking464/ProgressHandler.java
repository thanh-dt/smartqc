package app.com.hss.cooking.jp.co.tegaraashi.Appcooking464;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

public class ProgressHandler extends Handler {
	
	public ProgressDialog    progressDialog;
	public AsyncFileDownload asyncFileDownload;
	
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		
		if (asyncFileDownload.isCancelled()) {
			progressDialog.dismiss();
			
		} else if (asyncFileDownload.getStatus() == AsyncTask.Status.FINISHED) {
			progressDialog.dismiss();
			
		} else {
			progressDialog.setProgress(asyncFileDownload.getLoadedBytePercent());
			this.sendEmptyMessageDelayed(0, 100);
			
		}
	}
}
