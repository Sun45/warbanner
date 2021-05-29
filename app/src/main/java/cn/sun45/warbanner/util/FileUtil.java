package cn.sun45.warbanner.util;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cn.sun45.warbanner.document.StaticHelper;
import cn.sun45.warbanner.framework.MyApplication;

/**
 * Created by Sun45 on 2020/1/10.
 * 文件方法类
 */
public class FileUtil {
    public static String getDbFilePath(String name) {
        return MyApplication.application.getDatabasePath(name).getAbsolutePath();
    }

    public static String getFileFilePath(String fileName) {
        return MyApplication.application.getFilesDir().getAbsolutePath() + "/" + fileName;
    }

    public static String getExternalFilesDir(String fileName) {
        return MyApplication.application.getExternalFilesDir(fileName).getAbsolutePath();
    }

    public static String getExternalCacheDir() {
        return MyApplication.application.getExternalCacheDir().getAbsolutePath();
    }


    /***
     * 单纯的复制文件
     * @param srcPath 源文件路径
     * @param desPath 目标路径
     * @param delete  是否删除源
     */
    public static void copyFile(String fileName, String srcPath, String desPath, boolean delete) {
        String srcFilePath = srcPath + fileName;
        String desFilePath = desPath + fileName;
        File dataBaseDir = new File(desPath);
        //检查数据库文件夹是否存在
        if (!dataBaseDir.exists())
            dataBaseDir.mkdirs();

        File exDB = new File(desFilePath);
        if (exDB.exists())
            exDB.delete();
        try {
            FileInputStream fileInputStream = new FileInputStream(srcFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(exDB);
            byte[] buffer = new byte[1024];
            int count;
            while ((count = fileInputStream.read(buffer)) > 0)
                fileOutputStream.write(buffer, 0, count);
            fileOutputStream.flush();
            fileOutputStream.close();
            fileInputStream.close();
            if (delete) {
                File srcFile = new File(srcFilePath);
                srcFile.delete();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean deleteDirectory(File directoryFile) {
        if (directoryFile.isDirectory()) {
            File[] files = directoryFile.listFiles();
            if (files != null) {
                for (File child : files) {
                    deleteDirectory(child);
                }
            }
        }
        return deleteFile(directoryFile);
    }

    public static boolean deleteFile(String filePath) {
        return deleteFile(new File(filePath));
    }

    public static boolean deleteFile(File file) {
        boolean flag = true;
        try {
            if (!file.delete()) {
                flag = false;
                throw new IOException("Failed to delete file: " + file.getAbsolutePath() + ". Size: " + file.length() / 1024 + "KB.");
            } else {
                Utils.logD("FileDelete", "Delete file " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static boolean checkFile(@NotNull File file) {
        if (!file.exists()) {
            Utils.logD("FileCheck", "FileNotExists: " + file.getAbsolutePath());
            return false;
        }
        return true;
    }

    public static boolean checkFile(String filePath) {
        File file = new File(filePath);
        return checkFile(file);
    }

    public static boolean checkFileAndSize(String filePath, long border) {
        File file = new File(filePath);
        if (!checkFile(file)) {
            return false;
        }
        if (file.length() < border * 1024) {
            Utils.logD("FileCheck", "AbnormalDbFileSize: " + file.length() / 1024 + "KB." + " At: " + file.getAbsolutePath());
            return false;
        }
        Utils.logD("FileCheck", file.getAbsolutePath() + ". Size: " + file.length() / 1024 + "KB.");
        return true;
    }

    public static void checkFileAndDeleteIfExists(File file) {
        if (file.exists()) deleteFile(file);
    }

    /**
     * Return the MD5 of file.
     *
     * @param filePath The path of file.
     * @return the md5 of file
     */
    public static String getFileMD5ToString(final String filePath) {
        File file = new File(filePath);
        return getFileMD5ToString(file);
    }

    /**
     * Return the MD5 of file.
     *
     * @param file The file.
     * @return the md5 of file
     */
    public static String getFileMD5ToString(final File file) {
        return Utils.bytes2HexString(getFileMD5(file), true);
    }

    /**
     * Return the MD5 of file.
     *
     * @param filePath The path of file.
     * @return the md5 of file
     */
    public static byte[] getFileMD5(final String filePath) {
        return getFileMD5(new File(filePath));
    }

    /**
     * Return the MD5 of file.
     *
     * @param file The file.
     * @return the md5 of file
     */
    public static byte[] getFileMD5(final File file) {
        if (file == null) return null;
        DigestInputStream dis = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            dis = new DigestInputStream(fis, md);
            byte[] buffer = new byte[1024 * 256];
            while (true) {
                if (!(dis.read(buffer) > 0)) break;
            }
            md = dis.getMessageDigest();
            return md.digest();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (dis != null) {
                    dis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir 要删除的目录的文件路径
     */
    public static void deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            return;
        }
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                files[i].delete();
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                deleteDirectory(files[i].getAbsolutePath());
            }
        }
        // 删除当前目录
        dirFile.delete();
    }

    /**
     * 读取文件内容为String
     *
     * @param path 文件路径
     * @return string
     */
    public static String ReadFile(String path) {
        File file = new File(path);
        BufferedReader reader = null;
        String laststr = "";
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            while ((tempString = reader.readLine()) != null) {
                laststr = laststr + tempString;
                ++line;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return laststr;
    }

    /**
     * 读取文件内容为String并自动转码
     *
     * @param str_filepath 文件路径
     * @return string
     */
    public static String convertCodeAndGetText(String str_filepath) {// 转码
        File file = new File(str_filepath);
        BufferedReader reader;
        String text = "";
        try {
            // FileReader f_reader = new FileReader(file);
            // BufferedReader reader = new BufferedReader(f_reader);
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream in = new BufferedInputStream(fis);
            in.mark(4);
            byte[] first3bytes = new byte[3];
            in.read(first3bytes);//找到文档的前三个字节并自动判断文档类型。
            in.reset();
            if (first3bytes[0] == (byte) 0xEF && first3bytes[1] == (byte) 0xBB && first3bytes[2] == (byte) 0xBF) {// utf-8
                reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
            } else if (first3bytes[0] == (byte) 0xFF && first3bytes[1] == (byte) 0xFE) {
                reader = new BufferedReader(new InputStreamReader(in, "unicode"));
            } else if (first3bytes[0] == (byte) 0xFE && first3bytes[1] == (byte) 0xFF) {
                reader = new BufferedReader(new InputStreamReader(in, "utf-16be"));
            } else if (first3bytes[0] == (byte) 0xFF && first3bytes[1] == (byte) 0xFF) {
                reader = new BufferedReader(new InputStreamReader(in, "utf-16le"));
            } else {
                reader = new BufferedReader(new InputStreamReader(in, "GBK"));
            }
            String str = reader.readLine();
            while (str != null) {
                text = text + str + "\n";
                str = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    /**
     * 写入文件内容
     *
     * @param path 文件路径
     * @param str  string
     * @throws IOException
     */
    public static void writeFile(String path, String str) {
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(path));
            writer.write(str);
        } catch (Exception e) {
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
