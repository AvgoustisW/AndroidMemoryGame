package aris.kek;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;


import java.util.Random;


public class OldDrawable extends Activity {

    public static final int lines=4;
    public static final int columns=4;
    public int reset=0;
    public int clicked=0;
    boolean check=false;
    int[] icons ={R.drawable.image1,R.drawable.image2,R.drawable.image3,R.drawable.image4,R.drawable.image5,R.drawable.image6,R.drawable.image7,R.drawable.image8};
    // array with all our resource images
    int n;
    Random randomizer = new Random();
    Button buttons[][] = new Button[lines][columns];

    Drawable getPictures[] = new Drawable[lines*columns];
    Drawable checkPictures[] = new Drawable[3];


    BitmapDrawable bitcompare[] = new BitmapDrawable[lines*columns];   //rows*columns/2 is the correct size ofcourse. Testing with big one first


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.old);

        createButtons(); // function that creates buttons dynamically and leads to these buttons functionality



        // test
        // createImageButtons();

    }

    public void createButtons()
    {
        int counter=0;
        TableLayout table = (TableLayout) findViewById(R.id.ButtonsTable); // get a TableLayout inside our Linear Layout

        for(int row=0; row < lines;row++) {
            TableRow tableRow = new TableRow(this); // add a new row - activity we are at right now
            //layout parameters for Table
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT, 1.0f ));
            //add here
            table.addView(tableRow);

            for (int col = 0; col < columns; col++) {
                final int FINAL_COL = col; // declaring as final for usage on function input through an EventHandler later
                final int FINAL_ROW = row;

                Button button = new Button(this); // onClickListener button
                TableRow.LayoutParams parameters = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT, 1.0f);

                button.setBackgroundResource(R.drawable.defaultim);
                parameters.setMargins(0, 0, 10, 10);
                button.setLayoutParams(parameters);


                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buttonClicker(FINAL_COL, FINAL_ROW);
                    }
                });

                // adding to our dynamically created buttons matrix
                buttons[row][col]= button;// now we have direct access to it
                button.setSelected(false);



                tableRow.addView(button);


                // add garbage on Drawable array that will be used to check between 2 drawables
              //  checkPictures[1]=getResources().getDrawable(R.drawable.garbage1);
               // checkPictures[2]=getResources().getDrawable(R.drawable.garbage2);




                // setting all default background for all buttons




            } // column for
        } // rows for
    }






    private void buttonClicker(int col, int row) {
        // random image shenanigans change bro



        if (reset < 2) {    //reset variable checks if we have 2 images selected.

            if(true) { // gonna check for same button click here , placeholder for now TODO

                reset++;

                // if(reset!=2)
                n = randomizer.nextInt(7);



                Button button = buttons[row][col];
                lockButtonSizes();//LOCKING BUTTON SIZES Through a custom made function
                //doing this when we click the button because there's no convenient place to lock the size, not even after the finish of createButtons();

                // below code is an example of setting non-scalable background that creates bugs all over
                // button.setBackgroundResource(R.drawable.image1);


                int newWidth = button.getWidth(); // gotta scale image to button here, if we didn't lock button sizes it would resize when we have a small amount of buttons
                int newHeight = button.getHeight();
                //creating a Bitmap and setting it to our set in stone size parameters - the new one is called scaledBitmap
                // TODO not randomized
                Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), icons[n]); // sets random image with button.setBackground through this
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
                Resources resource = getResources(); // resource is context
                button.setBackground(new BitmapDrawable(resource, scaledBitmap));
                // keep in mind that BitmapDrawable derives from Drawable


                getPictures[row*col]=getResources().getDrawable(icons[n]); // getting info on picture to compare 2 of them later
                Drawable drawable = getPictures[row*col]; // setting it into a Drawable object

                check = foundChecker(drawable, reset); // calling foundChecker to see whats good - checks if icons match  | check is a boolean variable

                if(check)
                    Toast.makeText(OldDrawable.this, "Found a Match", Toast.LENGTH_SHORT).show();



                // if(button.getBackground() == (new BitmapDrawable(resource, scaledBitmap)));
                //  button.setBackgroundResource(android.R.drawable.btn_default);



                button.getBackground();

            }


        }
        else // After choosing 2 pics // also haved to check if found here with our foundchecker()
        {

            if(check) // if its found
            {

            }
            else // TODO
            {
                for (row = 0; row < lines; row++)
                    for (col = 0; col < columns; col++) {

                        //layout for TableRow
                        // right now reseting everything, find a smart way to reset only the ones that we haven't parsed yet
                        buttons[row][col].setBackgroundResource((R.drawable.defaultim));

                    }
            }

            //resetting completely, also re-adding garbage drawables to array that is used to check em
          //  checkPictures[1]=getResources().getDrawable(R.drawable.garbage1);
         //  checkPictures[2]=getResources().getDrawable(R.drawable.garbage2);
            reset = 0;
            check = false;
        }
    }




    private void lockButtonSizes()
    {

        for(int row=0; row < lines ; row++)
            for(int col=0; col < columns ; col++)
            {

                Button button = buttons[row][col];

                int width = button.getWidth();
                button.setMinWidth(width);
                button.setMaxWidth(width);


                int height = button.getHeight();
                button.setMinHeight(height);
                button.setMaxHeight(height);

            }
    }

    private boolean foundChecker(Drawable drawable, int place)
    {

        checkPictures[place]=drawable; // inserting current drawable

        if(checkPictures[1].getConstantState()== checkPictures[2].getConstantState()) //always check between the two , we need the getConstantState() fot this to work without false positives
            return true;

        return false;
    }






    /*                      */


    public void createImageButtons()
    {

        LinearLayout gameBoard = (LinearLayout)findViewById(R.id.linlay);

        ImageButton[] imageButtons;

        final int NUM_IBUTTONS=6;

        //LinearLayout.LayoutParams lp = new LinearLayout().LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        imageButtons = new ImageButton[NUM_IBUTTONS];

        for(int i = 0 ; i < NUM_IBUTTONS; i++){
            imageButtons[i] = new ImageButton(this);
            imageButtons[i].setImageResource(R.drawable.image1);
            // imageButtons[i].setLayoutParams(lp);
            imageButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ibuttonClicker(3);
                }
            });
            imageButtons[i].setBackgroundColor(Color.TRANSPARENT);
            imageButtons[i].setTag(i);
            imageButtons[i].setId(i);
            gameBoard.addView(imageButtons[i]);
        }


    }

    private void ibuttonClicker(int num)
    {



    }


}



