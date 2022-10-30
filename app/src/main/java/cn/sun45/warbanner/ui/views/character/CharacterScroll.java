package cn.sun45.warbanner.ui.views.character;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.character.CharacterHelper;
import cn.sun45.warbanner.document.database.setup.models.TeamGroupScreenUsedCharacterModel;
import cn.sun45.warbanner.document.database.source.models.CharacterModel;
import cn.sun45.warbanner.document.statics.charactertype.CharacterUseType;
import cn.sun45.warbanner.ui.views.character.characterview.CharacterView;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2022/10/28
 * 角色Scroll控件
 */
public class CharacterScroll extends HorizontalScrollView {
    private LinearLayout mLay;

    private List<CharacterModel> characterModelList;

    public CharacterScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        int padding = Utils.dip2px(getContext(), 5);
        setPadding(padding, padding, padding, padding);
        mLay = new LinearLayout(getContext());
        mLay.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mLay.setOrientation(LinearLayout.HORIZONTAL);
        addView(mLay);
    }

    public void showUsedCharacters(List<CharacterModel> characterModelList) {
        this.characterModelList = characterModelList;
        mLay.removeAllViews();
        List<TeamGroupScreenUsedCharacterModel> teamGroupScreenUsedCharacterList = CharacterHelper.getUsedCharacterList();
        if (teamGroupScreenUsedCharacterList.isEmpty()) {
            addCharactershow(true, 0, CharacterUseType.TYPE_USEABLE.getScreenType().getType());
        }
        List<Integer> usingCharacters = new ArrayList<>();
        List<Integer> usedCharacters = new ArrayList<>();
        for (TeamGroupScreenUsedCharacterModel teamGroupScreenUsedCharacterModel : teamGroupScreenUsedCharacterList) {
            int id = teamGroupScreenUsedCharacterModel.getCharacterId();
            int type = teamGroupScreenUsedCharacterModel.getType();
            if (type == CharacterUseType.TYPE_USING.getScreenType().getType()) {
                usingCharacters.add(id);
            } else if (type == CharacterUseType.TYPE_USED.getScreenType().getType()) {
                usedCharacters.add(id);
            }
        }
        boolean first = true;
        for (Integer id : usingCharacters) {
            addCharactershow(first, id, CharacterUseType.TYPE_USING.getScreenType().getType());
            if (first) {
                first = false;
            }
        }
        for (Integer id : usedCharacters) {
            addCharactershow(first, id, CharacterUseType.TYPE_USED.getScreenType().getType());
            if (first) {
                first = false;
            }
        }
    }

    private void addCharactershow(boolean first, int id, int type) {
        CharacterView characterView = new CharacterView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                Utils.dip2px(getContext(), 50), Utils.dip2px(getContext(), 50));
        characterView.setLayoutParams(layoutParams);
        if (!first) {
            layoutParams.setMargins(Utils.dip2px(getContext(), 5), 0, 0, 0);
        }
        if (id != 0) {
            characterView.setCharacterModel(CharacterHelper.findCharacterById(id, characterModelList), id);
        }
        characterView.setBackGroundType(type);
        mLay.addView(characterView);
    }
}
