package cn.sun45.warbanner.assist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.drawable.DrawableCompat;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.framework.MyApplication;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2022/2/23
 * 辅助功能面板控件
 */
public class AssistPanelView extends AssistView {
    private static final String TAG = "AssistPanelView";
    private AssistPanelViewListener listener;

    //区域展示
    private boolean areaShow;
    private AppCompatImageView mAreaShow;

    //收缩
    private boolean shrink;
    private AppCompatImageView mShrinkView;

    //多重点击
    private boolean multiplyTap;
    private AppCompatImageView mMultiplyTap;
    private boolean multiplyTapFunction;
    private AppCompatImageView mMultiplyTapFunction;

    //角色选择
    private CharacterHolder mCharacterOne;
    private CharacterHolder mCharacterTwo;
    private CharacterHolder mCharacterThree;
    private CharacterHolder mCharacterFour;
    private CharacterHolder mCharacterFive;

    private int characterSelectionListPosition;
    private List<Integer> characterSelectionList;

    private int characterSelection;

    public AssistPanelView(Context context, AssistPanelViewListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected View buildView(Context context) {
        View lay = LayoutInflater.from(MyApplication.application).inflate(R.layout.assist_pannel, null);
        mAreaShow = lay.findViewById(R.id.area_show);
        mAreaShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areaShow = !areaShow;
                listener.areaShow(areaShow);
            }
        });
        mShrinkView = lay.findViewById(R.id.shrink);
        mShrinkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shrink = !shrink;
                listener.shrink();
            }
        });
        mMultiplyTap = lay.findViewById(R.id.multiply_tap);
        mMultiplyTap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiplyTap = !multiplyTap;
                int tint = 0;
                if (multiplyTap) {
                    tint = Utils.getColor(R.color.orange_dark);
                } else {
                    tint = Utils.getColor(R.color.white_50);
                }
                Drawable drawable = mMultiplyTap.getDrawable();
                DrawableCompat.setTint(drawable, tint);
                mMultiplyTap.setImageDrawable(drawable);
                if (multiplyTap) {
                    mCharacterOne.multiplyTap(true);
                    mCharacterTwo.multiplyTap(true);
                    mCharacterThree.multiplyTap(true);
                    mCharacterFour.multiplyTap(true);
                    mCharacterFive.multiplyTap(true);
                } else {
                    mCharacterOne.multiplyTap(false);
                    mCharacterTwo.multiplyTap(false);
                    mCharacterThree.multiplyTap(false);
                    mCharacterFour.multiplyTap(false);
                    mCharacterFive.multiplyTap(false);
                }
            }
        });
        mMultiplyTapFunction = lay.findViewById(R.id.multiply_tap_function);
        mMultiplyTapFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (multiplyTap) {
                    multiplyTapFunction = !multiplyTapFunction;
                    int tint = 0;
                    if (multiplyTapFunction) {
                        tint = Utils.getColor(R.color.orange_dark);
                    } else {
                        tint = Utils.getColor(R.color.white_50);
                    }
                    Drawable drawable = mMultiplyTapFunction.getDrawable();
                    DrawableCompat.setTint(drawable, tint);
                    mMultiplyTapFunction.setImageDrawable(drawable);
                    if (multiplyTapFunction) {
                        characterSelectionListPosition = 0;
                        characterSelectionList = new ArrayList<>();
                        if (mCharacterOne.isMultiplyTapFunction()) {
                            characterSelectionList.add(1);
                        }
                        if (mCharacterTwo.isMultiplyTapFunction()) {
                            characterSelectionList.add(2);
                        }
                        if (mCharacterThree.isMultiplyTapFunction()) {
                            characterSelectionList.add(3);
                        }
                        if (mCharacterFour.isMultiplyTapFunction()) {
                            characterSelectionList.add(4);
                        }
                        if (mCharacterFive.isMultiplyTapFunction()) {
                            characterSelectionList.add(5);
                        }
                        listener.startTap();
                    }
                }
            }
        });
        mCharacterOne = new CharacterHolder(lay, R.id.character_one, R.id.character_one_hint, 1);
        mCharacterTwo = new CharacterHolder(lay, R.id.character_two, R.id.character_two_hint, 2);
        mCharacterThree = new CharacterHolder(lay, R.id.character_three, R.id.character_three_hint, 3);
        mCharacterFour = new CharacterHolder(lay, R.id.character_four, R.id.character_four_hint, 4);
        mCharacterFive = new CharacterHolder(lay, R.id.character_five, R.id.character_five_hint, 5);

        return lay;
    }

    @Override
    public int getWidth() {
        if (shrink) {
            return Utils.dip2px(MyApplication.application, 30);
        } else {
            return Utils.dip2px(MyApplication.application, 80);
        }
    }

    @Override
    public int getHeight() {
        return Utils.dip2px(MyApplication.application, 285);
    }

    public boolean isAreaShow() {
        return areaShow;
    }

    public int getCharacterSelection() {
        if (multiplyTap) {
            if (characterSelectionList == null || characterSelectionList.isEmpty()) {
                return 0;
            }
            int selection = characterSelectionList.get(characterSelectionListPosition);
            characterSelectionListPosition++;
            if (characterSelectionListPosition >= characterSelectionList.size()) {
                characterSelectionListPosition = 0;
            }
            return selection;
        } else {
            return characterSelection;
        }
    }

    public void cancelTap() {
        characterSelection = 0;
        mCharacterOne.cancelTap();
        mCharacterTwo.cancelTap();
        mCharacterThree.cancelTap();
        mCharacterFour.cancelTap();
        mCharacterFive.cancelTap();
        multiplyTapFunction = false;
        Drawable drawable = mMultiplyTapFunction.getDrawable();
        DrawableCompat.setTint(drawable, Utils.getColor(R.color.white_50));
        mMultiplyTapFunction.setImageDrawable(drawable);
    }

    public interface AssistPanelViewListener {
        void shrink();

        void areaShow(boolean areaShow);

        void startTap();
    }

    public class CharacterHolder {
        private ViewGroup mLay;
        private AppCompatImageView mHint;
        private int mNumber;

        private boolean mMultiplyTapFunction;

        public CharacterHolder(View lay, int layId, int hintId, int number) {
            mLay = lay.findViewById(layId);
            mHint = lay.findViewById(hintId);
            mNumber = number;
            mLay.setOnClickListener(v -> {
                Utils.logD(TAG, mNumber + " onClick characterSelection:" + characterSelection);
                if (multiplyTap) {
                    mMultiplyTapFunction = !mMultiplyTapFunction;
                    showMultiplyTapState();
                } else {
                    if (characterSelection != mNumber) {
                        characterSelection = mNumber;
                        mLay.setBackgroundColor(Utils.getColor(R.color.orange_dark));
                        listener.startTap();
                    }
                }
            });
        }

        private void showMultiplyTapState() {
            if (mMultiplyTapFunction) {
                mHint.setImageResource(R.drawable.ic_assist_character_select_hint);
            } else {
                mHint.setImageResource(R.drawable.ic_assist_character_hint);
            }
        }

        public void multiplyTap(boolean function) {
            mMultiplyTapFunction = function;
            showMultiplyTapState();
        }

        public boolean isMultiplyTapFunction() {
            return mMultiplyTapFunction;
        }

        public void cancelTap() {
            mLay.setBackgroundColor(0x00000000);
        }
    }
}
