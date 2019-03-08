package magang_2018.aseangamesindonesianguide.content;


public class ExampleItemAtlit {
    private String mImageUrl;
    private String mCreator;
    private String mLikes;
    private String mDesAtlit;

    public ExampleItemAtlit(String imageUrl, String creator, String likes, String desAtlit) {
        mImageUrl = imageUrl;
        mCreator = creator;
        mLikes = likes;
        mDesAtlit = desAtlit;

    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getCreator() {
        return mCreator;
    }

    public String getLikeCount() {
        return mLikes;
    }

    public String getDesc(){ return  mDesAtlit;}
}
