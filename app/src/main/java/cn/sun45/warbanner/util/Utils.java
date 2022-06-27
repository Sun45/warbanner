package cn.sun45.warbanner.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.ArrayRes;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sun45.warbanner.framework.MyApplication;
import dalvik.system.DexClassLoader;

/**
 * Created by Sun45 on 2020/1/10.
 * 方法类
 */
public class Utils {
    //输出

    /**
     * 获取error信息
     *
     * @param throwable
     * @return
     */
    public static String printError(Throwable throwable) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        printWriter.close();
        return writer.toString();
    }

    /**
     * 测试log
     */
    public static void logTest(String msg) {
        logD("test", msg);
    }

    /**
     * log
     */
    public static void logD(String tag, String msg) {
        if (MyApplication.testing) {
            Log.d(tag, msg);
        }
    }

    /**
     * log
     */
    public static void logD(String tag, String msga, String msgb, Object obj) {
        if (MyApplication.testing) {
            Log.d(tag, msga + " " + msgb + ": " + obj);
        }
    }

    /**
     * log
     */
    public static void logW(String tag, String msg) {
        if (MyApplication.testing) {
            Log.w(tag, msg);
        }
    }

    /**
     * log
     */
    public static void logE(String tag, String msg) {
        if (MyApplication.testing) {
            Log.e(tag, msg);
        }
    }

    /**
     * 展示提示信息
     */
    public static void tip(View view, @StringRes int resId) {
        Snackbar.make(view, resId, Snackbar.LENGTH_LONG).show();
    }

    /**
     * 展示提示信息
     */
    public static void tip(View view, @NonNull CharSequence text) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
    }

    //system

    /**
     * 获取当前线程名
     */
    public static String getCurrentProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = activityManager.getRunningAppProcesses();
        if (list != null) {
            for (ActivityManager.RunningAppProcessInfo info : list) {
                if (info.pid == pid) {
                    return info.processName;
                }
            }
        }
        return null;
    }

    /**
     * 判断是否处在后台
     */
    public static boolean isAppIsInBackground() {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) MyApplication.application.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                //前台程序
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(MyApplication.application.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(MyApplication.application.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }

    /**
     * 重开应用
     */
    public static void reOpen() {
        final Intent intent = MyApplication.application.getPackageManager().getLaunchIntentForPackage(MyApplication.application.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        MyApplication.application.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 获取屏幕尺寸
     *
     * @return [屏幕宽(长边)，屏幕高(短边)]
     */
    public static int[] getScreenSize() {
        int[] screenSize = new int[2];
        WindowManager wm = (WindowManager) MyApplication.application.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        Class c;
        try {
            c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            screenSize[0] = Math.max(dm.widthPixels, dm.heightPixels);
            screenSize[1] = Math.min(dm.widthPixels, dm.heightPixels);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenSize;
    }

    /**
     * 展示软键盘
     *
     * @param view
     */
    public static void showSoftInput(View view) {
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftInput(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * 获取当前系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取androidId
     *
     * @return
     */
    public static String getAndroidId() {
        String androidId = Settings.Secure.getString(MyApplication.application.getContentResolver(), Settings.Secure.ANDROID_ID);
//        if (!"9774d56d682e549c".equals(androidId)) {
//        }
        return androidId;
    }

    /**
     * 获取mac地址
     *
     * @return
     */
    public static String getMacAddress() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.application.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {//无线
                WifiManager wifi = (WifiManager) MyApplication.application.getSystemService(Context.WIFI_SERVICE);
                WifiInfo winfo = wifi.getConnectionInfo();
                return winfo.getMacAddress();
            } else {//有线
                String str;
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader("/sys/class/net/eth0/address"), 256);
                    str = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "";
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return str;
            }
        }
        return "";
    }

    /**
     * 获取安卓设备当前的IP地址（有线或无线）
     *
     * @return
     */
    public static String getClientIP() {
        try {
            // 获取本地设备的所有网络接口
            Enumeration<NetworkInterface> enumerationNi = NetworkInterface.getNetworkInterfaces();
            while (enumerationNi.hasMoreElements()) {
                NetworkInterface networkInterface = enumerationNi.nextElement();
                String interfaceName = networkInterface.getDisplayName();
                if (interfaceName.equals("eth0") || interfaceName.equals("wlan0")) {// 有限网卡或无限网卡
                    Enumeration<InetAddress> enumIpAddr = networkInterface.getInetAddresses();
                    while (enumIpAddr.hasMoreElements()) {
                        // 返回枚举集合中的下一个IP地址信息
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        // 不是回环地址，并且是ipv4的地址
                        if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取CPU使用率
     *
     * @return
     */
    public static String getCPURateDesc() {
        String path = "/proc/stat";// 系统CPU信息文件
        long totalJiffies[] = new long[2];
        long totalIdle[] = new long[2];
        int firstCPUNum = 0;//设置这个参数，这要是防止两次读取文件获知的CPU数量不同，导致不能计算。这里统一以第一次的CPU数量为基准
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        Pattern pattern = Pattern.compile(" [0-9]+");
        for (int i = 0; i < 2; i++) {
            totalJiffies[i] = 0;
            totalIdle[i] = 0;
            try {
                fileReader = new FileReader(path);
                bufferedReader = new BufferedReader(fileReader, 8192);
                int currentCPUNum = 0;
                String str;
                while ((str = bufferedReader.readLine()) != null && (i == 0 || currentCPUNum < firstCPUNum)) {
                    if (str.toLowerCase().startsWith("cpu")) {
                        currentCPUNum++;
                        int index = 0;
                        Matcher matcher = pattern.matcher(str);
                        while (matcher.find()) {
                            try {
                                long tempJiffies = Long.parseLong(matcher.group(0).trim());
                                totalJiffies[i] += tempJiffies;
                                if (index == 3) {//空闲时间为该行第4条栏目
                                    totalIdle[i] += tempJiffies;
                                }
                                index++;
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (i == 0) {
                        firstCPUNum = currentCPUNum;
                        try {//暂停50毫秒，等待系统更新信息。
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        double rate = -1;
        if (totalJiffies[0] > 0 && totalJiffies[1] > 0 && totalJiffies[0] != totalJiffies[1]) {
            rate = 1.0 * ((totalJiffies[1] - totalIdle[1]) - (totalJiffies[0] - totalIdle[0])) / (totalJiffies[1] - totalJiffies[0]);
        }
        return String.format("%.2f", rate * 100);
    }

    /**
     * 调动shell命令
     *
     * @param commends
     */
    public static void adbShell(String[] commends) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            PrintStream outputStream = null;
            try {
                outputStream = new PrintStream(new BufferedOutputStream(process.getOutputStream(), 8192));
                for (String commend : commends) {
                    outputStream.println(commend);
                }
                outputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    /**
     * 调动shell命令
     *
     * @param commend
     */
    public static void adbShell(String commend) {
        adbShell(new String[]{commend});
    }

    //app

    /**
     * 获取是Debug版
     *
     * @return
     */
    public static boolean isDebugVersion() {
        try {
            ApplicationInfo info = MyApplication.application.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取PackageName
     *
     * @return
     */
    public static String getPackageName() {
        return MyApplication.application.getPackageName();
    }

    /**
     * 获取机顶盒应用的文件名称
     *
     * @return
     */
    public static String getStbApkName() {
        String packagename = MyApplication.application.getPackageName();
        return packagename.substring(packagename.lastIndexOf(".") + 1) + "_a_stb";
    }

    /**
     * 获取VerisonCode
     *
     * @return
     */
    public static int getVersionCode() {
        int code = 0;
        try {
            PackageManager manager = MyApplication.application.getPackageManager();
            PackageInfo info = manager.getPackageInfo(MyApplication.application.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

    /**
     * 获取VersionName
     *
     * @return
     */
    public static String getVersionName() {
        String name = "1.0.0";
        try {
            PackageManager manager = MyApplication.application.getPackageManager();
            PackageInfo info = manager.getPackageInfo(MyApplication.application.getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 获取控件显示
     *
     * @param view 控件
     * @return
     */
    private static Bitmap getViewDisplay(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    /**
     * shot the current screen ,with the status but the status is trans * * * @param ctx current activity
     */
    public static Bitmap shotActivity(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bp = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * 截屏到文件
     *
     * @param path 文件路径
     */
    public static void screencap(String path) {
        adbShell("screencap -p " + path);
    }
    //view

    /**
     * 获取控件在屏幕中的位置
     *
     * @param v 要获取位置的控件
     * @return int{距离屏幕左侧，距离屏幕顶部}
     */
    public static int[] getLocationInWindow(View v) {
        int[] location = new int[2];
        v.getLocationInWindow(location);
        return location;
    }

    //asset

    /**
     * 得到json文件中的内容
     */
    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        //获得assets资源管理器
        AssetManager assetManager = context.getAssets();
        //使用IO流读取json文件内容
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName), "utf-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    //Pixel

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //resouce

    /**
     * 获取资源文件中的尺寸数值
     *
     * @param res 资源id
     * @return 数值
     */
    public static int getDimension(@DimenRes int res) {
        return (int) (MyApplication.application.getResources().getDimension(res) + 0.5f);
    }

    /**
     * 获取资源文件中的浮点数尺寸数值
     *
     * @param res 资源id
     * @return 数值
     */
    public static float getFloatDimension(@DimenRes int res) {
        return MyApplication.application.getResources().getDimension(res);
    }

    /**
     * 获取资源文件中的颜色数值
     *
     * @param res 资源id
     * @return 数值
     */
    public static int getColor(@ColorRes int res) {
        return MyApplication.application.getResources().getColor(res);
    }

    /**
     * 获取资源文件中的String
     *
     * @param res 资源id
     * @return 数值
     */
    public static String getString(@StringRes int res) {
        return MyApplication.application.getString(res);
    }

    /**
     * 获取资源文件中带占位符的String
     *
     * @param res  资源id
     * @param args 替换内容
     * @return 数值
     */
    public static String getStringWithPlaceHolder(@StringRes int res, Object... args) {
        return MyApplication.application.getString(res, args);
    }

    /**
     * 生成变色文字
     *
     * @param text      文字
     * @param colorText 变色文字
     * @param color     色值
     * @return
     */
    public static SpannableStringBuilder createColorText(String text, String colorText, @ColorInt int color) {
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        int start = text.indexOf(colorText);
        int end = start + colorText.length();
        style.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return style;
    }

    /**
     * 获取资源文件中的StringArray
     *
     * @param res
     * @return
     */
    public static List<String> getStringArray(@ArrayRes int res) {
        String[] array = MyApplication.application.getResources().getStringArray(res);
        List<String> list = new ArrayList<>();
        for (String str : array) {
            list.add(str);
        }
        return list;
    }

    //attr

    /**
     * 获取预定义的颜色数值
     *
     * @param res 资源id
     * @return 数值
     */
    public static int getAttrColor(Context context, @AttrRes int res) {
        int[] attribute = new int[]{res};
        TypedArray array = context.getTheme().obtainStyledAttributes(attribute);
        int color = array.getColor(0, Color.TRANSPARENT);
        array.recycle();
        return color;
    }

    //file

    /**
     * 获得安装包文件的包名
     *
     * @param path
     * @return
     */
    public static String getApkPackageName(String path) {
        PackageManager pm = MyApplication.application.getPackageManager();
        PackageInfo packageInfo = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (packageInfo != null) {
            return packageInfo.packageName;
        } else {
            return "";
        }
    }

    /**
     * 获得安装包文件的版本号
     *
     * @param path
     * @return
     */
    public static int getApkVersionCode(String path) {
        PackageManager pm = MyApplication.application.getPackageManager();
        PackageInfo packageInfo = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (packageInfo != null) {
            return packageInfo.versionCode;
        } else {
            return 0;
        }
    }

    /**
     * 加载jar包中的类对象
     *
     * @param jarFilePath jar包地址
     * @param className   完整类地址类名
     * @param object      构造参数
     * @param <T>         父类类型
     * @return 父类对象
     */
    public static <T> T getClassInstanceFromJar(String jarFilePath, String className, Object object) {
        T t = null;
        File dexOutputDir = MyApplication.application.getDir("dex", 0);
        DexClassLoader classLoader = new DexClassLoader(jarFilePath, dexOutputDir.getAbsolutePath(), null, MyApplication.application.getClassLoader());
        try {
            Class<?> clazz = classLoader.loadClass(className);
            Constructor<?> constructor = clazz.getConstructor(object.getClass());//得到构造器
            t = (T) constructor.newInstance(object);//实例化
        } catch (Exception e) {
            String str = printError(e);
        }
        return t;
    }

    //string

    /**
     * 验证密码是否合法
     *
     * @param password 密码
     * @return
     */
    public static boolean passwordValid(String password) {
        String regEx = "^[a-zA-Z0-9_]*$";//必须为英文大小写、数字、下划线
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    /**
     * 检查ip合法
     *
     * @param value 255.255.255.255
     * @return
     */
    public static boolean isIpAddressValid(String value) {
        int start = 0;
        int end = value.indexOf('.');
        int numBlocks = 0;
        while (start < value.length()) {
            if (-1 == end) {
                end = value.length();
            }
            try {
                int block = Integer.parseInt(value.substring(start, end));
                if ((block > 255) || (block < 0)) {
//                    Log.w(TAG, "isValidIpAddress() : invalid 'block', block = " + block);
                    return false;
                }
            } catch (NumberFormatException e) {
//                Log.w(TAG, "isValidIpAddress() : e = " + e);
                return false;
            }
            numBlocks++;
            start = end + 1;
            end = value.indexOf('.', start);
        }
        return numBlocks == 4;
    }

    /**
     * 去除制表符回车空格
     *
     * @param src
     * @return
     */
    public static String replaceBlank(String src) {
        String dest = "";
        if (src != null) {
            Pattern pattern = Pattern.compile("\t|\r|\n|\\s*");
            Matcher matcher = pattern.matcher(src);
            dest = matcher.replaceAll("");
        }
        return dest;
    }

    /**
     * 检查url地址合法
     *
     * @param url
     * @return
     */
    public static boolean isHttpUrlValid(String url) {
        String regex = "^(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-Z0-9\\.&amp;%\\$\\-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,4})(\\:[0-9]+)?(/[^/][a-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&amp;%\\$#\\=~_\\-@]*)*$";
        //设置正则表达式
        Pattern pat = Pattern.compile(regex.trim());
        //比对
        Matcher mat = pat.matcher(url.trim());
        return mat.matches();
    }

    /**
     * 删除Html标签
     *
     * @param inputString
     * @return
     */
    public static String removeHtmlTag(String inputString) {
        if (inputString == null)
            return null;
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        Pattern p_script;
        Matcher m_script;
        Pattern p_style;
        Matcher m_style;
        Pattern p_html;
        Matcher m_html;
        Pattern p_special;
        Matcher m_special;
        try {
            //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
            //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
            // 定义HTML标签的正则表达式
            String regEx_html = "<[^>]+>";
            // 定义一些特殊字符的正则表达式 如：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            String regEx_special = "\\&[a-zA-Z]{1,10};";

            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签
            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签
            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签
            p_special = Pattern.compile(regEx_special, Pattern.CASE_INSENSITIVE);
            m_special = p_special.matcher(htmlStr);
            htmlStr = m_special.replaceAll(""); // 过滤特殊标签
            textStr = htmlStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return textStr;// 返回文本字符串
    }

    /**
     * Unicode码加非数字 转中文
     *
     * @param str
     * @return
     */
    public static String decodeUnicode(final String str) {
        char[] array = str.toCharArray();
        final StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            char a = array[i];
            if (a == '\\' && i < array.length - 1 && array[i + 1] == 'u') {
                i++;
                int j = i;
                String charStr = "";
                while (++j < array.length && Character.isDigit(array[j])) {
                    charStr += array[j];
                    i++;
                }
                char letter = (char) Integer.parseInt(charStr, 16);
                buffer.append(new Character(letter).toString());
            } else {
                buffer.append(a);
            }
        }
        return buffer.toString();
    }

    /**
     * 创建去除"-"的uuid
     *
     * @return
     */
    public static String makeUuid() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 创建uuid
     *
     * @return
     */
    public static String makeLongUuid() {
        return java.util.UUID.randomUUID().toString();
    }

    /**
     * MD5加密32位
     *
     * @param val
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String getMD5(String val) {
        StringBuffer buf = new StringBuffer();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(val.getBytes());
            byte[] b = md5.digest();//加密
            for (int i = 0; i < b.length; i++) {
                int a = b[i];
                if (a < 0)
                    a += 256;
                if (a < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(a));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return buf.toString();  //32位
//        return buf.toString().substring(8, 24);
    }

    public static String shaEncrypt(String strSrc) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-256");// 将此换成SHA-1、SHA-512、SHA-384等参数
            md.update(bt);
            strDes = bytes2HexString(md.digest(), false); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    /**
     * Bytes to hex string.
     * <p>e.g. bytes2HexString(new byte[] { 0, (byte) 0xa8 }, true) returns "00A8"</p>
     *
     * @param bytes       The bytes.
     * @param isUpperCase True to use upper case, false otherwise.
     * @return hex string
     */
    public static String bytes2HexString(final byte[] bytes, boolean isUpperCase) {
        if (bytes == null) return "";
        char[] HEX_DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] HEX_DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] hexDigits = isUpperCase ? HEX_DIGITS_UPPER : HEX_DIGITS_LOWER;
        int len = bytes.length;
        if (len <= 0) return "";
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = hexDigits[bytes[i] >> 4 & 0x0f];
            ret[j++] = hexDigits[bytes[i] & 0x0f];
        }
        return new String(ret);
    }
}
