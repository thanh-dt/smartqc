package app.com.hss.cooking.jp.co.tegaraashi.Appcooking464;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * http://www.ra13.org/java/ZipDecompresser.html
 * @author yoshitaka
 *
 */
public class ZipDecompresser {
	
	final private String TAG = "ZipDecompresser";
	
	private File file;
	
	public ZipDecompresser(File file)
	{
		this.file = file;
	}
	
	public void unzip()
	{
		try {
			File baseDir = new File(file.getParent());
			Log.d(this.TAG, "Parent Dir: " + baseDir);
			
			// ZIPファイルから ZipEntry を一つずつ取り出して、ファイルに保存する
			ZipFile zipFile = new ZipFile(file);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			
			while (entries.hasMoreElements()) {
				ZipEntry zipEntry = entries.nextElement();
				
				// 出力先ファイル
				File outFile = new File(baseDir, zipEntry.getName());
				
				// ディレクトリだった場合は、ディレクトリの作成
				if (zipEntry.isDirectory()) {
					outFile.mkdirs();
					
				} else {
					BufferedInputStream bufferedInputStream   = null;
					BufferedOutputStream bufferedOutputStream = null;
					
					try {
						InputStream inputStream = zipFile.getInputStream(zipEntry);
						bufferedInputStream = new BufferedInputStream(inputStream);
						
						// 出力先ファイルの保存先ディレクトリが存在しない場合は、
						// ディレクトリアを咲く区政しておく。
						if ( ! outFile.getParentFile().exists()) {
							outFile.getParentFile().mkdirs();
						}
						
						bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(outFile));
						
						int ava;
						while ((ava = bufferedInputStream.available()) > 0) {
							byte[] buffer = new byte[ava];
							bufferedInputStream.read(buffer);
							bufferedOutputStream.write(buffer);
						}
						
					} catch (FileNotFoundException e) {
						throw e;
						
					} catch (IOException e) {
						throw e;
						
					} finally {
						try {
							if (bufferedInputStream != null) {
								bufferedInputStream.close();
							}
							
						} catch (IOException e) {
						}
						
						try {
							if (bufferedOutputStream != null) {
								bufferedOutputStream.close();
							}
							
						} catch (IOException e) {
						}
						
					}
				}
				
			}
			
		} catch (Exception e) {
			Log.d(this.TAG, e.toString());
			
		}
	}
}
