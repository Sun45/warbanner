package cn.sun45.warbanner.ui.fragments.teamgroup;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.character.CharacterHelper;
import cn.sun45.warbanner.clanwar.ClanwarHelper;
import cn.sun45.warbanner.document.database.setup.models.TeamGroupScreenModel;
import cn.sun45.warbanner.document.database.source.models.CharacterModel;
import cn.sun45.warbanner.document.statics.charactertype.CharacterScreenType;
import cn.sun45.warbanner.framework.ui.BaseActivity;
import cn.sun45.warbanner.framework.ui.BaseFragment;
import cn.sun45.warbanner.stage.StageManager;
import cn.sun45.warbanner.ui.shared.SharedViewModelSource;
import cn.sun45.warbanner.ui.shared.SharedViewModelTeamScreenTeam;
import cn.sun45.warbanner.ui.views.character.CharacterScroll;
import cn.sun45.warbanner.ui.views.character.characterview.CharacterView;
import cn.sun45.warbanner.ui.views.selectgroup.SelectGroup;
import cn.sun45.warbanner.util.Utils;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * Created by Sun45 on 2021/7/3
 * 分刀筛选Fragment
 */
public class TeamGroupScreenFragment extends BaseFragment {
    private TeamGroupScreenModel teamGroupScreenModel;

    private SharedViewModelSource sharedSource;
    private SharedViewModelTeamScreenTeam sharedTeamScreenTeam;

    private TeamHolder mTeamOne;
    private TeamHolder mTeamTwo;
    private TeamHolder mTeamThree;

