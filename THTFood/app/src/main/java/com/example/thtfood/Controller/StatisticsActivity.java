package com.example.thtfood.Controller;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;

import com.example.thtfood.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
public class StatisticsActivity extends AppCompatActivity {
    BarChart barChart;
    private RecyclerView recyclerViewMenu;
    private StatisticsAdapter statisticsAdapter;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    String restaurantId = currentUser.getUid();
    DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("orders").child(restaurantId);
    List<String> title = new ArrayList<>();
    private String key;
    HashMap<String, Double> dailyTotalOrders = new HashMap<>();
    LocalDate date = LocalDate.now();
    List<BarEntry> barEntryList = new ArrayList<>();
    private int selectedPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        barChart = findViewById(R.id.chart);
        recyclerViewMenu = findViewById(R.id.recyclerViewMenu);
        List<String> statisticItem = new ArrayList<>();
        statisticItem.add("Tháng này");
        statisticItem.add("Tháng");
        statisticItem.add("Năm");

        statisticsAdapter = new StatisticsAdapter(statisticItem);

        recyclerViewMenu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewMenu.setAdapter(statisticsAdapter);
        handleData(selectedPosition);

        statisticsAdapter.setOnItemClickListener(new StatisticsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                selectedPosition = position;
                statisticsAdapter.setSelectedPosition(position);
                dailyTotalOrders.clear();
                handleData(position);
            }
        });
    }

    private void handleData(int position) {
        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                        String orderDate = orderSnapshot.child("orderDate").getValue(String.class);
                        LocalDate currentDate = LocalDate.parse(orderDate, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.getDefault()));

                        double orderTotal = orderSnapshot.child("totalAmount").getValue(Double.class);
                        String dayOfMonth = String.valueOf(currentDate.getDayOfMonth());
                        if (position == 0) {
                            if (currentDate.getMonthValue() == date.getMonthValue() && currentDate.getYear() == date.getYear()) {
                                String key = dayOfMonth + "/" + currentDate.getMonthValue() + "/" + currentDate.getYear();
                                if (dailyTotalOrders.containsKey(key)) {
                                    double currentTotal = dailyTotalOrders.get(key);
                                    dailyTotalOrders.put(key, currentTotal + orderTotal);
                                } else {
                                    dailyTotalOrders.put(key, orderTotal);
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
                    setValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void setValue() {
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

            barEntryList.add(new BarEntry(i++, (float) total));
            title.add(key);
        }

        BarDataSet barDataSet = new BarDataSet(barEntryList, "");
        BarData barData = new BarData(barDataSet);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextSize(12f);
        barChart.setData(barData);
        barChart.invalidate();
        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(title));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getAxisRight().setEnabled(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getLegend().setEnabled(false);
    }

}