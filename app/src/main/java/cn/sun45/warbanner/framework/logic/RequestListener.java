package cn.sun45.warbanner.framework.logic;

/**
 * Created by Sun45 on 2019/10/28.
 * 网络请求监听器
 */
public interface RequestListener<T> {
    void onSuccess(T result);

    void onFailed(String message);
}
