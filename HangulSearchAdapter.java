package com.how2marry.planery.util;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chihacker on 2016. 7. 31..
 */
public abstract class HangulSearchAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<T> original;
    private List<T> searcher;

    private Map<String, HangulSearcher.HangulPos> startPos;

    private String searchText;

    public HangulSearchAdapter(){
        original = new ArrayList<>();
        searcher = new ArrayList<>();
        startPos = new HashMap<>();
    }

    public List<T> getOriginalData(){
        if(original == null) original = new ArrayList<>();
        return original;
    }

    public List<T> getSearchedData(){
        if(searcher == null) searcher = new ArrayList<>();
        return searcher;
    }

    public void setOriginalData(List<T> data){

        if(original == null) original = new ArrayList<>();
        if(searcher == null) searcher = new ArrayList<>();

        original.clear();
        searcher.clear();

        original.addAll(data);
        searcher.addAll(original);
        notifyDataSetChanged();

    }

    public T getSearchedItem(int position){
        return searcher.get(position);
    }

    public int getSearchedSize(){
        return searcher.size();
    }

    public int getOriginalSize(){
        return original.size();
    }

    public boolean isSearchMode(){
        return searchText!=null && !searchText.isEmpty();
    }

    public void search(@NonNull String text){

        this.searchText = text;

        if(text.isEmpty()){
            searcher.clear();
            searcher.addAll(original);
            startPos.clear();
            notifyDataSetChanged();
            return;
        }

        List<T> items = new ArrayList<>();
        Map<String,HangulSearcher.HangulPos> startItems = new HashMap<>();

        for(T item : original){

            HangulSearcher.HangulPos pos = HangulSearcher.getMatchingPosition(getText(item),searchText);

            if( pos != null){
                items.add(item);
                startItems.put(getObjectId(item),pos);
            }
        }

        searcher.clear();
        searcher.addAll(items);
        startPos.clear();
        startPos.putAll(startItems);
        notifyDataSetChanged();
    }

    public abstract String getText(T item);
    public abstract String getObjectId(T item);

    public String getSearchText(){
        return searchText;
    }

    public int getSearchTextSize(){
        return searchText.length();
    }

    public int getSearchItemPos(T item){
        for(int i=0; i < searcher.size(); i++){
            if(getObjectId(searcher.get(i)).equals(getObjectId(item))){
                return i;
            }
        }

        return -1;
    }


    public SpannableStringBuilder getSearchColoredString(T item){

        String text = getText(item).trim();

        HangulSearcher.HangulPos pos = startPos.get(getObjectId(item));

        SpannableStringBuilder sps = new SpannableStringBuilder(text);

        LogUtils.d("text : "+text+ " / "+pos);


        if(pos != null){
            sps.setSpan(new ForegroundColorSpan(Color.parseColor("#f37161")),pos.start,pos.end+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return sps;
    }




}
