package me.flashcards.civicsflashcards;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CardViewModel mCardViewModel;
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewCardActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });

        //RecyclerView setup
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final CardListAdapter adapter = new CardListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ViewModelProviders are used to associate viewModel with UI controller
        mCardViewModel = ViewModelProviders.of(this).get(CardViewModel.class);
        mCardViewModel.getAllWords().observe(this, new Observer<List<Card>>() {
            @Override
            public void onChanged(List<Card> cards) {
                // update cached copy of cards
                adapter.setWords(cards);
            }
        });

        //add the functionality to swipe items in the recycler view to delete that item
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                         int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Card myCard = adapter.getWordAtPosition(position);
                        Toast.makeText(MainActivity.this, "Deleting the Card:  " +
                                myCard.getWord(), Toast.LENGTH_LONG).show();

                        //delete the card
                        mCardViewModel.deleteWord(myCard);
                    }
                });

        helper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //method to handle bar item clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        int id = item.getItemId();
        //animation fall down
        if (id == R.id.action_fall_down) {
            runAnimation(recyclerView, 0);
        }
        //animation from bottom to top
        else if (id == R.id.action_slide_bottom) {
            runAnimation(recyclerView, 1);
        }
        //animation slide from the right to the left side
        else if (id == R.id.action_slide_right) {
            runAnimation(recyclerView, 2);
        }
        //to delete all the cards
        else if (id == R.id.clear_data) {
            //add a confirmation toast message
            Toast.makeText(this, "Removing all the cards...",
                    Toast.LENGTH_SHORT).show();

            //delete the existing cards
            mCardViewModel.deleteAll();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Card card = new Card(data.getStringExtra(NewCardActivity.EXTRA_REPLY));
            mCardViewModel.insert(card);
        } else {
            Toast.makeText(getApplicationContext(), R.string.empty_not_saved, Toast.LENGTH_LONG).show();
        }
    }

    //to start animation effects
    private void runAnimation(RecyclerView recyclerView, int type) {
        Context context = recyclerView.getContext();
        LayoutAnimationController controller = null;

        //fall down animation
        if (type == 0)
            controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fall_down);

            //slide from bottom to top
        else if (type == 1)
            controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_slide_from_bottom);

            //slide from the right
        else if (type == 2)
            controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_slide_from_right);

        final CardListAdapter adapter = new CardListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCardViewModel = ViewModelProviders.of(this).get(CardViewModel.class);
        mCardViewModel.getAllWords().observe(this, new Observer<List<Card>>() {
            @Override
            public void onChanged(List<Card> cards) {
                // update cached copy of words
                adapter.setWords(cards);
            }
        });

        //set anim
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();

    }
}
