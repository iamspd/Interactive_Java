package com.example.interactivejava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {

        CheckBox mWhippedCream = findViewById(R.id.cbWhippedCream);
        boolean checkWhippedCream = mWhippedCream.isChecked();

        CheckBox mChocolate = findViewById(R.id.cbChocolate);
        boolean checkChocolate = mChocolate.isChecked();

        EditText mName = findViewById(R.id.etName);
        String name = mName.getText().toString();
        int price = calculatePrice(checkWhippedCream, checkChocolate);
        String orderText = createOrderSummary(name, price, checkWhippedCream, checkChocolate);

        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setData(Uri.parse("mailto:"));
        i.putExtra(Intent.EXTRA_SUBJECT, "Coffee order of " + name);
        i.putExtra(Intent.EXTRA_TEXT, orderText);

        if (i.resolveActivity(getPackageManager()) != null) {
            startActivity(i);
        }
    }

    /**
     * This method gives order summary for the given action.
     *
     * @param price of the given order
     * @return text summary
     */
    private String createOrderSummary(String name, int price, boolean addWhippedCream, boolean addChocolate) {

        String priceMessage = getString(R.string.name, name)
                + "\n " + getString(R.string.summary_whipped_cream, addWhippedCream)
                + "\n " + getString(R.string.summary_chocolate, addChocolate)
                + "\n " + getString(R.string.summary_quantity, quantity)
                + "\n " + getString(R.string.summary_price,
                NumberFormat.getCurrencyInstance().format(price));
        priceMessage += "\n " + getString(R.string.thanks);
        return priceMessage;
    }

    /**
     * This method is called when '+' button is clicked.
     */
    public void incrementQty(View view) {
        if (quantity == 100) {
            Toast.makeText(this, "You cannot have more than 100 cups of cooffees", Toast.LENGTH_LONG).show();
            return;
        }
        quantity += 1;
        display(quantity);
    }

    /**
     * This method is called when '-' button is clicked.
     */
    public void decrementQty(View view) {
        if (quantity == 0) {
            Toast.makeText(this, "You cannot have less than 1 cup of coffee", Toast.LENGTH_LONG).show();
            return;
        }
        quantity -= 1;
        display(quantity);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView tvQuantity = findViewById(R.id.tvQty);
        tvQuantity.setText("" + number);
    }

    /**
     * Calculates the price for the given order.
     *
     * @param checkWhippedCream topping of the ordered coffee
     * @param checkChocolate    topping of ordered coffee
     * @return price of the order
     */
    private int calculatePrice(boolean checkWhippedCream, boolean checkChocolate) {

        int pricePerCup = 5;

        if (checkWhippedCream) {
            pricePerCup += 1;
        }

        if (checkChocolate) {
            pricePerCup += 2;
        }

        return quantity * pricePerCup;
    }
}

