package com.example.thtfood.Controller;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thtfood.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.O)
public class RevenueStatisticsActivity extends AppCompatActivity {
    private ImageButton imageButtonQuit;
    BarChart barChart;
    private RecyclerView recyclerViewMenu;
    private TextView textViewCountOrder;
    private TextView textViewTotal;
    private TextView textViewPercentOrder;
    private TextView textViewPercentTotal;
    private RevenueStatisticsAdapter statisticsAdapter;
    double totalOrder;
    double totalPrice;
    double totalPricePreviousMonth;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    String restaurantId = currentUser.getUid();
    DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("orders").child(restaurantId);
    List<String> title = new ArrayList<>();
    private String key;
    private FrameLayout frameLayout, frameLayout1;
    private int currentMonthOrderCount;
    private int previousMonthOrderCount;
    HashMap<String, Double> dailyTotalOrders = new HashMap<>();
    HashMap<String, Double> previousMonthData = new HashMap<>();
    LocalDate date = LocalDate.now();
    List<BarEntry> barEntryList = new ArrayList<>();
    private int selectedPosition = 0;
    NumberFormat vndFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue_statistics);

        barChart = findViewById(R.id.chart);
        recyclerViewMenu = findViewById(R.id.recyclerViewMenu);
        textViewCountOrder = findViewById(R.id.textViewCountOrder);
        textViewTotal = findViewById(R.id.textViewTotal);
        textViewPercentOrder = findViewById(R.id.textViewPercentOrder);
        textViewPercentTotal = findViewById(R.id.textViewPercentTotal);
        frameLayout = findViewById(R.id.frameLayout);
        frameLayout1 = findViewById(R.id.frameLayout1);
        imageButtonQuit = findViewById(R.id.imageButtonQuit);
        List<String> statisticItem = new ArrayList<>();
        statisticItem.add("Tháng này");
        statisticItem.add("Tháng");
        statisticItem.add("Năm");

        statisticsAdapter = new RevenueStatisticsAdapter(statisticItem);
        imageButtonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerViewMenu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewMenu.setAdapter(statisticsAdapter);
        handleData(selectedPosition);

        statisticsAdapter.setOnItemClickListener(new RevenueStatisticsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                selectedPosition = position;
                statisticsAdapter.setSelectedPosition(position);
                dailyTotalOrders.clear();
                previousMonthData.clear();
                handleData(position);
            }
        });
    }

    private void handleData(int position) {
        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentMonthOrderCount = 0;
                    previousMonthOrderCount = 0;
                    totalPrice = 0;
                    totalPricePreviousMonth = 0;

                    for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                        String orderDate = orderSnapshot.child("orderDate").getValue(String.class);
                        LocalDate currentDate = LocalDate.parse(orderDate, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.getDefault()));
                        LocalDate previousMonthDate = date.minusMonths(1).withDayOfMonth(1);


                        double orderTotal = orderSnapshot.child("totalAmount").getValue(Double.class);
                        String dayOfMonth = String.valueOf(currentDate.getDayOfMonth());
                        if (position == 0) {
                            if (currentDate.getMonthValue() == date.getMonthValue() && currentDate.getYear() == date.getYear()) {
                                currentMonthOrderCount++;
                                String key = dayOfMonth + "/" + currentDate.getMonthValue() + "/" + currentDate.getYear();
                                if (dailyTotalOrders.containsKey(key)) {
                                    double currentTotal = dailyTotalOrders.get(key);
                                    dailyTotalOrders.put(key, currentTotal + orderTotal);
                                } else {
                                    dailyTotalOrders.put(key, orderTotal);
                                }
                            } else if (currentDate.getMonthValue() == previousMonthDate.getMonthValue() && currentDate.getYear() == previousMonthDate.getYear()) {
                                previousMonthOrderCount++;
                                String key = currentDate.getMonthValue() + "/" + currentDate.getYear();
                                if (previousMonthData.containsKey(key)) {
                                    double currentTotal = previousMonthData.get(key);
                                    previousMonthData.put(key, currentTotal + orderTotal);
                                } else {
                                    previousMonthData.put(key, orderTotal);
                                }
                            }
                        } else if (position == 1) {
                            if (currentDate.getYear() == date.getYear()) {
                                key = currentDate.getMonthValue() + "/" + currentDate.getYear();
                                if (dailyTotalOrders.containsKey(key)) {
                                    double currentTotal = dailyTotalOrders.get(key);
                                    dailyTotalOrders.put(key, currentTotal + orderTotal);
                                } else {
                                    dailyTotalOrders.put(key, orderTotal);
                                }
                            }


                        } else {
                            key = String.valueOf(currentDate.getYear());
                            if (dailyTotalOrders.containsKey(key)) {
                                double currentTotal = dailyTotalOrders.get(key);
                                dailyTotalOrders.put(key, currentTotal + orderTotal);
                            } else {
                                dailyTotalOrders.put(key, orderTotal);
                            }
                        }
                    }
                    handleDisplay();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void handleDisplay() {
        barEntryList.clear();
        title.clear();

        List<Map.Entry<String, Double>> sortedEntries = new ArrayList<>(dailyTotalOrders.entrySet());
        Collections.sort(sortedEntries, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> entry1, Map.Entry<String, Double> entry2) {
                return entry1.getKey().compareTo(entry2.getKey());
            }
        });

        int i = 0;
        for (Map.Entry<String, Double> entry : sortedEntries) {
            String key = entry.getKey();
            double total = entry.getValue();
            totalPrice += total;
            barEntryList.add(new BarEntry(i++, (float) total));
            title.add(key);
        }
        for (Map.Entry<String, Double> entry : previousMonthData.entrySet()) {
            double total = entry.getValue();
            totalPricePreviousMonth += total;
        }

        if (selectedPosition == 0) {
            frameLayout.setVisibility(View.VISIBLE);
            frameLayout1.setVisibility(View.VISIBLE);
            handlStatistics();
        } else {
            frameLayout.setVisibility(View.GONE);
            frameLayout1.setVisibility(View.GONE);
        }

        BarDataSet barDataSet = new BarDataSet(barEntryList, "");
        BarData barData = new BarData(barDataSet);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextSize(12f);
        barDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return vndFormat.format(value);
            }
        });
        barChart.setData(barData);
        barChart.invalidate();
        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setValueFormatter(new CustomValueFormatter(title));
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getLegend().setEnabled(false);
    }

    private void handlStatistics() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString spannablePreviousCount;

        SpannableStringBuilder builder2 = new SpannableStringBuilder();
        SpannableString spannablePreviousTotal;
        double percentTotal;

        if (previousMonthOrderCount == 0) {
            totalOrder = currentMonthOrderCount * 100;
        } else {
            totalOrder = Math.round(Math.abs((double) (currentMonthOrderCount - previousMonthOrderCount) / previousMonthOrderCount) * 10000.0) / 100.0;
        }

        if (currentMonthOrderCount >= previousMonthOrderCount) {
            String arrow = "\u2191";
            spannablePreviousCount = new SpannableString(arrow + String.valueOf(totalOrder) + "%");
            spannablePreviousCount.setSpan(new ForegroundColorSpan(Color.parseColor("#006400")), 0, spannablePreviousCount.length(), 0);
        } else {
            String arrow = "\u2193";
            spannablePreviousCount = new SpannableString(arrow + String.valueOf(totalOrder) + "%");
            spannablePreviousCount.setSpan(new ForegroundColorSpan(Color.parseColor("#8B0000")), 0, spannablePreviousCount.length(), 0);
        }
        builder.append(spannablePreviousCount);
        builder.append(" so với tháng trước");

        textViewCountOrder.setText(String.valueOf(currentMonthOrderCount));
        textViewPercentOrder.setText(builder);



        if(totalPricePreviousMonth == 0){
            percentTotal = 100;




        } else{
            percentTotal = Math.round(Math.abs((double) (totalPrice - totalPricePreviousMonth) / totalPricePreviousMonth) * 10000.0) / 100.0;
        }

        if (totalPrice >= totalPricePreviousMonth) {
            String arrow = "\u2191";
            spannablePreviousTotal = new SpannableString(arrow + String.valueOf(percentTotal) + "%");
            spannablePreviousTotal.setSpan(new ForegroundColorSpan(Color.parseColor("#006400")), 0, spannablePreviousTotal.length(), 0);
        } else {
            String arrow = "\u2193";
            spannablePreviousTotal = new SpannableString(arrow + String.valueOf(percentTotal) + "%");
            spannablePreviousTotal.setSpan(new ForegroundColorSpan(Color.parseColor("#8B0000")), 0, spannablePreviousTotal.length(), 0);
        }
        builder2.append(spannablePreviousTotal);
        builder2.append(" so với tháng trước");
        textViewPercentTotal.setText(builder2);
        textViewTotal.setText(String.valueOf(vndFormat.format(totalPrice)));


    }

}