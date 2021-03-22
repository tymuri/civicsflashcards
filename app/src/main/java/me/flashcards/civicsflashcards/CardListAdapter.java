package me.flashcards.civicsflashcards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


//adapter class for recyclerView
public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.WordViewHolder> {

    private final LayoutInflater mInflater;
    private List<Card> mCards;

    CardListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    // inflate a view item and return the viewHolder containing it
    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new WordViewHolder(itemView);
    }

    // set content of view item at a certain position
    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        if (mCards != null) {
            Card current = mCards.get(position);
            holder.wordItemView.setText(current.getWord());
        } else {
            holder.wordItemView.setText("No Word");
        }
    }

    //
    void setWords(List<Card> cards) {
        mCards = cards;
        notifyDataSetChanged();
    }

    // return the number of items in the RecyclerView
    @Override
    public int getItemCount() {
        if (mCards != null) {
            return mCards.size();
        } else {
            return 0;
        }
    }

    //ViewHolder to display one item using the separate recyclerView layout
    class WordViewHolder extends RecyclerView.ViewHolder {
        private final TextView wordItemView;

        private WordViewHolder(View itemView) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.textView);
        }
    }

    //method to get the word at a given position
    public Card getWordAtPosition(int position) {
        return mCards.get(position);
    }
}
