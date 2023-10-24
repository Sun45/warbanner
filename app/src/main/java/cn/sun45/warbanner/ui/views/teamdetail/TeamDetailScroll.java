package cn.sun45.warbanner.ui.views.teamdetail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.wanglu.photoviewerlibrary.PhotoViewer;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.document.database.source.models.TeamModel;
import cn.sun45.warbanner.framework.MyApplication;
import cn.sun45.warbanner.framework.image.ImageRequester;
import cn.sun45.warbanner.util.Utils;

/**
 * Created by Sun45 on 2021/7/25
 * 阵容详情Scroll
 */
public class TeamDetailScroll extends ScrollView {
    private LinearLayout lay;

    private TeamModel teamModel;

    private List<RemarkHolder> remarkHolderList;

    public TeamDetailScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
        lay = new LinearLayout(context);
        lay.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.setPadding(Utils.dip2px(getContext(), 5), 0, Utils.dip2px(getContext(), 5), 0);
        addView(lay);
    }

    public void setTeamModel(TeamModel teamModel) {
        this.teamModel = teamModel;
        lay.removeAllViews();
        List<View> layList = new ArrayList<>();
        remarkHolderList = new ArrayList<>();
        List<TeamModel.TimeLine> timeLineList = teamModel.getTimeLines();
        for (TeamModel.TimeLine timeLine : timeLineList) {
            RemarkHolder remarkHolder = new RemarkHolder(timeLine);
            layList.add(remarkHolder.getLay());
            remarkHolderList.add(remarkHolder);
        }
        for (int i = 0; i < layList.size(); i++) {
            if (i != 0) {
                View divider = new View(getContext());
                divider.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dip2px(getContext(), 10)));
                divider.setBackgroundResource(R.drawable.ic_teamgroupscreen_teamdivider);
                lay.addView(divider);
            }
            lay.addView(layList.get(i));
        }
    }

    private class RemarkHolder {
        private FrameLayout lay;

        public RemarkHolder(TeamModel.TimeLine timeLine) {
            String title = timeLine.getTitle();
            int returnTime = timeLine.getReturnTime();
            String description = timeLine.getDescription();
            String videoUrl = timeLine.getVideoUrl();
            List<TeamModel.Image> imageList = timeLine.getImageList();
            lay = new FrameLayout(getContext());
            lay.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            LinearLayout contentlay = new LinearLayout(getContext());
            contentlay.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            contentlay.setOrientation(LinearLayout.VERTICAL);
            TextView titleText = new TextView(getContext());
            titleText.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            if (teamModel.isFinish() || returnTime == 0) {
                titleText.setText(title);
            } else {
                SpannableStringBuilder builder = new SpannableStringBuilder(title + " 返" + returnTime + "s");
                builder.setSpan(new ForegroundColorSpan(Utils.getAttrColor(getContext(), R.attr.colorSecondary)), title.length() + 1, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                titleText.setText(builder);
            }
            contentlay.addView(titleText);
            if (!TextUtils.isEmpty(description)) {
                TextView descriptionText = new TextView(getContext());
                descriptionText.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                descriptionText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                descriptionText.setText(description);
                contentlay.addView(descriptionText);
            }
            if (!TextUtils.isEmpty(videoUrl)) {
                TextView linktext = new TextView(getContext());
                linktext.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                linktext.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                SpannableStringBuilder builder = new SpannableStringBuilder(videoUrl);
                builder.setSpan(new ForegroundColorSpan(Utils.getAttrColor(getContext(), R.attr.colorSecondary)), 0, videoUrl.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        Uri uri = Uri.parse(videoUrl);
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        intent.setData(uri);
                        getContext().startActivity(intent);
                    }
                }, 0, videoUrl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                linktext.setMovementMethod(LinkMovementMethod.getInstance());
                linktext.setText(builder, TextView.BufferType.SPANNABLE);
                contentlay.addView(linktext);
            }
            if (imageList != null) {
                for (TeamModel.Image image : imageList) {
                    String thumb = image.getThumb();
                    String poster = image.getPoster();
                    if (!TextUtils.isEmpty(thumb)) {
                        ImageView imageView = new ImageView(getContext());
                        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        ImageRequester.request(thumb, R.drawable.ic_baseline_image_24).loadImage(imageView);
                        imageView.setOnClickListener(v -> PhotoViewer.INSTANCE.setClickSingleImg(poster, imageView)
                                .setShowImageViewInterface((imageView1, url) -> ImageRequester.request(url, R.drawable.ic_baseline_image_24).loadImage(imageView1))
                                .start(MyApplication.getCurrentActivity()));
                        contentlay.addView(imageView);
                    }
                }
            }
            AppCompatImageView share = new AppCompatImageView(getContext());
            share.setLayoutParams(new FrameLayout.LayoutParams(Utils.dip2px(getContext(), 40), Utils.dip2px(getContext(), 40), Gravity.RIGHT));
            share.setPadding(Utils.dip2px(getContext(), 5), Utils.dip2px(getContext(), 5), Utils.dip2px(getContext(), 5), Utils.dip2px(getContext(), 5));
            share.setImageResource(R.drawable.ic_baseline_share_24);
            share.setOnClickListener(v -> {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(teamModel.getSn());
                stringBuilder.append("\n" + timeLine.getShare());
                intent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());
                intent.putExtra(Intent.EXTRA_SUBJECT, "share");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setType("text/plain");
                getContext().startActivity(Intent.createChooser(intent, Utils.getString(R.string.app_name)));
            });
            lay.addView(share);
            lay.addView(contentlay);
        }

        public FrameLayout getLay() {
            return lay;
        }
    }
}
