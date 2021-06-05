package cn.sun45.warbanner.ui.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.document.db.clanwar.TeamGroupModel;
import cn.sun45.warbanner.document.db.clanwar.TeamModel;
import cn.sun45.warbanner.document.db.source.CharacterModel;
import cn.sun45.warbanner.framework.document.db.DbHelper;
import cn.sun45.warbanner.framework.ui.BaseFragment;
import cn.sun45.warbanner.ui.shared.SharedViewModelCharacterModelList;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupList;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupListListener;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupListModel;

/**
 * Created by Sun45 on 2021/5/22
 * 收藏Fragment
 */
public class CollectionFragment extends BaseFragment implements TeamGroupListListener {
    private SharedViewModelCharacterModelList sharedCharacterModelList;

    private TeamGroupList mTeamGroupList;
    private FloatingActionButton mFloatingButton;

    private List<CharacterModel> characterModels;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_collection;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mTeamGroupList = mRoot.findViewById(R.id.teamgrouplist);
        mTeamGroupList.setListener(this);
        mFloatingButton = mRoot.findViewById(R.id.floating_button);
    }

    @Override
    protected void dataRequest() {
        logD("dataRequest");
        sharedCharacterModelList = new ViewModelProvider(requireActivity()).get(SharedViewModelCharacterModelList.class);
        sharedCharacterModelList.characterlist.observe(requireActivity(), new Observer<List<CharacterModel>>() {
            @Override
            public void onChanged(List<CharacterModel> characterModels) {
                mFloatingButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NavController controller = Navigation.findNavController(getView());
                        controller.navigate(R.id.action_nav_main_to_nav_teamgroup);
                    }
                });
                CollectionFragment.this.characterModels = characterModels;
                mTeamGroupList.setCharacterModels(characterModels);
                List<TeamGroupModel> collectionlist = DbHelper.query(getContext(), TeamGroupModel.class);
                List<TeamGroupListModel> list = new ArrayList<>();
                for (int i = collectionlist.size() - 1; i >= 0; i--) {
                    TeamGroupModel teamGroupModel = collectionlist.get(i);
                    TeamModel teamone = DbHelper.query(getContext(), TeamModel.class, teamGroupModel.getTeamone());
                    if (teamone == null) {
                        continue;
                    }
                    TeamModel teamtwo = DbHelper.query(getContext(), TeamModel.class, teamGroupModel.getTeamtwo());
                    if (teamtwo == null) {
                        continue;
                    }
                    TeamModel teamthree = DbHelper.query(getContext(), TeamModel.class, teamGroupModel.getTeamthree());
                    if (teamthree == null) {
                        continue;
                    }
                    TeamGroupListModel teamGroupListModel = new TeamGroupListModel(
                            teamone, buildIdlist(teamone), teamGroupModel.getBorrowindexone(),
                            teamtwo, buildIdlist(teamtwo), teamGroupModel.getBorrowindextwo(),
                            teamthree, buildIdlist(teamthree), teamGroupModel.getBorrowindexthree());
                    list.add(teamGroupListModel);
                }
                mTeamGroupList.setData(list);
            }
        });
    }

    private List<Integer> buildIdlist(TeamModel teamModel) {
        List<Integer> idlist = new ArrayList<>();
        idlist.add(findCharacter(teamModel.getCharacterone()).getId());
        idlist.add(findCharacter(teamModel.getCharactertwo()).getId());
        idlist.add(findCharacter(teamModel.getCharacterthree()).getId());
        idlist.add(findCharacter(teamModel.getCharacterfour()).getId());
        idlist.add(findCharacter(teamModel.getCharacterfive()).getId());
        return idlist;
    }

    /**
     * 根据昵称获取角色信息
     *
     * @param nickname 角色昵称
     */
    private CharacterModel findCharacter(String nickname) {
        CharacterModel characterModel = null;
        if (characterModels != null && !characterModels.isEmpty()) {
            boolean find = false;
            for (CharacterModel model : characterModels) {
                for (String str : model.getNicknames()) {
                    if (nickname.equals(str)) {
                        find = true;
                        break;
                    }
                }
                if (find) {
                    characterModel = model;
                    break;
                }
            }
        }
        return characterModel;
    }

    @Override
    protected void onShow() {
    }

    @Override
    protected void onHide() {

    }

    @Override
    public void collect(TeamGroupListModel teamGroupListModel) {
        if (teamGroupListModel.isCollected()) {
            TeamGroupModel teamGroupModel = new TeamGroupModel();
            teamGroupModel.setTeamone(teamGroupListModel.getTeamone().getNumber());
            teamGroupModel.setBorrowindexone(teamGroupListModel.getBorrowindexone());
            teamGroupModel.setTeamtwo(teamGroupListModel.getTeamtwo().getNumber());
            teamGroupModel.setBorrowindextwo(teamGroupListModel.getBorrowindextwo());
            teamGroupModel.setTeamthree(teamGroupListModel.getTeamthree().getNumber());
            teamGroupModel.setBorrowindexthree(teamGroupListModel.getBorrowindexthree());
            DbHelper.insert(getContext(), teamGroupModel);
        } else {
            DbHelper.delete(getContext(), TeamGroupModel.class
                    , new String[]{"teamone", "teamtwo", "teamthree"}
                    , new String[]{
                            teamGroupListModel.getTeamone().getNumber(),
                            teamGroupListModel.getTeamtwo().getNumber(),
                            teamGroupListModel.getTeamthree().getNumber()
                    });
        }
    }

    @Override
    public void open(TeamGroupListModel teamGroupListModel) {
        NavController controller = Navigation.findNavController(getView());
        Bundle bundle = new Bundle();
        bundle.putSerializable("teamGroupListModel", teamGroupListModel);
        controller.navigate(R.id.action_nav_main_to_nav_teamgroupdetail, bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedCharacterModelList.characterlist.removeObservers(requireActivity());
    }
}
