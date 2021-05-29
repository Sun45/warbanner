package cn.sun45.warbanner.framework.file;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.SpeedCalculator;
import com.liulishuo.okdownload.core.breakpoint.BlockInfo;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.listener.DownloadListener4WithSpeed;
import com.liulishuo.okdownload.core.listener.assist.Listener4SpeedAssistExtend;

import java.util.List;
import java.util.Map;

import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2019/10/29.
 * 文件请求类
 */
public class FileRequester {
    private static final String TAG = "FileRequester";

    /**
     * 创建下载任务
     *
     * @param url              url
     * @param path             保存路径
     * @param name             文件名
     * @param downloadevenexit 文件始终下载就算已下载过
     * @return 下载任务
     */
    public static DownloadTask buildTask(String url, String path, String name, boolean downloadevenexit) {
        DownloadTask task = new DownloadTask.Builder(url, path, name).setPassIfAlreadyCompleted(!downloadevenexit).build();
        return task;
    }

    /**
     * 文件下载
     *
     * @param url              url
     * @param path             保存路径
     * @param name             文件名
     * @param downloadevenexit 文件始终下载就算已下载过
     * @return 下载任务
     */
    public static DownloadTask request(String url, String path, String name, boolean downloadevenexit, final FileRequestListener listener) {
        Utils.logD(TAG, "request " + url);
        DownloadTask task = buildTask(url, path, name, downloadevenexit);
        task.enqueue(new DownloadListener4WithSpeed() {
            long totalLength;

            @Override
            public void taskStart(@NonNull DownloadTask task) {

            }

            @Override
            public void connectStart(@NonNull DownloadTask task, int blockIndex, @NonNull Map<String, List<String>> requestHeaderFields) {
            }

            @Override
            public void connectEnd(@NonNull DownloadTask task, int blockIndex, int responseCode, @NonNull Map<String, List<String>> responseHeaderFields) {
            }

            @Override
            public void infoReady(@NonNull DownloadTask task, @NonNull BreakpointInfo info, boolean fromBreakpoint, @NonNull Listener4SpeedAssistExtend.Listener4SpeedModel model) {
                totalLength = info.getTotalLength();
                Utils.logD(TAG, "infoReady totalLength :" + totalLength);
            }

            @Override
            public void progressBlock(@NonNull DownloadTask task, int blockIndex, long currentBlockOffset, @NonNull SpeedCalculator blockSpeed) {

            }

            @Override
            public void progress(@NonNull DownloadTask task, long currentOffset, @NonNull SpeedCalculator taskSpeed) {
                Utils.logD(TAG, "progress currentOffset :" + currentOffset);
                if (listener instanceof FileRequestListenerWithProgress) {
                    float percent = (float) currentOffset / (float) totalLength;
                    int progress = Math.round(percent * 100);
                    ((FileRequestListenerWithProgress) listener).progress(currentOffset, totalLength, percent, progress);
                }
                if (currentOffset == totalLength) {
                    taskEnd(task,EndCause.COMPLETED,null, (SpeedCalculator) null);
                }
            }

            @Override
            public void blockEnd(@NonNull DownloadTask task, int blockIndex, BlockInfo info, @NonNull SpeedCalculator blockSpeed) {
            }

            @Override
            public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause, @NonNull SpeedCalculator taskSpeed) {
                Utils.logD(TAG, "taskEnd cause:" + cause + " realCause:" + realCause);
                switch (cause) {
                    case COMPLETED:
                        listener.complete(task.getUrl(), task.getParentFile().getAbsolutePath(), task.getFilename());
                        break;
                    case ERROR:
                    case CANCELED:
                    case FILE_BUSY:
                    case PRE_ALLOCATE_FAILED:
                        String message = "";
                        if (realCause != null) {
                            message = realCause.getMessage();
                        }
                        listener.error(task.getUrl(), task.getParentFile().getAbsolutePath(), task.getFilename(), message);
                        break;
                    case SAME_TASK_BUSY:
                        break;
                    default:
                        break;
                }
                listener.end(task);
            }
        });
        return task;
    }

    /**
     * 批量取消下载任务
     *
     * @param tasks
     */
    public static void cancel(DownloadTask[] tasks) {
        DownloadTask.cancel(tasks);
    }

    /**
     * 批量取消下载任务
     *
     * @param downloadTaskList
     */
    public static void cancel(List<DownloadTask> downloadTaskList) {
        DownloadTask[] tasks = new DownloadTask[downloadTaskList.size()];
        for (int i = 0; i < downloadTaskList.size(); i++) {
            tasks[i] = downloadTaskList.get(i);
        }
        cancel(tasks);
    }
}
