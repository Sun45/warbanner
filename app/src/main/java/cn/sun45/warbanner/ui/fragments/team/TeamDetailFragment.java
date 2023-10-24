package cn.sun45.warbanner.ui.fragments.team;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.input.DialogInputExtKt;
import com.google.android.material.appbar.MaterialToolbar;

import org.jetbrains.annotations.NotNull;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.character.CharacterHelper;
import cn.sun45.warbanner.clanwar.ClanwarHelper;
import cn.sun45.warbanner.document.database.setup.models.TeamCustomizeModel;
import cn.sun45.warbanner.document.database.source.models.CharacterModel;
import cn.sun45.warbanner.document.database.source.models.TeamModel;
import cn.sun45.warbanner.framework.ui.BaseActivity;
import cn.sun45.warbanner.framework.ui.BaseFragment;
import cn.sun45.warbanner.ui.shared.SharedViewModelClanwar;
import cn.sun45.warbanner.ui.shared.SharedViewModelSource;
import cn.sun45.warbanner.ui.views.character.characterview.CharacterView;
import cn.sun45.warbanner.ui.views.teamdetail.TeamDetailScroll;
import cn.sun45.warbanner.util.Utils;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;

/**
 * Created by Sun45 on 2021/7/20
 * 阵容详情Fragment
 */
public class TeamDetailFragment extends BaseFragment {
    private TeamModel teamModel;
    private TeamCustomizeModel teamCustomizeModel;

    private SharedViewModelSource sharedSource;
    private SharedViewModelClanwar sharedClanwar;

    private TextView mTitle;
    private TextView mDamageText;
    private AppCompatImageView mDamageClean;

    private CharacterView mCharacterone;
    private CharacterView mCharactertwo;
    private CharacterView mCharacterthree;
    private CharacterView mCharacterfour;
    private CharacterView mCharacterfive;

    private TeamDetailScroll mTeamDetailScroll;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_teamdetail;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        teamModel = (TeamModel) bundle.getSerializable("teamModel");
        teamCustomizeModel = ClanwarHelper.getCustomizeModel(teamModel);
        setHasOptionsMenu(true);
    }

    @Override
    protected void initView() {
        MaterialToolbar toolbar = mRoot.findViewById(R.id.drop_toolbar);
        ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        mTitle = mRoot.findViewById(R.id.title);
        mDamageText = mRoot.findViewById(R.id.damage_text);
        mDamageClean = mRoot.findViewById(R.id.damage_clean);
        mCharacterone = mRoot.findViewById(R.id.characterone_lay);
        mCharactertwo = mRoot.findViewById(R.id.charactertwo_lay);
        mCharacterthree = mRoot.findViewById(R.id.characterthree_lay);
        mCharacterfour = mRoot.findViewById(R.id.characterfour_lay);
        mCharacterfive = mRoot.findViewById(R.id.characterfive_lay);
        mTeamDetailScroll = mRoot.findViewById(R.id.teamDetailScroll);
    }

    @Override
    protected void dataRequest() {
        sharedSource = new ViewModelProvider(requireActivity()).get(SharedViewModelSource.class);
        sharedClanwar = new ViewModelProvider(requireActivity()).get(SharedViewModelClanwar.class);

        showBlock();
        showDamage();
        mDamageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialog dialog = new MaterialDialog(getContext(), MaterialDialog.getDEFAULT_BEHAVIOR());
                dialog.title(R.string.team_detail_damage_dialog_title, null);
                int damage = teamModel.getDamage();
                if (teamCustomizeModel != null && teamCustomizeModel.damageEffective()) {
                    damage = teamCustomizeModel.getDamage();
                }
                DialogInputExtKt.input(dialog, Utils.getStringWithPlaceHolder(R.string.team_detail_damage_dialog_hint, teamModel.getDamage() + "w"), null, damage + "", null, InputType.TYPE_CLASS_NUMBER, null, true, false, new Function2<MaterialDialog, CharSequence, Unit>() {
                    @Override
                    public Unit invoke(MaterialDialog materialDialog, CharSequence charSequence) {
                        int damage = Integer.valueOf(charSequence.toString());
                        teamCustomizeModel = ClanwarHelper.customizeTeamDamage(teamModel, teamCustomizeModel, damage);
                        showDamage();
                        return null;
                    }
                });
                dialog.positiveButton(R.string.confirm, null, null);
                dialog.show();
            }
        });
        mDamageClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamCustomizeModel = ClanwarHelper.removeCustomizeTeamDamage(teamCustomizeModel);
                showDamage();
            }
        });
        mCharacterone.setAutoShow(teamModel.isAuto());
        mCharacterone.setHalfShow(teamModel.isFinish());
        characterDataSet(mCharacterone, teamModel.getCharacterone());
        characterDataSet(mCharactertwo, teamModel.getCharactertwo());
        characterDataSet(mCharacterthree, teamModel.getCharacterthree());
        characterDataSet(mCharacterfour, teamModel.getCharacterfour());
        characterDataSet(mCharacterfive, teamModel.getCharacterfive());
        mTeamDetailScroll.setTeamModel(teamModel);
    }

    private void showBlock() {
        if (teamCustomizeModel != null && teamCustomizeModel.isBlock()) {
            String str = teamModel.getSn();
            SpannableStringBuilder builder = new SpannableStringBuilder(str);
            builder.setSpan(new StrikethroughSpan(), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new ForegroundColorSpan(Utils.getColor(R.color.red_500)), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mTitle.setText(builder);
            Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_block_24);
            drawable.setTint(Utils.getColor(R.color.red_500));
            mTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null);
        } else {
            mTitle.setText(teamModel.getSn());
            mTitle.setCompoundDrawables(null, null, null, null);
        }
    }

    private void showDamage() {
        if (teamCustomizeModel != null && teamCustomizeModel.damageEffective()) {
            String str = teamCustomizeModel.getDamage() + "w";
            int length = str.length();
            str += "(" + teamModel.getDamage() + "w)";
            SpannableStringBuilder builder = new SpannableStringBuilder(str);
            builder.setSpan(new ForegroundColorSpan(Utils.getAttrColor(getContext(), R.attr.colorSecondary)), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mDamageText.setText(builder);
            mDamageClean.setVisibility(View.VISIBLE);
        } else {
            mDamageText.setText(teamModel.getDamage() + "w");
            mDamageClean.setVisibility(View.INVISIBLE);
        }
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
        inflater.inflate(R.menu.fragment_teamdetail_drop_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_block:
                teamCustomizeModel = ClanwarHelper.customizeTeamBlock(teamModel, teamCustomizeModel);
                showBlock();
                break;
            case R.id.menu_share:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, teamModel.getShare(sharedSource.characterList.getValue()));
                intent.putExtra(Intent.EXTRA_SUBJECT, "share");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, Utils.getString(R.string.app_name)));
                break;
        }
        return true;
    }

    private void characterDataSet(CharacterView characterView, int id) {
        CharacterModel characterModel = CharacterHelper.findCharacterById(id, sharedSource.characterList.getValue());
        characterView.setCharacterModel(characterModel, id);
        characterView.setBackGroundType(CharacterView.BG_DEFAULT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedClanwar.loadData();
    }
}
