/**
 * 
 */
package com.ailink.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;


/**
 * @author staker 这是一个图片点缓存工具，缓存点地址是sd卡里面，非常有用的一个工具
 * 04-06-13
 * 04-08-08 1.修改图片的加载方式，这样更节省内存
 */
public class ImageCacheUtil {

	
	
	public static String directoryName=Environment.getExternalStorageDirectory().getPath()+"//ailink_game";//根目录文件夹的名字，可以修改
	
	public interface OnLoadingListener{
		public void onLoadingStarted();
		public void onLoadingFailed(int resultType);
		public void onLoadingComplete();
	}
	/**
	 * 根据传进来的文件的网络地址，先去本地指定的目录里面查看是否有这个文件，有的话，则从本地获取，不存在的话，则从网络里面获取，并且保存在本地
	 * @param pathUrl  文件的网络地址
	 * @return  Uri  文件的uri地址
	 */
	public static Uri getImageUri(String pathUrl) {
		File file = writeToFile(pathUrl);
		if(file!=null){
			return Uri.fromFile(file);// Uri.fromFile(path)这个方法能得到文件的URI地址
		}
		return null;
	}
	
	
	/**
	 * 根据传进来的文件的网络地址，先去本地指定的目录里面查看是否有这个文件，有的话，则从本地获取，不存在的话，则从网络里面获取，并且保存在本地
	 * @param pathUrl  文件的网络地址
	 * @return  String  文件保存在本地的地址
	 */
	public static String getImagePath(String pathUrl) {
		File file = writeToFile(pathUrl);
		if(file!=null){
			return file.getAbsolutePath();//这个方法能得到文件的绝对路径
		}
		return null;
		
	}
				
	/**
	 * 根据传进来的文件的网络地址，先去本地指定的目录里面查看是否有这个文件，有的话，则从本地获取，不存在的话，则从网络里面获取，并且保存在本地
	 * @param pathUrl  文件的网络地址
	 * @return  Bitmap  根据地址拿到图片
	 */
	public static Bitmap getImageBitmap(String pathUrl) {
		File file = writeToFile(pathUrl);
		if(file!=null){
			return ResourceUtil.readBmpFromPath(file.getAbsolutePath(), 200, 200);
		}
		return null;	
	}
	
	/**
	 * 此方法不会把图片缓存在内存，而是保存在sd卡（缓存在内存并且保存在sd卡的方法请看SingleBitmapCache类）
	 * 一般针对比较大的图片，因为这个方法不会对获取到的图片进行压缩
	 * 
	 * 通过给定的网络地址，给imageview设置资源图片，  此方法会先去本地查询是否含有这张图片，没有的话才会去网络里面获取
	 * 
	 * @param img  imageview控件
	 * @param pathUrl 图片的网络地址
	 * 
	 * 此方法不是很适合在listview里面使用，因为没有设置刚开始进来的默认图片，所以当滑动的速度特别快的时候图片还是会有错乱现象
	 * 原因是：  当滑动到某一行的时候，view的id和之前的一样，但是由于没有设置默认的初始图片，那么它显示的则是上一个和它id相同的view的图片
	 * 就算设置tag也是无效的
	 */
	public static void setImageUrl(final ImageView img,final String pathUrl,final OnLoadingListener listener) {
		if(listener!=null){
			listener.onLoadingStarted();
		}
		new AsynTaskUtil().startTaskOnThread(new AsynTaskUtil.IAsynTask() {
			@Override
			public Object run() {
				File file = writeToFile(pathUrl);
				Logg.e("file="+file);
				if(file==null){
					return null;
				}
				
				return ResourceUtil.readBmpFromPath(file.getAbsolutePath(), 200, 200);
			}		
			public void over(Object object) {
				Bitmap bitmap=(Bitmap) object;
				if(bitmap!=null){
					img.setImageBitmap(bitmap);
					if(listener!=null){
						listener.onLoadingComplete();
					}
				}else{
					if(listener!=null){
						listener.onLoadingFailed(0);
					}
				}						
			}
		});
	}

