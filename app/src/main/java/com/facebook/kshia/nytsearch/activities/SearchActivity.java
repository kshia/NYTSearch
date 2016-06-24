package com.facebook.kshia.nytsearch.activities;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.kshia.nytsearch.Article;
import com.facebook.kshia.nytsearch.EndlessRecyclerViewScrollListener;
import com.facebook.kshia.nytsearch.FilterFragment;
import com.facebook.kshia.nytsearch.FilterSettings;
import com.facebook.kshia.nytsearch.R;
import com.facebook.kshia.nytsearch.ItemClickSupport;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity implements FilterFragment.FilterFragmentListener{

    String searchQuery;
    RecyclerView rvArticles;

    ArrayList<Article> articles;
    ArticleRecyclerAdapter adapter;
    StaggeredGridLayoutManager layoutManager;
    DialogFragment newFragment;
    EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    FilterSettings filterSettings;

    final int TOP_STORY = 0;
    final int NORMAL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();
        filterSettings = new FilterSettings();
    }

    public void setupViews() {

        rvArticles = (RecyclerView) findViewById(R.id.rvArticles);

        // Create adapter passing in the sample user data
        // Attach the adapter to the recyclerview to populate items
        articles = new ArrayList<>();
        adapter = new ArticleRecyclerAdapter(this, articles);
        rvArticles.setAdapter(adapter);

        int numColumns = 3;     // Default to 3 columns for portrait
        // Set layout manager to position the items
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            numColumns = 4;
        }
        layoutManager = new StaggeredGridLayoutManager(numColumns, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        rvArticles.setLayoutManager(layoutManager);

        // Add OnClickListener for article view
        ItemClickSupport.addTo(rvArticles).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Log.d("DEBUT", "click registered");Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
                        Article article = articles.get(position);

                        i.putExtra("article", article);
                        startActivity(i);

                    }
                }
        );

        loadTopStories();
    }

    // Append more data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void customLoadMoreDataFromApi(int page) {

        // Get params for API call
        //String query = etQuery.getText().toString();
        RequestParams params = new RequestParams();

        params.put("api-key", "1d306c0b070d482ca19cf71f537479ca");
        params.put("page", page);
        params.put("q", searchQuery);

        applyFilters(params);

        makeCall(params);

        // For efficiency purposes, notify the adapter of only the elements that got changed
        // curSize will equal to the index of the first element inserted because the list is 0-indexed
    }

    public void applyFilters(RequestParams params) {
        String beginDate = filterSettings.getBegin_date();
        String news_desk = filterSettings.getNews_desk();
        String sort = filterSettings.getSort();
        if (filterSettings.getBegin_date() != null) {

        }
        if (news_desk != null) {
            if (!news_desk.equals("All")){
                params.put("fq", "news_desk:(" + news_desk + ")");
                Log.d("Filters", "News_desk filter applied: " + news_desk);
            }

        }
        if (sort != null) {
            if (!sort.equals("Relevance")) {
                params.put("sort", sort);
                Log.d("Filters", "Sort filter applied: " + sort);
            }

        }
        if (beginDate != null) {
            params.put("begin_date", beginDate);
            Log.d("Filters", "Begin_date filter applied: " + beginDate);
        }
    }

    public void onArticleSearch() {

        Log.d("Filters", "News_desk: "  + filterSettings.getNews_desk()
                + " |Sort: " + filterSettings.getSort() + " |Begin_date: " + filterSettings.getBegin_date());

        articles.clear();
        adapter.notifyDataSetChanged();

        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                customLoadMoreDataFromApi(page);
            }
        };

        RequestParams params = new RequestParams();
        params.put("api-key", "1d306c0b070d482ca19cf71f537479ca");
        params.put("page", 0);
        params.put("q", searchQuery);

        applyFilters(params);

        makeCall(params);
    }


    public void openFilter() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        newFragment = FilterFragment.newInstance();
        newFragment.show(ft, "dialog");
    }

    @Override
    public void onFilterFinished() {

        if (searchQuery == null || searchQuery.equals("")) {
            Toast.makeText(this, "Enter a query before filtering", Toast.LENGTH_SHORT).show();
            loadTopStories();
        }
        else {
            onArticleSearch();
        }
    }

    public void loadTopStories () {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/topstories/v2/home.json?api-key=1d306c0b070d482ca19cf71f537479ca";
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJsonResults = null;
                try {
                    articleJsonResults = response.getJSONArray("results");
                    Log.d("TOPSTORIES", articleJsonResults.toString());

                    articles.addAll(Article.fromJSONArray(articleJsonResults, TOP_STORY));
                    adapter.notifyDataSetChanged();
                    Log.d("TOPSTORIES", articles.toString());

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public void makeCall (RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        Log.d("Filters", url + "?" + params);
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJsonResults = null;

                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    Log.d("DEBUG", articleJsonResults.toString());

                    rvArticles.clearOnScrollListeners();
                    rvArticles.addOnScrollListener(endlessRecyclerViewScrollListener);

                    // changes arraylist and notifies adapter
                    articles.addAll(Article.fromJSONArray(articleJsonResults, NORMAL));
                    adapter.notifyDataSetChanged();
                    Log.d("DEBUG", articles.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", "inside filtered call failed");
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

        int curSize = adapter.getItemCount();
        adapter.notifyItemRangeInserted(curSize, articles.size() - 1);
    }

    public FilterSettings getFilterSettings() {
        return filterSettings;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchQuery = query;
                getSupportActionBar().setTitle(query);

                searchView.clearFocus();

                onArticleSearch();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_filter) {
            openFilter();
        }

        return super.onOptionsItemSelected(item);
    }

}
