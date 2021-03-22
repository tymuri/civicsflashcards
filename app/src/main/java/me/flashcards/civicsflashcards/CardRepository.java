package me.flashcards.civicsflashcards;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

//class to handle data operations and manage query threads
public class CardRepository {
    private CardDao mCardDao;
    private LiveData<List<Card>> mAllWords;

    //constructor of the class to get handle to the database and initialize member variables
    CardRepository(Application application) {
        CardsDatabase db = CardsDatabase.getDatabase(application);
        mCardDao = db.wordDao();
        mAllWords = mCardDao.getAllWords();
    }

    //method to return cached cards as liveData
    LiveData<List<Card>> getAllWords() {
        return mAllWords;
    }

    //method which uses AsyncTask to run the query on a worker thread
    public void insert(Card card) {
        new insertAsyncTask(mCardDao).execute(card);
    }


    //AsyncTask inner class
    private static class insertAsyncTask extends AsyncTask<Card, Void, Void> {

        private CardDao mAsyncTaskDao;

        insertAsyncTask(CardDao cardDao) {
            mAsyncTaskDao = cardDao;
        }

        @Override
        protected Void doInBackground(final Card... cards) {
            mAsyncTaskDao.insert(cards[0]);
            return null;
        }
    }

    //to delete all of the cards
    private static class deleteAllWordsAsyncTask extends AsyncTask<Void, Void, Void> {
        private CardDao mAsyncTaskDao;

        deleteAllWordsAsyncTask(CardDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    public void deleteAll() {
        new deleteAllWordsAsyncTask(mCardDao).execute();
    }

    //to delete only one card
    private static class deleteWordAsyncTask extends AsyncTask<Card, Void, Void> {
        private CardDao mAsyncTaskDao;

        deleteWordAsyncTask(CardDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Card... params) {
            mAsyncTaskDao.deleteWord(params[0]);
            return null;
        }
    }

    public void deleteWord(Card card) {
        new deleteWordAsyncTask(mCardDao).execute(card);
    }

}
