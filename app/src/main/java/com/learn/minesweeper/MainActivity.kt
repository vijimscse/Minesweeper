package com.learn.minesweeper

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.learn.minesweeper.dto.CellType
import com.learn.minesweeper.dto.Number
import com.learn.minesweeper.dto.Level
import com.learn.minesweeper.utils.GameOverDialog
import com.learn.minesweeper.utils.ScreenUtils.getScreenHeight
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var mGameViewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mGameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        val spinner: Spinner = findViewById(R.id.game_level_selector)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.game_level,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        if (savedInstanceState == null)
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> mGameViewModel.setBoardSize(Level.EASY)
                    1 -> mGameViewModel.setBoardSize(Level.MEDIUM)
                    2 -> mGameViewModel.setBoardSize(Level.DIFFICULT)
                }
            }
        }

        mGameViewModel.getGameLevel().observe(this, Observer<Level> {
            mGameViewModel.initBoardContents()
            board.removeAllViews()
            addCells()
            showSelectedCells()
        })

        mGameViewModel.getBoard().isGameOver().observe(this, Observer<Boolean> {
            if (it) {
                //Toast.makeText(this, R.string.game_over, Toast.LENGTH_SHORT).show()
                val gameOverDialog = GameOverDialog {
                    mGameViewModel.initBoardContents()
                    board.removeAllViews()
                    addCells()
                }
                gameOverDialog.show(supportFragmentManager, getString(R.string.game_over))
            }
        })
    }

    private fun addCells() {
        for ((i, list) in mGameViewModel.getBoard().getCells().withIndex()) {
            val row = LinearLayout(this)
            row.gravity = Gravity.CENTER_HORIZONTAL
            row.orientation = LinearLayout.HORIZONTAL
            row.weightSum = mGameViewModel.getBoardSize().toFloat()
            for (j in list.indices) {
                val cell = CellView(this)
                cell.textSize = 30F
                cell.setTextColor(ContextCompat.getColor(this, R.color.number_text))
                cell.gravity = Gravity.CENTER
                cell.background = ContextCompat.getDrawable(this, R.drawable.cell_background_not_open)
                val param = LinearLayout.LayoutParams(0, (getScreenHeight() / (mGameViewModel.getBoardSize()) - 50), 1F)
                cell.layoutParams = param
                cell.tag = Pair(i,j)
                cell.setOnClickListener {
                    onSelectCell(cell)
                }
                row.addView(cell)
            }
            board.addView(row)
        }
    }

    private fun onSelectCell(cellView: CellView) {
        mGameViewModel.open(cellView.tag as Pair<Int, Int>)

        showSelectedCells()
    }

    private fun showSelectedCells() {
        for ((i, list) in mGameViewModel.getBoard().getCells().withIndex()) {
            for ((j, element) in list.withIndex()) {
                val iteratingCell = (board.getChildAt(i) as LinearLayout).getChildAt(j) as CellView

                if (mGameViewModel.getBoard().getCells()[i][j].isOpen) {
                    when (mGameViewModel.getBoard().getCells()[i][j].type) {
                        CellType.NUMBER -> iteratingCell.text = (element as Number).count.toString()
                        CellType.MINE -> {
                            iteratingCell.background =
                                ContextCompat.getDrawable(this, R.drawable.bomb)
                        }
                    }
                    iteratingCell.isSelected = true
                    iteratingCell.isClickable = false
                }
            }
        }
    }
}
