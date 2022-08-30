package cn.sun45.warbanner.ui.fragments.teamgroup;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.appbar.MaterialToolbar;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.character.CharacterHelper;
import cn.sun45.warbanner.clanwar.ClanwarHelper;
import cn.sun45.warbanner.document.database.setup.models.TeamGroupScreenModel;
import cn.sun45.warbanner.document.database.source.models.CharacterModel;
import cn.sun45.warbanner.document.database.source.models.TeamModel;
import cn.sun45.warbanner.framework.ui.BaseActivity;
import cn.sun45.warbanner.framework.ui.BaseFragment;
import cn.sun45.warbanner.stage.StageManager;
import cn.sun45.warbanner.ui.shared.SharedViewModelSource;
import cn.sun45.warbanner.ui.shared.SharedViewModelTeamScreenTeam;
import cn.sun45.warbanner.ui.views.characterview.CharacterView;
import cn.sun45.warbanner.ui.views.selectgroup.SelectGroup;
import cn.sun45.warbanner.ui.views.selectgroup.SelectGroupListener;
import cn.sun45.warbanner.util.Utils;

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
                    teamGroupScreenModel.setTeamonecharacteroneid(teamModel.getCharacterone());
                    teamGroupScreenModel.setTeamonecharactertwoid(teamModel.getCharactertwo());
                    teamGroupScreenModel.setTeamonecharacterthreeid(teamModel.getCharacterthree());
                    teamGroupScreenModel.setTeamonecharacterfourid(teamModel.getCharacterfour());
                    teamGroupScreenModel.setTeamonecharacterfiveid(teamModel.getCharacterfive());
                    mTeamOne.setData();
                    sharedTeamScreenTeam.teamOne.postValue(null);
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
                    teamGroupScreenModel.setTeamtwocharacteroneid(teamModel.getCharacterone());
                    teamGroupScreenModel.setTeamtwocharactertwoid(teamModel.getCharactertwo());
                    teamGroupScreenModel.setTeamtwocharacterthreeid(teamModel.getCharacterthree());
                    teamGroupScreenModel.setTeamtwocharacterfourid(teamModel.getCharacterfour());
                    teamGroupScreenModel.setTeamtwocharacterfiveid(teamModel.getCharacterfive());
                    mTeamTwo.setData();
                    sharedTeamScreenTeam.teamTwo.postValue(null);
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
                    teamGroupScreenModel.setTeamthreecharacteroneid(teamModel.getCharacterone());
                    teamGroupScreenModel.setTeamthreecharactertwoid(teamModel.getCharactertwo());
                    teamGroupScreenModel.setTeamthreecharacterthreeid(teamModel.getCharacterthree());
                    teamGroupScreenModel.setTeamthreecharacterfourid(teamModel.getCharacterfour());
                    teamGroupScreenModel.setTeamthreecharacterfiveid(teamModel.getCharacterfive());
                    mTeamThree.setData();
                    sharedTeamScreenTeam.teamThree.postValue(null);
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

        public TeamHolder(int team, ViewGroup lay) {
            this.team = team;
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

            mStage.setListener(new SelectGroupListener() {
                @Override
                public void select(int position) {
                    setStage(position);
                }
            });
            mBoss.setListener(new SelectGroupListener() {
                @Override
                public void select(int position) {
                    setBoss(position);
                }
            });
            mAuto.setListener(new SelectGroupListener() {
                @Override
                public void select(int position) {
                    setAuto(position);
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
            mStage.setData(StageManager.getInstance().getStageDescriptionList(), getStage());
            mBoss.setData(Utils.getStringArray(R.array.teamgroup_screen_boss_options), getBoss());
            mAuto.setData(Utils.getStringArray(R.array.teamgroup_screen_auto_options), getAuto());
            setCharacterData();
        }

        private void setCharacterData() {
            switch (team) {
                case 1:
                    characterDataSet(mCharacterone, teamGroupScreenModel.getTeamonecharacteroneid());
                    characterDataSet(mCharactertwo, teamGroupScreenModel.getTeamonecharactertwoid());
                    characterDataSet(mCharacterthree, teamGroupScreenModel.getTeamonecharacterthreeid());
                    characterDataSet(mCharacterfour, teamGroupScreenModel.getTeamonecharacterfourid());
                    characterDataSet(mCharacterfive, teamGroupScreenModel.getTeamonecharacterfiveid());
                    break;
                case 2:
                    characterDataSet(mCharacterone, teamGroupScreenModel.getTeamtwocharacteroneid());
                    characterDataSet(mCharactertwo, teamGroupScreenModel.getTeamtwocharactertwoid());
                    characterDataSet(mCharacterthree, teamGroupScreenModel.getTeamtwocharacterthreeid());
                    characterDataSet(mCharacterfour, teamGroupScreenModel.getTeamtwocharacterfourid());
                    characterDataSet(mCharacterfive, teamGroupScreenModel.getTeamtwocharacterfiveid());
                    break;
                case 3:
                    characterDataSet(mCharacterone, teamGroupScreenModel.getTeamthreecharacteroneid());
                    characterDataSet(mCharactertwo, teamGroupScreenModel.getTeamthreecharactertwoid());
                    characterDataSet(mCharacterthree, teamGroupScreenModel.getTeamthreecharacterthreeid());
                    characterDataSet(mCharacterfour, teamGroupScreenModel.getTeamthreecharacterfourid());
                    characterDataSet(mCharacterfive, teamGroupScreenModel.getTeamthreecharacterfiveid());
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
    }

    private void characterDataSet(CharacterView characterView, int id) {
        CharacterModel characterModel = CharacterHelper.findCharacterById(id, sharedSource.characterList.getValue());
        if (characterModel == null) {
            characterView.resetCharacterModel();
        } else {
            characterView.setCharacterModel(characterModel, id);
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
