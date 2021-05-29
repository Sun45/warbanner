package cn.sun45.warbanner.framework.file;

import com.liulishuo.okdownload.DownloadTask;

/**
 * Created by Sun45 on 2019/10/29.
 * 文件请求监听
 */
public interface FileRequestListener {
    void start();

    void complete(String url, String path, String name);

    void error(String url, String path, String name, String msg);

    void duplicate(String url, String path, String name);

    void end(DownloadTask task);
}
