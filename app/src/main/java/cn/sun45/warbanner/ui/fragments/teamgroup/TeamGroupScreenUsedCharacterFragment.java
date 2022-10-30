package cn.sun45.warbanner.ui.fragments.teamgroup;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.appbar.MaterialToolbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.character.CharacterHelper;
import cn.sun45.warbanner.document.database.setup.models.ScreenCharacterModel;
import cn.sun45.warbanner.document.database.setup.models.TeamGroupScreenUsedCharacterModel;
import cn.sun45.warbanner.document.database.source.models.CharacterModel;
import cn.sun45.warbanner.document.statics.charactertype.CharacterOwnType;
import cn.sun45.warbanner.document.statics.charactertype.CharacterScreenType;
import cn.sun45.warbanner.document.statics.charactertype.CharacterUseType;
import cn.sun45.warbanner.framework.ui.BaseActivity;
import cn.sun45.warbanner.framework.ui.BaseFragment;
import cn.sun45.warbanner.ui.shared.SharedViewModelSource;
import cn.sun45.warbanner.ui.views.character.CharacterScroll;
import cn.sun45.warbanner.ui.views.character.characterlist.CharacterListLay;
import cn.sun45.warbanner.ui.views.character.characterlist.CharacterListLayListener;
import cn.sun45.warbanner.ui.views.character.characterview.CharacterView;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2022/10/27
 * 分刀筛选已用角色Fragment
 */
public class TeamGroupScreenUsedCharacterFragment extends BaseFragment implements CharacterListLayListener {
    private SharedViewModelSource sharedSource;

    private CharacterScroll mCharacterScroll;

    private CharacterListLay mCharacterListLay;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_teamgroupscreenusedcharacter;
    }

    @Override
    protected void initData() {
        setHasOptionsMenu(true);
    }

    @Override
    protected void initView() {
        MaterialToolbar toolbar = mRoot.findViewById(R.id.drop_toolbar);
        ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });

        mCharacterScroll=mRoot.findViewById(R.id.charactershow_scroll);
        mCharacterListLay = mRoot.findViewById(R.id.characterlistlay);

        mCharacterListLay.setCharacterListLayListener(this);
    }

    @Override
    protected void dataRequest() {
        sharedSource = new ViewModelProvider(requireActivity()).get(SharedViewModelSource.class);

        sharedSource.characterList.observe(requireActivity(), new Observer<List<CharacterModel>>() {
            @Override
            public void onChanged(List<CharacterModel> characterModels) {
                showScroll();
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
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fragment_teamgroupscreenusedcharacter_drop_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_reset:
                reset();
                break;
        }
        return true;
    }

    private void reset() {
        CharacterHelper.clearUsedCharacterList();
        showScroll();
        showList();
    }

    private void showScroll() {
        mCharacterScroll.showUsedCharacters(sharedSource.characterList.getValue());
    }

    private void showList() {
        List<CharacterModel> characterModels = sharedSource.characterList.getValue();
        List<CharacterModel> characterModelList = new ArrayList<>();
        List<CharacterScreenType> characterScreenTypeList = new ArrayList<>();
        List<ScreenCharacterModel> screenCharacterList = CharacterHelper.getScreenCharacterList();
        List<TeamGroupScreenUsedCharacterModel> teamGroupScreenUsedCharacterList = CharacterHelper.getUsedCharacterList();
        for (CharacterModel characterModel : characterModels) {
            boolean skip = false;
            for (ScreenCharacterModel screenCharacterModel : screenCharacterList) {
                if (characterModel.getId() == screenCharacterModel.getCharacterId() &&
                        screenCharacterModel.getType() == CharacterOwnType.TYPE_SKIP.getScreenType().getType()) {
                    skip = true;
                    break;
                }
            }
            if (skip) {
                continue;
            }
            characterModelList.add(characterModel);
            CharacterScreenType characterScreenType = CharacterScreenType.TYPE_DEFAULT;
            for (TeamGroupScreenUsedCharacterModel teamGroupScreenUsedCharacterModel : teamGroupScreenUsedCharacterList) {
                if (characterModel.getId() == teamGroupScreenUsedCharacterModel.getCharacterId()) {
                    characterScreenType = CharacterScreenType.get(teamGroupScreenUsedCharacterModel.getType());
                    break;
                }
            }
            characterScreenTypeList.add(characterScreenType);
        }
        mCharacterListLay.setData(characterModelList, characterScreenTypeList);
    }

    @Override
    public void changeState(CharacterModel characterModel, CharacterScreenType characterScreenType) {
        CharacterHelper.saveCharacterUseType(characterModel, characterScreenType);
        showScroll();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedSource.characterList.removeObservers(requireActivity());
    }
}
