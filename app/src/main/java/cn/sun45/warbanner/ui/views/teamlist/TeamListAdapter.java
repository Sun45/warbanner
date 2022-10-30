package cn.sun45.warbanner.ui.views.teamlist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.character.CharacterHelper;
import cn.sun45.warbanner.clanwar.ClanwarHelper;
import cn.sun45.warbanner.document.database.setup.models.ScreenCharacterModel;
import cn.sun45.warbanner.document.database.setup.models.TeamCustomizeModel;
import cn.sun45.warbanner.document.database.source.models.CharacterModel;
import cn.sun45.warbanner.document.database.source.models.TeamModel;
import cn.sun45.warbanner.document.preference.SetupPreference;
import cn.sun45.warbanner.document.statics.StaticHelper;
import cn.sun45.warbanner.framework.image.ImageRequester;
import cn.sun45.warbanner.stage.StageManager;
import cn.sun45.warbanner.ui.views.character.characterview.CharacterView;
import cn.sun45.warbanner.ui.views.listselectbar.ListSelectItem;
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
                for (int stage = 1; stage <= StageManager.getInstance().getStageCount(); stage++) {
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
//            Utils.logD(TAG, "getItemCount count:" + count);
            return count;
        }
    }

    public List<ListSelectItem> getListSelectItemList() {
        if (list != null) {
            return null;
        } else {
            List<ListSelectItem> list = new ArrayList<>();
            int position = 0;
            int bossposition = 0;
            for (int stage = 1; stage <= StageManager.getInstance().getStageCount(); stage++) {
                for (int boss = 1; boss <= StaticHelper.BOSS_COUNT; boss++, bossposition++) {
                    if (stageSelection != 0 && stage != stageSelection) {
                        continue;
                    }
                    if (bossSelection != 0 && boss != bossSelection) {
                        continue;
                    }
                    TeamListBossModel teamListBossModel = teamWithBossList.get(bossposition);
                    ListSelectItem listSelectItem = new ListSelectItem(teamListBossModel.getIconUrl(), position);
                    list.add(listSelectItem);
                    position += (teamListBossModel.getModelCount(typeSelection) + 1);
                }
            }
            return list;
        }
    }

    private Object getItem(int position) {
        if (list != null) {
            return list.get(position);
        } else {
            int bossposition = 0;
            for (int stage = 1; stage <= StageManager.getInstance().getStageCount(); stage++) {
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
            ImageRequester.request(teamListBossModel.getIconUrl(), R.drawable.ic_character_default).loadRoundImage(icon);
            name.setText(teamListBossModel.getName());
        }
    }

    public class TeamHolder extends RecyclerView.ViewHolder {
        private TextView mBoss;
        private TextView mTitle;
        private CharacterView mCharacterone;
        private CharacterView mCharactertwo;
        private CharacterView mCharacterthree;
        private CharacterView mCharacterfour;
        private CharacterView mCharacterfive;
        private TextView mRemarks;

        private TeamListTeamModel model;

        public TeamHolder(@NonNull View itemView) {
            super(itemView);
            mBoss = itemView.findViewById(R.id.boss);
            mTitle = itemView.findViewById(R.id.title);
            mCharacterone = itemView.findViewById(R.id.characterone_lay);
            mCharactertwo = itemView.findViewById(R.id.charactertwo_lay);
            mCharacterthree = itemView.findViewById(R.id.characterthree_lay);
            mCharacterfour = itemView.findViewById(R.id.characterfour_lay);
            mCharacterfive = itemView.findViewById(R.id.characterfive_lay);
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
            if (borrowindex == -1) {
                characterDataSet(mCharacterone, teamModel.getCharacterone());
                characterDataSet(mCharactertwo, teamModel.getCharactertwo());
                characterDataSet(mCharacterthree, teamModel.getCharacterthree());
                characterDataSet(mCharacterfour, teamModel.getCharacterfour());
                characterDataSet(mCharacterfive, teamModel.getCharacterfive());
            } else {
                characterDataSet(mCharacterone, teamModel.getCharacterone(), borrowindex == 0);
                characterDataSet(mCharactertwo, teamModel.getCharactertwo(), borrowindex == 1);
                characterDataSet(mCharacterthree, teamModel.getCharacterthree(), borrowindex == 2);
                characterDataSet(mCharacterfour, teamModel.getCharacterfour(), borrowindex == 3);
                characterDataSet(mCharacterfive, teamModel.getCharacterfive(), borrowindex == 4);
            }
            if (showlink) {
                if (remarkModels.isEmpty()) {
                    mRemarks.setVisibility(View.GONE);
                } else {
                    SpannableStringBuilder ssb = new SpannableStringBuilder();
                    for (int i = 0; i < remarkModels.size(); i++) {
                        TeamListRemarkModel remarkModel = remarkModels.get(i);
                        String content = remarkModel.getContent();
                        String link = remarkModel.getLink();
                        if (i != 0) {
                            ssb.append("\n");
                        }
                        ssb.append(content);
                        if (!TextUtils.isEmpty(link)) {
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
                                        intent.putExtra(Intent.EXTRA_TEXT, model.getTeamModel().getSn() + " " + content + "\n" + link);
                                        intent.putExtra(Intent.EXTRA_SUBJECT, "share");
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.setType("text/plain");
                                        context.startActivity(Intent.createChooser(intent, Utils.getString(R.string.app_name)));
                                    }
                                }
                            }, ssb.length() - content.length(), ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                    mRemarks.setVisibility(View.VISIBLE);
                    mRemarks.setMovementMethod(LinkMovementMethod.getInstance());
                    mRemarks.setText(ssb, TextView.BufferType.SPANNABLE);
                }
            } else {
                mRemarks.setVisibility(View.GONE);
            }
        }

        private void characterDataSet(CharacterView characterView, int id) {
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
                            characterView.setBackGroundType(CharacterView.BG_YELLOW);
                            break;
                        case 2://TYPE_SKIP
                            characterView.setBackGroundType(CharacterView.BG_RED);
                            break;
                    }
                } else {
                    characterView.setBackGroundType(CharacterView.BG_DEFAULT);
                }
            } else {
                characterView.setBackGroundType(CharacterView.BG_DEFAULT);
            }
            characterView.setCharacterModel(characterModel, id);
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
}
