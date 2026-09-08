package islam.rogirojusalat.com;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Set;

public class Favorites extends AppCompatActivity {
    private AdView mAdView;
    private LinearLayout favoritesContainer;
    private TextView emptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);

        favoritesContainer = findViewById(R.id.favoritesContainer);
        emptyState = findViewById(R.id.emptyState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        renderFavorites();
    }

    private void renderFavorites() {
        favoritesContainer.removeAllViews();
        Set<String> entries = Prefs.getFavoriteEntries(this);
        if (entries.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            return;
        }
        emptyState.setVisibility(View.GONE);
        for (String entry : entries) {
            final String file = Prefs.fileFromEntry(entry);
            final String title = Prefs.titleFromEntry(entry);
            View row = LayoutInflater.from(this).inflate(R.layout.row_topic, favoritesContainer, false);
            ImageView icon = row.findViewById(R.id.rowIcon);
            TextView rowTitle = row.findViewById(R.id.rowTitle);
            TextView rowTag = row.findViewById(R.id.rowTag);
            icon.setImageResource(R.drawable.ic_heart);
            rowTitle.setText(title);
            rowTag.setText("পছন্দের তালিকা");
            row.setOnClickListener(v -> {
                Intent i = new Intent(Favorites.this, Content.class);
                i.putExtra("file", file);
                i.putExtra("title", title);
                startActivity(i);
            });
            favoritesContainer.addView(row);
        }
    }
}
