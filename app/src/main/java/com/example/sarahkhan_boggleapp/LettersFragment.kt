package com.example.sarahkhan_boggleapp

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import kotlin.random.Random


class LettersFragment : Fragment() {

    private lateinit var lettersGrid: GridLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_letters, container, false)

        lettersGrid = view.findViewById(R.id.lettersGrid)
        initializeGrid(view)

        view.findViewById<Button>(R.id.clearButton).setOnClickListener {
            // Clear selection logic here
        }

        view.findViewById<Button>(R.id.submitButton).setOnClickListener {
            // Submit word logic here
        }

        return view
    }

    private lateinit var gridLetters: List<Letter>

    private fun initializeGrid(view: View) {
        lettersGrid.removeAllViews()
        val context = view.context

        val vowels = listOf('A', 'E', 'I', 'O', 'U')
        val consonants = ('A'..'Z').toList() - vowels

        val totalCells = 16
        val vowelPositions = mutableSetOf<Int>()
        while (vowelPositions.size < 2) {
            vowelPositions.add((0 until totalCells).random())
        }

        gridLetters = mutableListOf()

        for (row in 0 until 4) {
            for (col in 0 until 4) {
                val position = row * 4 + col

                val letter = when {
                    position in vowelPositions -> vowels.random()
                    (0..3).random() > 2 -> vowels.random()
                    else -> consonants.random()
                }

                val button = Button(context).apply {
                    text = letter.toString()
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = 0
                        height = 0
                        setGravity(Gravity.FILL)
                        setMargins(5, 5, 5, 5)
                        columnSpec = GridLayout.spec(col, 1f)
                        rowSpec = GridLayout.spec(row, 1f)
                    }
                }

                val letterButton = Letter(button, row, col)
                //button.setOnClickListener {
                    //onLetterButtonClick(letterButton)
                //}

                (gridLetters as MutableList<Letter>).add(letterButton)

                lettersGrid.addView(button)
            }
        }
    }


}