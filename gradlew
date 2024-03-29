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
                String[] split = chapterName.split(" ");
                currentMangaToView.imageUrl = currentMangaToView.chapterIntegers.get(Integer.parseInt(split[0]));
                currentMangaToView.currentChapter = Integer.parseInt(split[0]);
                currentMangaToView.currentPage = 1;
                MainActivity.mangaDexManager.currrentManga = currentMangaToView;
                Log.i("test", "setting " + currentMangaToView.imageUrl);
                //TODO switch to reading page
            }
        });

        refreshPull = view.findViewById(R.id.mangachapter_refresh_view);
        refreshPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("test", "onRefresh: notifying of change");
                aa.notifyDataSetChanged();
                refreshPull.setRefreshing(false);
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Scrapes deeper into page for each chapter title and language
     */
    private class DeeperScraper extends AsyncTask<Void, Void, Void> {

        MangaDexManga m;

        public DeeperScraper(MangaDexManga m) {
            this.m = m;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Document d = null;
            try {
                d = Jsoup.connect(m.mainPageUrl).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements elements = d.select("div.chapter-row.d-flex.row.no-gutters.p-2.align-items-center.border-bottom.odd-row");
            for (int i = 1; i < elements.size(); i++) {
                Element ele = elements.get(i);
                Elements eles = ele.select("div.chapter-list-flag.col-auto.text-center.order-lg-4");
                Document dd = Jsoup.parse(eles.html());
                String language = dd.select("span.rounded.flag").attr("title");
                if (language.equals("English")) {
                    chapterList.add(ele.attr("data-chapter") + " - " + ele.attr("data-title"));

                    Document d2 = null;
                    try {
                        d2 = Jsoup.connect("https://mangadex.org/chapter/" + ele.attr("data-id")).get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String s =  d2.getElementsByClass("noselect").get(1).attr("src");
                    Log.i("tests", "stuff " + s);
                    s = s.substring(0,s.lastIndexOf("1")-1);
                    m.chapterIntegers.put(Integer.parseInt(ele.attr("data-chapter")), s);
                }
            }
            return null;
        }

    }
}
                                                                                                                                                                                                                                            