	 /**
	  * 后台给imageview设置图片
	  * @param img  ImageView控件
	  * @param pathUrl  图片文件的网络地址
	  * @param scaleSize 缩放的倍数，2的话，则高度和宽度都缩放到原来的一半
	  */
	public static void setImageUrl(final ImageView img,final String pathUrl,final int scaleSize) {
		new AsynTaskUtil().startTaskOnThread(new AsynTaskUtil.IAsynTask() {
			public Object run() {
				File file = writeToFile(pathUrl);
				if(file==null){
					return null;
				}
				return ResourceUtil.readBmpFromPath(file.getAbsolutePath(), 200, 200);		
			}		
			public void over(Object object) {
				Bitmap bitmap=(Bitmap) object;
				if(bitmap!=null){
					img.setImageBitmap(bitmap);
					bitmap=null;
				}						
			}
		});
	}
	
	
	
	
	
	
	
	/**
	 * 这个方法是存在弊端的，当sd卡不在，或者没有创建文件夹成功的时候，并没有从网络中读取图片
	 * @param pathUrl
	 * @return
	 */
	private static File writeToFile(String pathUrl){
		String name = null;
		try {
			name=MD5Util.md5String(pathUrl)+ pathUrl.substring(pathUrl.lastIndexOf("."));// 后面点是文件或者图片原来点后缀，即命名的时候保留原始后缀
		} catch (Exception e) {
			return null;
		}
		File fileDirectory=new File(directoryName);
		if(!fileDirectory.exists()){
			if(fileDirectory.mkdir()){
				Logg.e("文件目录创建创建成功="+fileDirectory.getAbsolutePath());
			}else{
				Logg.e("文件目录创建创建shibai="+fileDirectory.getAbsolutePath());
				return null;
			}		
		}	
		
		File file = new File(directoryName, name);// 如果文件存在本地缓存目录，则不去服务器下载	
		if (file.exists()) {
			//Logg.e("文件已经存在本里sd卡里面的");
			return file;//这个方法能得到文件的绝对路径
		}
		InputStream is =null;
		FileOutputStream fos=null;
		try {
			// 从网络上获取文件
			URL url = new URL(pathUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(10000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			if (conn.getResponseCode() == 200) {
				is = conn.getInputStream(); // 得到网络返回的输入流
				fos = new FileOutputStream(file);// 设置文件的保存路径
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}				
				return file;
			}
		} catch (Exception e) {
			Logg.e("下载网络文件出异常，可能是地址不正确",e);//出异常也可能是网络中根本不存在这个文件
		}finally{
			try {
				if(is!=null){
					is.close();
				}
				if(fos!=null){
					fos.flush();
					fos.close();
				}									
			} catch (Exception e2) {
			}
		}
		return null;
	}
	
	/**
	 * 通过网络url地址，从本地sd卡里面获取文件
	 * 如果此文件不存在，则返回null
	 * @param pathUrl  文件的网络地址
	 * @return  File
	 */
	public static File getFileFromSDcard(String pathUrl){
		if(pathUrl==null){
			return null;
		}
		String name = null;
		try {
			name=MD5Util.md5String(pathUrl)+ pathUrl.substring(pathUrl.lastIndexOf("."));// 后面点是文件或者图片原来点后缀，即命名的时候保留原始后缀
		} catch (Exception e) {
			return null;
		}
		File file = new File(directoryName, name);// 如果文件存在本地缓存目录，则不去服务器下载		
		if (file.exists()) {
			return file;//这个方法能得到文件的绝对路径
		}
		return null;
	}
	
	/**
	 * 通过网络url地址，从本地sd卡里面获取文件的保存路径
	 * 如果此文件不存在，则返回null
	 * @param pathUrl  文件的网络地址
	 * @return  File
	 */
	public static String getFilePathFromSDcard(String pathUrl){
		if(pathUrl==null){
			return null;
		}
		String name = null;
		try {
			name=MD5Util.md5String(pathUrl)+ pathUrl.substring(pathUrl.lastIndexOf("."));// 后面点是文件或者图片原来点后缀，即命名的时候保留原始后缀
		} catch (Exception e) {
			return null;
		}
		File file = new File(directoryName, name);// 如果文件存在本地缓存目录，则不去服务器下载		
		if (file.exists()) {
			return file.getAbsolutePath();//这个方法能得到文件的绝对路径
		}
		return null;
	}
}
