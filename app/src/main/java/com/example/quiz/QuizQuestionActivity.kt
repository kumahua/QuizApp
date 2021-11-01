package com.example.quiz

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.quiz.databinding.ActivityQuizQuestionBinding

class QuizQuestionActivity : AppCompatActivity(), OnClickListener {

    private lateinit var binding: ActivityQuizQuestionBinding
    private var mCurrentPosition: Int = 1
    private lateinit var mQuestionsList: ArrayList<Question>
    private var mSelectedOptionPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_quiz_question)
        binding = ActivityQuizQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mQuestionsList = Constants.getQuestions()

        setQuestion()

        binding.tvOptionOne.setOnClickListener(this)
        binding.tvOptionTwo.setOnClickListener(this)
        binding.tvOptionThree.setOnClickListener(this)
        binding.tvOptionFour.setOnClickListener(this)

        // Adding a click event for submit button.
        binding.btnSubmit.setOnClickListener(this)

    }

    @SuppressLint("SetTextI18n")
    private fun setQuestion() {
        //mCurrentPosition = 1
        val question = mQuestionsList[mCurrentPosition - 1]

        //每次出現新的選項時，確保btn維持預設值
        defaultOptionsView()

        if(mCurrentPosition == mQuestionsList.size) {
            binding.btnSubmit.text = "Finish"
        } else {
            binding.btnSubmit.text = "SUBMIT"
        }

        binding.apply {
            progressBar.progress = mCurrentPosition
            tvProgress.text = "$mCurrentPosition" + "/" + binding.progressBar.max
            tvQuestion.text = question.question
            ivMage.setImageResource(question.image)
            tvOptionOne.text = question.optionOne
            tvOptionTwo.text = question.optionTwo
            tvOptionThree.text = question.optionThree
            tvOptionFour.text = question.optionFour
        }
    }

    private fun defaultOptionsView() {
        val options = ArrayList<TextView>()

        options.add(0, binding.tvOptionOne)
        options.add(1, binding.tvOptionTwo)
        options.add(2, binding.tvOptionThree)
        options.add(3, binding.tvOptionFour)

        for(option in options) {
            option.apply {
                //parseColor:解析顏色字符串，返回對應的color-int
                setTextColor(Color.parseColor("#7A8089"))
                //typeface:字體
                typeface = Typeface.DEFAULT
            }
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.default_option_border_bg
            )
        }
    }

    override fun onClick(v: View?) {

        when(v?.id) {
            binding.tvOptionOne.id -> {
                selectedOptionView(binding.tvOptionOne, 1)
            }
            binding.tvOptionTwo.id -> {
                selectedOptionView(binding.tvOptionTwo, 2)
            }
            binding.tvOptionThree.id -> {
                selectedOptionView(binding.tvOptionThree, 3)
            }
            binding.tvOptionFour.id -> {
                selectedOptionView(binding.tvOptionFour, 4)
            }
            //Adding a click event for submit button. And change the questions and check the selected answers.
            binding.btnSubmit.id -> {
                if(mSelectedOptionPosition == 0) {

                    mCurrentPosition++

                    when {
                        mCurrentPosition <= mQuestionsList.size -> {
                            setQuestion()
                        } else -> {
                            Toast.makeText(this,
                                "You have successfully completed the quiz!",
                            Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    val question = mQuestionsList[mCurrentPosition - 1]

                    // This is to check if the answer is wrong
                    if(question.correctAnswer != mSelectedOptionPosition ) {
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                        //answerView(question.correctAnswer, R.drawable.correct_option_border_bg)
                    }

                    // This is for correct answer
                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

                    if(mCurrentPosition == mQuestionsList.size) {
                        binding.btnSubmit.text = "Finish"
                    } else {
                        binding.btnSubmit.text = "GO TO NEXT QUESTION"
                    }
                    //提交答案後將選像設為0，以進入下一題
                    mSelectedOptionPosition = 0
                }
            }
        }
    }

    private fun answerView(answer: Int, drawableView: Int) {
        when(answer) {
            1 -> {
                binding.tvOptionOne.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            2 -> {
                binding.tvOptionTwo.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            3 -> {
                binding.tvOptionThree.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            4 -> {
                binding.tvOptionFour.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
        }
    }

    private fun selectedOptionView(selectedtv: TextView, selectedOptionNum: Int) {

        //每次再點擊一次時，將所有tv設成default
        defaultOptionsView()

        mSelectedOptionPosition = selectedOptionNum

        selectedtv.apply {
            //parseColor:解析顏色字符串，返回對應的color-int
            setTextColor(Color.parseColor("#363A43"))
            //typeface:字體
            setTypeface(typeface, Typeface.BOLD)
        }
        selectedtv.background = ContextCompat.getDrawable(
            this,
            R.drawable.selected_option_border_bg
        )
    }
}
