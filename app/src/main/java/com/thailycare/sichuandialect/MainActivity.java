package com.thailycare.sichuandialect;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private List<LexiconEntry> allEntries;
    private RecyclerView recyclerView;
    private EditText searchEditText;
    private Button searchButton;
    private LexiconAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        try {
            dbHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dbHelper.openDataBase();

        allEntries = dbHelper.getAllEntries();

        recyclerView = findViewById(R.id.recyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        adapter = new LexiconAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            if (query.isEmpty()) {
                Toast.makeText(this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
                return;
            }
            List<LexiconEntry> results = performFuzzySearch(query, allEntries);
            adapter.updateEntries(results);
            if (results.isEmpty()) {
                Toast.makeText(this, "未找到结果", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private List<LexiconEntry> performFuzzySearch(String query, List<LexiconEntry> entries) {
        // 先检查完全匹配
        List<LexiconEntry> exactMatches = entries.stream()
                .filter(e -> e.character.equals(query))
                .collect(Collectors.toList());

        if (!exactMatches.isEmpty()) {
            return exactMatches; // 如果有完全匹配，只返回这些
        } else {
            // 没有完全匹配，进行模糊搜索
            List<String> characters = entries.stream().map(e -> e.character).collect(Collectors.toList());
            List<ExtractedResult> matches = FuzzySearch.extractTop(query, characters, 50, 90);
            List<LexiconEntry> resultEntries = new ArrayList<>();
            for (ExtractedResult match : matches) {
                for (LexiconEntry entry : entries) {
                    if (entry.character.equals(match.getString())) {
                        resultEntries.add(entry);
                        break;
                    }
                }
            }
            return resultEntries;
        }
    }
}