package cn.sun45.warbanner.ui.fragments.teamgroup;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.list.DialogSingleChoiceExtKt;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.character.CharacterHelper;
import cn.sun45.warbanner.clanwar.ClanwarHelper;
import cn.sun45.warbanner.document.db.clanwar.TeamGroupScreenModel;
import cn.sun45.warbanner.document.db.clanwar.TeamModel;
import cn.sun45.warbanner.document.db.source.CharacterModel;
import cn.sun45.warbanner.framework.image.ImageRequester;
import cn.sun45.warbanner.framework.ui.BaseActivity;
import cn.sun45.warbanner.framework.ui.BaseFragment;
import cn.sun45.warbanner.ui.shared.SharedViewModelSource;
import cn.sun45.warbanner.ui.shared.SharedViewModelTeamScreenTeam;
import cn.sun45.warbanner.util.Utils;
import kotlin.Unit;
import kotlin.jvm.functions.Function3;

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

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_teamgroupscreen;
    }

    @Override
    protected void initData() {
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
    }

    @Override
    protected void dataRequest() {
        sharedSource = new ViewModelProvider(requireActivity()).get(SharedViewModelSource.class);
        sharedTeamScreenTeam = new ViewModelProvider(requireActivity()).get(SharedViewModelTeamScreenTeam.class);

        mTeamOne.setData();
        mTeamTwo.setData();
        mTeamThree.setData();
        sharedTeamScreenTeam.teamOne.observe(requireActivity(), new Observer<TeamModel>() {
            @Override
            public void onChanged(TeamModel teamModel) {
                if (teamModel != null) {
                    int stage = teamModel.getStage() - 1;
                    int boss = Integer.valueOf(teamModel.getBoss().substring(1, 2)) - 1;
                    int auto = teamModel.isAuto() ? 1 : 2;
                    teamGroupScreenModel.setTeamonestage(stage);
                    teamGroupScreenModel.setTeamoneboss(boss);
                    teamGroupScreenModel.setTeamoneauto(auto);
                    List<CharacterModel> characterModels = sharedSource.characterlist.getValue();
                    teamGroupScreenModel.setTeamonecharacteroneid(CharacterHelper.findCharacterByNickname(teamModel.getCharacterone(), characterModels).getId());
                    teamGroupScreenModel.setTeamonecharactertwoid(CharacterHelper.findCharacterByNickname(teamModel.getCharactertwo(), characterModels).getId());
                    teamGroupScreenModel.setTeamonecharacterthreeid(CharacterHelper.findCharacterByNickname(teamModel.getCharacterthree(), characterModels).getId());
                    teamGroupScreenModel.setTeamonecharacterfourid(CharacterHelper.findCharacterByNickname(teamModel.getCharacterfour(), characterModels).getId());
                    teamGroupScreenModel.setTeamonecharacterfiveid(CharacterHelper.findCharacterByNickname(teamModel.getCharacterfive(), characterModels).getId());
                    mTeamOne.setData();
                }
            }
        });
        sharedTeamScreenTeam.teamTwo.observe(requireActivity(), new Observer<TeamModel>() {
            @Override
            public void onChanged(TeamModel teamModel) {
                if (teamModel != null) {
                    int stage = teamModel.getStage() - 1;
                    int boss = Integer.valueOf(teamModel.getBoss().substring(1, 2)) - 1;
                    int auto = teamModel.isAuto() ? 1 : 2;
                    teamGroupScreenModel.setTeamtwostage(stage);
                    teamGroupScreenModel.setTeamtwoboss(boss);
                    teamGroupScreenModel.setTeamtwoauto(auto);
                    List<CharacterModel> characterModels = sharedSource.characterlist.getValue();
                    teamGroupScreenModel.setTeamtwocharacteroneid(CharacterHelper.findCharacterByNickname(teamModel.getCharacterone(), characterModels).getId());
                    teamGroupScreenModel.setTeamtwocharactertwoid(CharacterHelper.findCharacterByNickname(teamModel.getCharactertwo(), characterModels).getId());
                    teamGroupScreenModel.setTeamtwocharacterthreeid(CharacterHelper.findCharacterByNickname(teamModel.getCharacterthree(), characterModels).getId());
                    teamGroupScreenModel.setTeamtwocharacterfourid(CharacterHelper.findCharacterByNickname(teamModel.getCharacterfour(), characterModels).getId());
                    teamGroupScreenModel.setTeamtwocharacterfiveid(CharacterHelper.findCharacterByNickname(teamModel.getCharacterfive(), characterModels).getId());
                    mTeamTwo.setData();
                }
            }
        });
        sharedTeamScreenTeam.teamThree.observe(requireActivity(), new Observer<TeamModel>() {
            @Override
            public void onChanged(TeamModel teamModel) {
                if (teamModel != null) {
                    int stage = teamModel.getStage() - 1;
                    int boss = Integer.valueOf(teamModel.getBoss().substring(1, 2)) - 1;
                    int auto = teamModel.isAuto() ? 1 : 2;
                    teamGroupScreenModel.setTeamthreestage(stage);
                    teamGroupScreenModel.setTeamthreeboss(boss);
                    teamGroupScreenModel.setTeamthreeauto(auto);
                    List<CharacterModel> characterModels = sharedSource.characterlist.getValue();
                    teamGroupScreenModel.setTeamthreecharacteroneid(CharacterHelper.findCharacterByNickname(teamModel.getCharacterone(), characterModels).getId());
                    teamGroupScreenModel.setTeamthreecharactertwoid(CharacterHelper.findCharacterByNickname(teamModel.getCharactertwo(), characterModels).getId());
                    teamGroupScreenModel.setTeamthreecharacterthreeid(CharacterHelper.findCharacterByNickname(teamModel.getCharacterthree(), characterModels).getId());
                    teamGroupScreenModel.setTeamthreecharacterfourid(CharacterHelper.findCharacterByNickname(teamModel.getCharacterfour(), characterModels).getId());
                    teamGroupScreenModel.setTeamthreecharacterfiveid(CharacterHelper.findCharacterByNickname(teamModel.getCharacterfive(), characterModels).getId());
                    mTeamThree.setData();
                }
            }
        });
    }

    @Override
    protected void onShow() {
    }

    @Override
    protected void onHide() {
    }

    private class TeamHolder {
        private int team;
        private TextView mStage;
        private TextView mBoss;
        private TextView mAuto;
        private ViewGroup mCharacterLay;
        private CharacterHolder mCharacterone;
        private CharacterHolder mCharactertwo;
        private CharacterHolder mCharacterthree;
        private CharacterHolder mCharacterfour;
        private CharacterHolder mCharacterfive;
        private AppCompatImageView mClean;
        private AppCompatImageView mSetting;

        public TeamHolder(int team, ViewGroup lay) {
            this.team = team;
            mStage = lay.findViewById(R.id.stage);
            mBoss = lay.findViewById(R.id.boss);
            mAuto = lay.findViewById(R.id.auto);
            mCharacterLay = lay.findViewById(R.id.character_lay);
            mCharacterone = new CharacterHolder(lay.findViewById(R.id.characterone_lay), R.id.characterone_icon, R.id.characterone_name);
            mCharactertwo = new CharacterHolder(lay.findViewById(R.id.charactertwo_lay), R.id.charactertwo_icon, R.id.charactertwo_name);
            mCharacterthree = new CharacterHolder(lay.findViewById(R.id.characterthree_lay), R.id.characterthree_icon, R.id.characterthree_name);
            mCharacterfour = new CharacterHolder(lay.findViewById(R.id.characterfour_lay), R.id.characterfour_icon, R.id.characterfour_name);
            mCharacterfive = new CharacterHolder(lay.findViewById(R.id.characterfive_lay), R.id.characterfive_icon, R.id.characterfive_name);
            mClean = lay.findViewById(R.id.clean);
            mSetting = lay.findViewById(R.id.setting);

            mStage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MaterialDialog dialog = new MaterialDialog(getContext(), MaterialDialog.getDEFAULT_BEHAVIOR());
                    dialog.title(R.string.teamgroup_screen_stage_dialog_title, null);
                    int selection = getStage();
                    DialogSingleChoiceExtKt.listItemsSingleChoice(dialog, R.array.teamgroup_screen_stage_dialog_options, null, null, selection, true, 0, 0, new Function3<MaterialDialog, Integer, CharSequence, Unit>() {
                        @Override
                        public Unit invoke(MaterialDialog materialDialog, Integer integer, CharSequence charSequence) {
                            setStage(integer);
                            return null;
                        }
                    });
                    dialog.positiveButton(R.string.teamgroup_screen_stage_dialog_confirm, null, null);
                    dialog.show();
                }
            });
            mBoss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MaterialDialog dialog = new MaterialDialog(getContext(), MaterialDialog.getDEFAULT_BEHAVIOR());
                    dialog.title(R.string.teamgroup_screen_boss_dialog_title, null);
                    int selection = getBoss();
                    DialogSingleChoiceExtKt.listItemsSingleChoice(dialog, R.array.teamgroup_screen_boss_dialog_options, null, null, selection, true, 0, 0, new Function3<MaterialDialog, Integer, CharSequence, Unit>() {
                        @Override
                        public Unit invoke(MaterialDialog materialDialog, Integer integer, CharSequence charSequence) {
                            setBoss(integer);
                            return null;
                        }
                    });
                    dialog.positiveButton(R.string.teamgroup_screen_boss_dialog_confirm, null, null);
                    dialog.show();
                }
            });
            mAuto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MaterialDialog dialog = new MaterialDialog(getContext(), MaterialDialog.getDEFAULT_BEHAVIOR());
                    dialog.title(R.string.teamgroup_screen_auto_dialog_title, null);
                    int selection = getAuto();
                    DialogSingleChoiceExtKt.listItemsSingleChoice(dialog, R.array.teamgroup_screen_auto_dialog_options, null, null, selection, true, 0, 0, new Function3<MaterialDialog, Integer, CharSequence, Unit>() {
                        @Override
                        public Unit invoke(MaterialDialog materialDialog, Integer integer, CharSequence charSequence) {
                            setAuto(integer);
                            return null;
                        }
                    });
                    dialog.positiveButton(R.string.teamgroup_screen_auto_dialog_confirm, null, null);
                    dialog.show();
                }
            });
            mCharacterLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            mClean.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (team) {
                        case 1:
                            sharedTeamScreenTeam.teamOne.postValue(null);
                            teamGroupScreenModel.setTeamonecharacteroneid(0);
                            teamGroupScreenModel.setTeamonecharactertwoid(0);
                            teamGroupScreenModel.setTeamonecharacterthreeid(0);
                            teamGroupScreenModel.setTeamonecharacterfourid(0);
                            teamGroupScreenModel.setTeamonecharacterfiveid(0);
                            break;
                        case 2:
                            sharedTeamScreenTeam.teamTwo.postValue(null);
                            teamGroupScreenModel.setTeamtwocharacteroneid(0);
                            teamGroupScreenModel.setTeamtwocharactertwoid(0);
                            teamGroupScreenModel.setTeamtwocharacterthreeid(0);
                            teamGroupScreenModel.setTeamtwocharacterfourid(0);
                            teamGroupScreenModel.setTeamtwocharacterfiveid(0);
                            break;
                        case 3:
                            sharedTeamScreenTeam.teamThree.postValue(null);
                            teamGroupScreenModel.setTeamthreecharacteroneid(0);
                            teamGroupScreenModel.setTeamthreecharactertwoid(0);
                            teamGroupScreenModel.setTeamthreecharacterthreeid(0);
                            teamGroupScreenModel.setTeamthreecharacterfourid(0);
                            teamGroupScreenModel.setTeamthreecharacterfiveid(0);
                            break;
                    }
                    setCharacterData();
                }
            });
            mSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavController controller = Navigation.findNavController(getView());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("team", team);
                    controller.navigate(R.id.action_nav_teamgroupscreen_to_nav_teamselect, bundle);
                }
            });
        }

        public void setData() {
            mStage.setText(Utils.getStringArray(R.array.teamgroup_screen_stage_dialog_options).get(getStage()));
            mBoss.setText(Utils.getStringArray(R.array.teamgroup_screen_boss_dialog_options).get(getBoss()));
            mAuto.setText(Utils.getStringArray(R.array.teamgroup_screen_auto_dialog_options).get(getAuto()));
            setCharacterData();
        }

        private void setCharacterData() {
            switch (team) {
                case 1:
                    mCharacterone.setdata(teamGroupScreenModel.getTeamonecharacteroneid());
                    mCharactertwo.setdata(teamGroupScreenModel.getTeamonecharactertwoid());
                    mCharacterthree.setdata(teamGroupScreenModel.getTeamonecharacterthreeid());
                    mCharacterfour.setdata(teamGroupScreenModel.getTeamonecharacterfourid());
                    mCharacterfive.setdata(teamGroupScreenModel.getTeamonecharacterfiveid());
                    break;
                case 2:
                    mCharacterone.setdata(teamGroupScreenModel.getTeamtwocharacteroneid());
                    mCharactertwo.setdata(teamGroupScreenModel.getTeamtwocharactertwoid());
                    mCharacterthree.setdata(teamGroupScreenModel.getTeamtwocharacterthreeid());
                    mCharacterfour.setdata(teamGroupScreenModel.getTeamtwocharacterfourid());
                    mCharacterfive.setdata(teamGroupScreenModel.getTeamtwocharacterfiveid());
                    break;
                case 3:
                    mCharacterone.setdata(teamGroupScreenModel.getTeamthreecharacteroneid());
                    mCharactertwo.setdata(teamGroupScreenModel.getTeamthreecharactertwoid());
                    mCharacterthree.setdata(teamGroupScreenModel.getTeamthreecharacterthreeid());
                    mCharacterfour.setdata(teamGroupScreenModel.getTeamthreecharacterfourid());
                    mCharacterfive.setdata(teamGroupScreenModel.getTeamthreecharacterfiveid());
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
            mStage.setText(Utils.getStringArray(R.array.teamgroup_screen_stage_dialog_options).get(stage));
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
            mBoss.setText(Utils.getStringArray(R.array.teamgroup_screen_boss_dialog_options).get(boss));
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
            mAuto.setText(Utils.getStringArray(R.array.teamgroup_screen_auto_dialog_options).get(auto));
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

        public void setdata(int id) {
            CharacterModel characterModel = CharacterHelper.findCharacterById(id, sharedSource.characterlist.getValue());
            if (characterModel == null) {
                icon.setImageBitmap(null);
            } else {
                ImageRequester.request(characterModel.getIconUrl(), R.drawable.ic_character_default).loadImage(icon);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ClanwarHelper.setScreenModel(teamGroupScreenModel);
        sharedTeamScreenTeam.teamOne.removeObservers(requireActivity());
        sharedTeamScreenTeam.teamTwo.removeObservers(requireActivity());
        sharedTeamScreenTeam.teamThree.removeObservers(requireActivity());
    }
}
