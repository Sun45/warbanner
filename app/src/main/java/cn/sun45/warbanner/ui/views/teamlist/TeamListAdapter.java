package cn.sun45.warbanner.ui.views.teamlist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.character.CharacterHelper;
import cn.sun45.warbanner.clanwar.ClanwarHelper;
import cn.sun45.warbanner.document.StaticHelper;
import cn.sun45.warbanner.document.db.clanwar.TeamCustomizeModel;
import cn.sun45.warbanner.document.db.clanwar.TeamModel;
import cn.sun45.warbanner.document.db.setup.ScreenCharacterModel;
import cn.sun45.warbanner.document.db.source.CharacterModel;
import cn.sun45.warbanner.document.preference.SetupPreference;
import cn.sun45.warbanner.framework.image.ImageRequester;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/5/20
 * 阵容列表Adapter
 */
public class TeamListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "TeamListAdapter";

    private Context context;

    private TeamListListener listener;

    private boolean showlink;
    private int stageSelection;
    private int bossSelection;
    private int typeSelection;

    private List<TeamListTeamModel> list;

    private List<TeamListBossModel> teamWithBossList;

    //阵容自定义信息
    private List<TeamCustomizeModel> teamCustomizeModels;

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

    public void setStageSelection(int stageSelection) {
        this.stageSelection = stageSelection;
    }

    public void setBossSelection(int bossSelection) {
        this.bossSelection = bossSelection;
    }

    public void setTypeSelection(int typeSelection) {
        this.typeSelection = typeSelection;
    }

    public void setList(List<TeamListTeamModel> list) {
        this.list = list;
    }

    public void setTeamWithBossList(List<TeamListBossModel> teamWithBossList) {
        this.teamWithBossList = teamWithBossList;
    }

    public void setTeamCustomizeModels(List<TeamCustomizeModel> teamCustomizeModels) {
        this.teamCustomizeModels = teamCustomizeModels;
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
        if (list != null) {
            return list.size();
        } else {
            int count = 0;
            if (teamWithBossList != null && !teamWithBossList.isEmpty()) {
                int position = 0;
                for (int stage = 1; stage <= StaticHelper.STAGE_COUNT; stage++) {
                    for (int boss = 1; boss <= StaticHelper.BOSS_COUNT; boss++, position++) {
                        if (stageSelection != 0 && stage != stageSelection) {
                            continue;
                        }
                        if (bossSelection != 0 && boss != bossSelection) {
                            continue;
                        }
                        count++;
                        count += teamWithBossList.get(position).getModelCount(typeSelection);
                    }
                }
            }
            Utils.logD(TAG, "getItemCount count:" + count);
            return count;
        }
    }

    private Object getItem(int position) {
        if (list != null) {
            return list.get(position);
        } else {
            int bossposition = 0;
            for (int stage = 1; stage <= StaticHelper.STAGE_COUNT; stage++) {
                for (int boss = 1; boss <= StaticHelper.BOSS_COUNT; boss++, bossposition++) {
                    if (stageSelection != 0 && stage != stageSelection) {
                        continue;
                    }
                    if (bossSelection != 0 && boss != bossSelection) {
                        continue;
                    }
                    TeamListBossModel teamListBossModel = teamWithBossList.get(bossposition);
                    if (position == 0) {
                        return teamListBossModel;
                    } else {
                        position--;
                        int count = teamListBossModel.getModelCount(typeSelection);
                        if (position < count) {
                            return teamListBossModel.getModel(typeSelection, position);
                        } else {
                            position -= count;
                        }
                    }
                }
            }
            return null;
        }
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
            int borrowindex = teamListTeamModel.getBorrowindex();
            List<TeamListRemarkModel> remarkModels = teamListTeamModel.getRemarkModels();
            mBoss.setText(teamModel.getBoss());
            TeamCustomizeModel teamCustomizeModel = ClanwarHelper.findCustomizeModel(teamModel, teamCustomizeModels);
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
            if (borrowindex == -1) {
                mCharacterone.setData(teamModel.getCharacterone());
                mCharactertwo.setData(teamModel.getCharactertwo());
                mCharacterthree.setData(teamModel.getCharacterthree());
                mCharacterfour.setData(teamModel.getCharacterfour());
                mCharacterfive.setData(teamModel.getCharacterfive());
            } else {
                mCharacterone.setData(teamModel.getCharacterone(), borrowindex == 0);
                mCharactertwo.setData(teamModel.getCharactertwo(), borrowindex == 1);
                mCharacterthree.setData(teamModel.getCharacterthree(), borrowindex == 2);
                mCharacterfour.setData(teamModel.getCharacterfour(), borrowindex == 3);
                mCharacterfive.setData(teamModel.getCharacterfive(), borrowindex == 4);
            }
            if (showlink) {
                if (remarkModels.isEmpty()) {
                    mRemarks.setVisibility(View.GONE);
                } else {
                    SpannableStringBuilder ssb = new SpannableStringBuilder();
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

        public void setData(int id, boolean borrow) {
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
