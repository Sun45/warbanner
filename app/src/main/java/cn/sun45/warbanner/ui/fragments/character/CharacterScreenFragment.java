package cn.sun45.warbanner.ui.fragments.character;

import android.view.View;

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
import cn.sun45.warbanner.document.statics.charactertype.CharacterScreenType;
import cn.sun45.warbanner.framework.ui.BaseFragment;
import cn.sun45.warbanner.ui.shared.SharedViewModelSource;
import cn.sun45.warbanner.ui.views.character.characterlist.CharacterListLay;
import cn.sun45.warbanner.ui.views.character.characterlist.CharacterListLayListener;

/**
 * Created by Sun45 on 2021/5/30
 * 角色筛选Fragment
 */
public class CharacterScreenFragment extends BaseFragment implements CharacterListLayListener {
    private SharedViewModelSource sharedSource;

    private CharacterListLay mCharacterListLay;

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
        toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        mCharacterListLay = mRoot.findViewById(R.id.characterlistlay);
        mCharacterListLay.setCharacterListLayListener(this);
    }

    @Override
    protected void dataRequest() {
        sharedSource = new ViewModelProvider(requireActivity()).get(SharedViewModelSource.class);
        sharedSource.characterList.observe(requireActivity(), new Observer<List<CharacterModel>>() {
            @Override
            public void onChanged(List<CharacterModel> characterModels) {
                List<CharacterModel> characterModelList = new ArrayList<>();
                List<CharacterScreenType> characterScreenTypeList = new ArrayList<>();
                List<ScreenCharacterModel> screenCharacterList = CharacterHelper.getScreenCharacterList();
                for (CharacterModel characterModel : characterModels) {
                    CharacterScreenType characterScreenType = CharacterScreenType.TYPE_DEFAULT;
                    for (ScreenCharacterModel screenCharacterModel : screenCharacterList) {
                        if (characterModel.getId() == screenCharacterModel.getCharacterId()) {
                            characterScreenType = CharacterScreenType.get(screenCharacterModel.getType());
                            break;
                        }
                    }
                    characterModelList.add(characterModel);
                    characterScreenTypeList.add(characterScreenType);
                }
                mCharacterListLay.setData(characterModelList, characterScreenTypeList);
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
    public void changeState(CharacterModel characterModel, CharacterScreenType characterScreenType) {
        CharacterHelper.saveCharacterOwnType(characterModel, characterScreenType);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedSource.characterList.removeObservers(requireActivity());
    }
}
