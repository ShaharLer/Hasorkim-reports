package il.ac.tau.cloudweb17a.hasorkim;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class NewEventHaveDogMsgActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_have_dog_msg);
    }

    public void openMoreDetailsRequest(View view)
    {
        Intent intent = new Intent(NewEventHaveDogMsgActivity.this, NewEventMoreDetailsRequestActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(NewEventHaveDogMsgActivity.this, ReportEventActivity.class));
        finish();

    }
}
