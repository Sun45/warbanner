package cn.sun45.warbanner.ui.views.combinationlist.selectbar;

import java.util.Objects;

/**
 * Created by Sun45 on 2023/9/24
 * 套餐列表选择元素
 */
public class CombinationListSelectItem {
    private String text;

    private int position;

    public CombinationListSelectItem(String text, int position) {
        this.text = text;
        this.position = position;
    }

    public String getText() {
        return text;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CombinationListSelectItem that = (CombinationListSelectItem) o;
        return position == that.position && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, position);
    }
}
