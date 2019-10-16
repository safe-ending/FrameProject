package common.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

/**
 * 文件工具类
 */
public class FileUtils {

    public final static String ROOT;

    static {
        File rootFile = Environment.getExternalStorageDirectory();
        if (rootFile != null) {
            ROOT = rootFile.getPath();
        } else {
            ROOT = "";
        }
    }


    //得到当前外部存储设备的目录 File.separator为文件分隔符”/“,方便之后在目录下创建文件
    public static       String SDCardRoot = Environment.getExternalStorageDirectory() + File.separator;
    //应用名字  创建父文件夹
    public static final String Assistant  = "Ecology";

    //文件夹名字  app的文件夹
    public static final String Apk        = "Apk";

    //文件夹名字  app的文件夹
    public static final String ApkName = "bjb.apk";

    //文件夹名字  app的文件夹
    public static final String NamePath = "ecology.apk";

    /**
     * 本地缓存根目录
     */
    public final static String PATH_ROOT = ROOT + "/" + Assistant + "/";
    /**
     * log
     */
    public final static String PATH_LOG = PATH_ROOT + "log/";


    //当前应用的文件存储目录
    public static final String AccessoryStorage = FileUtils.SDCardRoot + FileUtils.Assistant + File.separator;


    //在SD卡上创建文件  
    public static File createFileInSDCard(String fileName, String dir)
            throws IOException
    {
        File file = new File(SDCardRoot + dir + File.separator + fileName);
        file.createNewFile();
        return file;
    }

    //在SD卡上创建目录  
    public static File createSDDir(String dir)
            throws IOException
    {
        File dirFile = new File(SDCardRoot + dir);
        dirFile.mkdir();//mkdir()只能创建一层文件目录，mkdirs()可以创建多层文件目录  
        return dirFile;
    }

    //判断文件是否存在  
    public static boolean isFileExist(String fileName, String dir) {
        File file = new File(dir + File.separator + fileName);
        return file.exists();
    }

    //判断文件夹是否存在  不存在就创建
    public static void isFileDirExist(String dir) {
        File file = new File(dir);
        //如果文件夹不存在则创建
        if (!file.exists() && !file.isDirectory()) {
            file.mkdir();
        }
    }

    //将一个InoutStream里面的数据写入到SD卡中  
    public static File write2SDFromInput(String fileName, String dir, InputStream input) {
        File         file   = null;
        OutputStream output = null;
        try {
            //创建目录  
            createSDDir(dir);
            //创建文件  
            file = createFileInSDCard(fileName, dir);
            //写数据流  
            output = new FileOutputStream(file);
            byte buffer[] = new byte[4 * 1024];//每次存4K
            int  temp;
            //写入数据  
            while ((temp = input.read(buffer)) != -1) {
                output.write(buffer, 0, temp);
            }
            output.flush();
        } catch (Exception e) {
            System.out.println("写数据异常：" + e);
        } finally {
            try {
                output.close();
            } catch (Exception e2) {
                System.out.println(e2);
            }
        }
        return file;
    }

    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) { return null; }
        final String scheme = uri.getScheme();
        String       data   = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(scheme)) {
            Cursor cursor = context.getContentResolver()
                                   .query(uri,
                                          new String[]{MediaStore.Images.ImageColumns.DATA},
                                          null,
                                          null,
                                          null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    //动态压缩到2M
    private static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 2*1024>1024 && options >70) { //循环判断如果压缩后图片是否大于2M,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }



    /**
     * 图片按比例大小压缩方法（根据路径获取图片并压缩）
     *
     * @param srcPath
     * @return
     */
    public static Bitmap getImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是1280*720分辨率，所以高和宽我们设置为
        float hh = 1280f;// 这里设置高度为1280f
        float ww = 720f;// 这里设置宽度为720f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 将压缩的bitmap保存到SDCard卡临时文件夹，用于上传
     *
     * @param filename
     * @param bit
     * @return
     */
    private static String saveMyBitmap(String filename, Bitmap bit) {
        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/temp/";
        String filePath = baseDir + filename;
        File dir = new File(baseDir);
        if (!dir.exists()) {
            dir.mkdir();
        }

        File f = new File(filePath);
        try {
            f.createNewFile();
            FileOutputStream fOut = null;
            fOut = new FileOutputStream(f);
            bit.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return filePath;
    }


    /**
     * 递归删除
     */
    private static void RecursionDeleteFile(File file){
        if(file.isFile()){
            file.delete();
            return;
        }
        if(file.isDirectory()){
            File[] childFile = file.listFiles();
            if(childFile == null || childFile.length == 0){
                file.delete();
                return;
            }
            for(File f : childFile){
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }


    /**
     * 获取本应用内部缓存(/data/data/com.xxx.xxx/cache)
     * @param context
     * @return
     * @throws Exception
     */
    public static String getTotalCacheSize(Context context)
            throws Exception
    {
        //getCacheDir() 获取当前应用的缓存地址
        long cacheSize = getFolderSize(context.getCacheDir());
        //        if (Environment.getExternalStorageState()
        //                       .equals(Environment.MEDIA_MOUNTED))
        //        {
        //            cacheSize += getFolderSize(context.getExternalCacheDir());
        //        }


        return getFormatSize(cacheSize);
    }


    // 获取文件大小
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public static long getFolderSize(File file)
            throws Exception
    {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            //            return size + "Byte";
            return "0 K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                          .toPlainString() + " K";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                          .toPlainString() + " M";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                          .toPlainString() + " GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP)
                      .toPlainString() + " TB";
    }

    /**
     * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache)
     * @param context
     */
    public static void cleanInternalCache(Context context) {
        // getCacheDir()能够得到当前项目的缓存地址
        delete(context.getCacheDir());
    }

    /**
     * 删除文件
     * @param file
     */
    public static void delete(File file) {
        if (file != null && file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) { //some JVMs return null for empty dirs
                for (File f : files) {
                    if (f.isDirectory()) {
                        delete(f);
                        //                    	if ((f + "").endsWith(".zip") || (f + "").endsWith("rar")) {
                        //                    		f.delete();
                        ////                    		continue;
                        //                    	} else {
                        //                    		delete(f);
                        //                    	}
                    } else {
                        f.delete();
                    }
                }
            }
            file.delete();
        } else {
            file.delete();
        }
    }



}