package aris.kek;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;




public class MainActivity extends Activity {


    public static final int rows = MainMenu.rows; // this will be dynamically inputted from MainMenu Activity through a Spinner
    public static final int columns= MainMenu.columns;
    public static final int size = rows*columns;
    Button buttons[][] = new Button[columns][rows];
    FlipCard cards[][] = new FlipCard[columns][rows];
    int [] remainingImages = new int[size];
    int matched[][] = new int [columns][rows];
    MediaPlayer bgmusic; // sound


    boolean card_num=false;

    int first_r = 0; // usage to check images ID
    int first_c = 0; // usage to check images ID


    int counter = 0;
    long remaining = 0; // using long because we are messing with milliseconds
    CountDownTimer countdown;


/* how do we use this to not crash on that small bug? TODO
    @Override
    public void onBackPressed() {
        finish();
    }
*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish(); // this is not enough for the dialog to get destroyed because it is created after the timer.
                  // we can pause the timer here to bypass that but the activity will stay so we have to just destroy it completely
        System.exit(0); // we need this command to be done with it
        // also by exiting completely we get rid of the bug of not being able to change the difficulty



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamelay);

        bgmusic = MediaPlayer.create(this,R.raw.bgmusic);
        bgmusic.setVolume(2.0f,2.0f);
        bgmusic.start();

        createButtons(); // function that creates buttons dynamically and leads to these buttons functionality
       // createImageButtons();

    }


public int calculateTime() // depending on difficulty we give different countdown timer
{
    switch(size)
    {
        case 6: //2x3 or 3x2
            return 10000;
        case 12: // 4x3 or 3x4
            return 20000;
        case 16: // 4x4
            return 30000;
        case 20:// 4x5 or 5x4
            return 55000;
        case 30:// 5x6 or 6x5
           return 75000;
        case 36:// 6x6
            return 90000;

    }

    return 60000; // default although we never reach this statement
}



    public void createButtons()
    {
        int z,k;
        for(z=0 ; z<size; z++) // initializing remainingImages array that we will use later to insert images to cards
        {
            remainingImages[z]=2; // value of 2, every time we insert an image we subtract 1 until 0. Will be used later.
        }

        z=0;
        k=0;

        for(z=0; z<rows;z++)
            for(k=0;k<columns;k++)
                matched[k][z]=0;





        TableLayout table = (TableLayout)findViewById(R.id.ButtonsTable); // get a TableLayout inside our Linear Layout
        //drawable resources
        int[] images ={R.drawable.image1,R.drawable.image2,R.drawable.image3,
                       R.drawable.image4,R.drawable.image5,R.drawable.image6,
                       R.drawable.image7,R.drawable.image8,R.drawable.image9,
                       R.drawable.image10,R.drawable.image11,R.drawable.image12,
                       R.drawable.image13,R.drawable.image14,R.drawable.image15,
                       R.drawable.image16,R.drawable.image17,R.drawable.image18
                      };


        //button segment

        for(int row=0; row< rows; row++)
        {
            TableRow tableRow = new TableRow(this); // add a new row - activity we are at right now
            //layout parameters for Table
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
            //add here
            table.addView(tableRow);

            for(int col=0; col<columns; col++)
            {
                final int FINAL_COL = col; // declaring as final for usage on function input through an EventHandler later
                final int FINAL_ROW = row; // TODO I don't actually understand WHY the handler needs these to be final

                Button button = new Button(this); // onClickListener button
                TableRow.LayoutParams parameters = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                parameters.setMargins(0, 0, 10, 10);
                button.setLayoutParams(parameters);

                // adding to our dynamically created buttons matrix
                buttons[col][row]= button;// now we have direct access to it
                button.setBackgroundResource(R.drawable.defaultim);
                int inum;
                inum=imagesInit();

                FlipCard temp = new FlipCard(col,row,images[inum],button); // creates a FlipCard Object with coordinates, select image & button

                cards[col][row]= temp;  // inserts into our dynamically created  array.

                //final Button FINAL_BUTTON = button;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buttonClicker(FINAL_COL, FINAL_ROW); // here is why we need to declare a new final INT FINAL_COL/ROW above. It won't accept it otherwise.
                    }
                });

                tableRow.addView(button);

            } // column for
        } // row for

            Tick_Tock();//TIMER STARTS HERE
    } // create Buttons




    public int imagesInit() {
        Random randomizer = new Random();
        int flag = 1;
        int Inum = 0;

        while(flag == 1) {
            Inum = randomizer.nextInt(size/2); // 8 images for 4x4 for example because we ll have a total of 16 images but half of them are duplicates

            remainingImages[Inum]=remainingImages[Inum] - 1;
            if (remainingImages[Inum] >= 0) {
                flag = 0;
            }
        }


        return Inum; // returns int for which image goes to which button
    }



    public void Tick_Tock() {                      // Timer countdown

        TableLayout table = (TableLayout)findViewById(R.id.ButtonsTable);
        final TextView time = new TextView(this);
        table.addView(time);

       // calculateTime();// calculate how much time is given to the player depending on difficulty
        countdown = new CountDownTimer(calculateTime()+1000, 1000) { // 10-60 seconds depending on difficulty , the second variable is refresh rate ( 1 second )

            public void onTick(long millisUntilFinished) {
                time.setText("Time: " + millisUntilFinished / 1000);
                remaining = millisUntilFinished;
            }

            public void onFinish() {
                time.setText("GAME OVER");
                GameDecider(0);
            }
        };
        countdown.start();
    }

    public void timer_pause() {		  // pauses countdown to show results after a win
        countdown.cancel();
    }


    public void buttonClicker(int c,int r) {



        int i,j;
        final int _c = c; // Once again declaring as final for usage on function input through an EventHandler later
        final int _r = r; // TODO I don't actually understand WHY the handler needs these to be final


        // buttons are cards, using card terminology throughout for presentation reasons
        cards[c][r].FlipImage(); // flipping this card from a default state
        card_num = !card_num; // which card do we have here? 1st or 2nd
        // card_num is first declared as false, meaning that the first time we click a button its going to turn true
        // This means that we are going to start with the 1st card and then with the above command we are going to flip the number
        // every time we click a card

        if (card_num) {           // getting 1st card

            first_c = c; // using  id-like variables to compare
            first_r = r;
            cards[c][r].setUnClickable(); // sets card ( button behind it ) unClickable
        }
        else  //second card chosen
        {
                           // .Image is an int that represents an image number. Here we compare the 1st card that has coordinates of first_c / first_r
            if (cards[c][r].Image == cards[first_c][first_r].Image)          // with the second card which took from [c][r] at the start of buttonClicker
            {
                cards[c][r].setUnClickable();                                // if true then make second card unClickable
                counter++;                                                  // counter to see if we ve reached our goal
                matched[c][r]=1;
                matched[first_c][first_r]=1;

                                                  // In the occasion of reaching the goal we timer_pause the Tick_Tock and call our exit function with appropriate values
              if (counter == size/2) // size/2 because we have rows*columns, so 4x4 = 16 but we half of those are duplicates so win condition is 8 matches found
                {
                    timer_pause(); // stops the timer through timer_pause() function
                    GameDecider(1); // if we have found all of the card matches then we win
                }

            } // not the same cards
            else // restart to clickable cards and hide their images
            { //TODO *** SOLVED GLITCHES *** - use thread instead of runnable? Search for implications and find solution to small glitches
                    // we require a handler because otherwise we would have to use
                    // a sleep method in the main thread to give time for the player to look upon these images and remember them
                    // and ofCourse doing that is impossible in AndroidOS. So we start a new thread here.

                for(i=0; i<rows; i++) // don't let user click on any buttons until the failure time perishes
                    for(j =0; j<columns; j++)
                      if(cards[j][i]!=cards[_c][_r] && cards[j][i]!=cards[first_c][first_r])
                      {
                          cards[j][i].setUnClickable();
                      }

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {



                        cards[_c][_r].FlipHidden();
                        cards[first_c][first_r].FlipHidden();
                        cards[_c][_r].setClickable();
                        cards[first_c][first_r].setClickable();

                        for(int i=0; i<rows; i++) // failure time is over now set the rest of them clickable again
                            for(int j =0; j<columns; j++)
                                if(cards[j][i]!=cards[_c][_r] && cards[j][i]!=cards[first_c][first_r] && matched[j][i]!=1)
                                {
                                    cards[j][i].setClickable();
                                }

                    }

                }, 450); // 450 ms is the time it is given by the game for you to look upon the images and remember them



            }



        }


    }

    public void GameDecider(int decider) {		  // called when we have a game winner or time's up
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

        if (decider==1) // TODO display as milliseconds on time-left perhaps?
                if(remaining <= 2000) // if you had 2 seconds left displays a relief message
                {
                    dialog.setTitle("Nice! You made it by the skin of your teeth!! " );
                    dialog.setMessage("You had: " + (remaining) / 1000 + " seconds left ^ ^");
                }
                else if(remaining >= 1000+calculateTime()-calculateTime()/2) // if you made it in less than half the time  (20seconds = made it in 10 seconds
                {
                    dialog.setTitle("WoW that was fast, sure you re not cheating?? ");
                    dialog.setMessage("Your time was: " + (calculateTime()+1000 - remaining) / 1000 + " seconds");
                }
                else // displays your time
                    dialog.setTitle("Good Job! " + "Your time was: " + (calculateTime()+1000-remaining)/1000 + " seconds" );
        else
            dialog.setTitle("Tough Luck!");

        dialog.setPositiveButton("Play Again?", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent restart = new Intent(MainActivity.this, MainActivity.class);
                MainActivity.this.startActivity(restart);
            }
        });

        dialog.setNegativeButton("Back to main menu", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
               // finish();
               // System.exit(0); // we need this for spinner to reset for some reason.
                //There is a time lag when using exit() whereas finish() is faster.(seems like more background operations are there in exit())
                // however, by not using system.exit the dialog is going to be shown no matter what and then we are going to crash

                System.exit(0);
                //TODO investigate, ask questions
                //below code won't compile, the problem is that while we want below code to work so we wont have issues it will not allow for
                // the spinner to change values for some reason...
               Intent mainmenu = new Intent(MainActivity.this, MainMenu.class);
                MainActivity.this.startActivity(mainmenu);

            }
        });

        dialog.show(); // if we do not exit with System.exit(0) we have 2 bugs, one is we cannot change the Spinner for some reason...
                        // the other is that this dialog will crash our program since we will be in a different activity.


    }



    @Override
    protected void onPause()
    {
        super.onPause();
        bgmusic.release();

    }




}



