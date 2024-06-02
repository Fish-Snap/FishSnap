package com.example.fishsnap.components

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.example.fishsnap.R
import com.google.android.material.textfield.TextInputLayout

class ConfirmPasswordTextField @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    var passwordTextField: PasswordTextField? = null
        set(value) {
            field = value
            inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val parent = parent.parent
                    if (parent is TextInputLayout) {
                        parent.errorIconDrawable = null
                        when {
                            s.isNullOrEmpty() -> {
                                parent.error = null
                            }
                            value != null && s.toString() != value.text.toString() -> {
                                parent.error = context.getString(R.string.error_confirm_password)
                            }
                            else -> {
                                parent.error = null
                                parent.boxStrokeColor = context.getColor(R.color.transparent)
                            }
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
}