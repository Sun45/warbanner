package cn.sun45.warbanner.assist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    private ImageView mAreaShow;

    //收缩
    private boolean shrink;
    private ImageView mShrinkView;

    //角色选择
    private TextView mCharacterOne;
    private TextView mCharacterTwo;
    private TextView mCharacterThree;
    private TextView mCharacterFour;
    private TextView mCharacterFive;

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
        mCharacterOne = lay.findViewById(R.id.character_one);
        mCharacterOne.setOnClickListener(v -> {
            Utils.logD(TAG, "one onClick characterSelection:" + characterSelection);
            if (characterSelection != 1) {
                characterSelection = 1;
                mCharacterOne.setBackgroundColor(Utils.getColor(R.color.orange_dark));
                listener.characterSelect(characterSelection);
            }
        });
        mCharacterTwo = lay.findViewById(R.id.character_two);
        mCharacterTwo.setOnClickListener(v -> {
            Utils.logD(TAG, "two onClick characterSelection:" + characterSelection);
            if (characterSelection != 2) {
                characterSelection = 2;
                mCharacterTwo.setBackgroundColor(Utils.getColor(R.color.orange_dark));
                listener.characterSelect(characterSelection);
            }
        });
        mCharacterThree = lay.findViewById(R.id.character_three);
        mCharacterThree.setOnClickListener(v -> {
            Utils.logD(TAG, "three onClick characterSelection:" + characterSelection);
            if (characterSelection != 3) {
                characterSelection = 3;
                mCharacterThree.setBackgroundColor(Utils.getColor(R.color.orange_dark));
                listener.characterSelect(characterSelection);
            }
        });
        mCharacterFour = lay.findViewById(R.id.character_four);
        mCharacterFour.setOnClickListener(v -> {
            Utils.logD(TAG, "four onClick characterSelection:" + characterSelection);
            if (characterSelection != 4) {
                characterSelection = 4;
                mCharacterFour.setBackgroundColor(Utils.getColor(R.color.orange_dark));
                listener.characterSelect(characterSelection);
            }
        });
        mCharacterFive = lay.findViewById(R.id.character_five);
        mCharacterFive.setOnClickListener(v -> {
            Utils.logD(TAG, "five onClick characterSelection:" + characterSelection);
            if (characterSelection != 5) {
                characterSelection = 5;
                mCharacterFive.setBackgroundColor(Utils.getColor(R.color.orange_dark));
                listener.characterSelect(characterSelection);
            }
        });
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
        return Utils.dip2px(MyApplication.application, 235);
    }

    public boolean isAreaShow() {
        return areaShow;
    }

    public void cancelTap() {
        characterSelection = 0;
        mCharacterOne.setBackgroundColor(0x00000000);
        mCharacterTwo.setBackgroundColor(0x00000000);
        mCharacterThree.setBackgroundColor(0x00000000);
        mCharacterFour.setBackgroundColor(0x00000000);
        mCharacterFive.setBackgroundColor(0x00000000);
    }

    public interface AssistPanelViewListener {
        void shrink();

        void areaShow(boolean areaShow);

        void characterSelect(int characterSelection);
    }
}
