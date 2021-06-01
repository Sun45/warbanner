package cn.sun45.warbanner.ui.views.teamgrouplist;

import android.content.Context;
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

        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
            mCollection = itemView.findViewById(R.id.collection);
            mTeamOne = new TeamHolder(itemView.findViewById(R.id.teamone));
            mTeamTwo = new TeamHolder(itemView.findViewById(R.id.teamtwo));
            mTeamThree = new TeamHolder(itemView.findViewById(R.id.teamthree));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.setCollected(!model.isCollected());
                    showCollection();
                    if (listener != null) {
                        listener.collect(model);
                    }
                }
            });
        }

        public void setData(TeamGroupListModel teamGroupListModel) {
            model = teamGroupListModel;
            TeamModel teamone = teamGroupListModel.getTeamone();
            TeamModel teamtwo = teamGroupListModel.getTeamtwo();
            TeamModel teamthree = teamGroupListModel.getTeamthree();
            String title = teamGroupListModel.getTotaldamage() + "w" + " " + teamone.getBoss() + " " + teamtwo.getBoss() + " " + teamthree.getBoss();
            mTitle.setText(title);
            showCollection();
            mTeamOne.setdata(teamGroupListModel.getTeamone(), teamGroupListModel.getIdlistone(), teamGroupListModel.getBorrowindexone());
            mTeamTwo.setdata(teamGroupListModel.getTeamtwo(), teamGroupListModel.getIdlisttwo(), teamGroupListModel.getBorrowindextwo());
            mTeamThree.setdata(teamGroupListModel.getTeamthree(), teamGroupListModel.getIdlistthree(), teamGroupListModel.getBorrowindexthree());
        }

        private void showCollection() {
            if (model.isCollected()) {
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
            this.mTitle = lay.findViewById(R.id.title);
            mAuto = lay.findViewById(R.id.auto);
            this.mCharacterone = new CharacterHolder(lay.findViewById(R.id.characterone_lay), R.id.characterone_icon, R.id.characterone_name);
            this.mCharactertwo = new CharacterHolder(lay.findViewById(R.id.charactertwo_lay), R.id.charactertwo_icon, R.id.charactertwo_name);
            this.mCharacterthree = new CharacterHolder(lay.findViewById(R.id.characterthree_lay), R.id.characterthree_icon, R.id.characterthree_name);
            this.mCharacterfour = new CharacterHolder(lay.findViewById(R.id.characterfour_lay), R.id.characterfour_icon, R.id.characterfour_name);
            this.mCharacterfive = new CharacterHolder(lay.findViewById(R.id.characterfive_lay), R.id.characterfive_icon, R.id.characterfive_name);
        }

        public void setdata(TeamModel teamModel, List<Integer> idlist, int borrowindex) {
            mTitle.setText(teamModel.getNumber() + " " + teamModel.getDamage());
            if (teamModel.isAuto()) {
                mAuto.setVisibility(View.VISIBLE);
            } else {
                mAuto.setVisibility(View.INVISIBLE);
            }
            mCharacterone.setdata(teamModel.getCharacterone(), idlist.get(0), borrowindex == 0);
            mCharactertwo.setdata(teamModel.getCharacterone(), idlist.get(1), borrowindex == 1);
            mCharacterthree.setdata(teamModel.getCharacterone(), idlist.get(2), borrowindex == 2);
            mCharacterfour.setdata(teamModel.getCharacterone(), idlist.get(3), borrowindex == 3);
            mCharacterfive.setdata(teamModel.getCharacterone(), idlist.get(4), borrowindex == 4);
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

        public void setdata(String nickname, int id, boolean borrow) {
            CharacterModel characterModel = findCharacter(id);
            if (borrow) {
                lay.setCardBackgroundColor(Utils.getColor(R.color.red_dark));
            } else {
                lay.setCardBackgroundColor(Utils.getColor(R.color.gray));
            }
            if (characterModel == null) {
                icon.setImageBitmap(null);
                name.setVisibility(View.VISIBLE);
                name.setText(nickname);
            } else {
                ImageRequester.request(characterModel.getIconUrl(), R.drawable.ic_character_default).loadImage(icon);
                name.setVisibility(View.INVISIBLE);
                name.setText("");
            }
        }

        /**
         * 根据id获取角色信息
         *
         * @param id 角色id
         */
        private CharacterModel findCharacter(int id) {
            CharacterModel characterModel = null;
            if (characterModels != null && !characterModels.isEmpty()) {
                for (CharacterModel model : characterModels) {
                    if (model.getId() == (id)) {
                        characterModel = model;
                        break;
                    }
                }
            }
            return characterModel;
        }
    }
}
