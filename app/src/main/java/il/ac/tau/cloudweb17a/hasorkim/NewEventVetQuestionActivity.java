package il.ac.tau.cloudweb17a.hasorkim;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class NewEventVetQuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_vet_question);
    }

    public void openVetList(View view)
    {
        Intent intent = new Intent(NewEventVetQuestionActivity.this, VetListActivity.class);
        startActivity(intent);
    }

    public void openHaveDogMsg(View view)
    {
        Intent intent = new Intent(NewEventVetQuestionActivity.this, NewEventHaveDogMsgActivity.class);
        startActivity(intent);
    }

}
