package islam.rogirojusalat.com;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Dashboard extends AppCompatActivity {
    private AdView mAdView;
    private InterstitialAd interstitialAd;
    private String AD_UNIT_ID;

    private LinearLayout topicContainer;
    private final List<View> topicRows = new ArrayList<>();
    private final List<Topic> topics = new ArrayList<>();
    private int clickCount = 0;

    private static class Topic {
        final String title;
        final String file;
        final String tag;
        final int icon;

        Topic(String title, String file, String tag, int icon) {
            this.title = title;
            this.file = file;
            this.tag = tag;
            this.icon = icon;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        AD_UNIT_ID = getString(R.string.admob_interstitial_id);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        //ads
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);

        loadAd();

        buildTopicList();

        topicContainer = findViewById(R.id.topicContainer);
        for (Topic topic : topics) {
            addTopicRow(topic);
        }

        View favoritesEntry = findViewById(R.id.favoritesEntry);
        favoritesEntry.setOnClickListener(v -> startActivity(new Intent(Dashboard.this, Favorites.class)));

        EditText searchBox = findViewById(R.id.searchBox);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTopics(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void buildTopicList() {
        topics.add(new Topic("রোগীর অযু ও সালাত", "btn1.html", "মূল বিষয়", R.drawable.ic_book));
        topics.add(new Topic("রোগীর অযু", "btn2.html", "তাহারাত", R.drawable.ic_book));
        topics.add(new Topic("রোগীর সালাত", "btn3.html", "সালাত", R.drawable.ic_book));
        topics.add(new Topic("রোগীর জন্য দোয়া", "btn4.html", "দোয়া", R.drawable.ic_health_plus));
        topics.add(new Topic("অসুস্থ ব্যক্তির রোযার বিধান", "btn5.html", "ফিকহ", R.drawable.ic_book));
        topics.add(new Topic("রোগী দেখতে যাওয়ার ফযীলত ও আদব", "btn6.html", "ফযীলত", R.drawable.ic_star_badge));
        topics.add(new Topic("ধৈর্য ও মুসিবতের ফযীলত", "btn7.html", "ফযীলত", R.drawable.ic_star_badge));
        topics.add(new Topic("মুমূর্ষু ব্যক্তির পাশে করণীয়", "btn8.html", "জানাযা", R.drawable.ic_book));
        topics.add(new Topic("জানাযার সালাতের সংক্ষিপ্ত নিয়ম", "btn9.html", "জানাযা", R.drawable.ic_book));
        topics.add(new Topic("আসমাউল হুসনা - আল্লাহর ৯৯ নাম", "btn10.html", "যিকির", R.drawable.ic_star_badge));
        topics.add(new Topic("প্রতিদিনের প্রয়োজনীয় দোয়াসমূহ", "btn11.html", "দোয়া", R.drawable.ic_health_plus));
        topics.add(new Topic("সালাতের গুরুত্ব ও ফযীলত", "btn12.html", "ফযীলত", R.drawable.ic_star_badge));
        topics.add(new Topic("দুশ্চিন্তা দূর করার আমল", "btn13.html", "যিকির", R.drawable.ic_health_plus));
    }

    private void addTopicRow(Topic topic) {
        View row = LayoutInflater.from(this).inflate(R.layout.row_topic, topicContainer, false);
        ImageView icon = row.findViewById(R.id.rowIcon);
        TextView title = row.findViewById(R.id.rowTitle);
        TextView tag = row.findViewById(R.id.rowTag);
        icon.setImageResource(topic.icon);
        title.setText(topic.title);
        tag.setText(topic.tag);
        row.setOnClickListener(v -> {
            Intent i = new Intent(Dashboard.this, Content.class);
            i.putExtra("file", topic.file);
            i.putExtra("title", topic.title);
            startActivity(i);
            clickCount++;
            if (clickCount % 2 == 0) {
                showInterstitial();
            }
        });
        topicContainer.addView(row);
        topicRows.add(row);
    }

    private void filterTopics(String query) {
        String q = query.trim().toLowerCase(Locale.getDefault());
        for (int i = 0; i < topics.size(); i++) {
            boolean match = q.isEmpty() || topics.get(i).title.toLowerCase(Locale.getDefault()).contains(q)
                    || topics.get(i).tag.toLowerCase(Locale.getDefault()).contains(q);
            topicRows.get(i).setVisibility(match ? View.VISIBLE : View.GONE);
        }
    }

    //intersial ads
    public void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                this,
                AD_UNIT_ID,
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        Dashboard.this.interstitialAd = interstitialAd;
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        Dashboard.this.interstitialAd = null;
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        Dashboard.this.interstitialAd = null;
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        interstitialAd = null;
                    }
                });
    }

    private void showInterstitial() {
        if (interstitialAd != null) {
            interstitialAd.show(this);
        } else {
            loadAd();
        }
    }
}
