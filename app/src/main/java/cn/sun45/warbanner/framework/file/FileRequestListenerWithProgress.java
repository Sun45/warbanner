package cn.sun45.warbanner.framework.file;

/**
 * Created by Sun45 on 2019/10/29.
 * 带进度文件请求监听
 */
public interface FileRequestListenerWithProgress extends FileRequestListener {
    void progress(long currentOffset, long totalLength, float percent, int progress);
}
