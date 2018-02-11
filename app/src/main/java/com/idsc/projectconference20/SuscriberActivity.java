package com.idsc.projectconference20;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.Connection;
import com.opentok.android.OpentokError;
import com.opentok.android.Session;
import com.opentok.android.Stream;

import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Subscriber;
import com.opentok.android.OpentokError;
import android.support.annotation.NonNull;
import android.Manifest;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.opentok.android.SubscriberKit;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.EglBase;
import org.webrtc.EglBase14;


import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SuscriberActivity extends AppCompatActivity
implements  Session.SessionListener,
        PublisherKit.PublisherListener,
        Session.SignalListener
{
    private static String API_KEY = "46047072";
    private static String SESSION_ID = "1_MX40NjA0NzA3Mn5-MTUxNjk1ODEyODM5OH5xTExVUjV4SUYwRXVJR1RjRVVUd2VhOFV-fg";
    //private static String SESSION_ID = "2_MX40NjA0NzA3Mn5-MTUxNzc0ODU1NzcxNn4va0VRb1p3NmRVV1BXdDA4dVRHTHhsK3R-fg";
    //publisher token
    //private static String TOKEN = "T1==cGFydG5lcl9pZD00NjA0NzA3MiZzaWc9ZDYwODhiNmYxODNlZGE2ZTFmMzE0YjA3OGI3MjliMzQ5OTc1ZDBlMDpzZXNzaW9uX2lkPTFfTVg0ME5qQTBOekEzTW41LU1UVXhOamsxT0RFeU9ETTVPSDV4VEV4VlVqVjRTVVl3UlhWSlIxUmpSVlZVZDJWaE9GVi1mZyZjcmVhdGVfdGltZT0xNTE2OTU4MTU0Jm5vbmNlPTAuODExNTQ1MzQ1NTE2OTk0MyZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNTE5NTUwMTUyJmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9";
    private static String TOKEN="T1==cGFydG5lcl9pZD00NjA0NzA3MiZzaWc9MTZmYmQ2NmI2YTQ5OGQxNGE1Zjg3MTEwOGY3OThmZDhkZTRiNWZjZjpzZXNzaW9uX2lkPTFfTVg0ME5qQTBOekEzTW41LU1UVXhOamsxT0RFeU9ETTVPSDV4VEV4VlVqVjRTVVl3UlhWSlIxUmpSVlZVZDJWaE9GVi1mZyZjcmVhdGVfdGltZT0xNTE4MDEzMDEwJm5vbmNlPTAuNDM1MTkyMjc3NTMwMjUzJnJvbGU9cHVibGlzaGVyJmV4cGlyZV90aW1lPTE1MjA2MDUwMDkmaW5pdGlhbF9sYXlvdXRfY2xhc3NfbGlzdD0=";
    //subscriber token
    //  private static String TOKEN= "T1==cGFydG5lcl9pZD00NjA0NzA3MiZzaWc9NTc3OGMwMGYwZjk1YTIwYjg1Mjc5MWIyNTkxODZiMGFmZTFhMzg2YzpzZXNzaW9uX2lkPTFfTVg0ME5qQTBOekEzTW41LU1UVXhOamsxT0RFeU9ETTVPSDV4VEV4VlVqVjRTVVl3UlhWSlIxUmpSVlZVZDJWaE9GVi1mZyZjcmVhdGVfdGltZT0xNTE3NzUwMjM0Jm5vbmNlPTAuNTcxNzY5MjkyNTE3MDc1JnJvbGU9c3Vic2NyaWJlciZleHBpcmVfdGltZT0xNTIwMzQyMjMzJmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9";
    private static final String LOG_TAG = SuscriberActivity.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;
    private Session mSession;
    private FrameLayout mPublisherViewContainer;
    //private FrameLayout mSubscriberViewContainer;
    private RelativeLayout mSubscriberViewContainer;
    private Publisher mPublisher;
    private Subscriber mSubscriber;
    private final int MAX_NUM_SUBSCRIBERS =5;
    private ArrayList<Subscriber> mSubscribers = new ArrayList<Subscriber>();
    private ArrayList<String> mStreams= new ArrayList<String>();
   private HashMap<Stream, Subscriber> mSubscriberStreams = new HashMap<Stream, Subscriber>();
 // private HashMap<String, Subscriber> mSubscriberStreams = new HashMap<String, Subscriber>();
   private HashMap<String, Stream> mStringStreams = new HashMap<String, Stream>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suscriber_layout);
        //added -1
        final Button swapCamera = (Button) findViewById(R.id.swapCamera);

        swapCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mPublisher == null) {
                    return;
                }
                mPublisher.cycleCamera();
            }
        });

        final ToggleButton toggleAudio = (ToggleButton) findViewById(R.id.toggleAudio);
        toggleAudio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mPublisher == null) {
                    return;
                }
                if (isChecked) {
                    mPublisher.setPublishAudio(true);//true
                } else {
                    mPublisher.setPublishAudio(false);//false
                }
            }
        });

        final ToggleButton toggleVideo = (ToggleButton) findViewById(R.id.toggleVideo);
        toggleVideo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mPublisher == null) {
                    return;
                }
                if (isChecked) {
                    mPublisher.setPublishVideo(true);//true
                } else {
                    mPublisher.setPublishVideo(false);//false
                }
            }
        });//added-1

        requestPermissions();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int menuItemThatWasSelected = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (menuItemThatWasSelected == R.id.btn_chat) {
            Intent menu_intent = new Intent(SuscriberActivity.this, ChatActivity.class);
            startActivity(menu_intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @SuppressLint("WrongViewCast")
    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {
        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // initialize view objects from your layout
            mPublisherViewContainer = (FrameLayout)findViewById(R.id.publisherview);
            mSubscriberViewContainer = (RelativeLayout) findViewById(R.id.subscriber_container);
            // initialize and connect to the session
            mSession = new Session.Builder(this, API_KEY, SESSION_ID).build();
            mSession.setSessionListener((Session.SessionListener) this);
            mSession.connect(TOKEN);
            mSession.setSignalListener(this);

           /*//Suppressing connection events with the OpenTok Android SDK
            mSession = new Session.Builder(this, API_KEY, SESSION_ID)
                    .connectionEventsSuppressed(true)
                    .build();
            mSession.connect(TOKEN);
*/
        } else {
            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, perms);
        }
    }


    // SessionListener methods
    @Override
    public void onConnected(Session session) {
        Log.i(LOG_TAG, "Session Connected");
        mPublisher = new Publisher
                .Builder(this)
                .build();
        mPublisher.setPublisherListener((PublisherKit.PublisherListener) this);
        mPublisher.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
        mPublisherViewContainer.addView(mPublisher.getView());
        mSession.publish(mPublisher);
    }

    @Override
    public void onDisconnected(Session session) {
        Log.i(LOG_TAG, "Session Disconnected");
    }

    @Override
    public void onStreamReceived(Session session, final Stream stream) {

        Log.d(LOG_TAG, "onStreamReceived: New Stream Received " + stream.getStreamId() + " in session: " + session.getSessionId()+" Stream name: "+stream.getName());

           if(stream.getName().equalsIgnoreCase("Main_stream"))
            {
                    mSubscriber = new Subscriber.Builder(SuscriberActivity.this, stream).build();
                    mSession.subscribe(mSubscriber);
                   mSubscriberViewContainer.addView(mSubscriber.getView());

            }
        if (mSubscribers.size() + 1 > MAX_NUM_SUBSCRIBERS ) {
            Toast.makeText(this, "New subscriber ignored. MAX_NUM_SUBSCRIBERS limit reached.", Toast.LENGTH_LONG).show();
            return;
        }
        if(stream.getName().compareTo("Main_stream")!=0)
        {
            final Subscriber subscriber = new Subscriber.Builder(SuscriberActivity.this, stream).build();
            mStringStreams.put(stream.getStreamId().toString(), stream);
            mSession.subscribe(subscriber);
            mSubscribers.add(subscriber);
            mSubscriberStreams.put(stream,subscriber);
            int position = mSubscribers.size() - 1;
            final int id = getResources().getIdentifier("subscriberview" + (new Integer(position)).toString(), "id", SuscriberActivity.this.getPackageName());
            FrameLayout subscriberViewContainer = (FrameLayout) findViewById(id);
            subscriber.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
            subscriberViewContainer.addView(subscriber.getView());
        }
        Log.i(LOG_TAG, "Stream Received");
        Log.d(LOG_TAG, "onStreamReceived: New Stream Received " + stream.getStreamId() + " in session: " + session.getSessionId());
        //working
       /* if (mSubscriber == null) {

            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSession.subscribe(mSubscriber);
            mSubscriberViewContainer.addView(mSubscriber.getView());
        }*/

/*
                if (mSubscriber == null) {
            //  Log.d(LOG_TAG,)String id=stream.getStreamId().toString();
                Log.d(LOG_TAG, "onStreamReceived: New Stream Received " + stream.getStreamId() + " in session: " + session.getSessionId());
                mSubscriber = new Subscriber.Builder(this, stream).build();
                mSubscriber.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
                mSubscriber.setSubscriberListener(this);
                mSession.subscribe(mSubscriber);
                mSubscriberViewContainer.addView(mSubscriber.getView());


        }*/

    }
    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Dropped");
       /* if (mSubscriber != null) {
            mSubscriber = null;
            mSubscriberViewContainer.removeAllViews();
        }*/
        Subscriber subscriber = mSubscriberStreams.get(stream);
        if (subscriber == null) {
            return;
        }
        int position = mSubscribers.indexOf(subscriber);
        int id = getResources().getIdentifier("subscriberview" + (new Integer(position)).toString(), "id", SuscriberActivity.this.getPackageName());

        mSubscribers.remove(subscriber);
        mSubscriberStreams.remove(stream);

        FrameLayout subscriberViewContainer = (FrameLayout) findViewById(id);
        subscriberViewContainer.removeView(subscriber.getView());

    }
    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.e(LOG_TAG, "Session error: " + opentokError.getMessage());
    }
    // PublisherListener methods
    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
        Log.i(LOG_TAG, "Publisher onStreamCreated");

        mStreams.add(stream.getStreamId().toString());
    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
        Log.i(LOG_TAG, "Publisher onStreamDestroyed");
        mStreams.remove(stream.getStreamId().toString());
    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
        Log.e(LOG_TAG, "Publisher error: " + opentokError.getMessage());
    }
    @Override
    //The connection identifying the client that sent the message.
    public void onSignalReceived(Session session, String s, String s1, Connection connection) {
        Toast.makeText(SuscriberActivity.this, "Stream Received: " + s1, Toast.LENGTH_SHORT).show();
        Toast.makeText(SuscriberActivity.this, "Hash Map: " + mStringStreams.containsKey(s1), Toast.LENGTH_SHORT).show();
        if (mStreams.get(0).equalsIgnoreCase(s1)) {
          //mSubscriberViewContainer.removeView(mSubscriber.getView());
            mPublisherViewContainer.removeAllViews();
            mSubscriberViewContainer.addView(mPublisher.getView());
        }
        else {
            //stream = mSubscriberStreams.get(s1).getStream();
          final Stream stream= mStringStreams.get(s1);
            mSubscriber = new Subscriber.Builder(SuscriberActivity.this, stream).build();
            mSubscriber.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
           //mSubscriberViewContainer.removeView(mPublisher.getView());
            mSubscriberViewContainer.addView(mSubscriber.getView());
        }
    }
}

