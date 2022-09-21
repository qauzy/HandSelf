package jt.kundream.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;

/**
 * @author YangTianKun
 */

public class AboutBitmap {

    /**
     * 图片压缩  三种方法
     */
    /**
     * 方法1  质量压缩
     *
     * @param beforBitmap
     * @return
     */
    public static Bitmap compressImage(Bitmap beforBitmap) {

        // 可以捕获内存缓冲区的数据，转换成字节数组。
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if (beforBitmap != null) {
            // 第一个参数：图片压缩的格式；第二个参数：压缩的比率；第三个参数：压缩的数据存放到bos中
            beforBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            int options = 100;
            // 循环判断压缩后的图片是否是大于100kb,如果大于，就继续压缩，否则就不压缩
            while (bos.toByteArray().length / 1024 > 100) {
                bos.reset();// 置为空
                // 压缩options%
                beforBitmap.compress(Bitmap.CompressFormat.JPEG, options, bos);
                // 每次都减少10
                options -= 10;

            }
            // 从bos中将数据读出来 存放到ByteArrayInputStream中
            ByteArrayInputStream bis = new ByteArrayInputStream(
                    bos.toByteArray());
            // 将数据转换成图片
            Bitmap afterBitmap = BitmapFactory.decodeStream(bis);
            return afterBitmap;
        }
        return null;
    }

//    /*
//     * 图片压缩方法02：获得缩略图
//     */
//    public Bitmap getThumbnail(int id) {
//        // 获得原图
//        Bitmap beforeBitmap = BitmapFactory.decodeResource(
//                mContext.getResources(), id);
//        // 宽
//        int w = mContext.getResources()
//                .getDimensionPixelOffset(R.dimen.image_w);
//        // 高
//        int h = mContext.getResources().getDimensionPixelSize(R.dimen.image_h);
//
//        // 获得缩略图
//        Bitmap afterBitmap = ThumbnailUtils
//                .extractThumbnail(beforeBitmap, w, h);
//        return afterBitmap;
//
//    }

    /**
     * 图片压缩03
     *
     * @param filePath  要操作的图片的大小
     * @param newWidth  图片指定的宽度
     * @param newHeight 图片指定的高度
     * @return
     */
    public static Bitmap compressBitmap(String filePath, double newWidth, double newHeight) {

        //根据路径 获得原图
        Bitmap beforeBitmap = BitmapFactory.decodeFile(filePath);

        // 图片原有的宽度和高度
        float beforeWidth = beforeBitmap.getWidth();
        float beforeHeight = beforeBitmap.getHeight();

        // 计算宽高缩放率
        float scaleWidth = 0;
        float scaleHeight = 0;
        if (beforeWidth > beforeHeight) {
            scaleWidth = ((float) newWidth) / beforeWidth;
            scaleHeight = ((float) newHeight) / beforeHeight;
        } else {
            scaleWidth = ((float) newWidth) / beforeHeight;
            scaleHeight = ((float) newHeight) / beforeWidth;
        }

        // 矩阵对象
        Matrix matrix = new Matrix();
        // 缩放图片动作 缩放比例
        matrix.postScale(scaleWidth, scaleHeight);

        // 创建一个新的Bitmap 从原始图像剪切图像
        Bitmap afterBitmap = Bitmap.createBitmap(beforeBitmap, 0, 0,
                (int) beforeWidth, (int) beforeHeight, matrix, true);

        return afterBitmap;
    }

    ///////////////////////////////=========将图片处理为base64 并上传============/////////////////////////////////////////////////

