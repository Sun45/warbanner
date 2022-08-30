package cn.sun45.warbanner.ui.views.characterview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Callback;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.document.database.source.models.CharacterModel;
import cn.sun45.warbanner.framework.image.ImageRequester;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2022/8/30
 * 角色信息加载控件
 */
public class CharacterView extends MaterialCardView {
    public static final int BG_DEFAULT = 0;
    public static final int BG_YELLOW = 1;
    public static final int BG_RED = 2;

    private CharacterModel characterModel;

    private CardView mLay;
    private ImageView mIcon;
    private TextView mName;
    private View mAuto;

    public CharacterView(@NonNull Context context) {
        super(context);
        init();
    }

    public CharacterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mLay = (CardView) LayoutInflater.from(getContext()).inflate(R.layout.characterview_lay, this, true);
        mIcon = mLay.findViewById(R.id.character_icon);
        mName = mLay.findViewById(R.id.character_name);
        mAuto = mLay.findViewById(R.id.character_auto);
    }

    public void setCharacterModel(CharacterModel characterModel, int id) {
        this.characterModel = characterModel;
        if (characterModel == null) {
            mIcon.setVisibility(View.VISIBLE);
            mIcon.setImageBitmap(null);
            mName.setVisibility(View.VISIBLE);
            mName.setText(id + "");
        } else {
            mIcon.setVisibility(View.VISIBLE);
            ImageRequester.request(characterModel.getIconUrl(), R.drawable.ic_character_default).loadRoundImage(mIcon, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    if (characterModel != null) {
                        mIcon.setVisibility(INVISIBLE);
                        mName.setVisibility(View.VISIBLE);
                        mName.setText(characterModel.getName());
                    }
                }
            });
            mName.setVisibility(View.INVISIBLE);
            mName.setText("");
        }
    }

    public void resetCharacterModel() {
        mIcon.setVisibility(View.VISIBLE);
        mIcon.setImageBitmap(null);
        mName.setVisibility(View.INVISIBLE);
        mName.setText("");
    }

    public void setBackGroundType(int bgType) {
        switch (bgType) {
            case BG_DEFAULT:
                mLay.setCardBackgroundColor(Utils.getColor(R.color.gray));
                break;
            case BG_YELLOW:
                mLay.setCardBackgroundColor(Utils.getAttrColor(getContext(), R.attr.colorSecondary));
                break;
            case BG_RED:
                mLay.setCardBackgroundColor(Utils.getAttrColor(getContext(), R.attr.colorPrimary));
                break;
        }
    }

    public void setAutoShow(boolean auto) {
        if (auto) {
            mAuto.setVisibility(VISIBLE);
        } else {
            mAuto.setVisibility(INVISIBLE);
        }
    }
}
