package cn.sun45.warbanner.ui.views.teamlist;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.document.db.clanwar.TeamModel;
import cn.sun45.warbanner.document.db.setup.ScreenCharacterModel;
import cn.sun45.warbanner.document.db.source.CharacterModel;
import cn.sun45.warbanner.framework.image.ImageRequester;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/5/20
 * 阵容列表Adapter
 */
public class TeamListAdapter extends RecyclerView.Adapter<TeamListAdapter.Holder> {
    private static final String TAG = "TeamListAdapter";

    public static final int SHOW_TYPE_ALL = 0;
    public static final int SHOW_TYPE_ONE = 1;
    public static final int SHOW_TYPE_TWO = 2;
    public static final int SHOW_TYPE_THREE = 3;

    private Context context;

    private int showtype;
    private List<TeamModel> onelist;
    private List<TeamModel> twolist;
    private List<TeamModel> threelist;
    private List<CharacterModel> characterModels;

    private boolean screenfunction;
    private List<ScreenCharacterModel> screenCharacterModels;

    public TeamListAdapter(Context context) {
        this.context = context;
    }

    public void setShowtype(int showtype) {
        this.showtype = showtype;
    }

    public void setList(List<TeamModel> list) {
        onelist = new ArrayList<>();
        twolist = new ArrayList<>();
        threelist = new ArrayList<>();
        for (TeamModel teamModel : list) {
            switch (teamModel.getStage()) {
                case 1:
                    onelist.add(teamModel);
                    break;
                case 2:
                    twolist.add(teamModel);
                    break;
                case 3:
                    threelist.add(teamModel);
                    break;
                default:
                    break;
            }
        }
    }

    public void setCharacterModels(List<CharacterModel> characterModels) {
        this.characterModels = characterModels;
    }

    public void setScreenfunction(boolean screenfunction) {
        this.screenfunction = screenfunction;
    }

    public void setScreenCharacterModels(List<ScreenCharacterModel> screenCharacterModels) {
        this.screenCharacterModels = screenCharacterModels;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.teamlist_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamListAdapter.Holder holder, int position) {
        holder.setData(getItem(position));
    }

    @Override
    public int getItemCount() {
        int count = 0;
        switch (showtype) {
            case SHOW_TYPE_ALL:
                if (onelist != null) {
                    count += onelist.size();
                }
                if (twolist != null) {
                    count += twolist.size();
                }
                if (threelist != null) {
                    count += threelist.size();
                }
                break;
            case SHOW_TYPE_ONE:
                count = onelist != null ? onelist.size() : 0;
                break;
            case SHOW_TYPE_TWO:
                count = twolist != null ? twolist.size() : 0;
                break;
            case SHOW_TYPE_THREE:
                count = threelist != null ? threelist.size() : 0;
                break;
            default:
                break;
        }
        Utils.logD(TAG, "getItemCount count:" + count);
        return count;
    }

    private TeamModel getItem(int position) {
        TeamModel teamModel = null;
        switch (showtype) {
            case SHOW_TYPE_ALL:
                int p = position;
                if (onelist != null && onelist.size() > p) {
                    teamModel = onelist.get(p);
                } else {
                    if (onelist != null) {
                        p -= onelist.size();
                    }
                    if (twolist != null && twolist.size() > p) {
                        teamModel = twolist.get(p);
                    } else {
                        if (twolist != null) {
                            p -= twolist.size();
                        }
                        teamModel = threelist.get(p);
                    }
                }
                break;
            case SHOW_TYPE_ONE:
                teamModel = onelist.get(position);
                break;
            case SHOW_TYPE_TWO:
                teamModel = twolist.get(position);
                break;
            case SHOW_TYPE_THREE:
                teamModel = threelist.get(position);
                break;
            default:
                break;
        }
        return teamModel;
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView mBoss;
        private TextView mTitle;
        private View mAuto;
        private CharacterHolder mCharacterone;
        private CharacterHolder mCharactertwo;
        private CharacterHolder mCharacterthree;
        private CharacterHolder mCharacterfour;
        private CharacterHolder mCharacterfive;

        public Holder(@NonNull View itemView) {
            super(itemView);
            mBoss = itemView.findViewById(R.id.boss);
            mTitle = itemView.findViewById(R.id.title);
            mAuto = itemView.findViewById(R.id.auto);
            mCharacterone = new CharacterHolder(itemView.findViewById(R.id.characterone_lay), R.id.characterone_icon, R.id.characterone_name);
            mCharactertwo = new CharacterHolder(itemView.findViewById(R.id.charactertwo_lay), R.id.charactertwo_icon, R.id.charactertwo_name);
            mCharacterthree = new CharacterHolder(itemView.findViewById(R.id.characterthree_lay), R.id.characterthree_icon, R.id.characterthree_name);
            mCharacterfour = new CharacterHolder(itemView.findViewById(R.id.characterfour_lay), R.id.characterfour_icon, R.id.characterfour_name);
            mCharacterfive = new CharacterHolder(itemView.findViewById(R.id.characterfive_lay), R.id.characterfive_icon, R.id.characterfive_name);
        }

        public void setData(TeamModel teamModel) {
            mBoss.setText(teamModel.getBoss());
            String title = teamModel.getNumber() + " " + teamModel.getDamage();
            mTitle.setText(title);
            if (teamModel.isAuto()) {
                mAuto.setVisibility(View.VISIBLE);
            } else {
                mAuto.setVisibility(View.INVISIBLE);
            }
            mCharacterone.setData(teamModel.getCharacterone());
            mCharactertwo.setData(teamModel.getCharactertwo());
            mCharacterthree.setData(teamModel.getCharacterthree());
            mCharacterfour.setData(teamModel.getCharacterfour());
            mCharacterfive.setData(teamModel.getCharacterfive());
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

        public void setData(String nickname) {
            CharacterModel characterModel = findCharacter(nickname);
            if (screenfunction) {
                boolean screen = false;
                if (screenCharacterModels != null && characterModel != null) {
                    for (ScreenCharacterModel screenCharacterModel : screenCharacterModels) {
                        if (screenCharacterModel.getId() == characterModel.getId()) {
                            screen = true;
                            break;
                        }
                    }
                }
                if (screen) {
                    lay.setCardBackgroundColor(Utils.getColor(R.color.red_dark));
                } else {
                    lay.setCardBackgroundColor(Utils.getColor(R.color.gray));
                }
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
}
