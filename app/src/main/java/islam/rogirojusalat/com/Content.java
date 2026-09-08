package islam.rogirojusalat.com;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Content extends AppCompatActivity {
    private AdView mAdView;
    public TextView main, share, copy;
    private ImageView favoriteIcon;
    private String shareContent;
    private String assetFile;
    private WebView webview1;
    private int fontZoom;
    private boolean darkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

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

        main = findViewById(R.id.main);
        share = findViewById(R.id.share);
        copy = findViewById(R.id.copy);
        favoriteIcon = findViewById(R.id.favoriteIcon);
        webview1 = findViewById(R.id.webview1);
        webview1.getSettings().setJavaScriptEnabled(true);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        assetFile = intent.getStringExtra("file");
        if (assetFile == null) {
            assetFile = "btn1.html";
        }
        if (title == null) {
            title = getString(R.string.app_name);
        }
        main.setText(title);
        shareContent = extractPlainText(assetFile, title);

        fontZoom = Prefs.getFontZoom(this);
        webview1.getSettings().setTextZoom(fontZoom);
        darkMode = Prefs.isDarkMode(this);

        webview1.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (darkMode) {
                    applyDarkMode(true);
                }
            }
        });
        webview1.loadUrl("file:///android_asset/" + assetFile);

        updateFavoriteIcon();
    }

    private String extractPlainText(String file, String fallback) {
        try (InputStream is = getAssets().open(file)) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int len;
            while ((len = is.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            String html = bos.toString("UTF-8");
            @SuppressWarnings("deprecation")
            String plain = Html.fromHtml(html).toString().trim();
            return plain.isEmpty() ? fallback : plain;
        } catch (IOException e) {
            return fallback;
        }
    }

    //share words
    public void wordShare() {
        //share
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "" + shareContent + "\n\nhttps://play.google.com/store/apps/details?id=" + getPackageName();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, main.getText());
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void shareHere(View view) {
        wordShare();
    }

    public void copyHere(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("text label", "" + shareContent);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(Content.this, "Saved to clip board", Toast.LENGTH_SHORT).show();
    }

    public void toggleFavorite(View view) {
        Prefs.toggleFavorite(this, assetFile, main.getText().toString());
        updateFavoriteIcon();
    }

    private void updateFavoriteIcon() {
        boolean fav = Prefs.isFavorite(this, assetFile);
        favoriteIcon.setImageResource(fav ? R.drawable.ic_heart : R.drawable.ic_heart_outline);
    }

    public void increaseFont(View view) {
        fontZoom = Math.min(160, fontZoom + 10);
        webview1.getSettings().setTextZoom(fontZoom);
        Prefs.setFontZoom(this, fontZoom);
    }

    public void decreaseFont(View view) {
        fontZoom = Math.max(70, fontZoom - 10);
        webview1.getSettings().setTextZoom(fontZoom);
        Prefs.setFontZoom(this, fontZoom);
    }

    public void toggleDarkMode(View view) {
        darkMode = !darkMode;
        applyDarkMode(darkMode);
        Prefs.setDarkMode(this, darkMode);
    }

    private void applyDarkMode(boolean enable) {
        if (enable) {
            webview1.evaluateJavascript(
                    "document.documentElement.style.filter='invert(1) hue-rotate(180deg)';" +
                            "document.documentElement.style.backgroundColor='#000000';", null);
        } else {
            webview1.evaluateJavascript(
                    "document.documentElement.style.filter='none';" +
                            "document.documentElement.style.backgroundColor='#ffffff';", null);
        }
    }
}
