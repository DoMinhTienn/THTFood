package com.example.thtfood.Controller;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.List;

public class CustomValueFormatter extends IndexAxisValueFormatter {
    private List<String> titles;

    public CustomValueFormatter(List<String> titles) {
        this.titles = titles;
    }

    @Override
    public String getFormattedValue(float value) {
        int index = (int) value;
        if (index >= 0 && index < titles.size()) {
            return titles.get(index);
        }
        return "";
    }
}

