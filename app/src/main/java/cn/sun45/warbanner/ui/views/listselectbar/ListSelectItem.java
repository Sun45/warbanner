package cn.sun45.warbanner.ui.views.listselectbar;

import java.util.Objects;

/**
 * Created by Sun45 on 2022/6/20
 * 列表选择元素
 */
public class ListSelectItem {
    private String picUrl;

    private int position;

    public ListSelectItem(String picUrl, int position) {
        this.picUrl = picUrl;
        this.position = position;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public int getPosition() {
        return position;
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
