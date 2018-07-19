package com.example.android.westtrumpnewsreader;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class ArticleAdapter extends ArrayAdapter<Article> {

    private static final String DATE_SEPARATOR = "T";


    public ArticleAdapter(Context context, List<Article> articles) {
        super(context, 0, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.article_list_item, parent, false);
        }
        Article currentArticle = getItem(position);

        // gets the section name for currentArticle by calling getSectionName()
        // which was declared in Article
        String sectionName = (String) currentArticle.getSectionName();
        // Find the TextView in the location_item.xml layout with the ID location_name
        TextView sectionNameView = (TextView) listItemView.findViewById(R.id.section_name);
        // Set the TextView to the name associated with the currentLocation
        sectionNameView.setText(sectionName);

        // gets the web title for currentArticle by calling getWebTitle()
        // which was declared in Article
        String webTitle = (String) currentArticle.getWebTitle();
        // Find the TextView in the location_item.xml layout with the ID location_name
        TextView webTitleView = (TextView) listItemView.findViewById(R.id.web_title);
        // Set the TextView to the name associated with the currentLocation
        webTitleView.setText(webTitle);

        TextView contributorView = (TextView) listItemView.findViewById(R.id.contributor);

        if(currentArticle.getContributor() != null) {
            String contributor = currentArticle.getContributor();
            contributorView.setText(contributor);
        }

        // gets the web publication date for currentArticle by calling getPublicationDate()
        // which was declared in Article
        String webPublicationDate = currentArticle.getWebPublicationDate();
        String publicationDate;
        String publicationTime;
        if (webPublicationDate.contains(DATE_SEPARATOR)) {
            String[] parts = webPublicationDate.split(DATE_SEPARATOR);
            publicationDate = parts[0] + DATE_SEPARATOR;
            publicationTime = parts[1];
            StringBuilder timeStringBuilder = new StringBuilder(publicationTime);
            timeStringBuilder.deleteCharAt(8);
            publicationTime = timeStringBuilder.toString();
            StringBuilder dateStringBuilder = new StringBuilder(publicationDate);
            dateStringBuilder.deleteCharAt(10);
            publicationDate = dateStringBuilder.toString();
        } else {
            publicationTime = getContext().getString(R.string.no_time);
            publicationDate = webPublicationDate;
            StringBuilder sb = new StringBuilder(publicationDate);
            sb.deleteCharAt(10);
            publicationDate = sb.toString();
        }

        // Find the TextView in the article_list_item.xml layout with the ID publication date
        TextView publicationDateView = (TextView) listItemView.findViewById(R.id.publication_date);
        // Set the TextView to the name associated with the currentLocation
        publicationDateView.setText(publicationDate);

        // Find the TextView in the article_list_item.xml layout with the ID publication time
        TextView publicationTimeView = (TextView) listItemView.findViewById(R.id.publication_time);
        // Set the TextView to the name associated with the currentLocation
        publicationTimeView.setText(publicationTime);

        return listItemView;


    }
}