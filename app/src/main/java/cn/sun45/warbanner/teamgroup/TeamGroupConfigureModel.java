package cn.sun45.warbanner.teamgroup;

/**
 * Created by Sun45 on 2023/10/24
 * 分刀配置数据
 */
public class TeamGroupConfigureModel {
    private Configure teamOneConfigure;
    private Configure teamTwoConfigure;
    private Configure teamThreeConfigure;

    public Configure getTeamOneConfigure() {
        return teamOneConfigure;
    }

    public void setTeamOneConfigure(Configure teamOneConfigure) {
        this.teamOneConfigure = teamOneConfigure;
    }

    public Configure getTeamTwoConfigure() {
        return teamTwoConfigure;
    }

    public void setTeamTwoConfigure(Configure teamTwoConfigure) {
        this.teamTwoConfigure = teamTwoConfigure;
    }

    public Configure getTeamThreeConfigure() {
        return teamThreeConfigure;
    }

    public void setTeamThreeConfigure(Configure teamThreeConfigure) {
        this.teamThreeConfigure = teamThreeConfigure;
    }

    public class Configure {
        private int stage;
        private int boss;
        private int auto;
        private int characteroneid;
        private int charactertwoid;
        private int characterthreeid;
        private int characterfourid;
        private int characterfiveid;
        private int borrowindex;
        private boolean extra;

        public Configure(int stage, int boss, int auto, int characteroneid, int charactertwoid, int characterthreeid, int characterfourid, int characterfiveid, int borrowindex, boolean extra) {
            this.stage = stage;
            this.boss = boss;
            this.auto = auto;
            this.characteroneid = characteroneid;
            this.charactertwoid = charactertwoid;
            this.characterthreeid = characterthreeid;
            this.characterfourid = characterfourid;
            this.characterfiveid = characterfiveid;
            this.borrowindex = borrowindex;
            this.extra = extra;
        }

        public int getStage() {
            return stage;
        }

        public int getBoss() {
            return boss;
        }

        public int getAuto() {
            return auto;
        }

        public int getCharacteroneid() {
            return characteroneid;
        }

        public int getCharactertwoid() {
            return charactertwoid;
        }

        public int getCharacterthreeid() {
            return characterthreeid;
        }

        public int getCharacterfourid() {
            return characterfourid;
        }

        public int getCharacterfiveid() {
            return characterfiveid;
        }

        public int getBorrowindex() {
            return borrowindex;
        }

        public boolean isExtra() {
            return extra;
        }
    }
}
