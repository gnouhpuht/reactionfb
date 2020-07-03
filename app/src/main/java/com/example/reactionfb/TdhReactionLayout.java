package com.example.reactionfb;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TdhReactionLayout extends FrameLayout {
    private TdhReactionView mReactionViewTop;
    private TdhReactionView mReactionViewBottom;
    private long mTimeStartEvent = 0;
    private Handler mHandler = new Handler();
    private View mCurrentAnchorView;

    private int mMarginLeftAnchor = 0;

    public TdhReactionLayout(@NonNull Context context) {
        super(context);
        initView(context, null);
    }

    public TdhReactionLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public TdhReactionLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {

    }

    public void initReactionView() {
        List<ReactButton> reactButtons = new ArrayList<>();
        reactButtons.add(new ReactButton(ReactType.LIKE, "Thích", R.drawable.like_new));
        reactButtons.add(new ReactButton(ReactType.HAHA, "Haha", R.drawable.haha_new));
        reactButtons.add(new ReactButton(ReactType.LOVE, "Yêu thích", R.drawable.tym_new));
        reactButtons.add(new ReactButton(ReactType.SAD, "Buồn", R.drawable.sad_new));
        reactButtons.add(new ReactButton(ReactType.WOW, "WOW", R.drawable.wow_new));

        mReactionViewTop = new TdhReactionView(getContext(), TdhReactionView.Gravity.TOP, reactButtons);
        addView(mReactionViewTop);
        mReactionViewTop.setVisibility(INVISIBLE);

        mReactionViewBottom = new TdhReactionView(getContext(), TdhReactionView.Gravity.BOTTOM, reactButtons);
        addView(mReactionViewBottom);
        mReactionViewBottom.setVisibility(INVISIBLE);

        mReactionViewTop.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mReactionViewTop.setOnTouchAtPoint(event);
                return false;
            }
        });

        mReactionViewBottom.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mReactionViewBottom.setOnTouchAtPoint(event);
                return false;
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (3 != getChildCount()) {
            throw new ReactLayoutException("Child views must be 1 views. Child count: " + getChildCount());
        }
    }

    public void showReaction(View viewAnchor) {
        mCurrentAnchorView = viewAnchor;
        Rect offsetViewBounds = new Rect();
        viewAnchor.getDrawingRect(offsetViewBounds);
        try {
            offsetDescendantRectToMyCoords(viewAnchor, offsetViewBounds);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (offsetViewBounds.top > mReactionViewTop.getContainerHeightTop() + viewAnchor.getMeasuredHeight() + 30) {
            if (mReactionViewTop != null) {
                LayoutParams params = (LayoutParams) mReactionViewTop.getLayoutParams();
                int marginTop = offsetViewBounds.top - mReactionViewTop.getContainerHeightTop() - viewAnchor.getMeasuredHeight();
                Log.e("@@@@@@", "Margin Top: " + marginTop);
                Log.e("@@@@@@", "Offset Top: " + offsetViewBounds.top);

                params.setMargins(
                        offsetViewBounds.left + mMarginLeftAnchor,
                        marginTop,
                        0,
                        0
                );
                mReactionViewTop.setLayoutParams(params);
                mReactionViewTop.invalidate();
                mReactionViewTop.show();
            }
        } else {
            if (mReactionViewBottom != null) {
                LayoutParams params = (LayoutParams) mReactionViewBottom.getLayoutParams();
                int marginTop = offsetViewBounds.top + mReactionViewBottom.getContainerHeightBottom();
                Log.e("@@@@@@", "Margin Top: " + marginTop);
                Log.e("@@@@@@", "Offset Top: " + offsetViewBounds.top);

                params.setMargins(
                        offsetViewBounds.left + mMarginLeftAnchor,
                        marginTop,
                        0,
                        0
                );
                mReactionViewBottom.setLayoutParams(params);
                mReactionViewBottom.invalidate();
                mReactionViewBottom.show();
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isShowingReaction()) {
            if (mCurrentAnchorView != null) {
                mCurrentAnchorView.dispatchTouchEvent(ev);
            }
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void hideReaction(OnReactionSelectListener onReactionSelectListener) {
        if (mReactionViewTop != null && mReactionViewTop.getVisibility() == VISIBLE) {
            if (onReactionSelectListener != null) {
                if (mReactionViewTop.getCurrentItem() != -1) {
                    onReactionSelectListener.onReactionSelect(mReactionViewTop.getCurrentItem());
                } else {
                    onReactionSelectListener.onDismissReaction();
                }
            }
            mReactionViewTop.hide();
        }

        if (mReactionViewBottom != null && mReactionViewBottom.getVisibility() == VISIBLE) {
            if (onReactionSelectListener != null) {
                if (mReactionViewBottom.getCurrentItem() != -1) {
                    onReactionSelectListener.onReactionSelect(mReactionViewBottom.getCurrentItem());
                } else {
                    onReactionSelectListener.onDismissReaction();
                }
            }
            mReactionViewBottom.hide();
        }
    }

    public boolean isShowingReaction() {
        if (mReactionViewTop == null || mReactionViewBottom == null) return false;
        return mReactionViewTop.getVisibility() == VISIBLE || mReactionViewBottom.getVisibility() == VISIBLE;
    }

    public void dispatchTouchEventCustom(MotionEvent event) {
        Log.e("@@@@@@", "dispatchTouchEventCustom");
        if (mReactionViewTop != null) {
            mReactionViewTop.dispatchTouchEvent(event);
        }
        if (mReactionViewBottom != null) {
            mReactionViewBottom.dispatchTouchEvent(event);
        }
    }

    public OnTouchListener initTouchListener(final OnReactionSelectListener onReactionSelectListener) {
        return new OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mTimeStartEvent = System.currentTimeMillis();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showReaction(v);
                        }
                    }, ViewConfiguration.getLongPressTimeout());
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (System.currentTimeMillis() - mTimeStartEvent <= ViewConfiguration.getTapTimeout()) {
                        if (onReactionSelectListener != null) {
                            onReactionSelectListener.onClick();
                        }
                    }
                    mHandler.removeCallbacksAndMessages(null);
                    if (isShowingReaction()) {
                        hideReaction(onReactionSelectListener);
                    }
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (System.currentTimeMillis() - mTimeStartEvent > ViewConfiguration.getLongPressTimeout() && isShowingReaction()) {
                        dispatchTouchEventCustom(event);
                    } else {
                        if (isShowingReaction()) {
                            hideReaction(onReactionSelectListener);
                        }
                    }
                    return true;
                }
                return false;
            }
        };
    }

    public void setMarginLeftAnchor(int mMarginLeftAnchor) {
        this.mMarginLeftAnchor = mMarginLeftAnchor;
    }

    public interface OnReactionSelectListener {
        void onClick();

        void onReactionSelect(int index);

        void onDismissReaction();
    }
}
