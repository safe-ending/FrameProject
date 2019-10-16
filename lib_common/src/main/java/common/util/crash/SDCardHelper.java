package common.util.crash;

import android.os.Environment;
import android.os.StatFs;

/**
 * SD 卡相关操作类
 * 
 * @author Yek Mobile
 * 
 */
public class SDCardHelper {

	private static SDCardHelper mSDCardHelper = new SDCardHelper();

	private SDCardHelper() {

	}

	public static SDCardHelper getInstance() {
		return mSDCardHelper;
	}

	/**
	 * 获得sd卡的可用空间
	 * 
	 * @return 可用字节数
	 */
	public long getAvailableSdCardSize() {
		String path = Environment.getExternalStorageDirectory().getPath();
		StatFs statFs = new StatFs(path);
		long blockSize = statFs.getBlockSizeLong();
		long available = statFs.getAvailableBlocksLong();
		return available * blockSize;
	}

	/**
	 * 检查是否安装了sd卡
	 * 
	 * @return false 未安装
	 */
	public boolean sdCardMounted() {
		final String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED) && !state.equals(Environment.MEDIA_MOUNTED_READ_ONLY);
    }

}
