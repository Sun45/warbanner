package cn.sun45.warbanner.ui.views.combinationlist;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
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
import cn.sun45.warbanner.ui.views.listselectbar.ListSelectItem;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2023/9/23
 * 套餐列表Adapter
 */
public class CombinationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "CombinationListAdapter";

    private Context context;

    private CombinationListListener listener;

    private List<CharacterModel> characterModels;

    private List<CombinationGroupModel> list;

    public CombinationListAdapter(Context context) {
        this.context = context;
    }

    public void setListener(CombinationListListener listener) {
        this.listener = listener;
    }

    public void setCharacterModels(List<CharacterModel> characterModels) {
        this.characterModels = characterModels;
    }

    public void setList(List<CombinationGroupModel> list) {
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = getItem(position);
        return item instanceof CombinationGroupModel ? 0 : 1;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case 0:
                holder = new CombinationListAdapter.DescriptionHolder(LayoutInflater.from(context).inflate(R.layout.combinationlist_descriptionitem, parent, false));
                break;
            case 1:
                holder = new CombinationListAdapter.CombinationHolder(LayoutInflater.from(context).inflate(R.layout.combinationlist_combinationitem, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                ((DescriptionHolder) holder).setData((CombinationGroupModel) getItem(position));
                break;
            case 1:
                ((CombinationHolder) holder).setData((CombinationListModel) getItem(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        } else {
            int count = 0;
            for (CombinationGroupModel combinationGroupModel : list) {
                count++;
                count += combinationGroupModel.getList().size();
            }
            return count;
        }
    }

    public List<ListSelectItem> getListSelectItemList() {
        List<ListSelectItem> listSelectItems = new ArrayList<>();
        int position = 0;
        for (int i = 0; i < list.size(); i++, position++) {
            CombinationGroupModel combinationGroupModel = list.get(i);
            ListSelectItem listSelectItem = new ListSelectItem(position, combinationGroupModel.getPicSrc());
            listSelectItems.add(listSelectItem);
            position += combinationGroupModel.getList().size();
        }
        return listSelectItems;
    }

    private Object getItem(int position) {
        if (list != null) {
            for (CombinationGroupModel combinationGroupModel : list) {
                if (position == 0) {
                    return combinationGroupModel;
                } else {
                    position--;
                    int count = combinationGroupModel.getList().size();
                    if (position < count) {
                        return combinationGroupModel.getList().get(position);
                    } else {
                        position -= count;
                    }
                }
            }
        }
        return null;
    }

    public class DescriptionHolder extends RecyclerView.ViewHolder {
        private TextView mDescription;

        public DescriptionHolder(@NonNull View itemView) {
            super(itemView);
            mDescription = itemView.findViewById(R.id.describtion);
        }

        public void setData(CombinationGroupModel combinationGroupModel) {
            mDescription.setText(combinationGroupModel.getDescription());
        }
    }

    public class CombinationHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TeamHolder mTeamOne;
        private TeamHolder mTeamTwo;
        private TeamHolder mTeamThree;
        private TeamHolder mTeamFour;
        private TeamHolder mTeamFive;
        private TeamHolder mTeamSix;
        private TextView reCalucate;

        private CombinationListModel model;

        public CombinationHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
            mTeamOne = new TeamHolder(itemView.findViewById(R.id.teamone));
            mTeamTwo = new TeamHolder(itemView.findViewById(R.id.teamtwo));
            mTeamThree = new TeamHolder(itemView.findViewById(R.id.teamthree));
            mTeamFour = new TeamHolder(itemView.findViewById(R.id.teamfour));
            mTeamFive = new TeamHolder(itemView.findViewById(R.id.teamfive));
            mTeamSix = new TeamHolder(itemView.findViewById(R.id.teamsix));
            reCalucate = itemView.findViewById(R.id.recalucate);
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.open(model);
                }
            });
            String text = Utils.getString(R.string.re_calucate_title) + "--->";
            SpannableString content = new SpannableString(text);
            content.setSpan(new UnderlineSpan(), 0, text.length(), 0);
            reCalucate.setText(content);
            reCalucate.setOnClickListener(v -> {
                if (listener != null) {
                    listener.reCalucate(model);
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
            if (model.getTeamsix() != null) {
                reCalucate.setVisibility(View.GONE);
            } else {
                reCalucate.setVisibility(View.VISIBLE);
            }
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
                "   " + teamfive.getBoss() + "+" + (teamsix != null ? teamsix.getBoss() : "?");
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
