package cn.sun45.warbanner.framework.record;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.sun45.warbanner.framework.MyApplication;
import cn.sun45.warbanner.util.FileUtil;


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
        FileUtil.writeFile(file, time + "\n" + content + "\n", true);
    }
}
