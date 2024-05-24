package com.example.fishsnap.components

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.example.fishsnap.R

class usernameTextField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : AppCompatEditText(context, attrs) {

    private var handler = Handler(Looper.getMainLooper())
    private val usernameValidationRunnable = Runnable {
        val text = text.toString()
        val words = text.split(" ")
        if (words.size > 1) {
            error = context.getString(R.string.error_username)
        }
    }

    private var isFieldFilled = false

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isFieldFilled = s.toString().isNotEmpty()
                handler.removeCallbacks(usernameValidationRunnable)
                handler.postDelayed(usernameValidationRunnable, 500)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            requestFocus()
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }
        return super.onTouchEvent(event)
    }
}