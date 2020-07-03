package com.example.reactionfb;

public class ReactButton {
    private ReactType mReactType;
    private String mName;
    private int mDrawableId;

    public ReactButton(ReactType mReactType, String mName, int mDrawableId) {
        this.mReactType = mReactType;
        this.mName = mName;
        this.mDrawableId = mDrawableId;
    }

    public ReactType getReactType() {
        return mReactType;
    }

    public void setReactType(ReactType mReactType) {
        this.mReactType = mReactType;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public int getDrawableId() {
        return mDrawableId;
    }

    public void setDrawableId(int mDrawableId) {
        this.mDrawableId = mDrawableId;
    }
}
