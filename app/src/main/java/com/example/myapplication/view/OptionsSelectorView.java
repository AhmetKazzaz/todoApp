package com.example.myapplication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;

import com.example.myapplication.R;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

@BindingMethods(
        @BindingMethod(
                type = OptionsSelectorView.class,
                attribute = "onOptionChanged",
                method = "setChangeListener"
        )
)
public class OptionsSelectorView extends LinearLayout implements View.OnClickListener {

    private String mTitle = "";
    private CharSequence[] mOptions;
    private int mSelectedIndex = 0;
    private OnChangeListener mChangeListener;


    private TextView tvTitle;
    private ArrayList<TextView> optionsTextViews = new ArrayList<>();

    public OptionsSelectorView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public OptionsSelectorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, 0);
    }

    public OptionsSelectorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr);
    }

    public void setChangeListener(OnChangeListener listener) {
        mChangeListener = listener;
    }

    public int getSelectedIndex() {
        return mSelectedIndex;
    }

    public void setSelectedIndex(int index) {
        if (mSelectedIndex != -1) {
            setOptionViewSelected(optionsTextViews.get(mSelectedIndex), false);
        }
        mSelectedIndex = index;
        if (mSelectedIndex != -1) {
            setOptionViewSelected(optionsTextViews.get(mSelectedIndex), true);
        }

        if (mChangeListener != null) {
            mChangeListener.onChanged(this, mSelectedIndex);
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            int index = optionsTextViews.indexOf(v);
            if (index != -1) {
                setSelectedIndex(index);
            }
        }
    }

    public interface OnChangeListener {
        void onChanged(OptionsSelectorView selectorView, int selectedOptionIndex);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.OptionsSelectorView,
                defStyleAttr, 0);

        try {
            mTitle = a.getString(R.styleable.OptionsSelectorView_groupTitle);
            mOptions = a.getTextArray(R.styleable.OptionsSelectorView_android_entries);
        } finally {
            a.recycle();

            if (mOptions == null) {
                mOptions = new CharSequence[]{};
            }
        }


        setupLayoutDefaults();

        generateChildViews();
    }

    private void setupLayoutDefaults() {
        setOrientation(VERTICAL);
        int topPadding = getResources().getDimensionPixelOffset(R.dimen.dimen_eight);
        setPadding(0, topPadding, 0, 0);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        lp.bottomMargin = getResources().getDimensionPixelOffset(R.dimen.dimen_sixteen);
        setLayoutParams(lp);

        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
    }

    private void generateChildViews() {
        tvTitle = createTitleView(mTitle);
        addView(tvTitle);

        generateOptionViews();
    }

    private void generateOptionViews() {
        for (TextView tvOption : optionsTextViews) {
            removeView(tvOption);
        }
        optionsTextViews.clear();

        for (int i = 0; i < mOptions.length; i++) {
            String option = mOptions[i].toString();

            TextView tvOption = createOptionView(option);
            optionsTextViews.add(tvOption);

            if (mSelectedIndex == i) {
                setOptionViewSelected(tvOption, true);
            }

            addView(tvOption);
        }
    }

    private void setOptionViewSelected(TextView tvOption, boolean selected) {
        int drawable = selected ? R.drawable.ic_checked : 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            tvOption.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0, 0, drawable, 0
            );
        } else {
            tvOption.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, drawable, 0
            );
        }
    }

    private TextView createTitleView(String title) {
        TextView textView = new TextView(getContext());
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setText(title);

        LayoutParams lp = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        int vMargin = getResources().getDimensionPixelOffset(R.dimen.dimen_eight);
        int marginStart = getResources().getDimensionPixelOffset(R.dimen.dimen_sixteen);
        lp.bottomMargin = vMargin;
        lp.topMargin = vMargin;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            lp.setMarginStart(marginStart);
        } else {
            lp.leftMargin = marginStart;
        }

        textView.setLayoutParams(lp);

        return textView;
    }

    private TextView createOptionView(String option) {

        TextView textView = new TextView(getContext());
        textView.setText(option);
        textView.setClickable(true);
        textView.setFocusable(true);

        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        textView.setBackgroundResource(outValue.resourceId);

        int padding = getResources().getDimensionPixelOffset(R.dimen.dimen_sixteen);
        textView.setPadding(padding, padding, padding, padding);

        int height = getResources().getDimensionPixelOffset(R.dimen.options_text_view_height);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(MATCH_PARENT, height);
        textView.setLayoutParams(lp);

        textView.setOnClickListener(this);

        return textView;
    }
}