package com.example.ben.coolmusic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.support.v4.app.FragmentActivity;

import ai.api.AIListener;
import ai.api.RequestExtras;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIContext;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.Result;
import ai.api.android.GsonFactory;
import ai.api.model.Status;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

//import com.spotify.sdk.android.authentication.AuthenticationClient;
//import com.spotify.sdk.android.authentication.AuthenticationRequest;
//import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

public class MainActivity extends AppCompatActivity implements AIListener {

    private AIService aiService;
    private EditText contextEditText;
    private TextView resultTextView;


    private Player mPlayer;
    private Gson gson = GsonFactory.getGson();

    public static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contextEditText = (EditText)findViewById(R.id.contextEditText);
        resultTextView = (TextView)findViewById(R.id.resultTextView);

        initService();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item){
        final int id = item.getItemId();
        if(id == R.id.action_settings){
            startActivity(SpotifyLoginIn.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningFinished() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    private void initService() {
//        final AIConfiguration.SupportedLanguages lang = AIConfiguration.SupportedLanguages.fromLanguageTag(selectedLanguage.getLanguageCode());
        final AIConfiguration config = new AIConfiguration("771661a648eb40d2b123f8fea1e237ff",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiService = AIService.getService(this, config);
        aiService.setListener(this);
    }

    public void startRecognition(final View view) {
        final String contextString = String.valueOf(contextEditText.getText());
        if (TextUtils.isEmpty(contextString)) {
            aiService.startListening();
        } else {
            final List<AIContext> contexts = Collections.singletonList(new AIContext(contextString));
            final RequestExtras requestExtras = new RequestExtras(contexts, null);
            aiService.startListening(requestExtras);
        }

    }

    public void stopRecognition(final View view){
        aiService.stopListening();
    }

    private void startActivity(Class<?> cls){
        final Intent intent = new Intent(this,cls);
        startActivity(intent);
    }

    @Override
    public void onResult(final AIResponse response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onResult");

                resultTextView.setText(gson.toJson(response));

                Log.i(TAG, "Received success response");

                // this is example how to get different parts of result object
                final Status status = response.getStatus();
                Log.i(TAG, "Status code: " + status.getCode());
                Log.i(TAG, "Status type: " + status.getErrorType());

                final Result result = response.getResult();
                Log.i(TAG, "Resolved query: " + result.getResolvedQuery());

                Log.i(TAG, "Action: " + result.getAction());



                final Metadata metadata = result.getMetadata();
                if (metadata != null) {
                    Log.i(TAG, "Intent id: " + metadata.getIntentId());
                    Log.i(TAG, "Intent name: " + metadata.getIntentName());
                }

                final HashMap<String, JsonElement> params = result.getParameters();
                if (params != null && !params.isEmpty()) {
                    Log.i(TAG, "Parameters: ");
                    for (final Map.Entry<String, JsonElement> entry : params.entrySet()) {
                        Log.i(TAG, String.format("%s: %s", entry.getKey(), entry.getValue().toString()));
                    }
                }
            }

        });
    }

}
