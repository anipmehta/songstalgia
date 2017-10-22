package vandyhacks.com.songstalgia;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import vandyhacks.com.songstalgia.model.SongDetails;
import vandyhacks.com.songstalgia.utilities.NetworkUtils;

/**
 * Created by rishabh on 21/10/17.
 */

public class SongActivity extends AppCompatActivity {

    private TextView songResultsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        songResultsTextView = (TextView) findViewById(R.id.song_results);

        makeSearchQuery(3);
    }

    private void makeSearchQuery(int mood) {


        URL searchUrl = NetworkUtils.buildUrl(mood);

        new QueryTask().execute(searchUrl);
    }

    // COMPLETED (1) Create a class called GithubQueryTask that extends AsyncTask<URL, Void, String>
    public class QueryTask extends AsyncTask<URL, Void, String> {

        // COMPLETED (2) Override the doInBackground method to perform the query. Return the results. (Hint: You've already written the code to perform the query)
        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String searchResults = null;
            try {
                searchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        protected ArrayList<SongDetails> parseHtml(String searchResults){
            //"\"Hello\""  data-vpi-videoid="'(.*?)'"
            Document doc = Jsoup.parse(searchResults);

            ArrayList<SongDetails> songsList = new ArrayList<SongDetails>();


            //video links
            for (Element e : doc.getElementsByClass("videoPlayer")) {
                SongDetails song = new SongDetails();

                song.setUrl(e.attr("data-vpi-videoid"));
                songsList.add(song);
            }

//            int i=0;
//            for (Element e : doc.getElementsByClass("listItem__blather")) {
//
//
//                    if(e.attr("itemprop").contains("description"))
//                        songsList.get(i).setArtist(e.text());
//
//                    i++;
//
//            }
//
//            i=0;
//
//            for (Element e : doc.getElementsByClass("listItem__title")) {
//
////                if((e.child(1).getElementsByClass("listItem_title").contains("name") && e.attr("rel").contains("nofollow")))
//                if(i<30) {
//                    String[] strings = e.html().split("<");
//                    System.out.println("ytytyty" + strings[0] + " " + i);//.("listItem_title").text());
//                    songsList.get(i).setTitle(strings[0]);
//
//                }
//                i++;
//
//
//            }
//
//s

            return songsList;




        }
        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        @Override
        protected void onPostExecute(String searchResults) {
            if (searchResults != null && !searchResults.equals("")) {

                for(SongDetails song: parseHtml(searchResults)){
                    System.out.println("----------" + song.getArtist() + " " + song.getTitle() + " " + song.getUrl());
                }



                songResultsTextView.setText(searchResults);


                /*
                Context context = getApplicationContext();


                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context,searchResults , duration);
                toast.show();
                */
            }
        }
    }
}
