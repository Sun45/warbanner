package cn.sun45.warbanner.ui.views.teamlist;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.document.db.clanwar.TeamModel;
import cn.sun45.warbanner.document.db.source.CharacterModel;
import cn.sun45.warbanner.framework.image.ImageRequester;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/5/20
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
        private View mCharacteroneLay;
        private ImageView mCharacteroneImage;
        private TextView mCharacteroneText;
        private View mCharactertwoLay;
        private ImageView mCharactertwoImage;
        private TextView mCharactertwoText;
        private View mCharacterthreeLay;
        private ImageView mCharacterthreeImage;
        private TextView mCharacterthreeText;
        private View mCharacterfourLay;
        private ImageView mCharacterfourImage;
        private TextView mCharacterfourText;
        private View mCharacterfiveLay;
        private ImageView mCharacterfiveImage;
        private TextView mCharacterfiveText;

        public Holder(@NonNull View itemView) {
            super(itemView);
            mBoss = itemView.findViewById(R.id.boss);
            mTitle = itemView.findViewById(R.id.title);
            mCharacteroneLay = itemView.findViewById(R.id.characterone_lay);
            mCharacteroneImage = itemView.findViewById(R.id.characterone_image);
            mCharacteroneText = itemView.findViewById(R.id.characterone_text);
            mCharactertwoLay = itemView.findViewById(R.id.charactertwo_lay);
            mCharactertwoImage = itemView.findViewById(R.id.charactertwo_image);
            mCharactertwoText = itemView.findViewById(R.id.charactertwo_text);
            mCharacterthreeLay = itemView.findViewById(R.id.characterthree_lay);
            mCharacterthreeImage = itemView.findViewById(R.id.characterthree_image);
            mCharacterthreeText = itemView.findViewById(R.id.characterthree_text);
            mCharacterfourLay = itemView.findViewById(R.id.characterfour_lay);
            mCharacterfourImage = itemView.findViewById(R.id.characterfour_image);
            mCharacterfourText = itemView.findViewById(R.id.characterfour_text);
            mCharacterfiveLay = itemView.findViewById(R.id.characterfive_lay);
            mCharacterfiveImage = itemView.findViewById(R.id.characterfive_image);
            mCharacterfiveText = itemView.findViewById(R.id.characterfive_text);
        }

        public void setData(TeamModel teamModel) {
            mBoss.setText(teamModel.getBoss());
            String title = teamModel.getNumber() + " " + teamModel.getDamage();
            String remarkstr = teamModel.getRemarks();
            if (!TextUtils.isEmpty(remarkstr)) {
                try {
                    JSONArray remarks = new JSONArray(remarkstr);
                    for (int i = 0; i < remarks.length(); i++) {
                        title += " " + remarks.getJSONObject(i).get("content");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mTitle.setText(title);
            if (buildCharacterIcon(teamModel.getCharacterone(), mCharacteroneImage)) {
                mCharacteroneText.setVisibility(View.VISIBLE);
                mCharacteroneText.setText(teamModel.getCharacterone());
            } else {
                mCharacteroneText.setVisibility(View.INVISIBLE);
                mCharacteroneText.setText("");
            }
            if (buildCharacterIcon(teamModel.getCharactertwo(), mCharactertwoImage)) {
                mCharactertwoText.setVisibility(View.VISIBLE);
                mCharactertwoText.setText(teamModel.getCharactertwo());
            } else {
                mCharactertwoText.setVisibility(View.INVISIBLE);
                mCharactertwoText.setText("");
            }
            if (buildCharacterIcon(teamModel.getCharacterthree(), mCharacterthreeImage)) {
                mCharacterthreeText.setVisibility(View.VISIBLE);
                mCharacterthreeText.setText(teamModel.getCharacterthree());
            } else {
                mCharacterthreeText.setVisibility(View.INVISIBLE);
                mCharacterthreeText.setText("");
            }
            if (buildCharacterIcon(teamModel.getCharacterfour(), mCharacterfourImage)) {
                mCharacterfourText.setVisibility(View.VISIBLE);
                mCharacterfourText.setText(teamModel.getCharacterfour());
            } else {
                mCharacterfourText.setVisibility(View.INVISIBLE);
                mCharacterfourText.setText("");
            }
            if (buildCharacterIcon(teamModel.getCharacterfive(), mCharacterfiveImage)) {
                mCharacterfiveText.setVisibility(View.VISIBLE);
                mCharacterfiveText.setText(teamModel.getCharacterfive());
            } else {
                mCharacterfiveText.setVisibility(View.INVISIBLE);
                mCharacterfiveText.setText("");
            }
        }
    }

    /**
     * 根据昵称记载角色头像
     *
     * @param nickname  角色昵称
     * @param imageView imageview
     * @return {@code true} 记载不到头像数据
     */
    private boolean buildCharacterIcon(String nickname, ImageView imageView) {
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
        if (characterModel != null) {
            ImageRequester.request(characterModel.getIconUrl()).loadImage(imageView);
            return false;
        } else {
            imageView.setImageBitmap(null);
            return true;
        }
    }
}
