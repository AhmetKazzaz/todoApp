package com.example.myapplication.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.R;

/**
 * Dialog for adding a new todolist with a given name.
 */
public class AddNewTodoListDialog extends Dialog {

    private OnDismissListener listener;
    private String value = null;
    private EditText editText;

    public AddNewTodoListDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_add_todolist);

        if (getWindow() != null)
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        editText = findViewById(R.id.et_prompt_value);
        Button buttonOk = findViewById(R.id.btn_prompt_ok);

        buttonOk.setOnClickListener(btn -> {
            value = editText.getText().toString();
            if (value.isEmpty()) {
                Toast.makeText(getContext(), getContext().getString(R.string.EMPTY_FIELD), Toast.LENGTH_SHORT).show();
                return;
            }

            dismiss();
        });

        setOnDismissListener(dialog -> {
            if (listener != null)
                listener.onDismiss(this, value);
        });
    }

    public void setListener(OnDismissListener listener) {
        this.listener = listener;
    }

    public interface OnDismissListener {
        void onDismiss(AddNewTodoListDialog dialog, String value);
    }
}
