package cn.sun45.warbanner.framework.record;

import cn.sun45.warbanner.util.FileUtil;

/**
 * Created by Sun45 on 2021/2/5.
 * 错误记录管理
 */
public class ErrorRecordManager extends BaseRecordManager {
    @Override
    public String getDirPath() {
        return FileUtil.getExternalFilesDir("error");
    }

    //单例对象
    private static ErrorRecordManager instance;

    public static ErrorRecordManager getInstance() {
        if (instance == null) {
            synchronized (ErrorRecordManager.class) {
                if (instance == null) {
                    instance = new ErrorRecordManager();
                }
            }
        }
        return instance;
    }

    public void save(String content) {
        append(content);
    }
}
