package me.flashcards.civicsflashcards;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

//to provide data to the UI and survive configuration changes
public class CardViewModel extends AndroidViewModel {

    private CardRepository mRepository;
    private LiveData<List<Card>> mAllWords;

    //constructor of the class to get reference to CardRepository and get list of all cards from the repository
    public CardViewModel(@NonNull Application application) {
        super(application);
        mRepository = new CardRepository(application);
        mAllWords = mRepository.getAllWords();
    }

    //methods to get all the cards from the repository and insert cards into the database;
    LiveData<List<Card>> getAllWords() {
        return mAllWords;
    }

    public void insert(Card card) {
        mRepository.insert(card);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void deleteWord(Card card) {
        mRepository.deleteWord(card);
    }
}
