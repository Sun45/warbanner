package cn.sun45.warbanner.ui.views.character.characterview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.squareup.picasso.Callback;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.document.database.source.models.CharacterModel;
import cn.sun45.warbanner.framework.image.ImageRequester;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2022/8/30
 * 角色信息加载控件
 */
public class CharacterView extends CardView {
    public static final int BG_DEFAULT = 0;
    public static final int BG_YELLOW = 1;
    public static final int BG_RED = 2;

    private CharacterModel characterModel;

    private ImageView mIcon;
    private TextView mName;
    private View mAuto;
    private View mHalf;

    public CharacterView(@NonNull Context context) {
        this(context, null);
    }

    public CharacterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CharacterView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setCardBackgroundColor(Utils.getColor(R.color.gray));
        setRadius(Utils.dip2px(getContext(), 5));
        LayoutInflater.from(getContext()).inflate(R.layout.characterview_lay, this, true);
        mIcon = findViewById(R.id.character_icon);
        mName = findViewById(R.id.character_name);
        mAuto = findViewById(R.id.character_auto);
        mHalf = findViewById(R.id.character_half);
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
        int color = 0;
        switch (bgType) {
            case BG_DEFAULT:
                color = Utils.getColor(R.color.gray);
                break;
            case BG_YELLOW:
                color = Utils.getAttrColor(getContext(), R.attr.colorSecondary);
                break;
            case BG_RED:
                color = Utils.getAttrColor(getContext(), R.attr.colorPrimary);
                break;
        }
        setCardBackgroundColor(color);
    }

    public void setAutoShow(boolean auto) {
        if (auto) {
            mAuto.setVisibility(VISIBLE);
        } else {
            mAuto.setVisibility(INVISIBLE);
        }
    }

    public void setHalfShow(boolean half) {
        if (half) {
            mHalf.setVisibility(VISIBLE);
        } else {
            mHalf.setVisibility(INVISIBLE);
        }
    }
}
