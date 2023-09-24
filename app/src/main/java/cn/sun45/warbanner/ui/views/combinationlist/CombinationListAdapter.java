package cn.sun45.warbanner.ui.views.combinationlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.character.CharacterHelper;
import cn.sun45.warbanner.document.database.source.models.CharacterModel;
import cn.sun45.warbanner.document.database.source.models.TeamModel;
import cn.sun45.warbanner.ui.views.character.characterview.CharacterView;
import cn.sun45.warbanner.ui.views.combinationlist.selectbar.CombinationListSelectItem;

/**
 * Created by Sun45 on 2023/9/23
 * 套餐列表Adapter
 */
public class CombinationListAdapter extends RecyclerView.Adapter<CombinationListAdapter.Holder> {
    private static final String TAG = "CombinationListAdapter";

    private Context context;

    private CombinationListListener listener;

    private List<CharacterModel> characterModels;

    private List<CombinationListModel> list;

    public CombinationListAdapter(Context context) {
        this.context = context;
    }

    public void setListener(CombinationListListener listener) {
        this.listener = listener;
    }

    public void setCharacterModels(List<CharacterModel> characterModels) {
        this.characterModels = characterModels;
    }

    public void setList(List<CombinationListModel> list) {
        this.list = list;
    }

    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.combinationlist_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public List<CombinationListSelectItem> getListSelectItemList() {
        List<CombinationListSelectItem> listSelectItems = new ArrayList<>();
        String lasttitle = null;
        for (int i = 0; i < list.size(); i++) {
            CombinationListModel combinationListModel = list.get(i);
            String title = buildTitle(combinationListModel);
            if (title.equals(lasttitle)) {
                continue;
            }
            lasttitle = title;
            CombinationListSelectItem listSelectItem = new CombinationListSelectItem(buildShortTitle(combinationListModel), i);
            listSelectItems.add(listSelectItem);
        }
        return listSelectItems;
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TeamHolder mTeamOne;
        private TeamHolder mTeamTwo;
        private TeamHolder mTeamThree;
        private TeamHolder mTeamFour;
        private TeamHolder mTeamFive;
        private TeamHolder mTeamSix;

        private CombinationListModel model;

        public Holder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
            mTeamOne = new TeamHolder(itemView.findViewById(R.id.teamone));
            mTeamTwo = new TeamHolder(itemView.findViewById(R.id.teamtwo));
            mTeamThree = new TeamHolder(itemView.findViewById(R.id.teamthree));
            mTeamFour = new TeamHolder(itemView.findViewById(R.id.teamfour));
            mTeamFive = new TeamHolder(itemView.findViewById(R.id.teamfive));
            mTeamSix = new TeamHolder(itemView.findViewById(R.id.teamsix));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.open(model);
                    }
                }
            });
        }

        public void setData(CombinationListModel combinationListModel) {
            model = combinationListModel;
            mTitle.setText(buildTitle(combinationListModel));
            mTeamOne.setData(combinationListModel.getTeamone(), combinationListModel.getReturntimeone(), combinationListModel.getIdlistone(), combinationListModel.getBorrowindexone());
            mTeamTwo.setData(combinationListModel.getTeamtwo(), combinationListModel.getReturntimetwo(), combinationListModel.getIdlisttwo(), combinationListModel.getBorrowindextwo());
            mTeamThree.setData(combinationListModel.getTeamthree(), combinationListModel.getReturntimethree(), combinationListModel.getIdlistthree(), combinationListModel.getBorrowindexthree());
            mTeamFour.setData(combinationListModel.getTeamfour(), combinationListModel.getReturntimefour(), combinationListModel.getIdlistfour(), combinationListModel.getBorrowindexfour());
            mTeamFive.setData(combinationListModel.getTeamfive(), combinationListModel.getReturntimefive(), combinationListModel.getIdlistfive(), combinationListModel.getBorrowindexfive());
            mTeamSix.setData(combinationListModel.getTeamsix(), combinationListModel.getReturntimesix(), combinationListModel.getIdlistsix(), combinationListModel.getBorrowindexsix());
        }
    }

    private String buildTitle(CombinationListModel combinationListModel) {
        TeamModel teamone = combinationListModel.getTeamone();
        TeamModel teamtwo = combinationListModel.getTeamtwo();
        TeamModel teamthree = combinationListModel.getTeamthree();
        TeamModel teamfour = combinationListModel.getTeamfour();
        TeamModel teamfive = combinationListModel.getTeamfive();
        TeamModel teamsix = combinationListModel.getTeamsix();
        return teamone.getBoss() + "+" + teamtwo.getBoss() +
                "   " + teamthree.getBoss() + "+" + teamfour.getBoss() +
                "   " + teamfive.getBoss() + "+" + teamsix.getBoss();
    }

    private String buildShortTitle(CombinationListModel combinationListModel) {
        TeamModel teamone = combinationListModel.getTeamone();
        TeamModel teamtwo = combinationListModel.getTeamtwo();
        TeamModel teamthree = combinationListModel.getTeamthree();
        TeamModel teamfour = combinationListModel.getTeamfour();
        TeamModel teamfive = combinationListModel.getTeamfive();
        TeamModel teamsix = combinationListModel.getTeamsix();
        return teamone.getBoss().substring(1, 2) + "+" + teamtwo.getBoss().substring(1, 2) +
                "/" + teamthree.getBoss().substring(1, 2) + "+" + teamfour.getBoss().substring(1, 2) +
                "/" + teamfive.getBoss().substring(1, 2) + "+" + teamsix.getBoss().substring(1, 2);
    }

    private class TeamHolder {
        private ViewGroup lay;
        private TextView mTitle;
        private CharacterView mCharacterone;
        private CharacterView mCharactertwo;
        private CharacterView mCharacterthree;
        private CharacterView mCharacterfour;
        private CharacterView mCharacterfive;

        public TeamHolder(ViewGroup lay) {
            this.lay = lay;
            mTitle = lay.findViewById(R.id.title);
            mCharacterone = lay.findViewById(R.id.characterone_lay);
            mCharactertwo = lay.findViewById(R.id.charactertwo_lay);
            mCharacterthree = lay.findViewById(R.id.characterthree_lay);
            mCharacterfour = lay.findViewById(R.id.characterfour_lay);
            mCharacterfive = lay.findViewById(R.id.characterfive_lay);
        }

        public void setData(TeamModel teamModel, int returntime, List<Integer> idlist, int borrowindex) {
            if (teamModel == null) {
                lay.setVisibility(View.GONE);
                return;
            }
            lay.setVisibility(View.VISIBLE);
            mTitle.setText(teamModel.getSn() + " 返" + returntime + "s");
            mCharacterone.setAutoShow(teamModel.isAuto());
            mCharacterone.setHalfShow(teamModel.isFinish());
            characterDataSet(mCharacterone, idlist.get(0), borrowindex == 0);
            characterDataSet(mCharactertwo, idlist.get(1), borrowindex == 1);
            characterDataSet(mCharacterthree, idlist.get(2), borrowindex == 2);
            characterDataSet(mCharacterfour, idlist.get(3), borrowindex == 3);
            characterDataSet(mCharacterfive, idlist.get(4), borrowindex == 4);
        }
    }

    private void characterDataSet(CharacterView characterView, int id, boolean borrow) {
        CharacterModel characterModel = CharacterHelper.findCharacterById(id, characterModels);
        if (borrow) {
            characterView.setBackGroundType(CharacterView.BG_RED);
        } else {
            characterView.setBackGroundType(CharacterView.BG_DEFAULT);
        }
        characterView.setCharacterModel(characterModel, id);
    }
}
