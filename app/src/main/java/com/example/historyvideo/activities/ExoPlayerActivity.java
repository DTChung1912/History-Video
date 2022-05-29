package com.example.historyvideo.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.historyvideo.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.drm.DrmSessionManagerProvider;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.util.Util;

public class ExoPlayerActivity extends Activity implements MediaSource.Factory{
    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";

    private PlayerView playerView;
    private ExoPlayer player;
    private ProgressBar progressBar;
    private long backTime;

    private MediaSource mVideoSource;
    private boolean mExoPlayerFullscreen = false;
    private FrameLayout mFullScreenButton;
    private ImageView mFullScreenIcon;
    private Dialog mFullScreenDialog;
    private  DataSource.Factory dataSourceFactory;

    private int mResumeWindow;
    private long mResumePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_exo_player);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        playerView = findViewById(R.id.exoplayer);
        progressBar = findViewById(R.id.progressBar);
        //Toast.makeText(this, Link, Toast.LENGTH_SHORT).show();

        dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, getString(R.string.app_name)));


        String Link = getIntent().getStringExtra("Link");
        String Sub = getIntent().getStringExtra("Link_Sub");

//        initPlayer(Link, Sub);
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        // Build the media item.
        MediaItem mediaItem = MediaItem.fromUri(Link);
// Set the media item to be played.
        player.setMediaItem(mediaItem);
// Prepare the player.
        player.prepare();
// Start the playback.
        player.play();

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

    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
//                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }
//
//
//    private void openFullscreenDialog() {
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        ((ViewGroup) playerView.getParent()).removeView(playerView);
//        mFullScreenDialog.addContentView(playerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(ExoPlayerActivity.this, R.drawable.ic_fullscreen));
//        mExoPlayerFullscreen = true;
//        mFullScreenDialog.show();
//
//    }
//
//
//    private void closeFullscreenDialog() {
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        ((ViewGroup) playerView.getParent()).removeView(playerView);
//        ((FrameLayout) findViewById(R.id.main_media_frame)).addView(playerView);
//        mExoPlayerFullscreen = false;
//        mFullScreenDialog.dismiss();
//        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(ExoPlayerActivity.this, R.drawable.ic_fullscreen_exit));
//
//    }
//
//
//    private void initFullscreenButton() {
//
//        PlayerControlView controlView = playerView.findViewById(R.id.exo_controller);
//        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
//        mFullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
//        mFullScreenButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!mExoPlayerFullscreen)
//                    openFullscreenDialog();
//                else
//                    closeFullscreenDialog();
//            }
//        });
//
//    }
//
//    private void initExoPlayer() {
//
//        player = FileDataSource.Factory.newSimpleInstance(this);
//        playerView.setPlayer(player);
//
//        boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;
//
//        if (haveResumePosition) {
//            player.seekTo(mResumeWindow, mResumePosition);
//        }
//        String contentUrl = getString(R.string.content_url);
//        mVideoSource = buildMediaSource(Uri.parse(contentUrl));
//
//        player.prepare(mVideoSource);
//        player.setPlayWhenReady(true);
//
//    }

    @Override
    public MediaSource.Factory setDrmSessionManagerProvider(@Nullable DrmSessionManagerProvider drmSessionManagerProvider) {
        return null;
    }

    @Override
    public MediaSource.Factory setLoadErrorHandlingPolicy(@Nullable LoadErrorHandlingPolicy loadErrorHandlingPolicy) {
        return null;
    }

    @Override
    public int[] getSupportedTypes() {
        return new int[0];
    }

    @Override
    public MediaSource createMediaSource(MediaItem mediaItem) {
        return null;
    }
}
