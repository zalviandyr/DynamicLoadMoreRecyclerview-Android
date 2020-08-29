package com.zukron.dynamicloadrecyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final List<Letter> letters = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            letters.add(new Letter("Title " + i, "Description " + i));
        }

        final LetterAdapter letterAdapter = new LetterAdapter(recyclerView, this);
        letterAdapter.setLetters(letters);
        letterAdapter.setOnLoadMoreListener(new LetterAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (letters.size() <= 50) {
                    letters.add(null);
                    letterAdapter.notifyItemInserted(letters.size() - 1);

                    new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            letters.remove(letters.size() - 1);
                            letterAdapter.notifyItemRemoved(letters.size());

                            // generate
                            int index = letters.size();
                            int end = index + 10;

                            for (int i = index; i < end; i++) {
                                letters.add(new Letter("Title " + i, "Description " + i));
                            }

                            letterAdapter.notifyDataSetChanged();
                            letterAdapter.setLoaded();
                        }
                    }, 2000);
                }
            }
        });

        recyclerView.setAdapter(letterAdapter);
    }
}