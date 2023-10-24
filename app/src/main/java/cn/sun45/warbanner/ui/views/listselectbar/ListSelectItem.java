package cn.sun45.warbanner.ui.views.listselectbar;

import java.util.Objects;

/**
 * Created by Sun45 on 2022/6/20
 * 列表选择元素
 */
public class ListSelectItem {
    private int position;
    private String picUrl;

    private int picSrc;

    public ListSelectItem(int position, String picUrl) {
        this.position = position;
        this.picUrl = picUrl;
    }

    public ListSelectItem(int position, int picSrc) {
        this.position = position;
        this.picSrc = picSrc;
    }

    public int getPosition() {
        return position;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public int getPicSrc() {
        return picSrc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListSelectItem that = (ListSelectItem) o;
        return position == that.position && Objects.equals(picUrl, that.picUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(picUrl, position);
    }
}
