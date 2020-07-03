package com.example.reactionfb;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TdhReactionView extends FrameLayout {
    private View mReactionContainerView;
    private List<View> mReactionViews;
    private TextView mReactionNameTv;
    private int mCurrentItem = Integer.MIN_VALUE;
    private Gravity mGravity = Gravity.TOP;

    private LinearLayout mReactGroupView;

    private List<ReactButton> mReactButtons;

    public TdhReactionView(Context context) {
        super(context);
        initView(context, null);
    }

    public TdhReactionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public TdhReactionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public TdhReactionView(Context context, Gravity gravity, List<ReactButton> reactButtons) {
        super(context);
        this.mGravity = gravity;
        mReactButtons = new ArrayList<>();
        mReactButtons.addAll(reactButtons);
        initView(context, null);
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) return;
        View view = inflater.inflate(mGravity == Gravity.TOP ? R.layout.reaction_view_top : R.layout.reaction_view_bottom, this, true);

        int width = dpToPx(45)*(mReactButtons.size()) + dpToPx(10);
        LayoutParams param = new LayoutParams(width, dpToPx(45));

        mReactionContainerView = view.findViewById(R.id.reaction_content_view);
        mReactionNameTv = view.findViewById(R.id.reaction_name_tv);

        mReactGroupView = view.findViewById(R.id.react_group_view);

        if (mGravity == Gravity.TOP) {
            param.topMargin  = dpToPx(75);
            param.bottomMargin = dpToPx(30);
        } else {
            param.topMargin = dpToPx(30);
        }
        mReactionContainerView.setLayoutParams(param);

        if (mReactButtons == null) {
            mReactButtons = new ArrayList<>();
        }
        if (mReactionViews == null) {
            mReactionViews = new ArrayList<>();
        }

        for (ReactButton reactButton : mReactButtons) {

            ImageView imageView = new ImageView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(45), dpToPx(45));
            if (mGravity == Gravity.TOP) {
                params.bottomMargin = dpToPx(30);
            } else {
                params.topMargin = dpToPx(30);
            }
            imageView.setLayoutParams(params);
            int padding = dpToPx(5);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setImageResource(reactButton.getDrawableId());
            mReactGroupView.addView(imageView);

            mReactionViews.add(imageView);
        }
    }

    public void show() {
        mCurrentItem = Integer.MIN_VALUE;
        mReactionNameTv.setVisibility(GONE);

        setVisibility(View.VISIBLE);

        mReactionContainerView.setAlpha(0.0f);
        mReactionContainerView.setTranslationY(mGravity.getValue() * 70);
        for (int i = 0; i < mReactionViews.size(); i++) {
            View view = mReactionViews.get(i);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.width = dpToPx(45);
            params.height = dpToPx(45);
            view.setLayoutParams(params);
            view.invalidate();

            view.setTranslationY(mGravity.getValue() * 150);
            view.setScaleX(0.0f);
            view.setScaleY(0.0f);
        }

        mReactionContainerView.animate()
                .translationY(0)
                .alpha(1.0f)
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                });

        for (int i = 0; i < mReactionViews.size(); i++) {
            View view = mReactionViews.get(i);
            view.animate()
                    .translationY(0)
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setStartDelay(100 + i * 30)
                    .setDuration(200)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                        }
                    });
        }
    }

    public void hide() {
        mReactionNameTv.setVisibility(GONE);
        mReactionContainerView.animate()
                .translationY(mReactionContainerView.getMeasuredHeight())
                .alpha(0.0f)
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                });

        for (int i = 0; i < mReactionViews.size(); i++) {
            View view = mReactionViews.get(i);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.width = dpToPx(45);
            params.height = dpToPx(45);
            view.setLayoutParams(params);
            view.invalidate();
            final int finalIndex = i;
            view.animate()
                    .translationY(mGravity.getValue() * mReactionContainerView.getMeasuredHeight())
                    .scaleX(0.0f)
                    .scaleY(0.0f)
                    .setDuration(200)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);

                            if (finalIndex == mReactionViews.size() - 1) {
                                setVisibility(GONE);
                            }
                        }
                    });
        }
        mCurrentItem = Integer.MIN_VALUE;
    }

    public void setOnTouchAtPoint(MotionEvent event) {
        int[] locationOfView = new int[2];
        getLocationOnScreen(locationOfView);
        int widthOfItem = mReactionContainerView.getMeasuredWidth() / 6;
        int currentItem = (int) (Math.floor((event.getX() - locationOfView[0]) / widthOfItem));
        if (currentItem < 0 || currentItem >= mReactionViews.size()) {
            mReactionNameTv.setVisibility(GONE);
            if (mCurrentItem == Integer.MIN_VALUE) {
                return;
            }
            mCurrentItem = Integer.MIN_VALUE;
            for (int i = 0; i < mReactionViews.size(); i++) {
                final View reactionView = mReactionViews.get(i);
                ValueAnimator anim = ValueAnimator.ofInt(reactionView.getMeasuredHeight(), dpToPx(45));
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int val = (Integer) valueAnimator.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = reactionView.getLayoutParams();
                        layoutParams.width = val;
                        layoutParams.height = val;
                        reactionView.setLayoutParams(layoutParams);
                    }
                });
                anim.setDuration(150);
                anim.start();
            }
        } else {
            if (mCurrentItem == currentItem) {
                return;
            }
            mCurrentItem = currentItem;
            mReactionNameTv.setText(mReactButtons.get(mCurrentItem).getName());
            mReactionNameTv.setVisibility(VISIBLE);
            mReactionNameTv.measure(0, 0);

            int sizeActive = dpToPx(90);
            int sizeSmall = dpToPx(36);

            int nameTvWidthPx = mReactionNameTv.getMeasuredWidth();
            int leftMarginOfText = sizeSmall * currentItem + sizeActive / 2 - nameTvWidthPx / 2;
            LayoutParams nameTvParams = (LayoutParams) mReactionNameTv.getLayoutParams();
            nameTvParams.leftMargin = leftMarginOfText;
            mReactionNameTv.setLayoutParams(nameTvParams);

            for (int i = 0; i < mReactionViews.size(); i++) {
                final View reactionView = mReactionViews.get(i);
                ValueAnimator anim = ValueAnimator.ofInt(reactionView.getMeasuredHeight(), mCurrentItem == i ? sizeActive : sizeSmall);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int val = (Integer) valueAnimator.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = reactionView.getLayoutParams();
                        layoutParams.width = val;
                        layoutParams.height = val;
                        reactionView.setLayoutParams(layoutParams);
                    }
                });
                anim.setDuration(150);
                anim.start();
            }
        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public int getContainerHeightTop() {
        return dpToPx(90);
    }

    public int getContainerHeightBottom() {
        return dpToPx(30);
    }

    public enum Gravity {
        TOP,
        BOTTOM;

        public int getValue() {
            return this == TOP ? 1 : -1;
        }
    }

    public int getCurrentItem() {
        if (mCurrentItem >= 0 && mCurrentItem < mReactionViews.size()) {
            return mCurrentItem;
        }
        return -1;
    }
}
