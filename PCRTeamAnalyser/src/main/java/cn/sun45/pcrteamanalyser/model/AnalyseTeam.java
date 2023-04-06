package cn.sun45.pcrteamanalyser.model;

/**
 * Created by Sun45 on 2023年4月1日
 * <p>
 * 数据分析模型组(阵容)
 */
public class AnalyseTeam<M> {
    // 空对象
    public static AnalyseTeam EMPTY_INSTANCE = new AnalyseTeam();

    private AnalyseTeam() {
        idCode = "";
    }

    // 数据分析模型
    // 数据分析唯一Id组
    private int[] ids;
    // 数据分析唯模型组
    private AnalyseModel[] AnalyseModels;

    // 伤害
    private int damage;

    // 固定借人位唯一Id
    private int borrowId;

    // 唯一识别码
    private String idCode;

    // 接入数据模型
    private M m;

    public AnalyseTeam(int[] ids, int damage) {
        this.ids = ids;
        this.damage = damage;
        buildIdCode();
    }

    public AnalyseTeam(int[] ids, int damage, int borrowId) {
        this(ids, damage);
        this.borrowId = borrowId;
    }

    public AnalyseTeam(AnalyseModel[] AnalyseModels, int damage) {
        this.AnalyseModels = AnalyseModels;
        if (AnalyseModels != null) {
            ids = new int[AnalyseModels.length];
            for (int i = 0; i < AnalyseModels.length; i++) {
                ids[i] = AnalyseModels[i].getAnalyseId();
            }
        }
        this.damage = damage;
        buildIdCode();
    }

    public AnalyseTeam(AnalyseModel[] AnalyseModels, int damage, int borrowId) {
        this(AnalyseModels, damage);
        this.borrowId = borrowId;
    }

    public int[] getIds() {
        return ids;
    }

    public AnalyseModel[] getAnalyseModels() {
        return AnalyseModels;
    }

    public int getDamage() {
        return damage;
    }

    public int getBorrowId() {
        return borrowId;
    }

    public String getIdCode() {
        return idCode;
    }

    /**
     * 构建唯一识别码
     */
    private void buildIdCode() {
        StringBuilder sb = new StringBuilder();
        if (ids != null) {
            for (int i = 0; i < ids.length; i++) {
                if (i != 0) {
                    sb.append(",");
                }
                sb.append(ids[i]);
            }
        }
        idCode = sb.toString();
    }

    public M getModel() {
        return m;
    }

    public void setModel(M m) {
        this.m = m;
    }

    /**
     * 是空模型
     *
     * @return 空模型
     */
    public boolean isEmpty() {
        if (this == EMPTY_INSTANCE) {
            return true;
        }
        if (ids == null || ids.length == 0) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("AnalyseTeam [idCode=").append(idCode).append(", borrowId=").append(borrowId)
                .append(", damage=").append(damage).append("]").toString();
    }

}
