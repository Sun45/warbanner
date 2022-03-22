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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.document.db.clanwar.TeamModel;
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

    private List<SketchHolder> sketchHolderList;
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
        sketchHolderList = new ArrayList<>();
        try {
            JSONArray sketcharray = new JSONArray(teamModel.getSketch());
            for (int i = 0; i < sketcharray.length(); i++) {
                String sketch = sketcharray.optString(i);
                SketchHolder sketchHolder = new SketchHolder(sketch);
                layList.add(sketchHolder.getLay());
                sketchHolderList.add(sketchHolder);
            }
        } catch (JSONException e) {
            SketchHolder sketchHolder = new SketchHolder(teamModel.getSketch());
            layList.add(sketchHolder.getLay());
            sketchHolderList.add(sketchHolder);
        }
        remarkHolderList = new ArrayList<>();
        try {
            JSONArray remarkarray = new JSONArray(teamModel.getRemarks());
            for (int i = 0; i < remarkarray.length(); i++) {
                JSONObject remark = remarkarray.optJSONObject(i);
                String content = remark.optString("content");
                content = Utils.replaceBlank(content);
                String link = remark.optString("link");
                JSONArray images = remark.optJSONArray("images");
                JSONArray comments = remark.optJSONArray("comments");
                String image = null;
                if (images != null && images.length() > 0) {
                    if (comments != null && comments.length() > 0) {
                        content = comments.getString(0);
                        content = content.replace("←", "");
                    }
                    image = images.getString(0);
                }
                RemarkHolder remarkHolder = new RemarkHolder(content, link, image);
                layList.add(remarkHolder.getLay());
                remarkHolderList.add(remarkHolder);
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

    private class SketchHolder {
        private FrameLayout lay;

        public SketchHolder(String sketch) {
            lay = new FrameLayout(getContext());
            lay.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            TextView textView = new TextView(getContext());
            textView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            textView.setText(sketch);
            lay.addView(textView);
        }

        public FrameLayout getLay() {
            return lay;
        }
    }

    private class RemarkHolder {
        private FrameLayout lay;

        public RemarkHolder(String content, String link, String image) {
            lay = new FrameLayout(getContext());
            lay.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            LinearLayout contentlay = new LinearLayout(getContext());
            contentlay.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            contentlay.setOrientation(LinearLayout.VERTICAL);
            TextView contenttext = new TextView(getContext());
            contenttext.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            contenttext.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            contenttext.setText(content);
            contentlay.addView(contenttext);
            if (!TextUtils.isEmpty(link)) {
                TextView linktext = new TextView(getContext());
                linktext.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                linktext.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                SpannableStringBuilder builder = new SpannableStringBuilder(link);
                builder.setSpan(new ForegroundColorSpan(Utils.getAttrColor(getContext(), R.attr.colorSecondary)), 0, link.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        Uri uri = Uri.parse(link);
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        intent.setData(uri);
                        getContext().startActivity(intent);
                    }
                }, 0, link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                linktext.setMovementMethod(LinkMovementMethod.getInstance());
                linktext.setText(builder, TextView.BufferType.SPANNABLE);
                contentlay.addView(linktext);
            }
            if (!TextUtils.isEmpty(image)) {
                ImageView imageView = new ImageView(getContext());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                ImageRequester.request(image, R.drawable.ic_baseline_image_24).loadImage(imageView);
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PhotoViewer.INSTANCE.setClickSingleImg(image, imageView).setShowImageViewInterface(new PhotoViewer.ShowImageViewInterface() {
                            @Override
                            public void show(@NonNull ImageView imageView, @NonNull String url) {
                                ImageRequester.request(url, R.drawable.ic_baseline_image_24).loadImage(imageView);
                            }
                        }).start(MyApplication.getCurrentActivity());
                    }
                });
                contentlay.addView(imageView);
            }
            AppCompatImageView share = new AppCompatImageView(getContext());
            share.setLayoutParams(new FrameLayout.LayoutParams(Utils.dip2px(getContext(), 40), Utils.dip2px(getContext(), 40), Gravity.RIGHT));
            share.setPadding(Utils.dip2px(getContext(), 5), Utils.dip2px(getContext(), 5), Utils.dip2px(getContext(), 5), Utils.dip2px(getContext(), 5));
            share.setImageResource(R.drawable.ic_baseline_share_24);
            share.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    String str = teamModel.getNumber() + " " + content;
                    if (!TextUtils.isEmpty(link)) {
                        str += "\n" + link;
                    }
                    if (!TextUtils.isEmpty(image)) {
                        str += "\n" + image;
                    }
                    intent.putExtra(Intent.EXTRA_TEXT, str);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "share");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setType("text/plain");
                    getContext().startActivity(Intent.createChooser(intent, Utils.getString(R.string.app_name)));
                }
            });
            lay.addView(share);
            lay.addView(contentlay);
        }

        public FrameLayout getLay() {
            return lay;
        }
    }
}
