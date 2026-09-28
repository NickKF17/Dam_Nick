package com.example.diceroller

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/*Con un dado
/**
 * This activity allows the user to roll a dice and view the result
 * on the screen.
 */

//la de arriba hace que se lancen los dados al entrar por primera vez

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rollButton: Button = findViewById(R.id.button)
        rollButton.setOnClickListener { rollDice() }

        // Do a dice roll when the app starts
        rollDice()
        rollButton.setOnClickListener {
            rollDice()
                 val toast = Toast.makeText(this, "Dice Rolled!", Toast.LENGTH_SHORT)
                 toast.show()

        }
    }
/*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val rollButton: Button = findViewById(R.id.button)

        rollButton.setOnClickListener {
            rollDice()/*  Texto temporal para saber que funciona el boton
                 val toast = Toast.makeText(this, "Dice Rolled!", Toast.LENGTH_SHORT)
                 toast.show()
                 //otra forma de ponerlo
                 //Toast.makeText(this, "Dice Rolled!", Toast.LENGTH_SHORT).show()
             */
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }*/
    /**
     * Roll the dice and update the screen with the result.
     */
    private fun rollDice() {
        val dice = Dice(6)
        val diceRoll = dice.roll()

        val diceImage: ImageView = findViewById(R.id.imageView)

        when (diceRoll) {
            1 -> diceImage.setImageResource(R.drawable.dice_1)
            2 -> diceImage.setImageResource(R.drawable.dice_2)
            3 -> diceImage.setImageResource(R.drawable.dice_3)
            4 -> diceImage.setImageResource(R.drawable.dice_4)
            5 -> diceImage.setImageResource(R.drawable.dice_5)
            6 -> diceImage.setImageResource(R.drawable.dice_6)
        }
    }
/* Otra forma de ponerlo
val drawableResource = when (diceRoll) {
   1 -> R.drawable.dice_1
   2 -> R.drawable.dice_2
   3 -> R.drawable.dice_3
   4 -> R.drawable.dice_4
   5 -> R.drawable.dice_5
   6 -> R.drawable.dice_6
}

diceImage.setImageResource(drawableResource)

 */





    /*
    // Update the screen with the dice roll
    val resultTextView: TextView = findViewById(R.id.textView)
    resultTextView.text = diceRoll.toString()
    */

    }


class Dice(val numSides: Int) {

    fun roll(): Int {
        return (1..numSides).random()
    }
}
*/


// DOS DADOS
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rollButton: Button = findViewById(R.id.button)
        rollButton.setOnClickListener {
            rollDice()
            Toast.makeText(this, "Dice Rolled!", Toast.LENGTH_SHORT).show()
        }

        // Tirada inicial al abrir la app
        rollDice()
    }

    private fun rollDice() {
        val dice = Dice(6)

        val diceImage: ImageView = findViewById(R.id.imageView)
        val diceImage2: ImageView = findViewById(R.id.imageView2)

        diceImage.setImageResource(drawableFor(dice.roll()))
        diceImage2.setImageResource(drawableFor(dice.roll()))
    }

    private fun drawableFor(roll: Int): Int = when (roll) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }
}

class Dice(val numSides: Int) {
    fun roll(): Int = (1..numSides).random()
}