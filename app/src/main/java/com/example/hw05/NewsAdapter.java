package com.example.hw05;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(@NonNull Context context, int resource, @NonNull List<News> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        News news = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_item,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.textViewtitle = convertView.findViewById(R.id.textViewTitle);
            viewHolder.textViewAuthor = convertView.findViewById(R.id.textViewAuthor);
            viewHolder.textViewdate = convertView.findViewById(R.id.textViewDate);
            viewHolder.imageViewurl = convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textViewtitle.setText(news.getTitle());
        viewHolder.textViewAuthor.setText(news.getAuthor());
        viewHolder.textViewdate.setText(news.getPublishedAt());
        Picasso.get().load(news.getUrlToImage()).into(viewHolder.imageViewurl);
        return convertView;
    }

    private static class ViewHolder {
        TextView textViewtitle;
        TextView textViewAuthor;
        TextView textViewdate;
        ImageView imageViewurl;

    }
}
