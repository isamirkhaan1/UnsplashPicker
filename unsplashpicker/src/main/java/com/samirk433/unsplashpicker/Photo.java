package com.samirk433.unsplashpicker;

import com.google.gson.annotations.SerializedName;

/**
 * Created by samirk433 on 4/24/2018.
 * <p>
 * Model class for UnSplash image
 */

public class Photo {


    /*
     * Unique image id which are required for up-voting, down-voting
     * or downloading image
     * */
    String id;


    //URLs of pre-defined image sizes
    @SerializedName("urls")
    Urls urls;


    //Necessary links of the image
    @SerializedName("links")
    Links links;


    //User profile
    @SerializedName("user")
    UserInfo userInfo;


    public String getUrl() {
        return links == null ? null : links.download;
    }

    /*
     * POJO for pre-defined image sizes
     *
     * */
    class Urls {
        public String raw, full, regular, small, thumb;
    }


    /*
     * POJO for "UnSplash Image Links"
     *
     * */
    class Links {
        public String self, html, download, download_location;
    }


    /*
     * Model class for UnSplash user
     *
     * */
    class UserInfo {

        public String id, username, name, location, bio;

        @SerializedName("portfolio_url")
        public String portfolioUrl;


        public String getUrl() {
            return Constants.UNSPLASH_URL + username;
        }
    }

}