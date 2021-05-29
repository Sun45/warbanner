package cn.sun45.warbanner.framework.record;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.sun45.warbanner.framework.MyApplication;


/**
 * Created by Sun45 on 2021/2/5.
 * 记录管理
 */
public abstract class BaseRecordManager {
    protected abstract String getDirPath();

    /**
     * 保存记录内容
     *
     * @param content
     */
    protected void append(String content) {
        String file = getDirPath();
        new File(file).mkdirs();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(MyApplication.getTimecurrent()));
        String date = time.substring(0, 10);
        time = time.substring(11);
        file += File.separator + date;
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            out.write(time + content + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