    /**
     * 通过base32  将图片处理为base64字符串
     *
     * @param bit
     * @return
     */
    public static String Bitmap2StrByBase64(Bitmap bit) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.PNG, 40, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }


    /**
     * 获取指定文件大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return FormetFileSize(blockSize, sizeType);
    }

    /**
     * 获取指定文件大小
     *
     * @param file 文件
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 获取指定文件夹大小
     *
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */

    public static final int SIZETYPE_B = 1;//获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;//获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;//获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;//获取文件大小单位为GB的double值

    private static double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

    /**
     * 打开相册
     *
     * @param ctx
     * @param requestCode
     */
    public static void openPic(Activity ctx, int requestCode) {
        Intent picIntent = new Intent(Intent.ACTION_PICK, null);
        picIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        ctx.startActivityForResult(picIntent, requestCode);
    }

    /**
     * 调用相机     相机拍照之后 一般都是一个  时间.jpg 文件  T恤
     */
    public static File openCamera(Activity ctx, int requestCode) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!file.exists()) {
                file.mkdirs();
            }
            File mFile = new File(file, System.currentTimeMillis() + ".jpg");
            Uri uri = getCameraFileUri(ctx, mFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            ctx.startActivityForResult(intent, requestCode);
            return mFile;
        } else {
            Toast.makeText(ctx, "请确认已经插入SD卡", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    /**
     * 针对7.0报的 FileUri
     *
     * @param ctx
     * @param mFile
     * @return
     */
    public static Uri getCameraFileUri(Activity ctx, File mFile) {
        Uri uri;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            uri = Uri.fromFile(mFile);
        } else {
            /**
             * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
             * 并且这样可以解决MIUI系统上拍照返回size为0的情况
             */
//            uri = FileProvider.getUriForFile(ctx, "net.iyunbei.ayunbeishop.fileprovider", mFile);
            uri = FileProvider.getUriForFile(ctx.getApplicationContext(), ctx.getApplicationInfo().packageName + ".fileprovider", mFile);
        }
        return uri;
    }


    /**
     * 打开系统图片裁剪功能
     *
     * @param uri
     */
    public static Uri startPhotoZoom(Uri uri, int requestCode, Activity ctx) {

        //保证输出的图片文件是一个唯一的空的图片文件。
//        File outputImage = new File(Environment.getExternalStorageDirectory(), "crop.jpg");
//        try {
//            if (outputImage.exists()) {
//                outputImage.delete();
//            }
//            outputImage.createNewFile();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
////        Uri userImageUri = Uri.fromFile(outputImage);
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
////            //添加这一句表示对目标应用临时授权该Uri所代表的文件
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        }
//        intent.setType("image");
//        intent.setDataAndType(uri, "image/*");
//        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
//        intent.putExtra("crop", true);
//        intent.putExtra("scale", true);
//        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);//输出是X方向的比例
//        intent.putExtra("aspectY", 1);
//        // outputX outputY 是裁剪图片宽高，切忌不要再改动下列数字，会卡死
//        intent.putExtra("outputX", 500);//输出X方向的像素
//        intent.putExtra("outputY", 500);
//        intent.putExtra("noFaceDetection", true);
////        intent.putExtra("return-data", false);//设置为不返回数据
//        /**
//         * 此方法返回的图片只能是小图片（测试为高宽160px的图片）
//         * 故将图片保存在Uri中，调用时将Uri转换为Bitmap，此方法还可解决miui系统不能return data的问题
//         */
//        intent.putExtra("return-data", true);
//        intent.putExtra("output", Uri.fromFile(new File("/mnt/sdcard/temp")));//保存路径
//        Uri userImageUri = Uri.parse("file:///"+ Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, userImageUri);
////        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//        Log.d("裁剪图片", "reSizeImage() called with: " + "uri = [" + userImageUri + "]");
//        ctx.startActivityForResult(intent, requestCode);

        // TODO: 以下为原来修改MIUI相机之前的
//        System.gc();
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        if (android.os.Build.MANUFACTURER.contains("HUAWEI")) {// 华为特殊处理 不然会显示圆
            intent.putExtra("aspectX", 9998);
            intent.putExtra("aspectY", 9999);
        } else {
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        }

        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);

        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!file.exists()) {
            file.mkdirs();
        }
        File cropFile = new File(file, System.currentTimeMillis() + "_crop.jpg");
        Uri outUri = Uri.fromFile(cropFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        intent.putExtra("scale", true); //黑边
        intent.putExtra("scaleUpIfNeeded", true); //黑边
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//
        //todo  此处改为不使用系统获取的uri，改为自己这个方法的返回值
//        intent.putExtra("return-data", true);
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", true);
        ctx.startActivityForResult(intent, requestCode);
        return outUri;
    }

    public static Bitmap getBitmapFromUri(Uri uri, Context context, ImageView iv) {
//        try {
//            // 读取uri所在的图片
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
//            return bitmap;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
        try {
            Bitmap mBitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
            iv.setImageBitmap(mBitmap);
            return mBitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void startPhotoZoomForSFZ(Uri uri, int requestCode, Activity ctx) {


        //图片上传成功之前都是false，用于处理用户点击保存时候的图片保存不成功的情况


        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);

        if (android.os.Build.MANUFACTURER.contains("HUAWEI")) {// 华为特殊处理 不然会显示圆
            intent.putExtra("aspectX", 9998);
            intent.putExtra("aspectY", 9999);
        } else {
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        }
//        intent.putExtra("aspectX", 1);
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 350);
        intent.putExtra("scale", true); //黑边
        intent.putExtra("scaleUpIfNeeded", true); //黑边
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        ctx.startActivityForResult(intent, requestCode);
    }

    /**
     * 设置图片到某一个ImageView
     *
     * @param data
     * @return
     */
    public static Bitmap setPic2View(Intent data, ImageView iv) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            //这里也可以做文件上传
            Bitmap mBitmap = bundle.getParcelable("data");
            iv.setImageBitmap(mBitmap);
            return mBitmap;
        }
        return null;
    }

}
