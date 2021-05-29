package cn.sun45.warbanner.document.preference;

import cn.sun45.warbanner.framework.document.preference.BasePreference;

/**
 * Created by Sun45 on 2021/5/23
 * 资源信息
 */
public class SourcePreference extends BasePreference {
    private long dbVersion;

    private String dbHash;

    @Override
    public String getName() {
        return "source";
    }

    public long getDbVersion() {
        return load("dbVersion");
    }

    public void setDbVersion(long dbVersion) {
        save("dbVersion", dbVersion);
    }

    public String getDbHash() {
        return load("dbHash");
    }

    public void setDbHash(String dbHash) {
        this.dbHash = dbHash;
        save("dbHash", dbHash);
    }
}
