package com.example.android.westtrumpnewsreader;

public class Article {

    private String mSectionName;

    private String mWebTitle;

    private String mWebPublicationDate;

    private String mWebUrl;

    private String mContributor;

    public Article(String sectionName, String webTitle, String webPublicationDate, String webUrl) {
        mSectionName = sectionName;
        mWebTitle = webTitle;
        mWebPublicationDate = webPublicationDate;
        mWebUrl = webUrl;
    }

    public Article(String sectionName, String webTitle, String contributor, String webPublicationDate, String webUrl) {
        mSectionName = sectionName;
        mWebTitle = webTitle;
        mWebPublicationDate = webPublicationDate;
        mWebUrl = webUrl;
        mContributor = contributor;
    }

    public String getSectionName() {return mSectionName;}

    public String getWebTitle() {return mWebTitle;}

    public String getWebPublicationDate() {return mWebPublicationDate;}

    public String getWebUrl() {return mWebUrl;}

    public String getContributor() {return mContributor;}
}