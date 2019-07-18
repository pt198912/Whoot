package com.app.whoot.bean;

/**
 * Created by Sunrise on 3/14/2019.
 */

public class VersionsBean {

    /**
     * Version : 1.2.0
     * URL : https://hk.whoot.com/
     * ImgURL : https://hk.whoot.com/gcs/file
     * ImgBucket : world
     */

    private String Version;
    private String URL;
    private String ImgURL;
    private String ImgBucket;

    public String getVersion() {
        return Version;
    }

    public void setVersion(String Version) {
        this.Version = Version;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getImgURL() {
        return ImgURL;
    }

    public void setImgURL(String ImgURL) {
        this.ImgURL = ImgURL;
    }

    public String getImgBucket() {
        return ImgBucket;
    }

    public void setImgBucket(String ImgBucket) {
        this.ImgBucket = ImgBucket;
    }
}
