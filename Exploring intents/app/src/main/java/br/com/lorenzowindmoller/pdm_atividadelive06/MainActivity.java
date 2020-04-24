package br.com.lorenzowindmoller.pdm_atividadelive06;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, DialogCall.DialogCallListener,
        DialogMessage.DialogMessageListener, DialogEmail.DialogEmailListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private ArrayList<FrameLayout> frames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frames = new ArrayList<>();

        frames.add((FrameLayout)findViewById(R.id.frame1));
        frames.add((FrameLayout)findViewById(R.id.frame2));
        frames.add((FrameLayout)findViewById(R.id.frame3));
        frames.add((FrameLayout)findViewById(R.id.frame4));
        frames.add((FrameLayout)findViewById(R.id.frame5));
        frames.add((FrameLayout)findViewById(R.id.frame6));
        frames.add((FrameLayout)findViewById(R.id.frame7));
        frames.add((FrameLayout)findViewById(R.id.frame8));
        frames.add((FrameLayout)findViewById(R.id.frame9));
        frames.add((FrameLayout)findViewById(R.id.frame10));
        frames.add((FrameLayout)findViewById(R.id.frame11));

        set_click_listener();
    }

    private void set_click_listener() {
        for (FrameLayout i : frames) {
            i.setOnClickListener(this);
        }
    }

    public void onClick(View v) {
        for (int index = 0; index < frames.size(); index++) {
            if (v == frames.get(index)) {
                switch (index) {
                    case 0:
                        makeCall();
                        break;
                    case 1:
                        goWebPage();
                        break;
                    case 2:
                        openMap(1, Uri.parse("geo:-3.0941209,-59.9742328?z=15"));
                        break;
                    case 3:
                        openMap(1, Uri.parse("geo:-3.134586,-59.9798095?z=15"));
                        break;
                    case 4:
                        openMap(2, Uri.parse("geo:0,0?q=SIDIA"));
                        break;
                    case 5:
                        sendMessage(1);
                        break;
                    case 6:
                        sendMessage(2);
                        break;
                    case 7:
                        openCamera(1);
                        break;
                    case 8:
                        sendEmail();
                        break;
                    case 9:
                        openLocaleConfig();
                        break;
                    case 10:
                        openCamera(2);
                        break;
                }
                break;
            }
        }
    }

    public void makeCall() {
        DialogCall dialogCall = new DialogCall();
        dialogCall.show(getSupportFragmentManager(), "DialogCall");
    }

    @Override
    public void applyTextDialogCall(String number) {
        if(number.length() != 0) {
            startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + number)));
        } else {
            Toast.makeText(this, R.string.toastCompleteField, Toast.LENGTH_SHORT).show();
        }
    }

    private void goWebPage() {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, "https://www.fundacaomatiasmachline.org.br/");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void openMap(Integer operation, Uri geo) {
        if(operation.equals(1)){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(geo);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
        else if(operation.equals(2)){
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, geo);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
    }

    private void sendMessage(Integer operation) {
        DialogMessage dialogMessage = new DialogMessage();
        dialogMessage.setOperation(operation);
        dialogMessage.show(getSupportFragmentManager(), "DialogMessage");
    }

    @Override
    public void applyTextsDialogMessage(String number, String text, Integer operation) {
        if(number.length() != 0 && text.length() != 0) {
            if(operation.equals(1)){
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("smsto:" + number));
                intent.putExtra("sms_body", text);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            } else if(operation.equals(2)){
                Intent intent = new Intent();
                try {
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, text);
                    intent.setType("text/plain");
                    intent.setPackage("com.whatsapp");
                    startActivity(intent);
                } catch (Exception e){
                    Toast.makeText(this, R.string.toastNotInstalled, Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, R.string.toastCompleteFields, Toast.LENGTH_SHORT).show();
        }
    }

    private void openCamera(Integer operation) {
        if(operation.equals(1)){
            Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }
        else if(operation.equals(2)){
            goToLoadPhotoActivity();
            finish();
        }
    }

    public void goToLoadPhotoActivity() {
        Intent intent = new Intent(this, LoadPhotoActivity.class);
        startActivity(intent);
    }

    private void sendEmail() {
        DialogEmail dialogEmail = new DialogEmail();
        dialogEmail.show(getSupportFragmentManager(), "DialogEmail");
    }

    @Override
    public void applyTextsDialogEmail(String address, String subject, String body) {
        if(address.length() != 0 && subject.length() != 0 && body.length() != 0) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            String[] emails = new String[1];
            emails[0] = address;
            intent.putExtra(Intent.EXTRA_EMAIL, emails);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, body);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        } else {
            Toast.makeText(this, R.string.toastCompleteFields, Toast.LENGTH_SHORT).show();
        }
    }

    private void openLocaleConfig(){
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
