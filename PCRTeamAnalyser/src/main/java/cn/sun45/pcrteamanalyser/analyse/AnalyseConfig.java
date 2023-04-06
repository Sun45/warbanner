package cn.sun45.pcrteamanalyser.analyse;

import cn.sun45.pcrteamanalyser.model.AnalyseTeam;

/**
 * Created by Sun45 on 2023年4月1日
 * <p>
 * 数据分析参数
 */
public class AnalyseConfig<M, R> {
    // 测试
    public boolean testing = false;

    // 分刀中断阈值
    public int interruptSize = 10_0000;

    /**
     * 接入数据模型转化为数据分析模型组
     *
     * @param model 接入数据模型
     * @return 数据分析模型组
     */
    public AnalyseTeam<M> buildAnalyseTeam(M model) {
        return (AnalyseTeam<M>) model;
    }

    /**
     * 数据分析结果转化为输出数据模型
     *
     * @param result 数据分析结果
     * @return 输出数据模型
     */
    public R buildResult(AnalyseResult<M> result) {
        return (R) result;
    }
}
