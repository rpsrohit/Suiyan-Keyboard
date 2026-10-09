package com.example.suiyankeyboard

import android.inputmethodservice.InputMethodService
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.graphics.Typeface
import android.util.TypedValue

class CustomKeyboardIME : InputMethodService() {
    private lateinit var mainView: LinearLayout
    private var isCapsLocked = false
    private var isLeetMode = false
    private var currentFont = "normal"
    
    private val leetMap = mapOf(
        'A' to "4",'B' to "8",'E' to "3",'G' to "9",'H' to "#",
        'I' to "1",'L' to "1",'O' to "0",'S' to "5",'T' to "7",'Z' to "2"
    )
    
    private val letters = arrayOf(
        "Q W E R T Y U I O P",
        "A S D F G H J K L",
        "Z X C V B N M"
    )
    
    private val fonts = listOf("Normal", "Bold", "Italic", "Mono", "Serif")

    override fun onCreateInputView(): View {
        mainView = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(0xFF1A1A1A.toInt())
        }

        val fontRow = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 50
            )
            orientation = LinearLayout.HORIZONTAL
            setPadding(5, 5, 5, 5)
        }

        fonts.forEach { font ->
            val btn = Button(this).apply {
                text = font
                layoutParams = LinearLayout.LayoutParams(0, 40, 1f)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f)
                setBackgroundColor(0xFF333333.toInt())
                setTextColor(0xFFFFFFFF.toInt())
                setOnClickListener { currentFont = font }
            }
            fontRow.addView(btn)
        }
        mainView.addView(fontRow)

        val modeRow = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 50
            )
            orientation = LinearLayout.HORIZONTAL
            setPadding(5, 5, 5, 5)
        }

        val capsBtn = Button(this).apply {
            text = "CAPS"
            layoutParams = LinearLayout.LayoutParams(0, 40, 1f)
            setBackgroundColor(0xFF555555.toInt())
            setTextColor(0xFFFFFFFF.toInt())
            setOnClickListener {
                isCapsLocked = !isCapsLocked
                setBackgroundColor(if (isCapsLocked) 0xFF00CC00.toInt() else 0xFF555555.toInt())
            }
        }
        modeRow.addView(capsBtn)

        val leetBtn = Button(this).apply {
            text = "L33T"
            layoutParams = LinearLayout.LayoutParams(0, 40, 1f)
            setBackgroundColor(0xFF555555.toInt())
            setTextColor(0xFFFFFFFF.toInt())
            setOnClickListener {
                isLeetMode = !isLeetMode
                setBackgroundColor(if (isLeetMode) 0xFF00CC00.toInt() else 0xFF555555.toInt())
            }
        }
        modeRow.addView(leetBtn)

        val delBtn = Button(this).apply {
            text = "DEL"
            layoutParams = LinearLayout.LayoutParams(0, 40, 1f)
            setBackgroundColor(0xFF555555.toInt())
            setTextColor(0xFFFFFFFF.toInt())
            setOnClickListener {
                currentInputConnection?.deleteSurroundingText(1, 0)
            }
        }
        modeRow.addView(delBtn)

        val spaceBtn = Button(this).apply {
            text = "SPACE"
            layoutParams = LinearLayout.LayoutParams(0, 40, 2f)
            setBackgroundColor(0xFF555555.toInt())
            setTextColor(0xFFFFFFFF.toInt())
            setOnClickListener {
                currentInputConnection?.commitText(" ", 1)
            }
        }
        modeRow.addView(spaceBtn)

        mainView.addView(modeRow)

        letters.forEach { row ->
            val rowView = LinearLayout(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 55
                )
                orientation = LinearLayout.HORIZONTAL
                setPadding(5, 5, 5, 5)
            }

            row.split(" ").forEach { letter ->
                val keyBtn = Button(this).apply {
                    text = letter
                    layoutParams = LinearLayout.LayoutParams(0, 45, 1f)
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                    setBackgroundColor(0xFF444444.toInt())
                    setTextColor(0xFFFFFFFF.toInt())
                    typeface = getTypeface()
                    setOnClickListener { sendKey(letter) }
                }
                rowView.addView(keyBtn)
            }
            mainView.addView(rowView)
        }

        return mainView
    }

    private fun sendKey(letter: String) {
        val ic = currentInputConnection ?: return
        var result = letter
        
        if (isCapsLocked) result = result.uppercase()
        
        if (isLeetMode) {
            result = letter.uppercase().map { char ->
                leetMap[char] ?: char.toString()
            }.joinToString("")
        }
        
        ic.commitText(result, 1)
    }

    private fun getTypeface(): Typeface = when (currentFont) {
        "Bold" -> Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        "Italic" -> Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
        "Mono" -> Typeface.MONOSPACE
        "Serif" -> Typeface.SERIF
        else -> Typeface.DEFAULT
    }
}
