package cn.sun45.warbanner.assist;

import android.graphics.Rect;

import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2022/2/23
 * 辅助功能数据模型
 */
public class AssistDataModel {
    //角色区域
    private Rect characterOne;
    private Rect characterTwo;
    private Rect characterThree;
    private Rect characterFour;
    private Rect characterFive;

    private int[] characterOnePoint;
    private int[] characterTwoPoint;
    private int[] characterThreePoint;
    private int[] characterFourPoint;
    private int[] characterFivePoint;

    public AssistDataModel() {
        int[] screenSize = Utils.getScreenSize();
        float width = screenSize[0];
        float height = screenSize[1];
        float w = width;
        float h = height;
        float startx = 0;
        float starty = 0;
        float p = width / height;
        float standard = 16f / 9f;
        if (p < standard) {
            h = width / standard;
            starty = height - h;
        } else if (p > standard) {
            w = h * standard;
            startx = (width - w) / 2;
        }
        int top = (int) (starty + h * 590 / 810);
        int bottom = (int) (starty + h * 730 / 810);
        int size = (int) (w * 140 / 1440);
        int characterOneLeft = (int) (startx + w * 290 / 1440);
        int characterTwoLeft = (int) (startx + w * 470 / 1440);
        int characterThreeLeft = (int) (startx + w * 650 / 1440);
        int characterFourLeft = (int) (startx + w * 830 / 1440);
        int characterFiveLeft = (int) (startx + w * 1010 / 1440);
        characterOne = new Rect(characterOneLeft, top, characterOneLeft + size, bottom);
        characterTwo = new Rect(characterTwoLeft, top, characterTwoLeft + size, bottom);
        characterThree = new Rect(characterThreeLeft, top, characterThreeLeft + size, bottom);
        characterFour = new Rect(characterFourLeft, top, characterFourLeft + size, bottom);
        characterFive = new Rect(characterFiveLeft, top, characterFiveLeft + size, bottom);
        int centery = (top + bottom) / 2;
        characterOnePoint = new int[]{characterOneLeft + size / 2, centery};
        characterTwoPoint = new int[]{characterTwoLeft + size / 2, centery};
        characterThreePoint = new int[]{characterThreeLeft + size / 2, centery};
        characterFourPoint = new int[]{characterFourLeft + size / 2, centery};
        characterFivePoint = new int[]{characterFiveLeft + size / 2, centery};
    }

    public Rect getCharacterOne() {
        return characterOne;
    }

    public Rect getCharacterTwo() {
        return characterTwo;
    }

    public Rect getCharacterThree() {
        return characterThree;
    }

    public Rect getCharacterFour() {
        return characterFour;
    }

    public Rect getCharacterFive() {
        return characterFive;
    }

    public int[] getTapPoint(int characterSelection) {
        int[] point = null;
        switch (characterSelection) {
            case 1:
                point = characterOnePoint;
                break;
            case 2:
                point = characterTwoPoint;
                break;
            case 3:
                point = characterThreePoint;
                break;
            case 4:
                point = characterFourPoint;
                break;
            case 5:
                point = characterFivePoint;
                break;
        }
        return point;
    }
}
