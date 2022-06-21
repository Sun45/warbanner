package cn.sun45.warbanner.ui.fragments.character;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.character.CharacterHelper;
import cn.sun45.warbanner.document.database.setup.models.ScreenCharacterModel;
import cn.sun45.warbanner.document.database.source.models.CharacterModel;
import cn.sun45.warbanner.framework.ui.BaseFragment;
import cn.sun45.warbanner.ui.shared.SharedViewModelSource;
import cn.sun45.warbanner.ui.views.characterlist.CharacterList;
import cn.sun45.warbanner.ui.views.characterlist.CharacterListListener;
import cn.sun45.warbanner.ui.views.characterlist.CharacterListModel;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/5/30
 * 角色筛选Fragment
 */
public class CharacterScreenFragment extends BaseFragment implements CharacterListListener {
    private SharedViewModelSource sharedSource;

    private View mPositionFrontLay;
    private TextView mPositionFrontText;
    private View mPositionMiddleLay;
    private TextView mPositionMiddleText;
    private View mPositionBackLay;
    private TextView mPositionBackText;
    private CharacterList mCharacterList;
    private TextView mEmptyHint;

    private int currentPosition;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_characterscreen;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        MaterialToolbar toolbar = mRoot.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });

        mPositionFrontLay = mRoot.findViewById(R.id.position_front_lay);
        mPositionFrontText = mRoot.findViewById(R.id.position_front_text);
        mPositionMiddleLay = mRoot.findViewById(R.id.position_middle_lay);
        mPositionMiddleText = mRoot.findViewById(R.id.position_middle_text);
        mPositionBackLay = mRoot.findViewById(R.id.position_back_lay);
        mPositionBackText = mRoot.findViewById(R.id.position_back_text);
        mCharacterList = mRoot.findViewById(R.id.characterlist);
        mEmptyHint = mRoot.findViewById(R.id.empty_hint);

        mCharacterList.setListener(this);
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

    private void showList() {
        List<CharacterModel> characterModels = sharedSource.characterList.getValue();
        List<CharacterListModel> list = new ArrayList<>();
        for (CharacterModel characterModel : characterModels) {
            if (characterModel.getGroup() == currentPosition) {
                int type = 0;
                for (ScreenCharacterModel screenCharacterModel : CharacterHelper.getScreenCharacterList()) {
                    if (characterModel.getId() == screenCharacterModel.getCharacterId()) {
                        type = screenCharacterModel.getType();
                        break;
                    }
                }
                list.add(new CharacterListModel(characterModel, type));
            }
        }
        mCharacterList.scrollToPosition(0);
        mCharacterList.setData(list);
        if (list != null && !list.isEmpty()) {
            mEmptyHint.setVisibility(View.INVISIBLE);
        } else {
            mEmptyHint.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void dataRequest() {
        sharedSource = new ViewModelProvider(requireActivity()).get(SharedViewModelSource.class);
        sharedSource.characterList.observe(requireActivity(), new Observer<List<CharacterModel>>() {
            @Override
            public void onChanged(List<CharacterModel> characterModels) {
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
                showList();
            }
        });
    }

    @Override
    protected void onShow() {

    }

    @Override
    protected void onHide() {

    }

    @Override
    public void changeState(CharacterModel characterModel, int type) {
        CharacterHelper.screenCharacter(characterModel, type);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedSource.characterList.removeObservers(requireActivity());
    }
}