    private CharacterScroll mCharacterScroll;
    private AppCompatImageView mUsedCharacterSetting;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_teamgroupscreen;
    }

    @Override
    protected void initData() {
        setHasOptionsMenu(true);
        if (teamGroupScreenModel == null) {
            teamGroupScreenModel = ClanwarHelper.getScreenModel();
        }
    }

    @Override
    protected void initView() {
        MaterialToolbar toolbar = mRoot.findViewById(R.id.drop_toolbar);
        ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });

        mTeamOne = new TeamHolder(1, mRoot.findViewById(R.id.teamone));
        mTeamTwo = new TeamHolder(2, mRoot.findViewById(R.id.teamtwo));
        mTeamThree = new TeamHolder(3, mRoot.findViewById(R.id.teamthree));

        mCharacterScroll = mRoot.findViewById(R.id.charactershow_scroll);
        mUsedCharacterSetting = mRoot.findViewById(R.id.used_character_setting);
        mUsedCharacterSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(getView());
                controller.navigate(R.id.action_nav_teamgroupscreen_to_nav_teamgroupscreenusedcharacter);
            }
        });
    }

    @Override
    protected void dataRequest() {
        sharedSource = new ViewModelProvider(requireActivity()).get(SharedViewModelSource.class);
        sharedTeamScreenTeam = new ViewModelProvider(requireActivity()).get(SharedViewModelTeamScreenTeam.class);

        mTeamOne.setData();
        mTeamTwo.setData();
        mTeamThree.setData();
        sharedSource.characterList.observe(requireActivity(), characterModelList -> showUsedCharacter());
        sharedTeamScreenTeam.teamOne.observe(requireActivity(), teamModel -> {
            if (teamModel != null) {
                int stage = teamModel.getStage() - 1;
                int boss = Integer.valueOf(teamModel.getBoss().substring(1, 2)) - 1;
                int auto = 2;
                if (teamModel.isAuto()) {
                    auto = 1;
                } else if (teamModel.isFinish()) {
                    auto = 3;
                }
                teamGroupScreenModel.setTeamonestage(stage);
                teamGroupScreenModel.setTeamoneboss(boss);
                teamGroupScreenModel.setTeamoneauto(auto);
                teamGroupScreenModel.setTeamonecharacteroneid(teamModel.getCharacterone());
                teamGroupScreenModel.setTeamonecharactertwoid(teamModel.getCharactertwo());
                teamGroupScreenModel.setTeamonecharacterthreeid(teamModel.getCharacterthree());
                teamGroupScreenModel.setTeamonecharacterfourid(teamModel.getCharacterfour());
                teamGroupScreenModel.setTeamonecharacterfiveid(teamModel.getCharacterfive());
                mTeamOne.setData();
                sharedTeamScreenTeam.teamOne.postValue(null);
            }
        });
        sharedTeamScreenTeam.teamTwo.observe(requireActivity(), teamModel -> {
            if (teamModel != null) {
                int stage = teamModel.getStage() - 1;
                int boss = Integer.valueOf(teamModel.getBoss().substring(1, 2)) - 1;
                int auto = teamModel.isFinish() ? 3 : teamModel.isAuto() ? 1 : 2;
                teamGroupScreenModel.setTeamtwostage(stage);
                teamGroupScreenModel.setTeamtwoboss(boss);
                teamGroupScreenModel.setTeamtwoauto(auto);
                teamGroupScreenModel.setTeamtwocharacteroneid(teamModel.getCharacterone());
                teamGroupScreenModel.setTeamtwocharactertwoid(teamModel.getCharactertwo());
                teamGroupScreenModel.setTeamtwocharacterthreeid(teamModel.getCharacterthree());
                teamGroupScreenModel.setTeamtwocharacterfourid(teamModel.getCharacterfour());
                teamGroupScreenModel.setTeamtwocharacterfiveid(teamModel.getCharacterfive());
                mTeamTwo.setData();
                sharedTeamScreenTeam.teamTwo.postValue(null);
            }
        });
        sharedTeamScreenTeam.teamThree.observe(requireActivity(), teamModel -> {
            if (teamModel != null) {
                int stage = teamModel.getStage() - 1;
                int boss = Integer.valueOf(teamModel.getBoss().substring(1, 2)) - 1;
                int auto = teamModel.isAuto() ? 1 : 2;
                teamGroupScreenModel.setTeamthreestage(stage);
                teamGroupScreenModel.setTeamthreeboss(boss);
                teamGroupScreenModel.setTeamthreeauto(auto);
                teamGroupScreenModel.setTeamthreecharacteroneid(teamModel.getCharacterone());
                teamGroupScreenModel.setTeamthreecharactertwoid(teamModel.getCharactertwo());
                teamGroupScreenModel.setTeamthreecharacterthreeid(teamModel.getCharacterthree());
                teamGroupScreenModel.setTeamthreecharacterfourid(teamModel.getCharacterfour());
                teamGroupScreenModel.setTeamthreecharacterfiveid(teamModel.getCharacterfive());
                mTeamThree.setData();
                sharedTeamScreenTeam.teamThree.postValue(null);
            }
        });
    }

    @Override
    protected void onShow() {
    }

    @Override
    protected void onHide() {
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fragment_teamgroupscreen_drop_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_reset:
                MaterialDialog dialog = new MaterialDialog(getContext(), MaterialDialog.getDEFAULT_BEHAVIOR());
                dialog.title(R.string.teamgroup_screen_menu_reset_dialog_title, null);
                dialog.message(R.string.teamgroup_screen_menu_reset_dialog_message, null, null);
                dialog.positiveButton(R.string.teamgroup_screen_menu_reset_dialog_confirm, null, new Function1<MaterialDialog, Unit>() {
                    @Override
                    public Unit invoke(MaterialDialog materialDialog) {
                        reset();
                        return null;
                    }
                });
                dialog.show();
                break;
        }
        return true;
    }

    private void reset() {
        sharedTeamScreenTeam.teamOne.postValue(null);
        teamGroupScreenModel.setTeamonecharacteroneid(0);
        teamGroupScreenModel.setTeamonecharactertwoid(0);
        teamGroupScreenModel.setTeamonecharacterthreeid(0);
        teamGroupScreenModel.setTeamonecharacterfourid(0);
        teamGroupScreenModel.setTeamonecharacterfiveid(0);
        teamGroupScreenModel.setTeamoneborrowindex(-1);
        teamGroupScreenModel.setTeamoneextra(false);
        teamGroupScreenModel.setTeamoneenable(true);
        mTeamOne.setData();
        sharedTeamScreenTeam.teamTwo.postValue(null);
        teamGroupScreenModel.setTeamtwocharacteroneid(0);
        teamGroupScreenModel.setTeamtwocharactertwoid(0);
        teamGroupScreenModel.setTeamtwocharacterthreeid(0);
        teamGroupScreenModel.setTeamtwocharacterfourid(0);
        teamGroupScreenModel.setTeamtwocharacterfiveid(0);
        teamGroupScreenModel.setTeamtwoborrowindex(-1);
        teamGroupScreenModel.setTeamtwoextra(false);
        teamGroupScreenModel.setTeamtwoenable(true);
        mTeamTwo.setData();
        sharedTeamScreenTeam.teamThree.postValue(null);
        teamGroupScreenModel.setTeamthreecharacteroneid(0);
        teamGroupScreenModel.setTeamthreecharactertwoid(0);
        teamGroupScreenModel.setTeamthreecharacterthreeid(0);
        teamGroupScreenModel.setTeamthreecharacterfourid(0);
        teamGroupScreenModel.setTeamthreecharacterfiveid(0);
        teamGroupScreenModel.setTeamthreeborrowindex(-1);
        teamGroupScreenModel.setTeamthreeextra(false);
        teamGroupScreenModel.setTeamthreeenable(true);
        mTeamThree.setData();
        CharacterHelper.clearUsedCharacterList();
        showUsedCharacter();
    }

    private void showUsedCharacter() {
        mCharacterScroll.showUsedCharacters(sharedSource.characterList.getValue());
    }

    private class TeamHolder {
        public int team;
        private MaterialCardView mLay;
        private SelectGroup mStage;
        private SelectGroup mBoss;
        private SelectGroup mAuto;
        private ViewGroup mCharacterLay;
        private CharacterView mCharacterone;
        private CharacterView mCharactertwo;
        private CharacterView mCharacterthree;
        private CharacterView mCharacterfour;
        private CharacterView mCharacterfive;
        private AppCompatImageView mClean;
        private AppCompatImageView mSetting;
        public TextView mExtra;

        public TeamHolder(int team, MaterialCardView lay) {
            this.team = team;
            mLay = lay;
            mStage = lay.findViewById(R.id.stage);
            mBoss = lay.findViewById(R.id.boss);
            mAuto = lay.findViewById(R.id.auto);
            mCharacterLay = lay.findViewById(R.id.character_lay);
            mCharacterone = lay.findViewById(R.id.characterone_lay);
            mCharactertwo = lay.findViewById(R.id.charactertwo_lay);
            mCharacterthree = lay.findViewById(R.id.characterthree_lay);
            mCharacterfour = lay.findViewById(R.id.characterfour_lay);
            mCharacterfive = lay.findViewById(R.id.characterfive_lay);
            mClean = lay.findViewById(R.id.clean);
            mSetting = lay.findViewById(R.id.setting);
            mExtra = lay.findViewById(R.id.extra);

            mLay.setOnClickListener(v -> {
                boolean enable = getEnable();
                enable = !enable;
                int enablecount = 0;
                if (teamGroupScreenModel.isTeamoneenable()) {
                    enablecount++;
                }
                if (teamGroupScreenModel.isTeamtwoenable()) {
                    enablecount++;
                }
                if (teamGroupScreenModel.isTeamthreeenable()) {
                    enablecount++;
                }
                if (enablecount == 1) {
                    if (!enable) {
                        return;
                    }
                }
                setEnable(enable);
            });
            mStage.setListener(position -> setStage(position));
            mBoss.setListener(position -> setBoss(position));
            mAuto.setListener(position -> setAuto(position));
            mCharacterLay.setOnClickListener(v -> {
            });
            mCharacterone.setOnClickListener(v -> {
                characterClick(0);
            });
            mCharactertwo.setOnClickListener(v -> {
                characterClick(1);
            });
            mCharacterthree.setOnClickListener(v -> {
                characterClick(2);
            });
            mCharacterfour.setOnClickListener(v -> {
                characterClick(3);
            });
            mCharacterfive.setOnClickListener(v -> {
                characterClick(4);
            });
            mClean.setOnClickListener(v -> {
                switch (team) {
                    case 1:
                        sharedTeamScreenTeam.teamOne.postValue(null);
                        teamGroupScreenModel.setTeamonecharacteroneid(0);
                        teamGroupScreenModel.setTeamonecharactertwoid(0);
                        teamGroupScreenModel.setTeamonecharacterthreeid(0);
                        teamGroupScreenModel.setTeamonecharacterfourid(0);
                        teamGroupScreenModel.setTeamonecharacterfiveid(0);
                        teamGroupScreenModel.setTeamoneborrowindex(-1);
                        break;
                    case 2:
                        sharedTeamScreenTeam.teamTwo.postValue(null);
                        teamGroupScreenModel.setTeamtwocharacteroneid(0);
                        teamGroupScreenModel.setTeamtwocharactertwoid(0);
                        teamGroupScreenModel.setTeamtwocharacterthreeid(0);
                        teamGroupScreenModel.setTeamtwocharacterfourid(0);
                        teamGroupScreenModel.setTeamtwocharacterfiveid(0);
                        teamGroupScreenModel.setTeamtwoborrowindex(-1);
                        break;
                    case 3:
                        sharedTeamScreenTeam.teamThree.postValue(null);
                        teamGroupScreenModel.setTeamthreecharacteroneid(0);
                        teamGroupScreenModel.setTeamthreecharactertwoid(0);
                        teamGroupScreenModel.setTeamthreecharacterthreeid(0);
                        teamGroupScreenModel.setTeamthreecharacterfourid(0);
                        teamGroupScreenModel.setTeamthreecharacterfiveid(0);
                        teamGroupScreenModel.setTeamthreeborrowindex(-1);
                        break;
                }
                setCharacterData();
            });
            mSetting.setOnClickListener(v -> {
                NavController controller = Navigation.findNavController(getView());
                Bundle bundle = new Bundle();
                bundle.putSerializable("team", team);
                controller.navigate(R.id.action_nav_teamgroupscreen_to_nav_teamselect, bundle);
            });
            mExtra.setOnClickListener(v -> {
                boolean extra = getExtra();
                extra = !extra;
                setExtra(extra);
                if (extra) {
                    if (mTeamOne.team != team) {
                        mTeamOne.setExtra(false);
                    }
                    if (mTeamTwo.team != team) {
                        mTeamTwo.setExtra(false);
                    }
                    if (mTeamThree.team != team) {
                        mTeamThree.setExtra(false);
                    }
                }
            });
        }

        public void setData() {
            mStage.setData(StageManager.getInstance().getStageDescriptionList(), getStage());
            mBoss.setData(Utils.getStringArray(R.array.teamgroup_screen_boss_options), getBoss());
            mAuto.setData(Utils.getStringArray(R.array.teamgroup_screen_auto_options), getAuto());
            setCharacterData();
            setExtra(getExtra());
            setEnable(getEnable());
        }

        private void setCharacterData() {
            switch (team) {
                case 1:
                    int teamoneborrowindex = teamGroupScreenModel.getTeamoneborrowindex();
                    characterDataSet(mCharacterone, teamGroupScreenModel.getTeamonecharacteroneid(), teamoneborrowindex == 0);
                    characterDataSet(mCharactertwo, teamGroupScreenModel.getTeamonecharactertwoid(), teamoneborrowindex == 1);
                    characterDataSet(mCharacterthree, teamGroupScreenModel.getTeamonecharacterthreeid(), teamoneborrowindex == 2);
                    characterDataSet(mCharacterfour, teamGroupScreenModel.getTeamonecharacterfourid(), teamoneborrowindex == 3);
                    characterDataSet(mCharacterfive, teamGroupScreenModel.getTeamonecharacterfiveid(), teamoneborrowindex == 4);
                    break;
                case 2:
                    int teamtwoborrowindex = teamGroupScreenModel.getTeamtwoborrowindex();
                    characterDataSet(mCharacterone, teamGroupScreenModel.getTeamtwocharacteroneid(), teamtwoborrowindex == 0);
                    characterDataSet(mCharactertwo, teamGroupScreenModel.getTeamtwocharactertwoid(), teamtwoborrowindex == 1);
                    characterDataSet(mCharacterthree, teamGroupScreenModel.getTeamtwocharacterthreeid(), teamtwoborrowindex == 2);
                    characterDataSet(mCharacterfour, teamGroupScreenModel.getTeamtwocharacterfourid(), teamtwoborrowindex == 3);
                    characterDataSet(mCharacterfive, teamGroupScreenModel.getTeamtwocharacterfiveid(), teamtwoborrowindex == 4);
                    break;
                case 3:
                    int teamthreeborrowindex = teamGroupScreenModel.getTeamthreeborrowindex();
                    characterDataSet(mCharacterone, teamGroupScreenModel.getTeamthreecharacteroneid(), teamthreeborrowindex == 0);
                    characterDataSet(mCharactertwo, teamGroupScreenModel.getTeamthreecharactertwoid(), teamthreeborrowindex == 1);
                    characterDataSet(mCharacterthree, teamGroupScreenModel.getTeamthreecharacterthreeid(), teamthreeborrowindex == 2);
                    characterDataSet(mCharacterfour, teamGroupScreenModel.getTeamthreecharacterfourid(), teamthreeborrowindex == 3);
                    characterDataSet(mCharacterfive, teamGroupScreenModel.getTeamthreecharacterfiveid(), teamthreeborrowindex == 4);
                    break;
            }
        }

        private int getStage() {
            int stage = 0;
            switch (team) {
                case 1:
                    stage = teamGroupScreenModel.getTeamonestage();
                    break;
                case 2:
                    stage = teamGroupScreenModel.getTeamtwostage();
                    break;
                case 3:
                    stage = teamGroupScreenModel.getTeamthreestage();
                    break;
                default:
                    break;
            }
            return stage;
        }

        private void setStage(int stage) {
            switch (team) {
                case 1:
                    teamGroupScreenModel.setTeamonestage(stage);
                    break;
                case 2:
                    teamGroupScreenModel.setTeamtwostage(stage);
                    break;
                case 3:
                    teamGroupScreenModel.setTeamthreestage(stage);
                    break;
                default:
                    break;
            }
        }

        private int getBoss() {
            int boss = 0;
            switch (team) {
                case 1:
                    boss = teamGroupScreenModel.getTeamoneboss();
                    break;
                case 2:
                    boss = teamGroupScreenModel.getTeamtwoboss();
                    break;
                case 3:
                    boss = teamGroupScreenModel.getTeamthreeboss();
                    break;
                default:
                    break;
            }
            return boss;
        }

        private void setBoss(int boss) {
            switch (team) {
                case 1:
                    teamGroupScreenModel.setTeamoneboss(boss);
                    break;
                case 2:
                    teamGroupScreenModel.setTeamtwoboss(boss);
                    break;
                case 3:
                    teamGroupScreenModel.setTeamthreeboss(boss);
                    break;
                default:
                    break;
            }
        }

        private int getAuto() {
            int auto = 0;
            switch (team) {
                case 1:
                    auto = teamGroupScreenModel.getTeamoneauto();
                    break;
                case 2:
                    auto = teamGroupScreenModel.getTeamtwoauto();
                    break;
                case 3:
                    auto = teamGroupScreenModel.getTeamthreeauto();
                    break;
                default:
                    break;
            }
            return auto;
        }

        private void setAuto(int auto) {
            switch (team) {
                case 1:
                    teamGroupScreenModel.setTeamoneauto(auto);
                    break;
                case 2:
                    teamGroupScreenModel.setTeamtwoauto(auto);
                    break;
                case 3:
                    teamGroupScreenModel.setTeamthreeauto(auto);
                    break;
                default:
                    break;
            }
        }

        private void characterClick(int index) {
            switch (team) {
                case 1:
                    if (teamGroupScreenModel.getTeamoneborrowindex() == index) {
                        teamGroupScreenModel.setTeamoneborrowindex(-1);
                        index = -1;
                    } else {
                        if (teamGroupScreenModel.getTeamonecharacteroneid() == 0) {
                            return;
                        }
                        teamGroupScreenModel.setTeamoneborrowindex(index);
                    }
                    break;
                case 2:
                    if (teamGroupScreenModel.getTeamtwoborrowindex() == index) {
                        teamGroupScreenModel.setTeamtwoborrowindex(-1);
                        index = -1;
                    } else {
                        if (teamGroupScreenModel.getTeamtwocharacteroneid() == 0) {
                            return;
                        }
                        teamGroupScreenModel.setTeamtwoborrowindex(index);
                    }
                    break;
                case 3:
                    if (teamGroupScreenModel.getTeamthreeborrowindex() == index) {
                        teamGroupScreenModel.setTeamthreeborrowindex(-1);
                        index = -1;
                    } else {
                        if (teamGroupScreenModel.getTeamthreecharacteroneid() == 0) {
                            return;
                        }
                        teamGroupScreenModel.setTeamthreeborrowindex(index);
                    }
                    break;
                default:
                    break;
            }
            mCharacterone.setBackGroundType(CharacterScreenType.TYPE_DEFAULT.getType());
            mCharactertwo.setBackGroundType(CharacterScreenType.TYPE_DEFAULT.getType());
            mCharacterthree.setBackGroundType(CharacterScreenType.TYPE_DEFAULT.getType());
            mCharacterfour.setBackGroundType(CharacterScreenType.TYPE_DEFAULT.getType());
            mCharacterfive.setBackGroundType(CharacterScreenType.TYPE_DEFAULT.getType());
            switch (index) {
                case 0:
                    mCharacterone.setBackGroundType(CharacterScreenType.TYPE_RED.getType());
                    break;
                case 1:
                    mCharactertwo.setBackGroundType(CharacterScreenType.TYPE_RED.getType());
                    break;
                case 2:
                    mCharacterthree.setBackGroundType(CharacterScreenType.TYPE_RED.getType());
                    break;
                case 3:
                    mCharacterfour.setBackGroundType(CharacterScreenType.TYPE_RED.getType());
                    break;
                case 4:
                    mCharacterfive.setBackGroundType(CharacterScreenType.TYPE_RED.getType());
                    break;
                default:
                    break;
            }
        }

        private boolean getExtra() {
            boolean extra = false;
            switch (team) {
                case 1:
                    extra = teamGroupScreenModel.isTeamoneextra();
                    break;
                case 2:
                    extra = teamGroupScreenModel.isTeamtwoextra();
                    break;
                case 3:
                    extra = teamGroupScreenModel.isTeamthreeextra();
                    break;
                default:
                    break;
            }
            return extra;
        }

        public void setExtra(boolean extra) {
            switch (team) {
                case 1:
                    teamGroupScreenModel.setTeamoneextra(extra);
                    break;
                case 2:
                    teamGroupScreenModel.setTeamtwoextra(extra);
                    break;
                case 3:
                    teamGroupScreenModel.setTeamthreeextra(extra);
                    break;
                default:
                    break;
            }
            String str = mExtra.getText().toString();
            if (extra) {
                SpannableStringBuilder builder = new SpannableStringBuilder(str);
                builder.setSpan(new ForegroundColorSpan(Utils.getAttrColor(getContext(), R.attr.colorSecondary)), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mExtra.setText(builder);
            } else {
                mExtra.setText(str);
            }
        }

        private boolean getEnable() {
            boolean enable = false;
            switch (team) {
                case 1:
                    enable = teamGroupScreenModel.isTeamoneenable();
                    break;
                case 2:
                    enable = teamGroupScreenModel.isTeamtwoenable();
                    break;
                case 3:
                    enable = teamGroupScreenModel.isTeamthreeenable();
                    break;
                default:
                    break;
            }
            return enable;
        }

        public void setEnable(boolean enable) {
            switch (team) {
                case 1:
                    teamGroupScreenModel.setTeamoneenable(enable);
                    break;
                case 2:
                    teamGroupScreenModel.setTeamtwoenable(enable);
                    break;
                case 3:
                    teamGroupScreenModel.setTeamthreeenable(enable);
                    break;
                default:
                    break;
            }
            if (enable) {
                mLay.setCardBackgroundColor(Utils.getColor(R.color.theme));
            } else {
                mLay.setCardBackgroundColor(Color.BLACK);
            }
        }
    }

    private void characterDataSet(CharacterView characterView, int id, boolean select) {
        CharacterModel characterModel = CharacterHelper.findCharacterById(id, sharedSource.characterList.getValue());
        characterView.setBackGroundType(CharacterScreenType.TYPE_DEFAULT.getType());
        if (characterModel == null) {
            characterView.resetCharacterModel();
        } else {
            characterView.setCharacterModel(characterModel, id);
            if (select) {
                characterView.setBackGroundType(CharacterScreenType.TYPE_RED.getType());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ClanwarHelper.setScreenModel(teamGroupScreenModel);
        sharedSource.characterList.removeObservers(requireActivity());
        sharedTeamScreenTeam.teamOne.removeObservers(requireActivity());
        sharedTeamScreenTeam.teamTwo.removeObservers(requireActivity());
        sharedTeamScreenTeam.teamThree.removeObservers(requireActivity());
    }
}
