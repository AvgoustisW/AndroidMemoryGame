package aris.kek;


import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;
import android.media.MediaPlayer;

//TODO **SOLVED** ODD NUMBERS ARE A NO-NO, MAKE SURE TO NOT GIVE THE OPTION OF 3X3 FOR EXAMPLE
public class MainMenu extends Activity {

    public static int columns=3;
    public static int rows=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);



        Spinner dimensions = (Spinner) findViewById(R.id.dimensions);
        dimensions.setPrompt("Choose Difficulty");
        dimensions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                switch(parent.getSelectedItem().toString())
                {
                    case "2x3":
                        rows = 2;
                        columns = 3;
                        break;
                    case "3x4":
                        rows = 3;
                        columns =4;
                        break;
                    case "4x4":
                        rows = 4;
                        columns = 4;
                        break;
                    case "4x5":
                        rows = 4;
                        columns = 5;
                        break;
                    case "5x6":
                        rows = 5;
                        columns = 6;
                        break;
                    case "6x6":
                        rows = 6 ;
                        columns = 6;
                        break;
                    default:
                        rows = 2;
                        columns = 2;
                        break;

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                rows = 2;
                columns = 2;
            }


        });



        Button play = new Button(this);                 // button for new game
        play.setText("PLAY");
        TableLayout table = (TableLayout) findViewById(R.id.main_table);
        table.addView(play);
        play.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(MainMenu.this, MainActivity.class);         //new activity - play
                MainMenu.this.startActivity(myIntent);
            }
        });

        Button exit = new Button(this);                 //button that closes the android app
        exit.setText("EXIT");
        table.addView(exit);
        exit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });



    }








}



