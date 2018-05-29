package aris.kek;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Button;
import aris.kek.MainActivity;

public class FlipCard {


    int x,y; // coordinates on our matrix
    public int Image; // image id basically
    Button button; // which button to connect with the card

    public FlipCard (int X, int Y, int IMG, Button B) { // Constructor
        x = X;
        y = Y;
        Image = IMG;
        button = B;
    }

    public void FlipImage() {    //Show image under card
        button.setBackgroundResource(Image);
    }

    public void FlipHidden() {   //default state, card image not visible
        button.setBackgroundResource(R.drawable.defaultim);
    }

    public void setUnClickable() {             //called when we click on button
        button.setEnabled(false);
    }

    public void setClickable() {            // self-explanatory
        button.setEnabled(true);
    }

/*
    public void DrawCardImage()
    {

        MainActivity.lockButtonSizes();//LOCKING BUTTON SIZES Through a custom made function
        //doing this when we click the button because there's no convenient place to lock the size, not even after the finish of createButtons();

        // below code is an example of setting non-scalable background that creates bugs all over
        // button.setBackgroundResource(R.drawable.image1);


        int newWidth = button.getWidth(); // gotta scale image to button here, if we didn't lock button sizes it would resize when we have a small amount of buttons
        int newHeight = button.getHeight();
        //creating a Bitmap and setting it to our set in stone size parameters - the new one is called scaledBitmap

        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), icons[n]); // sets random image with button.setBackground through this
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
        Resources resource = getResources(); // resource is context
        button.setBackground(new BitmapDrawable(resource, scaledBitmap));
    }

    public void lockButtonSizes()
    {

        for(int row=0; row < rows ; row++)
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
*/
}





