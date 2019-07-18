package com.example.mangareader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MangaChapterPage extends Fragment {

    private ArrayList<String> chapterList;
    private ArrayAdapter<String> aa;
    private SwipeRefreshLayout refreshPull;
    private MangaDexManga currentMangaToView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        chapterList = new ArrayList<>();
        currentMangaToView = MainActivity.mangaDexManager.getByName(getArguments().getString("mangaName"));
        DeeperScraper ds = new DeeperScraper(currentMangaToView);
        ds.execute();

        View v = inflater.inflate(R.layout.fragment_mangachapter_view, container, false);
        setHasOptionsMenu(false);

        return v;
    }

    //TODO deal with caching of chapters
    //TODO give more specific info when clicking on specific page instead of instantly loading it

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        aa = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, chapterList);
        ListView lv = view.findViewById(R.id.manga_chapter_view);
        lv.setAdapter(aa);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String chapterName = (String) parent.getItemAtPosition(position);
                String[] split = chapterName.split(" ")