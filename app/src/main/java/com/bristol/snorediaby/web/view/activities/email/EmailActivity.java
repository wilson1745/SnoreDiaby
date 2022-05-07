package com.bristol.snorediaby.web.view.activities.email;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.bristol.snorediaby.common.constants.SdConstants;
import com.bristol.snorediaby.web.view.AbstractInterface;
import java.util.Objects;
import com.bristol.snorediaby.R;

public class EmailActivity extends AppCompatActivity implements AbstractInterface {

    private final String TAG = this.getClass().getSimpleName();

    private EditText etSub, etMsg;
    private Button btSend, btnClear;
    private String to, subject, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Log.i(TAG, "Start EmailActivity onCreate");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_email);

            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.feedback);
            findView();
            setBackbutton();

            //      btSend.setOnClickListener(new View.OnClickListener() {
            //         @Override
            //         public void onClick(View v) {
            //            //to = etTo.getText().toString();
            //            to = SdConstants.EMAIL;
            //            subject = etSub.getText().toString();
            //            message = etMsg.getText().toString();
            //
            //            if (subject.isEmpty()) Toast.makeText(EmailActivity.this, R.string.enter_subject, Toast.LENGTH_SHORT).show();
            //            else if(message.isEmpty()) Toast.makeText(EmailActivity.this, R.string.enter_messeage, Toast.LENGTH_SHORT).show();
            //            else {
            //               Intent email = new Intent(Intent.ACTION_SEND);
            //               email.putExtra(Intent.EXTRA_EMAIL, new String[] {to});
            //               email.putExtra(Intent.EXTRA_SUBJECT, subject);
            //               email.putExtra(Intent.EXTRA_TEXT, message);
            //               email.setType(SdConstants.MESSAGE); //need this to prompts email client only
            //               startActivity(Intent.createChooser(email, SdConstants.CLIENT));
            //            }
            //         }
            //      });

            btSend.setOnClickListener((View v) -> {
                //to = etTo.getText().toString();
                to = SdConstants.EMAIL;
                subject = etSub.getText().toString();
                message = etMsg.getText().toString();

                if (subject.isEmpty()) {
                    Toast.makeText(EmailActivity.this, R.string.enter_subject, Toast.LENGTH_SHORT).show();
                } else if (message.isEmpty()) {
                    Toast.makeText(EmailActivity.this, R.string.enter_messeage, Toast.LENGTH_SHORT).show();
                } else {
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
                    email.putExtra(Intent.EXTRA_SUBJECT, subject);
                    email.putExtra(Intent.EXTRA_TEXT, message);
                    email.setType(SdConstants.MESSAGE); //need this to prompts email client only
                    startActivity(Intent.createChooser(email, SdConstants.CLIENT));
                }
            });

            btnClear.setOnClickListener((View v) -> setText());
        } catch (Exception e) {
            Log.e(TAG, "EmailActivity Exception: " + e, e);
            e.printStackTrace();
        } finally {
            Log.i(TAG, "End EmailActivity onCreate");
        }
    }

   /*@Override
   public void onBackPressed() {
      int count = getFragmentManager().getBackStackEntryCount();

      if(count == 0) super.onBackPressed();
      else getFragmentManager().popBackStack();
   }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (Objects.equals(android.R.id.home, item.getItemId())) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Toast.makeText(this, R.string.back_keyDown, Toast.LENGTH_SHORT).show();
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }

    @Override
    public void findView() {
        etSub = findViewById(R.id.subjectEditText);
        etMsg = findViewById(R.id.messageEditText);
        btSend = findViewById(R.id.sendMessageButton);
        btnClear = findViewById(R.id.clearButton);
    }

    @Override
    public void setBackbutton() {
        if (!Objects.equals(null, getSupportActionBar())) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public void setText() {
        //etTo.setText(null);
        etSub.setText(null);
        etMsg.setText(null);
    }

    @Override public void writeBean() {
        // TODO
    }

}
