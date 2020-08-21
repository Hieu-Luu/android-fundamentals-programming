package com.android.persistentdata.ui.room;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.persistentdata.R;
import com.android.persistentdata.model.room.Word;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class WordRoomFragment extends Fragment {

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    public static final int UPDATE_WORD_ACTIVITY_REQUEST_CODE = 2;

    public static final String EXTRA_DATA_UPDATE_WORD = "extra_word_to_be_updated";
    public static final String EXTRA_DATA_ID = "extra_data_id";

    private WordViewModel mWordViewModel;

    public static WordRoomFragment newInstance() {
        return new WordRoomFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_word_room, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView recyclerView = requireActivity().findViewById(R.id.recyclerview);
        final WordListAdapter adapter = new WordListAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Get a new or existing ViewModel from the ViewModelProvider.
        mWordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        mWordViewModel.getAllWords().observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                // Update the cached copy of the words in the adapter.
                adapter.setWords(words);
            }
        });
        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewWordActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });
        setHasOptionsMenu(true);
        // Add the functionality to swipe items in the
        // recycler view to delete that item
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Word mWord = adapter.getWordAtPosition(position);
                Toast.makeText(getContext(), "Deleting " + mWord.getWord(), Toast.LENGTH_LONG).show();
                mWordViewModel.deleteWord(mWord);
            }
        });
        helper.attachToRecyclerView(recyclerView);
        adapter.setOnItemClickListener(new WordListAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Word word = adapter.getWordAtPosition(position);
                launchUpdateWordActivity(word);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (null != data && requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Word word = new Word(Objects.requireNonNull(data.getStringExtra(NewWordActivity.EXTRA_REPLY)));
            mWordViewModel.insert(word);
            Toast.makeText(getContext(), "Added" + word.getWord(), Toast.LENGTH_LONG).show();
        } else if (requestCode == UPDATE_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String wordString = Objects.requireNonNull(data).getStringExtra(NewWordActivity.EXTRA_REPLY);
            int id = data.getIntExtra(NewWordActivity.EXTRA_REPLY_ID, -1);
            if (id != -1){
                mWordViewModel.update(new Word(id, Objects.requireNonNull(wordString)));
            } else {
                Toast.makeText(getContext(), R.string.unable_to_update,
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), R.string.empty_not_saved, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        requireActivity().getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_contact).setVisible(false);
        menu.findItem(R.id.action_favorites).setVisible(false);
        menu.findItem(R.id.action_order).setVisible(false);
        menu.findItem(R.id.action_status).setVisible(false);
        menu.findItem(R.id.action_settings).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_clear_data) {
            Toast.makeText(getContext(), "Clearing data...", Toast.LENGTH_LONG).show();
            mWordViewModel.deleteAll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void launchUpdateWordActivity(Word word) {
        Intent intent = new Intent(getContext(), NewWordActivity.class);
        intent.putExtra(EXTRA_DATA_UPDATE_WORD, word.getWord());
        intent.putExtra(EXTRA_DATA_ID, word.getId());
        startActivityForResult(intent, UPDATE_WORD_ACTIVITY_REQUEST_CODE);
    }
}
