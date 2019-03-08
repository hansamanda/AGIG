package magang_2018.aseangamesindonesianguide.model;

/**
 * Created by Anggiat on 8/18/2018.
 */

public class Cabor {
    private String mImageUrl;
    private String mCreator;
    private String mLikes;

    public Cabor(String imageUrl, String creator, String likes) {
        mImageUrl = imageUrl;
        mCreator = creator;
        mLikes = likes;
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
}


