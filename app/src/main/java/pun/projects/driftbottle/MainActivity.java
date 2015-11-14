package pun.projects.driftbottle;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    public String TAG = "DriftBottle";
    public TextView bottlesText;
    public TextView messageText;
    public Button sendButton;
    public ImageView bottleView;

    private Random rand = new Random();

    private Handler handler = new Handler();
    private Runnable checkRunnable = new Runnable(){
        public void run() {
            checkBottlesInTheSea();
            handler.postDelayed(checkRunnable, 10000);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rand.setSeed(Calendar.getInstance().get(Calendar.SECOND));

        bottlesText = (TextView) findViewById(R.id.bottlesText);
        messageText = (TextView) findViewById(R.id.messageText);
        sendButton = (Button) findViewById(R.id.sendButton);
        bottleView = (ImageView) findViewById(R.id.bottleView);

        handler.post(checkRunnable);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (messageText.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Message incomplete!",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                ParseObject message = new ParseObject("Message");
                message.put("seenBy", 0);
                message.put("content", messageText.getText().toString());
                message.saveInBackground();
                messageText.setText("");
                checkBottlesInTheSea();
                Toast.makeText(getApplicationContext(), "Your message in a bottle was sent.",
                        Toast.LENGTH_LONG).show();
            }
        });

        bottleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> messageList, ParseException e) {
                        if (e == null) {
                            Integer n = messageList.size();
                            ParseObject message = messageList.get(rand.nextInt(n));
                            String messageContent = message.getString("content");

                            message.increment("seenBy");
                            message.saveInBackground();

                            Integer seenBy = message.getInt("seenBy");

                            Intent startMessageActivity = new Intent(getApplicationContext(),
                                    MessageActivity.class);
                            startMessageActivity.putExtra("content", messageContent);
                            startMessageActivity.putExtra("seenBy", seenBy.toString());
                            startActivity(startMessageActivity);

                        } else {
                            Log.d(TAG, "Error: " + e.getMessage());
                        }
                    }
                });
            }
        });

    }


    public void checkBottlesInTheSea()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> messageList, ParseException e) {
                if (e == null) {
                    Integer n = messageList.size();
                    bottlesText.setText("Bottles in the sea: " + n.toString());
                    Log.d(TAG, "Updated bottles");
                } else {
                    Log.d(TAG, "Error: " + e.getMessage());
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onPause() {

        super.onPause();
        handler.removeCallbacks(checkRunnable);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
