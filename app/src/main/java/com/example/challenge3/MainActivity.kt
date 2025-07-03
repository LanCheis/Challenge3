package com.example.mathgame

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    // UI Components
    private lateinit var tvLives: TextView
    private lateinit var tvTimer: TextView
    private lateinit var tvScore: TextView
    private lateinit var tvQuestion: TextView
    private lateinit var etAnswer: EditText
    private lateinit var btnSubmit: Button
    private lateinit var btnStart: Button

    // Mode Selection Components
    private lateinit var btnAddition: Button
    private lateinit var btnSubtraction: Button
    private lateinit var btnMultiplication: Button
    private lateinit var btnMixed: Button

    // Game Screen Components
    private lateinit var gameScreen: View
    private lateinit var mainScreen: View
    private lateinit var resultScreen: View

    // Result Screen Components
    private lateinit var tvFinalScore: TextView
    private lateinit var tvCongratulations: TextView
    private lateinit var btnPlayAgain: Button
    private lateinit var btnExit: Button

    // Game Variables
    private var lives = 3
    private var score = 0
    private var timeLeft = 60 // 60 seconds
    private var num1 = 0
    private var num2 = 0
    private var correctAnswer = 0
    private var currentOperation = ""
    private var gameTimer: CountDownTimer? = null
    private var isGameActive = false
    private var selectedGameMode = "mixed" // default mode

    // Operation types
    private val operations = arrayOf("+", "-", "×")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setupClickListeners()
        showMainScreen()
    }

    private fun initializeViews() {
        // Main screen
        mainScreen = findViewById(R.id.mainScreen)
        btnStart = findViewById(R.id.btnStart)

        // Mode selection buttons
        btnAddition = findViewById(R.id.btnAddition)
        btnSubtraction = findViewById(R.id.btnSubtraction)
        btnMultiplication = findViewById(R.id.btnMultiplication)
        btnMixed = findViewById(R.id.btnMixed)

        // Game screen
        gameScreen = findViewById(R.id.gameScreen)
        tvLives = findViewById(R.id.tvLives)
        tvTimer = findViewById(R.id.tvTimer)
        tvScore = findViewById(R.id.tvScore)
        tvQuestion = findViewById(R.id.tvQuestion)
        etAnswer = findViewById(R.id.etAnswer)
        btnSubmit = findViewById(R.id.btnSubmit)

        // Result screen
        resultScreen = findViewById(R.id.resultScreen)
        tvFinalScore = findViewById(R.id.tvFinalScore)
        tvCongratulations = findViewById(R.id.tvCongratulations)
        btnPlayAgain = findViewById(R.id.btnPlayAgain)
        btnExit = findViewById(R.id.btnExit)
    }

    private fun setupClickListeners() {
        btnStart.setOnClickListener {
            startGame()
        }

        // Mode selection listeners
        btnAddition.setOnClickListener {
            selectGameMode("addition")
        }

        btnSubtraction.setOnClickListener {
            selectGameMode("subtraction")
        }

        btnMultiplication.setOnClickListener {
            selectGameMode("multiplication")
        }

        btnMixed.setOnClickListener {
            selectGameMode("mixed")
        }

        btnSubmit.setOnClickListener {
            checkAnswer()
        }

        btnPlayAgain.setOnClickListener {
            resetGame()
            showMainScreen()
        }

        btnExit.setOnClickListener {
            finish()
        }
    }

    private fun showMainScreen() {
        mainScreen.visibility = View.VISIBLE
        gameScreen.visibility = View.GONE
        resultScreen.visibility = View.GONE
        updateModeButtons()
    }

    private fun selectGameMode(mode: String) {
        selectedGameMode = mode
        updateModeButtons()
    }

    private fun updateModeButtons() {
        // Reset all buttons to default state
        resetButtonState(btnAddition)
        resetButtonState(btnSubtraction)
        resetButtonState(btnMultiplication)
        resetButtonState(btnMixed)

        // Highlight selected button
        when (selectedGameMode) {
            "addition" -> setSelectedButtonState(btnAddition)
            "subtraction" -> setSelectedButtonState(btnSubtraction)
            "multiplication" -> setSelectedButtonState(btnMultiplication)
            "mixed" -> setSelectedButtonState(btnMixed)
        }
    }

    private fun resetButtonState(button: Button) {
        button.alpha = 0.7f
        button.isSelected = false
    }

    private fun setSelectedButtonState(button: Button) {
        button.alpha = 1.0f
        button.isSelected = true
    }

    private fun showGameScreen() {
        mainScreen.visibility = View.GONE
        gameScreen.visibility = View.VISIBLE
        resultScreen.visibility = View.GONE
    }

    private fun showResultScreen() {
        mainScreen.visibility = View.GONE
        gameScreen.visibility = View.GONE
        resultScreen.visibility = View.VISIBLE

        // Display final results
        tvFinalScore.text = "Final Score: $score"

        val congratsMessage = when {
            score >= 25 -> "Outstanding! You're a math wizard! 🧙‍♂️"
            score >= 20 -> "Excellent! You're a math genius! 🌟"
            score >= 15 -> "Great job! Keep practicing! 👏"
            score >= 10 -> "Good work! You're improving! 👍"
            score >= 5 -> "Nice try! Practice makes perfect! 💪"
            else -> "Don't give up! Try again! 🚀"
        }
        tvCongratulations.text = congratsMessage
    }

    private fun startGame() {
        isGameActive = true
        showGameScreen()
        generateNewQuestion()
        startTimer()
        updateUI()
    }

    private fun resetGame() {
        lives = 3
        score = 0
        timeLeft = 60
        isGameActive = false
        gameTimer?.cancel()
        etAnswer.text.clear()
    }

    private fun generateNewQuestion() {
        when (selectedGameMode) {
            "addition" -> {
                currentOperation = "+"
                num1 = Random.nextInt(1, 50)
                num2 = Random.nextInt(1, 50)
                correctAnswer = num1 + num2
            }
            "subtraction" -> {
                currentOperation = "-"
                num1 = Random.nextInt(10, 100)
                num2 = Random.nextInt(1, num1)
                correctAnswer = num1 - num2
            }
            "multiplication" -> {
                currentOperation = "×"
                num1 = Random.nextInt(1, 13)
                num2 = Random.nextInt(1, 13)
                correctAnswer = num1 * num2
            }
            "mixed" -> {
                currentOperation = operations[Random.nextInt(operations.size)]
                when (currentOperation) {
                    "+" -> {
                        num1 = Random.nextInt(1, 50)
                        num2 = Random.nextInt(1, 50)
                        correctAnswer = num1 + num2
                    }
                    "-" -> {
                        num1 = Random.nextInt(10, 100)
                        num2 = Random.nextInt(1, num1)
                        correctAnswer = num1 - num2
                    }
                    "×" -> {
                        num1 = Random.nextInt(1, 13)
                        num2 = Random.nextInt(1, 13)
                        correctAnswer = num1 * num2
                    }
                }
            }
        }

        tvQuestion.text = "$num1 $currentOperation $num2 = ?"
        etAnswer.text.clear()
    }

    private fun checkAnswer() {
        if (!isGameActive) return

        val userAnswer = etAnswer.text.toString().trim()

        if (userAnswer.isEmpty()) {
            Toast.makeText(this, "Please enter an answer", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val answer = userAnswer.toInt()

            if (answer == correctAnswer) {
                score++
                Toast.makeText(this, "Correct! +1 point", Toast.LENGTH_SHORT).show()
                generateNewQuestion()
            } else {
                lives--
                Toast.makeText(this, "Wrong! Correct answer was $correctAnswer", Toast.LENGTH_SHORT).show()

                if (lives > 0) {
                    generateNewQuestion()
                } else {
                    endGame()
                }
            }

            updateUI()

        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startTimer() {
        gameTimer?.cancel()
        gameTimer = object : CountDownTimer(timeLeft * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = (millisUntilFinished / 1000).toInt()
                updateUI()
            }

            override fun onFinish() {
                timeLeft = 0
                endGame()
            }
        }.start()
    }

    private fun updateUI() {
        tvLives.text = "Lives: $lives"
        tvTimer.text = "Time: ${timeLeft}s"
        tvScore.text = "Score: $score"
    }

    private fun endGame() {
        isGameActive = false
        gameTimer?.cancel()
        showResultScreen()
    }

    override fun onDestroy() {
        super.onDestroy()
        gameTimer?.cancel()
    }
}