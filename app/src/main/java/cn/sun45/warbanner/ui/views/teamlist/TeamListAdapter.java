package cn.sun45.warbanner.ui.views.teamlist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import java.util.Arrays;
import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.character.CharacterHelper;
import cn.sun45.warbanner.document.db.clanwar.TeamModel;
import cn.sun45.warbanner.document.db.setup.ScreenCharacterModel;
import cn.sun45.warbanner.document.db.source.CharacterModel;
import cn.sun45.warbanner.document.preference.SetupPreference;
import cn.sun45.warbanner.framework.image.ImageRequester;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupListListener;
import cn.sun45.warbanner.ui.views.teamgrouplist.TeamGroupListModel;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/5/20
 * 阵容列表Adapter
 */
public class TeamListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "TeamListAdapter";

    public static final int SHOW_TYPE_ALL = 0;
    public static final int SHOW_TYPE_ONE = 1;
    public static final int SHOW_TYPE_TWO = 2;
    public static final int SHOW_TYPE_THREE = 3;

    private Context context;

    private TeamListListener listener;

    private boolean showlink;
    private int autoScreen;
    private int showtype;

    private List<Object> onelist;
    private List<Object> twolist;
    private List<Object> threelist;

    //角色信息
    private List<CharacterModel> characterModels;

    //筛选信息
    private boolean screenfunction;
    private List<ScreenCharacterModel> screenCharacterModels;

    public TeamListAdapter(Context context) {
        this.context = context;
    }

    public void setListener(TeamListListener listener) {
        this.listener = listener;
    }

    public void setShowlink(boolean showlink) {
        this.showlink = showlink;
    }

    public void setAutoScreen(int autoScreen) {
        this.autoScreen = autoScreen;
    }

    public void setShowtype(int showtype) {
        this.showtype = showtype;
    }

    public void setList(List<Object> onelist, List<Object> twolist, List<Object> threelist) {
        this.onelist = onelist;
        this.twolist = twolist;
        this.threelist = threelist;
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

    @Override
    public int getItemViewType(int position) {
        Object item = getItem(position);
        return item instanceof TeamListBossModel ? 0 : 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case 0:
                holder = new BossHolder(LayoutInflater.from(context).inflate(R.layout.teamlist_bossitem, parent, false));
                break;
            case 1:
                holder = new TeamHolder(LayoutInflater.from(context).inflate(R.layout.teamlist_teamitem, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                ((BossHolder) holder).setData((TeamListBossModel) getItem(position));
                break;
            case 1:
                ((TeamHolder) holder).setData((TeamListTeamModel) getItem(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        switch (showtype) {
            case SHOW_TYPE_ALL:
                if (onelist != null) {
                    count += countList(onelist);
                }
                if (twolist != null) {
                    count += countList(twolist);
                }
                if (threelist != null) {
                    count += countList(threelist);
                }
                break;
            case SHOW_TYPE_ONE:
                count = countList(onelist);
                break;
            case SHOW_TYPE_TWO:
                count = countList(twolist);
                break;
            case SHOW_TYPE_THREE:
                count = countList(threelist);
                break;
            default:
                break;
        }
        Utils.logD(TAG, "getItemCount count:" + count);
        return count;
    }

    private int countList(List<Object> list) {
        int count = 0;
        if (list != null) {
            switch (autoScreen) {
                case 0:
                    count = list.size();
                    break;
                case 1:
                    for (Object object : list) {
                        if (object instanceof TeamListTeamModel) {
                            if (!((TeamListTeamModel) object).getTeamModel().isAuto()) {
                                continue;
                            }
                        }
                        count++;
                    }
                    break;
                case 2:
                    for (Object object : list) {
                        if (object instanceof TeamListTeamModel) {
                            if (((TeamListTeamModel) object).getTeamModel().isAuto()) {
                                continue;
                            }
                        }
                        count++;
                    }
                    break;
            }
        }
        Utils.logD(TAG, "countList count:" + count);
        return count;
    }

    private Object getItem(int position) {
        Object item = null;
        switch (showtype) {
            case SHOW_TYPE_ALL:
                int p = position;
                int onelistcount = countList(onelist);
                if (onelist != null && onelistcount > p) {
                    item = getItemFromList(onelist, p);
                } else {
                    p -= onelistcount;
                    int twolistcount = countList(twolist);
                    if (twolist != null && twolistcount > p) {
                        item = getItemFromList(twolist, p);
                    } else {
                        p -= twolistcount;
                        item = getItemFromList(threelist, p);
                    }
                }
                break;
            case SHOW_TYPE_ONE:
                item = getItemFromList(onelist, position);
                break;
            case SHOW_TYPE_TWO:
                item = getItemFromList(twolist, position);
                break;
            case SHOW_TYPE_THREE:
                item = getItemFromList(threelist, position);
                break;
            default:
                break;
        }
        return item;
    }

    private Object getItemFromList(List<Object> list, int position) {
        int p = 0;
        Object item = null;
        switch (autoScreen) {
            case 0:
                item = list.get(position);
                break;
            case 1:
                for (int i = 0; i < list.size(); i++) {
                    Object object = list.get(i);
                    if (object instanceof TeamListTeamModel) {
                        if (!((TeamListTeamModel) object).getTeamModel().isAuto()) {
                            continue;
                        }
                    }
                    if (position == p) {
                        item = object;
                        break;
                    }
                    p++;
                }
                break;
            case 2:
                for (int i = 0; i < list.size(); i++) {
                    Object object = list.get(i);
                    if (object instanceof TeamListTeamModel) {
                        if (((TeamListTeamModel) object).getTeamModel().isAuto()) {
                            continue;
                        }
                    }
                    if (position == p) {
                        item = object;
                        break;
                    }
                    p++;
                }
                break;
        }
        return item;
    }

    public class BossHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView name;

        public BossHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.boss_icon);
            name = itemView.findViewById(R.id.boss_name);
        }

        public void setData(TeamListBossModel teamListBossModel) {
            ImageRequester.request(teamListBossModel.getIconUrl(), R.drawable.ic_character_default).loadImage(icon);
            name.setText(teamListBossModel.getName());
        }
    }

    public class TeamHolder extends RecyclerView.ViewHolder {
        private TextView mBoss;
        private TextView mTitle;
        private View mAuto;
        private CharacterHolder mCharacterone;
        private CharacterHolder mCharactertwo;
        private CharacterHolder mCharacterthree;
        private CharacterHolder mCharacterfour;
        private CharacterHolder mCharacterfive;
        private TextView mRemarks;

        private TeamListTeamModel model;

        public TeamHolder(@NonNull View itemView) {
            super(itemView);
            mBoss = itemView.findViewById(R.id.boss);
            mTitle = itemView.findViewById(R.id.title);
            mAuto = itemView.findViewById(R.id.auto);
            mCharacterone = new CharacterHolder(itemView.findViewById(R.id.characterone_lay), R.id.characterone_icon, R.id.characterone_name);
            mCharactertwo = new CharacterHolder(itemView.findViewById(R.id.charactertwo_lay), R.id.charactertwo_icon, R.id.charactertwo_name);
            mCharacterthree = new CharacterHolder(itemView.findViewById(R.id.characterthree_lay), R.id.characterthree_icon, R.id.characterthree_name);
            mCharacterfour = new CharacterHolder(itemView.findViewById(R.id.characterfour_lay), R.id.characterfour_icon, R.id.characterfour_name);
            mCharacterfive = new CharacterHolder(itemView.findViewById(R.id.characterfive_lay), R.id.characterfive_icon, R.id.characterfive_name);
            mRemarks = itemView.findViewById(R.id.remarks);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.select(model.getTeamModel());
                    }
                }
            });
        }

        public void setData(TeamListTeamModel teamListTeamModel) {
            model = teamListTeamModel;
            TeamModel teamModel = teamListTeamModel.getTeamModel();
            List<TeamListRemarkModel> remarkModels = teamListTeamModel.getRemarkModels();
            mBoss.setText(teamModel.getBoss());
            String title = teamModel.getNumber() + " " + teamModel.getEllipsisdamage() + "w";
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
            if (showlink) {
                if (remarkModels.isEmpty()) {
                    mRemarks.setVisibility(View.GONE);
                } else {
                    SpannableString spanStr = new SpannableString("");
                    SpannableStringBuilder ssb = new SpannableStringBuilder(spanStr);
                    for (int i = 0; i < remarkModels.size(); i++) {
                        if (i != 0) {
                            ssb.append("\n");
                        }
                        TeamListRemarkModel remarkModel = remarkModels.get(i);
                        String content = remarkModel.getContent();
                        String link = remarkModel.getLink();
                        ssb.append(content);
                        ssb.setSpan(new ClickableSpan() {
                            @Override
                            public void onClick(@NonNull View widget) {
                                if (new SetupPreference().getLinkopentype() == 0) {
                                    Uri uri = Uri.parse(link);
                                    Intent intent = new Intent();
                                    intent.setAction("android.intent.action.VIEW");
                                    intent.setData(uri);
                                    context.startActivity(intent);
                                } else {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_SEND);
                                    intent.putExtra(Intent.EXTRA_TEXT, model.getTeamModel().getNumber() + " " + content + "\n" + link);
                                    intent.putExtra(Intent.EXTRA_SUBJECT, "share");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setType("text/plain");
                                    context.startActivity(Intent.createChooser(intent, Utils.getString(R.string.app_name)));
                                }
                            }
                        }, ssb.length() - content.length(), ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    mRemarks.setVisibility(View.VISIBLE);
                    mRemarks.setMovementMethod(LinkMovementMethod.getInstance());
                    mRemarks.setText(ssb, TextView.BufferType.SPANNABLE);
                }
            } else {
                mRemarks.setVisibility(View.GONE);
            }
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

        public void setData(int id) {
            CharacterModel characterModel = CharacterHelper.findCharacterById(id, characterModels);
            if (screenfunction) {
                ScreenCharacterModel screenCharacterModel = null;
                if (screenCharacterModels != null && characterModel != null) {
                    for (ScreenCharacterModel model : screenCharacterModels) {
                        if (model.getCharacterId() == characterModel.getId()) {
                            screenCharacterModel = model;
                            break;
                        }
                    }
                }
                if (screenCharacterModel != null) {
                    switch (screenCharacterModel.getType()) {
                        case 1://TYPE_LACK
                            lay.setCardBackgroundColor(Utils.getAttrColor(context, R.attr.colorSecondary));
                            break;
                        case 2://TYPE_SKIP
                            lay.setCardBackgroundColor(Utils.getAttrColor(context, R.attr.colorPrimary));
                            break;
                    }
                } else {
                    lay.setCardBackgroundColor(Utils.getColor(R.color.gray));
                }
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
