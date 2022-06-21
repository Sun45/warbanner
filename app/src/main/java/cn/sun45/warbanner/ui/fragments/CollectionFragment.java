package cn.sun45.warbanner.ui.fragments;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.clanwar.ClanwarHelper;
import cn.sun45.warbanner.document.database.setup.models.TeamGroupCollectionModel;
import cn.sun45.warbanner.document.database.source.models.TeamModel;
import cn.sun45.warbanner.framework.ui.BaseFragment;
import cn.sun45.warbanner.ui.activities.MainActivity;
import cn.sun45.warbanner.ui.shared.SharedViewModelClanwar;
import cn.sun45.warbanner.ui.shared.SharedViewModelSource;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupList;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupListListener;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupListModel;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/5/22
 * 收藏Fragment
 */
public class CollectionFragment extends BaseFragment implements TeamGroupListListener {
    private SharedViewModelSource sharedSource;
    private SharedViewModelClanwar sharedClanwar;

    private TeamGroupList mTeamGroupList;
    private TextView mEmptyHint;

    private FloatingActionButton mProgressingButton;
    private FloatingActionButton mSearchButton;

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
        mEmptyHint = mRoot.findViewById(R.id.empty_hint);
        mProgressingButton = mRoot.findViewById(R.id.progressing_btn);
        mSearchButton = mRoot.findViewById(R.id.search_btn);

        mTeamGroupList.setListener(this);
    }

    @Override
    protected void dataRequest() {
        logD("dataRequest");
        sharedSource = new ViewModelProvider(requireActivity()).get(SharedViewModelSource.class);
        sharedClanwar = new ViewModelProvider(requireActivity()).get(SharedViewModelClanwar.class);
        String str = Utils.getString(R.string.collection_empty_hint);
        SpannableStringBuilder builder = new SpannableStringBuilder(str);
        builder.setSpan(new ImageSpan(getContext(), R.drawable.ic_baseline_search_24), 12, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mEmptyHint.setText(builder);

        sharedSource.characterList.observe(requireActivity(), characterModels -> {
            mProgressingButton.setOnClickListener(v -> {
                ((MainActivity) getActivity()).showSnackBar(R.string.progressing);
            });
            mSearchButton.setOnClickListener(v -> {
                NavController controller = Navigation.findNavController(getView());
                controller.navigate(R.id.action_nav_main_to_nav_teamgroup);
            });
            mTeamGroupList.setCharacterModels(characterModels);
        });
        sharedClanwar.teamGroupCollectionList.observe(requireActivity(), teamGroupCollectionModels -> {
            if (teamGroupCollectionModels != null && !teamGroupCollectionModels.isEmpty()) {
                List<TeamGroupListModel> list = new ArrayList<>();
                for (int i = teamGroupCollectionModels.size() - 1; i >= 0; i--) {
                    TeamGroupCollectionModel teamGroupCollectionModel = teamGroupCollectionModels.get(i);
                    if (ClanwarHelper.isCollect(teamGroupCollectionModel)) {
                        List<TeamModel> teamModels = ClanwarHelper.getTeamGroupCollectionTeamList(teamGroupCollectionModel);
                        if (teamModels == null) {
                            continue;
                        }
                        TeamModel teamone = teamModels.get(0);
                        TeamModel teamtwo = teamModels.get(1);
                        TeamModel teamthree = teamModels.get(2);
                        TeamGroupListModel teamGroupListModel = new TeamGroupListModel(
                                teamone, buildIdlist(teamone), teamGroupCollectionModel.getBorrowindexone(),
                                teamtwo, buildIdlist(teamtwo), teamGroupCollectionModel.getBorrowindextwo(),
                                teamthree, buildIdlist(teamthree), teamGroupCollectionModel.getBorrowindexthree());
                        list.add(teamGroupListModel);
                    }
                }
                mTeamGroupList.setData(list);
                if (list != null && !list.isEmpty()) {
                    mEmptyHint.setVisibility(View.INVISIBLE);
                } else {
                    mEmptyHint.setVisibility(View.VISIBLE);
                }
                sharedClanwar.teamCustomizeList.observe(requireActivity(), teamCustomizeModels -> mTeamGroupList.notifyCustomize(teamCustomizeModels));
            } else {
                mTeamGroupList.setData(null);
                mEmptyHint.setVisibility(View.VISIBLE);
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
        sharedSource.characterList.removeObservers(requireActivity());
        sharedClanwar.teamGroupCollectionList.removeObservers(requireActivity());
        sharedClanwar.teamCustomizeList.removeObservers(requireActivity());
    }
}
