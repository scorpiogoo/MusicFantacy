package com.example.ben.coolmusic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.EditText;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

/**
 * Created by ben on 2017/4/11.
 */

public class SpotifyLoginIn extends AppCompatActivity {

    private EditText accountEditText;
    private EditText passwordEditText;

    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "abd357acc5ce43c5acd09bccddf8df9b";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "rsps://callback/";

    private static final int REQUEST_CODE = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accountEditText = (EditText)findViewById(R.id.accountEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

    }
}
