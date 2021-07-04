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
import cn.sun45.warbanner.document.db.setup.ScreenCharacterModel;
import cn.sun45.warbanner.document.db.source.CharacterModel;
import cn.sun45.warbanner.framework.ui.BaseFragment;
import cn.sun45.warbanner.ui.shared.SharedViewModelSource;
import cn.sun45.warbanner.ui.views.characterlist.CharacterList;
import cn.sun45.warbanner.ui.views.characterlist.CharacterListListener;
import cn.sun45.warbanner.ui.views.characterlist.CharacterListModel;

/**
 * Created by Sun45 on 2021/5/30
 * 角色筛选Fragment
 */
public class CharacterScreenFragment extends BaseFragment implements CharacterListListener {
    private SharedViewModelSource sharedSource;

    private CharacterList mCharacterList;

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
        mCharacterList = mRoot.findViewById(R.id.characterlist);
        mCharacterList.setListener(this);
    }

    @Override
    protected void dataRequest() {
        sharedSource = new ViewModelProvider(requireActivity()).get(SharedViewModelSource.class);
        sharedSource.characterlist.observe(requireActivity(), new Observer<List<CharacterModel>>() {
            @Override
            public void onChanged(List<CharacterModel> characterModels) {
                List<CharacterListModel> list = new ArrayList<>();
                for (CharacterModel characterModel : characterModels) {
                    int type = 0;
                    for (ScreenCharacterModel screenCharacterModel : CharacterHelper.getScreenCharacterList()) {
                        if (characterModel.getId() == screenCharacterModel.getCharacterId()) {
                            type = screenCharacterModel.getType();
                            break;
                        }
                    }
                    list.add(new CharacterListModel(characterModel, type));
                }
                mCharacterList.setData(list);
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
        sharedSource.characterlist.removeObservers(requireActivity());
    }
}
