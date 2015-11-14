package pun.projects.driftbottle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
public class MessageActivity extends AppCompatActivity {


    public String TAG = "DriftBottleMessageActivity";
    public TextView seenByText;
    public TextView messageText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        seenByText = (TextView) findViewById(R.id.seenByText);
        messageText = (TextView) findViewById(R.id.messageText);

        String messageContent = getIntent().getStringExtra("content");
        String seenBy = getIntent().getStringExtra("seenBy");

        seenByText.setText("Message seen by: " + seenBy);
        messageText.setText("\n\n\t\t\t\t" + messageContent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
