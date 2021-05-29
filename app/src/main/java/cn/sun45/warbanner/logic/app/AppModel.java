package cn.sun45.warbanner.logic.app;

/**
 * Created by Sun45 on 2021/5/29
 * 安装包信息
 */
public class AppModel {
    private int versionCode;
    private String versionName;
    private boolean fource;
    private String content;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public boolean isFource() {
        return fource;
    }

    public void setFource(boolean fource) {
        this.fource = fource;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "AppModel{" +
                "versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", fource=" + fource +
                ", content='" + content + '\'' +
                '}';
    }
}
