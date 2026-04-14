package com.vamshigollapelly.istreamapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.vamshigollapelly.istreamapp.data.AppDatabase;
import com.vamshigollapelly.istreamapp.data.PlaylistItem;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeActivity extends AppCompatActivity {

    private WebView webViewPlayer;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SessionManager session = new SessionManager(this);
        AppDatabase db = AppDatabase.getDatabase(this);

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        EditText etVideoUrl = findViewById(R.id.etVideoUrl);
        webViewPlayer = findViewById(R.id.webViewPlayer);

        // Configure WebView
        webViewPlayer.getSettings().setJavaScriptEnabled(true);
        webViewPlayer.getSettings().setDomStorageEnabled(true);
        webViewPlayer.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webViewPlayer.getSettings().setMixedContentMode(
                WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webViewPlayer.getSettings().setUserAgentString(
                "Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 " +
                        "(KHTML, like Gecko) Chrome/91.0.4472.120 Mobile Safari/537.36");
        webViewPlayer.setWebChromeClient(new WebChromeClient());
        webViewPlayer.setWebViewClient(new WebViewClient());

        tvWelcome.setText("Welcome, " + session.getUsername());

        // If opened from playlist, pre-fill URL and auto play
        String preloadUrl = getIntent().getStringExtra("videoUrl");
        if (preloadUrl != null) {
            etVideoUrl.setText(preloadUrl);
            String videoId = extractVideoId(preloadUrl);
            if (videoId != null) {
                loadYouTubeVideo(videoId);
            }
        }

        // PLAY button
        findViewById(R.id.btnPlay).setOnClickListener(v -> {
            String url = etVideoUrl.getText().toString().trim();
            String videoId = extractVideoId(url);
            if (videoId != null) {
                loadYouTubeVideo(videoId);
            } else {
                Toast.makeText(this,
                        "Invalid YouTube URL. Please enter a valid link.",
                        Toast.LENGTH_LONG).show();
            }
        });

        // ADD TO PLAYLIST button
        findViewById(R.id.btnAddToPlaylist).setOnClickListener(v -> {
            String url = etVideoUrl.getText().toString().trim();
            if (url.isEmpty()) {
                Toast.makeText(this, "Please enter a URL first", Toast.LENGTH_SHORT).show();
                return;
            }
            if (extractVideoId(url) == null) {
                Toast.makeText(this,
                        "Invalid YouTube URL. Cannot add to playlist.",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            executor.execute(() -> {
                db.appDao().addToPlaylist(
                        new PlaylistItem(session.getUserId(), url));
                runOnUiThread(() ->
                        Toast.makeText(this, "Added to playlist!", Toast.LENGTH_SHORT).show()
                );
            });
        });

        // MY PLAYLIST button
        findViewById(R.id.btnMyPlaylist).setOnClickListener(v ->
                startActivity(new Intent(this, PlaylistActivity.class))
        );

        // LOGOUT button
        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            session.logout();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private String extractVideoId(String url) {
        if (url == null || url.isEmpty()) return null;
        String pattern = "(?:v=|youtu\\.be/|embed/)([a-zA-Z0-9_-]{11})";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(url);
        if (m.find()) return m.group(1);
        return null;
    }

    private void loadYouTubeVideo(String videoId) {
        String html = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1'>" +
                "<style>" +
                "body{margin:0;padding:0;background:#000;}" +
                "iframe{width:100%;height:100%;border:0;}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<iframe " +
                "src='https://www.youtube.com/embed/" + videoId +
                "?autoplay=1&playsinline=1&rel=0&modestbranding=1' " +
                "allow='autoplay; encrypted-media; fullscreen' " +
                "allowfullscreen='true' " +
                "frameborder='0'>" +
                "</iframe>" +
                "</body></html>";

        webViewPlayer.loadDataWithBaseURL(
                "https://www.youtube.com",
                html,
                "text/html",
                "utf-8",
                null
        );
    }
}