package com.vamshigollapelly.istreamapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.vamshigollapelly.istreamapp.adapters.PlaylistAdapter;
import com.vamshigollapelly.istreamapp.data.AppDatabase;
import com.vamshigollapelly.istreamapp.data.PlaylistItem;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlaylistActivity extends AppCompatActivity {

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        SessionManager session = new SessionManager(this);
        AppDatabase db = AppDatabase.getDatabase(this);
        RecyclerView rv = findViewById(R.id.rvPlaylist);
        rv.setLayoutManager(new LinearLayoutManager(this));

        executor.execute(() -> {
            List<PlaylistItem> items = db.appDao().getPlaylist(session.getUserId());
            runOnUiThread(() ->
                    rv.setAdapter(new PlaylistAdapter(items, item -> {
                        Intent intent = new Intent(this, HomeActivity.class);
                        intent.putExtra("videoUrl", item.videoUrl);
                        startActivity(intent);
                    }))
            );
        });

        findViewById(R.id.btnLogoutPlaylist).setOnClickListener(v -> {
            session.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}