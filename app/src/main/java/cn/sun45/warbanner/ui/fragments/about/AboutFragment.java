package cn.sun45.warbanner.ui.fragments.about;

import android.content.Intent;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.google.android.material.appbar.MaterialToolbar;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.framework.ui.BaseActivity;
import cn.sun45.warbanner.framework.ui.BaseFragment;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2022/2/20
 * 关于Fragment
 */
public class AboutFragment extends BaseFragment {
    private TextView mText;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_about;
    }

    @Override
    protected void initData() {

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

        mText = mRoot.findViewById(R.id.text);
    }

    @Override
    protected void dataRequest() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        appendStr(builder, "\n作业数据来源");
        appendStr(builder, " ");
        appendLink(builder, "花舞攻略组的在线作业文档", "https://docs.qq.com/sheet/DWkdtR2djbnFiUGRk?tab=ltc6xo");
        appendStr(builder, "\n");
        appendStr(builder, " ");
        appendQQLink(builder, "\n花舞交流群：", "700350785");
        appendStr(builder, "\n");
        appendStr(builder, "\n作业数据网站踩蘑菇");
        appendStr(builder, " ");
        appendLink(builder, "轴区网站", "https://www.caimogu.cc/gzlj.html");
        appendStr(builder, " ");
        appendLink(builder, "踩蘑菇社区", "https://www.caimogu.cc/");
        appendStr(builder, "\n");
        appendStr(builder, "\n作业数据镜像站github");
        appendStr(builder, " ");
        appendLink(builder, "PcrGuildLibrary", "https://github.com/acaly/PcrGuildLibrary");
        appendStr(builder, "\n");
        appendStr(builder, "\n启发及角色数据获取方式来自");
        appendStr(builder, " ");
        appendLink(builder, "静流笔记", "https://github.com/MalitsPlus/ShizuruNotes");
        appendStr(builder, "\n");
        appendStr(builder, "\n数据来源干炸里脊资源站");
        appendStr(builder, " ");
        appendLink(builder, "干炸里脊资源站", "https://redive.estertion.win/");
        appendStr(builder, "\n");
        appendLink(builder, "\n开源地址", "https://github.com/Sun45/warbanner");
        appendStr(builder, "\n");
        appendLink(builder, "\n版本及更新内容", "https://github.com/Sun45/warbanner/releases");
        appendStr(builder, "\n");
        appendLink(builder, "\n最新版安装包下载地址", "https://github.com/Sun45/warbanner/releases/latest/download/warbanner-release.apk");
        appendStr(builder, "\n");
        appendStr(builder, "\n百度网盘地址 \n链接：");
        appendLink(builder, "https://pan.baidu.com/s/1CpzfLEtD_rb9MALHSFquVg", "https://pan.baidu.com/s/1CpzfLEtD_rb9MALHSFquVg");
        appendStr(builder, "\n提取码：1234");
        mText.setMovementMethod(LinkMovementMethod.getInstance());
        mText.setText(builder, TextView.BufferType.SPANNABLE);
    }

    private void appendStr(SpannableStringBuilder builder, String str) {
        builder.append(str);
    }

    private void appendLink(SpannableStringBuilder builder, String str, String link) {
        int start = builder.length();
        int end = start + str.length();
        builder.append(str);
        builder.setSpan(new ForegroundColorSpan(Utils.getAttrColor(getContext(), R.attr.colorSecondary)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Uri uri = Uri.parse(link);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(uri);
                getContext().startActivity(intent);
            }
        }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void appendQQLink(SpannableStringBuilder builder, String name, String qq) {
        builder.append(name);
        int start = builder.length();
        int end = start + qq.length();
        builder.append(qq);
        builder.setSpan(new ForegroundColorSpan(Utils.getAttrColor(getContext(), R.attr.colorSecondary)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                joinQQGroup("jCLm9JM1niDgz5N5V9tu5EjBKPqGbLOD");
            }
        }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /****************
     *
     * 发起添加群流程。群号：花舞-九州界新正双鱼(700350785) 的 key 为： jCLm9JM1niDgz5N5V9tu5EjBKPqGbLOD
     * 调用 joinQQGroup(jCLm9JM1niDgz5N5V9tu5EjBKPqGbLOD) 即可发起手Q客户端申请加群 花舞-九州界新正双鱼(700350785)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回false表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }


    @Override
    protected void onShow() {

    }

    @Override
    protected void onHide() {

    }
}
