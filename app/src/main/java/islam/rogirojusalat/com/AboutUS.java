package islam.rogirojusalat.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class AboutUS extends AppCompatActivity {
    private AdView mAdView;
    public TextView main, mtext, share, copy;
    String shareContent;
    WebView webview1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_u_s);

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

        share = (TextView) findViewById(R.id.share);
        copy = (TextView) findViewById(R.id.copy);

        TextView versionText = findViewById(R.id.versionText);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionText.setText("Version " + pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            versionText.setText("");
        }

        shareContent = getResources().getString(R.string.app_name);
        webview1 = (WebView) findViewById(R.id.webview1);
        webview1.loadUrl("file:///android_asset/about.html");
    }

    public void showAbout(View view) {
        shareContent = getResources().getString(R.string.app_name);
        webview1.loadUrl("file:///android_asset/about.html");
    }

    public void showPrivacy(View view) {
        shareContent = getResources().getString(R.string.app_name) + " - Privacy Policy";
        webview1.loadUrl("file:///android_asset/privacy.html");
    }

    //share words
    public void wordShare() {
        //share
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "" + shareContent + "\n\nhttps://play.google.com/store/apps/details?id=" + getPackageName();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareContent);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void shareHere(View view) {
        wordShare();
    }

    public void copyHere(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("text label", // What should I set for this "label"?
                "" + shareContent);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(AboutUS.this, "Saved to clip board", Toast.LENGTH_SHORT).show();

    }
}
