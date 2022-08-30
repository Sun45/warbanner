package cn.sun45.warbanner.ui.views.teamgrouplist;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.character.CharacterHelper;
import cn.sun45.warbanner.clanwar.ClanwarHelper;
import cn.sun45.warbanner.document.database.setup.models.TeamCustomizeModel;
import cn.sun45.warbanner.document.database.source.models.CharacterModel;
import cn.sun45.warbanner.document.database.source.models.TeamModel;
import cn.sun45.warbanner.ui.views.characterview.CharacterView;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/5/30
 * 分刀列表Adapter
 */
public class TeamGroupListAdapter extends RecyclerView.Adapter<TeamGroupListAdapter.Holder> {
    private static final String TAG = "TeamGroupListAdapter";

    private Context context;

    private TeamGroupListListener listener;

    private List<CharacterModel> characterModels;

    private List<TeamGroupListModel> list;

    //阵容自定义信息
    private List<TeamCustomizeModel> teamCustomizeModels;

    public TeamGroupListAdapter(Context context) {
        this.context = context;
    }

    public void setListener(TeamGroupListListener listener) {
        this.listener = listener;
    }

    public void setCharacterModels(List<CharacterModel> characterModels) {
        this.characterModels = characterModels;
    }

    public void setList(List<TeamGroupListModel> list) {
        this.list = list;
    }

    public void setTeamCustomizeModels(List<TeamCustomizeModel> teamCustomizeModels) {
        this.teamCustomizeModels = teamCustomizeModels;
    }

    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.teamgrouplist_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TeamGroupListAdapter.Holder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private AppCompatImageView mCollection;
        private TeamHolder mTeamOne;
        private TeamHolder mTeamTwo;
        private TeamHolder mTeamThree;

        private TeamGroupListModel model;
        private boolean collect;

        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
            mCollection = itemView.findViewById(R.id.collection);
            mTeamOne = new TeamHolder(itemView.findViewById(R.id.teamone));
            mTeamTwo = new TeamHolder(itemView.findViewById(R.id.teamtwo));
            mTeamThree = new TeamHolder(itemView.findViewById(R.id.teamthree));
            mCollection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    collect = !collect;
                    showCollection();
                    if (listener != null) {
                        listener.collect(model, collect);
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.open(model);
                    }
                }
            });
        }

        public void setData(TeamGroupListModel teamGroupListModel) {
            model = teamGroupListModel;
            collect = ClanwarHelper.isCollect(teamGroupListModel);
            TeamModel teamone = teamGroupListModel.getTeamone();
            TeamModel teamtwo = teamGroupListModel.getTeamtwo();
            TeamModel teamthree = teamGroupListModel.getTeamthree();
            TeamCustomizeModel teamCustomizeone = ClanwarHelper.findCustomizeModel(teamone, teamCustomizeModels);
            TeamCustomizeModel teamCustomizetwo = ClanwarHelper.findCustomizeModel(teamtwo, teamCustomizeModels);
            TeamCustomizeModel teamCustomizethree = ClanwarHelper.findCustomizeModel(teamthree, teamCustomizeModels);
            if ((teamCustomizeone != null && teamCustomizeone.damageEffective()) || (teamCustomizetwo != null && teamCustomizetwo.damageEffective()) || (teamCustomizethree != null && teamCustomizethree.damageEffective())) {
                String title = ((teamCustomizeone != null && teamCustomizeone.damageEffective()) ? teamCustomizeone.getDamage() : teamone.getDamage())
                        + ((teamCustomizetwo != null && teamCustomizetwo.damageEffective()) ? teamCustomizetwo.getDamage() : teamtwo.getDamage())
                        + ((teamCustomizethree != null && teamCustomizethree.damageEffective()) ? teamCustomizethree.getDamage() : teamthree.getDamage()) + "w";
                int length = title.length();
                title += " " + teamone.getBoss() + " " + teamtwo.getBoss() + " " + teamthree.getBoss();
                SpannableStringBuilder builder = new SpannableStringBuilder(title);
                builder.setSpan(new ForegroundColorSpan(Utils.getAttrColor(context, R.attr.colorSecondary)), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mTitle.setText(builder);
            } else {
                String title = teamone.getDamage() + teamtwo.getDamage() + teamthree.getDamage() + "w" + " " + teamone.getBoss() + " " + teamtwo.getBoss() + " " + teamthree.getBoss();
                mTitle.setText(title);
            }
            showCollection();
            mTeamOne.setdata(teamone, teamCustomizeone, teamGroupListModel.getIdlistone(), teamGroupListModel.getBorrowindexone());
            mTeamTwo.setdata(teamtwo, teamCustomizetwo, teamGroupListModel.getIdlisttwo(), teamGroupListModel.getBorrowindextwo());
            mTeamThree.setdata(teamthree, teamCustomizethree, teamGroupListModel.getIdlistthree(), teamGroupListModel.getBorrowindexthree());
        }

        private void showCollection() {
            if (collect) {
                mCollection.setImageResource(R.drawable.ic_baseline_star_yellow);
            } else {
                mCollection.setImageResource(R.drawable.ic_baseline_star_white);
            }
        }
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
            mTitle = lay.findViewById(R.id.title);
            mCharacterone = lay.findViewById(R.id.characterone_lay);
            mCharactertwo = lay.findViewById(R.id.charactertwo_lay);
            mCharacterthree = lay.findViewById(R.id.characterthree_lay);
            mCharacterfour = lay.findViewById(R.id.characterfour_lay);
            mCharacterfive = lay.findViewById(R.id.characterfive_lay);
        }

        public void setdata(TeamModel teamModel, TeamCustomizeModel teamCustomizeModel, List<Integer> idlist, int borrowindex) {
            if (teamCustomizeModel == null) {
                String title = teamModel.getSn() + " " + teamModel.getDamage() + "w";
                mTitle.setText(title);
            } else {
                String str = teamModel.getSn();
                SpannableStringBuilder builder = new SpannableStringBuilder(str);
                if (teamCustomizeModel.isBlock()) {
                    builder.setSpan(new StrikethroughSpan(), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.setSpan(new ForegroundColorSpan(Utils.getColor(R.color.red_500)), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                builder.append(" ");
                int start = builder.length();
                if (teamCustomizeModel.damageEffective()) {
                    builder.append(teamCustomizeModel.getDamage() + "w");
                    builder.setSpan(new ForegroundColorSpan(Utils.getAttrColor(context, R.attr.colorSecondary)), start, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    builder.append(teamModel.getDamage() + "w");
                }
                mTitle.setText(builder);
            }
            mCharacterone.setAutoShow(teamModel.isAuto());
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
