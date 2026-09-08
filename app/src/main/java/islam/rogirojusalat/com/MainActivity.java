package islam.rogirojusalat.com;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private ConsentInformation consentInformation;

    private static final String[] DAILY_REMINDERS = {
            "“নিশ্চয় কষ্টের সাথে সহজতা আছে।” – সূরা আশ-শরহ, আয়াত: ৫-৬",
            "“তোমরা সবর ও সালাতের মাধ্যমে সাহায্য প্রার্থনা কর, নিশ্চয় আল্লাহ ধৈর্যশীলদের সাথে আছেন।” – সূরা আল-বাকারা, আয়াত: ১৫৩",
            "স্মরণ রাখুন: “আল্লাহর স্মরণেই অন্তর শান্তি পায়।” – সূরা আর-রা'দ, আয়াত: ২৮",
            "রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম বলেছেন: “দীন সহজ।” – সহীহ বুখারী",
            "রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম বলেছেন: “একজন মুমিন অপর মুমিনের জন্য দালানের মতো, একে অপরকে মজবুত করে।” – সহীহ বুখারী ও মুসলিম",
            "রোগীর খোঁজ নেওয়া সুন্নাত: যে ব্যক্তি রোগী দেখতে যায়, সে আল্লাহর রহমতের মধ্যে থাকে।",
            "ধৈর্যশীলদের জন্য আল্লাহর পক্ষ থেকে রয়েছে বিশেষ রহমত ও সুসংবাদ।",
            "সকল প্রশংসা আল্লাহর জন্য, যিনি ইচ্ছা করলে সুস্থতা দান করেন, দুর্বলতাতেও পরীক্ষা থাকে।",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView dailyReminder = findViewById(R.id.dailyReminder);
        if (dailyReminder != null) {
            int dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
            dailyReminder.setText(DAILY_REMINDERS[dayOfYear % DAILY_REMINDERS.length]);
        }

        //admob popup
        ConsentRequestParameters params = new ConsentRequestParameters
                .Builder()
                .setTagForUnderAgeOfConsent(false)
                .build();
        consentInformation = UserMessagingPlatform.getConsentInformation(this);
        consentInformation.requestConsentInfoUpdate(this,params,(ConsentInformation.OnConsentInfoUpdateSuccessListener) () -> {
            UserMessagingPlatform.loadAndShowConsentFormIfRequired(this,(ConsentForm.OnConsentFormDismissedListener) loadAndShowError -> {
                        if (loadAndShowError != null) {
                        }
                    }
            );
        },(ConsentInformation.OnConsentInfoUpdateFailureListener) requestConsentError -> {});
        getOnBackPressedDispatcher().addCallback(this, backPressedCallback);
    }
    private final OnBackPressedCallback backPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            onBackPressedn();
        }
    };
    public void onBackPressedn() {
        new AlertDialog.Builder(this)
                .setTitle("Do you want to exit?")
                .setMessage("Thank You For Using Our Application Please Give Us Your Feedback")
                .setCancelable(true)
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setPositiveButton("Rate Us", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
                }})
                .setNeutralButton("No", null)
                .show();
    }

    public void open(View view) {
        Intent int1 = new Intent(MainActivity.this, Dashboard.class);
        startActivity(int1);
    }
    public void openFavorites(View view) {
        Intent int1 = new Intent(MainActivity.this, Favorites.class);
        startActivity(int1);
    }
    public void rating(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
    }
    public void share(View view) {
        //share
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = getResources().getString(R.string.app_name)+" . https://play.google.com/store/apps/details?id="+getPackageName();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
    public void more(View view) {
        Intent int2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName()));
        startActivity(int2);
    }

    public void report(View view) {
        Intent mailIntent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:?subject=" + "Report for '"+getResources().getString(R.string.app_name)+"' "+ "&body=" + "Dear please type your problem" + "&to=" + "shahinalam6644@gmail.com");
        mailIntent.setData(data);
        startActivity(Intent.createChooser(mailIntent, "Send mail..."));
    }
    public void update(View view) {
        Intent int2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName()));
        startActivity(int2);
    }
    public void exite(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Do you want to exit?")
                .setMessage("Thank You For Using Our Application Please Give Us Your Feedback")
                .setCancelable(true)
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setPositiveButton("Rate Us", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
                }})
                .setNeutralButton("No", null)
                .show();
    }
    public void about(View view) {
        Intent int1 = new Intent(MainActivity.this, AboutUS.class);
        startActivity(int1);
    }
}
