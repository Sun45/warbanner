package cn.sun45.warbanner.ui.fragments;

import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.document.db.setup.ScreenCharacterModel;
import cn.sun45.warbanner.document.db.source.CharacterModel;
import cn.sun45.warbanner.framework.document.db.DbHelper;
import cn.sun45.warbanner.framework.ui.BaseFragment;
import cn.sun45.warbanner.ui.shared.SharedViewModelCharacterModelList;
import cn.sun45.warbanner.ui.shared.SharedViewModelSetup;
import cn.sun45.warbanner.ui.views.characterlist.CharacterList;
import cn.sun45.warbanner.ui.views.characterlist.CharacterListListener;
import cn.sun45.warbanner.ui.views.characterlist.CharacterListModel;

/**
 * Created by Sun45 on 2021/5/30
 * 角色筛选Fragment
 */
public class CharacterScreenFragment extends BaseFragment implements CharacterListListener {
    private SharedViewModelCharacterModelList sharedCharacterModelList;
    private SharedViewModelSetup sharedSetup;

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
        sharedCharacterModelList = new ViewModelProvider(requireActivity()).get(SharedViewModelCharacterModelList.class);
        sharedSetup = new ViewModelProvider(requireActivity()).get(SharedViewModelSetup.class);
        sharedCharacterModelList.characterlist.observe(requireActivity(), new Observer<List<CharacterModel>>() {
            @Override
            public void onChanged(List<CharacterModel> characterModels) {
                List<CharacterListModel> list = new ArrayList<>();
                for (CharacterModel characterModel : characterModels) {
                    boolean find = false;
                    for (ScreenCharacterModel screenCharacterModel : sharedSetup.screencharacterlist.getValue()) {
                        if (characterModel.getId() == screenCharacterModel.getId()) {
                            find = true;
                            break;
                        }
                    }
                    list.add(new CharacterListModel(characterModel, find));
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
    public void changeState(boolean select, CharacterModel characterModel) {
        int id = characterModel.getId();
        if (select) {
            ScreenCharacterModel screenCharacterModel = new ScreenCharacterModel();
            screenCharacterModel.setId(id);
            DbHelper.insert(getContext(), screenCharacterModel);
        } else {
            DbHelper.delete(getContext(), ScreenCharacterModel.class, id + "");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedSetup.loadData();
    }
}
