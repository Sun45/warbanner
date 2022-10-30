package cn.sun45.warbanner.ui.views.character.characterlist;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.document.database.source.models.CharacterModel;
import cn.sun45.warbanner.document.statics.charactertype.CharacterScreenType;
import cn.sun45.warbanner.ui.views.character.characterlist.characterlist.CharacterList;
import cn.sun45.warbanner.ui.views.character.characterlist.characterlist.CharacterListListener;
import cn.sun45.warbanner.ui.views.character.characterlist.characterlist.CharacterListModel;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2022/10/9
 * 角色列表容器
 */
public class CharacterListLay extends LinearLayout implements CharacterListListener {
    private View mPositionFrontLay;
    private TextView mPositionFrontText;
    private View mPositionMiddleLay;
    private TextView mPositionMiddleText;
    private View mPositionBackLay;
    private TextView mPositionBackText;
    private CharacterList mCharacterList;
    private TextView mEmptyHint;

    private int currentPosition;

    private List<CharacterListModel> characterListModelListOne;
    private List<CharacterListModel> characterListModelListTwo;
    private List<CharacterListModel> characterListModelListThree;

    private CharacterListLayListener characterListLayListener;

    public CharacterListLay(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.characterlistlay_lay, this);

        mPositionFrontLay = findViewById(R.id.position_front_lay);
        mPositionFrontText = findViewById(R.id.position_front_text);
        mPositionMiddleLay = findViewById(R.id.position_middle_lay);
        mPositionMiddleText = findViewById(R.id.position_middle_text);
        mPositionBackLay = findViewById(R.id.position_back_lay);
        mPositionBackText = findViewById(R.id.position_back_text);
        mCharacterList = findViewById(R.id.characterlist);
        mEmptyHint = findViewById(R.id.empty_hint);

        mCharacterList.setListener(this);
    }

    public void setCharacterListLayListener(CharacterListLayListener characterListLayListener) {
        this.characterListLayListener = characterListLayListener;
    }

    public void setData(List<CharacterModel> characterModelList, List<CharacterScreenType> characterScreenTypeList) {
        characterListModelListOne = new ArrayList<>();
        characterListModelListTwo = new ArrayList<>();
        characterListModelListThree = new ArrayList<>();
        for (int i = 0; i < characterModelList.size(); i++) {
            CharacterModel characterModel = characterModelList.get(i);
            CharacterScreenType characterScreenType = characterScreenTypeList.get(i);
            CharacterListModel characterListModel = new CharacterListModel(characterModel, characterScreenType);
            switch (characterModel.getGroup()) {
                case 0:
                    characterListModelListOne.add(characterListModel);
                    break;
                case 1:
                    characterListModelListTwo.add(characterListModel);
                    break;
                case 2:
                    characterListModelListThree.add(characterListModel);
                    break;
            }
        }
        mPositionFrontLay.setOnClickListener(v -> {
            select(0);
        });
        mPositionMiddleLay.setOnClickListener(v -> {
            select(1);
        });
        mPositionBackLay.setOnClickListener(v -> {
            select(2);
        });
        select(currentPosition);
    }

    private void select(int position) {
        currentPosition = position;
        showTextSelect(false, mPositionFrontText);
        showTextSelect(false, mPositionMiddleText);
        showTextSelect(false, mPositionBackText);
        switch (position) {
            case 0:
                showTextSelect(true, mPositionFrontText);
                break;
            case 1:
                showTextSelect(true, mPositionMiddleText);
                break;
            case 2:
                showTextSelect(true, mPositionBackText);
                break;
        }
        showList();
    }

    private void showList() {
        List<CharacterListModel> list = null;
        switch (currentPosition) {
            case 0:
                list = characterListModelListOne;
                break;
            case 1:
                list = characterListModelListTwo;
                break;
            case 2:
                list = characterListModelListThree;
                break;
        }
        mCharacterList.scrollToPosition(0);
        mCharacterList.setData(list);
        if (list != null && !list.isEmpty()) {
            mEmptyHint.setVisibility(View.INVISIBLE);
        } else {
            mEmptyHint.setVisibility(View.VISIBLE);
        }
    }

    private void showTextSelect(boolean select, TextView textView) {
        String str = textView.getText().toString();
        if (select) {
            SpannableStringBuilder builder = new SpannableStringBuilder(str);
            builder.setSpan(new ForegroundColorSpan(Utils.getAttrColor(getContext(), R.attr.colorSecondary)), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(builder);
        } else {
            textView.setText(str);
        }
    }

    @Override
    public void changeState(CharacterModel characterModel, CharacterScreenType characterScreenType) {
        characterListLayListener.changeState(characterModel, characterScreenType);
    }
}
