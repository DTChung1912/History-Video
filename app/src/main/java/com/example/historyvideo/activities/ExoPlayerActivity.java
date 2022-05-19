package com.example.historyvideo.activities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.historyvideo.R;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.StyledPlayerView;

public class ExoPlayerActivity extends Activity {
    private StyledPlayerView styledPlayerView;
    private SimpleExoPlayer player;
    private ProgressBar progressBar;
    private long backTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_exo_player);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        styledPlayerView = findViewById(R.id.exoplayer);
        progressBar = findViewById(R.id.progressBar);
        //Toast.makeText(this, Link, Toast.LENGTH_SHORT).show();

//        String Link = getIntent().getStringExtra("Link");
//        String Sub = getIntent().getStringExtra("Link_Sub");

//        initPlayer(Link, Sub);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

//    private void initPlayer(String Link, String Sub) {
//        try {
//
//            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//            DefaultBandwidthMeter.Builder trackSelectorfactory = new DefaultBandwidthMeter.Builder((Context) bandwidthMeter);
//            TrackSelector trackSelector = new DefaultTrackSelector(trackSelectorfactory.);
//            //khởi tạo player
//            player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
//            //khởi tạo exoplayerview
//            StyledPlayerView simpleExoPlayerView = findViewById(R.id.exoplayer);
//
//            //chuẩn bị datasource để player có thể load
//            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "Test ExoPlayer"));
//            //giải mã data
//            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
//
//            //Đường dẫn để player load video
//            Uri videoUri = Uri.parse(Link);
//            Uri subtitleUri = Uri.parse(Sub);
//            MediaSource videoSource = new ExtractorMediaSource(videoUri,
//                    dataSourceFactory,
//                    extractorsFactory,
//                    null,
//                    null);
//
//            // Build the subtitle MediaSource.
//            Format subtitleFormat = Format.createSampleFormat(
//                    null,
//                    null); // The subtitle language. May be null.
//
//            MediaSource subtitleSource = new SingleSampleMediaSource(subtitleUri, dataSourceFactory, subtitleFormat, C.TIME_UNSET);
//
//            MergingMediaSource mergedSource = new MergingMediaSource(videoSource, subtitleSource);
//
//            simpleExoPlayerView.getSubtitleView().setStyle(new CaptionStyleCompat(Color.WHITE,
//                    Color.TRANSPARENT, Color.TRANSPARENT, CaptionStyleCompat.EDGE_TYPE_OUTLINE, Color.BLACK,
//                    null));
//
//            simpleExoPlayerView.setPlayer(player);
//
//            if (!Link.equals("") && !Sub.equals("")) {
//                player.prepare(mergedSource);
//            } else if (!Link.equals("") && Sub.equals("")) {
//                player.prepare(videoSource);
//            }
//
//            player.setPlayWhenReady(true);
//
//            player.addListener(new Player.Listener() {
//                @Override
//                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//                    if (playbackState == player.STATE_BUFFERING) {
//                        progressBar.setVisibility(View.VISIBLE);
//                    } else {
//                        progressBar.setVisibility(View.INVISIBLE);
//                    }
//                }
//            });
//
//        } catch (Exception e) {
//            Log.e("MainActivity", "exoplayer error" + e.toString());
//        }
//    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (backTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(this, "Nhấn Back Một Lần Nữa Để Thoát", Toast.LENGTH_SHORT).show();
        }
        backTime = System.currentTimeMillis();
    }
}
