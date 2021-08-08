package cn.sun45.warbanner.ui.views.teamgrouplist;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.character.CharacterHelper;
import cn.sun45.warbanner.clanwar.ClanwarHelper;
import cn.sun45.warbanner.document.db.clanwar.TeamCustomizeModel;
import cn.sun45.warbanner.document.db.clanwar.TeamModel;
import cn.sun45.warbanner.document.db.source.CharacterModel;
import cn.sun45.warbanner.framework.image.ImageRequester;
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
                String title = ((teamCustomizeone != null && teamCustomizeone.damageEffective()) ? teamCustomizeone.getEllipsisdamage() : teamone.getEllipsisdamage())
                        + ((teamCustomizetwo != null && teamCustomizetwo.damageEffective()) ? teamCustomizetwo.getEllipsisdamage() : teamtwo.getEllipsisdamage())
                        + ((teamCustomizethree != null && teamCustomizethree.damageEffective()) ? teamCustomizethree.getEllipsisdamage() : teamthree.getEllipsisdamage()) + "w";
                int length = title.length();
                title += " " + teamone.getBoss() + " " + teamtwo.getBoss() + " " + teamthree.getBoss();
                SpannableStringBuilder builder = new SpannableStringBuilder(title);
                builder.setSpan(new ForegroundColorSpan(Utils.getAttrColor(context, R.attr.colorSecondary)), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mTitle.setText(builder);
            } else {
                String title = teamone.getEllipsisdamage() + teamtwo.getEllipsisdamage() + teamthree.getEllipsisdamage() + "w" + " " + teamone.getBoss() + " " + teamtwo.getBoss() + " " + teamthree.getBoss();
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
        private View mAuto;
        private CharacterHolder mCharacterone;
        private CharacterHolder mCharactertwo;
        private CharacterHolder mCharacterthree;
        private CharacterHolder mCharacterfour;
        private CharacterHolder mCharacterfive;

        public TeamHolder(ViewGroup lay) {
            mTitle = lay.findViewById(R.id.title);
            mAuto = lay.findViewById(R.id.auto);
            mCharacterone = new CharacterHolder(lay.findViewById(R.id.characterone_lay), R.id.characterone_icon, R.id.characterone_name);
            mCharactertwo = new CharacterHolder(lay.findViewById(R.id.charactertwo_lay), R.id.charactertwo_icon, R.id.charactertwo_name);
            mCharacterthree = new CharacterHolder(lay.findViewById(R.id.characterthree_lay), R.id.characterthree_icon, R.id.characterthree_name);
            mCharacterfour = new CharacterHolder(lay.findViewById(R.id.characterfour_lay), R.id.characterfour_icon, R.id.characterfour_name);
            mCharacterfive = new CharacterHolder(lay.findViewById(R.id.characterfive_lay), R.id.characterfive_icon, R.id.characterfive_name);
        }

        public void setdata(TeamModel teamModel, TeamCustomizeModel teamCustomizeModel, List<Integer> idlist, int borrowindex) {
            if (teamCustomizeModel == null) {
                String title = teamModel.getNumber() + " " + teamModel.getEllipsisdamage() + "w";
                mTitle.setText(title);
            } else {
                String str = teamModel.getNumber();
                SpannableStringBuilder builder = new SpannableStringBuilder(str);
                if (teamCustomizeModel.isBlock()) {
                    builder.setSpan(new StrikethroughSpan(), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.setSpan(new ForegroundColorSpan(Utils.getColor(R.color.red_500)), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                builder.append(" ");
                int start = builder.length();
                if (teamCustomizeModel.damageEffective()) {
                    builder.append(teamCustomizeModel.getEllipsisdamage() + "w");
                    builder.setSpan(new ForegroundColorSpan(Utils.getAttrColor(context, R.attr.colorSecondary)), start, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    builder.append(teamModel.getEllipsisdamage() + "w");
                }
                mTitle.setText(builder);
            }
            if (teamModel.isAuto()) {
                mAuto.setVisibility(View.VISIBLE);
            } else {
                mAuto.setVisibility(View.INVISIBLE);
            }
            mCharacterone.setdata(idlist.get(0), borrowindex == 0);
            mCharactertwo.setdata(idlist.get(1), borrowindex == 1);
            mCharacterthree.setdata(idlist.get(2), borrowindex == 2);
            mCharacterfour.setdata(idlist.get(3), borrowindex == 3);
            mCharacterfive.setdata(idlist.get(4), borrowindex == 4);
        }
    }

    private class CharacterHolder {
        private CardView lay;
        private ImageView icon;
        private TextView name;

        public CharacterHolder(CardView lay, int iconid, int nameid) {
            this.lay = lay;
            icon = lay.findViewById(iconid);
            name = lay.findViewById(nameid);
        }

        public void setdata(int id, boolean borrow) {
            CharacterModel characterModel = CharacterHelper.findCharacterById(id, characterModels);
            if (borrow) {
                lay.setCardBackgroundColor(Utils.getAttrColor(context, R.attr.colorPrimary));
            } else {
                lay.setCardBackgroundColor(Utils.getColor(R.color.gray));
            }
            if (characterModel == null) {
                icon.setImageBitmap(null);
                name.setVisibility(View.VISIBLE);
                name.setText(id + "");
            } else {
                ImageRequester.request(characterModel.getIconUrl(), R.drawable.ic_character_default).loadImage(icon);
                name.setVisibility(View.INVISIBLE);
                name.setText("");
            }
        }
    }
}
