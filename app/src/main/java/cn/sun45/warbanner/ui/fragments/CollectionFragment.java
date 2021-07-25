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
import cn.sun45.warbanner.character.CharacterHelper;
import cn.sun45.warbanner.clanwar.ClanwarHelper;
import cn.sun45.warbanner.document.db.clanwar.TeamCustomizeModel;
import cn.sun45.warbanner.document.db.clanwar.TeamGroupCollectionModel;
import cn.sun45.warbanner.document.db.clanwar.TeamModel;
import cn.sun45.warbanner.document.db.source.CharacterModel;
import cn.sun45.warbanner.framework.document.db.DbHelper;
import cn.sun45.warbanner.framework.ui.BaseFragment;
import cn.sun45.warbanner.ui.shared.SharedViewModelClanwar;
import cn.sun45.warbanner.ui.shared.SharedViewModelSource;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupList;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupListListener;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupListModel;

/**
 * Created by Sun45 on 2021/5/22
 * 收藏Fragment
 */
public class CollectionFragment extends BaseFragment implements TeamGroupListListener {
    private SharedViewModelSource sharedSource;
    private SharedViewModelClanwar sharedClanwar;

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
        sharedSource = new ViewModelProvider(requireActivity()).get(SharedViewModelSource.class);
        sharedClanwar = new ViewModelProvider(requireActivity()).get(SharedViewModelClanwar.class);

        sharedSource.characterlist.observe(requireActivity(), new Observer<List<CharacterModel>>() {
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
            }
        });
        sharedClanwar.teamGroupCollectionList.observe(requireActivity(), new Observer<List<TeamGroupCollectionModel>>() {
            @Override
            public void onChanged(List<TeamGroupCollectionModel> teamGroupCollectionModels) {
                List<TeamGroupListModel> list = new ArrayList<>();
                String date = ClanwarHelper.getCurrentClanWarDate();
                for (int i = teamGroupCollectionModels.size() - 1; i >= 0; i--) {
                    TeamGroupCollectionModel teamGroupCollectionModel = teamGroupCollectionModels.get(i);
                    if (ClanwarHelper.isCollect(teamGroupCollectionModel)) {
                        List<TeamModel> listone = DbHelper.query(getContext(), TeamModel.class, new String[]{"date", "number"}, new String[]{date, teamGroupCollectionModel.getTeamone()});
                        TeamModel teamone = null;
                        if (listone != null && !listone.isEmpty()) {
                            teamone = listone.get(0);
                        }
                        if (teamone == null) {
                            continue;
                        }
                        List<TeamModel> listtwo = DbHelper.query(getContext(), TeamModel.class, new String[]{"date", "number"}, new String[]{date, teamGroupCollectionModel.getTeamtwo()});
                        TeamModel teamtwo = null;
                        if (listtwo != null && !listtwo.isEmpty()) {
                            teamtwo = listtwo.get(0);
                        }
                        if (teamtwo == null) {
                            continue;
                        }
                        List<TeamModel> listthree = DbHelper.query(getContext(), TeamModel.class, new String[]{"date", "number"}, new String[]{date, teamGroupCollectionModel.getTeamthree()});
                        TeamModel teamthree = null;
                        if (listthree != null && !listthree.isEmpty()) {
                            teamthree = listthree.get(0);
                        }
                        if (teamthree == null) {
                            continue;
                        }
                        TeamGroupListModel teamGroupListModel = new TeamGroupListModel(
                                teamone, buildIdlist(teamone), teamGroupCollectionModel.getBorrowindexone(),
                                teamtwo, buildIdlist(teamtwo), teamGroupCollectionModel.getBorrowindextwo(),
                                teamthree, buildIdlist(teamthree), teamGroupCollectionModel.getBorrowindexthree());
                        list.add(teamGroupListModel);
                    }
                }
                mTeamGroupList.setData(list);
                sharedClanwar.teamCustomizeList.observe(requireActivity(), new Observer<List<TeamCustomizeModel>>() {
                    @Override
                    public void onChanged(List<TeamCustomizeModel> teamCustomizeModels) {
                        mTeamGroupList.notifyCustomize(teamCustomizeModels);
                    }
                });
            }
        });
    }

    private List<Integer> buildIdlist(TeamModel teamModel) {
        List<Integer> idlist = new ArrayList<>();
        idlist.add(teamModel.getCharacterone());
        idlist.add(teamModel.getCharactertwo());
        idlist.add(teamModel.getCharacterthree());
        idlist.add(teamModel.getCharacterfour());
        idlist.add(teamModel.getCharacterfive());
        return idlist;
    }

    @Override
    protected void onShow() {
    }

    @Override
    protected void onHide() {

    }

    @Override
    public void collect(TeamGroupListModel teamGroupListModel, boolean collect) {
        ClanwarHelper.collect(teamGroupListModel, collect);
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
        sharedSource.characterlist.removeObservers(requireActivity());
        sharedClanwar.teamGroupCollectionList.removeObservers(requireActivity());
        sharedClanwar.teamCustomizeList.removeObservers(requireActivity());
    }
}
