package br.com.lorenzowindmoller.pdm_atividadelive04;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.nfc.Tag;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageData selected_image;
    private ArrayList<ImageData> images;
    private TextView textAbove, textBelow, textChances;
    private Integer chances;

    private class ImageData {
        ImageView imageView;
        TextView textView;
        String textName;

        public ImageData(ImageView imageView, TextView textView) {
            this.imageView = imageView;
            this.textView = textView;
        }

        public TextView getTextView() {
            return textView;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageResourceImageView(Integer imageResource){
            this.imageView.setImageResource(imageResource);
        }

        public String getTextName() {
            return textName;
        }

        public void setTextName(String textName) {
            this.textName = textName;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chances = 3;

        images = new ArrayList<>();

        images.add(new ImageData((ImageView)findViewById(R.id.imageView1),(TextView)findViewById(R.id.textView1)));
        images.add(new ImageData((ImageView)findViewById(R.id.imageView2),(TextView)findViewById(R.id.textView2)));
        images.add(new ImageData((ImageView)findViewById(R.id.imageView3),(TextView)findViewById(R.id.textView3)));
        images.add(new ImageData((ImageView)findViewById(R.id.imageView4),(TextView)findViewById(R.id.textView4)));
        images.add(new ImageData((ImageView)findViewById(R.id.imageView5),(TextView)findViewById(R.id.textView5)));
        images.add(new ImageData((ImageView)findViewById(R.id.imageView6),(TextView)findViewById(R.id.textView6)));
        images.add(new ImageData((ImageView)findViewById(R.id.imageView7),(TextView)findViewById(R.id.textView7)));
        images.add(new ImageData((ImageView)findViewById(R.id.imageView8),(TextView)findViewById(R.id.textView8)));
        images.add(new ImageData((ImageView)findViewById(R.id.imageView9),(TextView)findViewById(R.id.textView9)));

        textAbove = findViewById(R.id.textAbove);
        textBelow = findViewById(R.id.textBelow);
        textChances = findViewById(R.id.textChances);

        set_click_listener_images(images);

        random_images_array(images);
        selected_image = random_image(images);

        textAbove.setText(textAbove.getText() + selected_image.getTextName());
        textChances.setText(textChances.getText() +  Integer.toString(chances));
    }

    private void set_click_listener_images(ArrayList<ImageData> images){
        textChances.setOnClickListener(this);

        for(ImageData i: images){
            i.getTextView().setOnClickListener(this);
        }
    }

    private void random_images_array(ArrayList<ImageData> images){
        Pair<Integer, String> [] vect = new Pair[] {
            new Pair<>(R.drawable.image_c, "C"),
            new Pair<>(R.drawable.image_cpp, "C++"),
            new Pair<>(R.drawable.image_html, "HTML"),
            new Pair<>(R.drawable.image_java, "Java"),
            new Pair<>(R.drawable.image_js, "JavaScript"),
            new Pair<>(R.drawable.image_php, "PHP"),
            new Pair<>(R.drawable.image_py, "Python"),
            new Pair<>(R.drawable.image_ruby, "Ruby"),
            new Pair<>(R.drawable.image_swift, "Swift")
        };
        Random r = new Random();
        int array_index;

        for(int i=0;i<9;i++){
            do {
                array_index = r.nextInt(9 - 0);
            }while(vect[array_index].first == -1);

            images.get(i).setImageResourceImageView(vect[array_index].first);
            images.get(i).setTextName(vect[array_index].second);

            Pair<Integer, String> p = new Pair<>(-1, vect[array_index].second);
            vect[array_index] = p;
        }
    }

    private ImageData random_image(ArrayList<ImageData> images ){
        Random r = new Random();
        int array_index = r.nextInt(9 - 0);

        return images.get(array_index);
    }

    public void onClick(View v){
        if(chances == 0){
            if(v == textChances)restartGame();
        }
        else {
            for (ImageData i : images) {
                if (v == i.getTextView()) {

                    i.getImageView().setVisibility(View.VISIBLE);
                    i.getTextView().setVisibility(View.INVISIBLE);

                    if (i.getImageView() != selected_image.getImageView()) {
                        textChances.setText(textChances.getText().subSequence(0, textChances.getText().length() - 1) + Integer.toString(--chances));
                        textBelow.setText("Errou! Você clicou no " + i.getTextName());

                        if (chances == 0) game_lost();
                    } else {
                        textBelow.setText("Parabéns você acertou!");
                        game_win();
                    }
                    break;
                }
            }
        }
    }

    private void game_win(){
        createToast("Você ganhou o jogo");
        textChances.setText("[clique aqui para reiniciar]");
        chances = 0;
    }

    private void game_lost(){
        createToast("Você perdeu o jogo");
        textChances.setText("[clique aqui para reiniciar]");
    }

    private void restartGame(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void createToast(String text){
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}
