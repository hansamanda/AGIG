package magang_2018.aseangamesindonesianguide.model;

/**
 * Created by Anggiat on 8/20/2018.
 */

public class Wisata {
    private String mImageUrl;
    private String mName;
    private String mLat;
    private String mLng;
    private String mAddress;
    private String mDescription;
    private String mTags;
    private String mContent;
    private String mCategory;
    private String mPhone;
    private String mWeb;
    private String mShown;

    public Wisata(String mImageUrl, String mName, String mLat, String mLng, String mAddress, String mDescription, String mTags, String mContent, String mCategory, String mPhone, String mWeb, String mShown) {
        this.mImageUrl = mImageUrl;

        this.mName = mName;
        this.mLat = mLat;
        this.mLng = mLng;
        this.mAddress = mAddress;
        this.mDescription = mDescription;
        this.mTags = mTags;
        this.mContent = mContent;
        this.mCategory = mCategory;
        this.mPhone = mPhone;
        this.mWeb = mWeb;
        this.mShown = mShown;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmLat() {
        return mLat;
    }

    public void setmLat(String mLat) {
        this.mLat = mLat;
    }

    public String getmLng() {
        return mLng;
    }

    public void setmLng(String mLng) {
        this.mLng = mLng;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmTags() {
        return mTags;
    }

    public void setmTags(String mTags) {
        this.mTags = mTags;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getmCategory() {
        return mCategory;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getmWeb() {
        return mWeb;
    }

    public void setmWeb(String mWeb) {
        this.mWeb = mWeb;
    }

    public String getmShown() {
        return mShown;
    }

    public void setmShown(String mShown) {
        this.mShown = mShown;
    }
}
