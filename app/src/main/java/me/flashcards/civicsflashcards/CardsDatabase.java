package me.flashcards.civicsflashcards;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

//Abstract class which is a layer on SQLiteDatabase,
@Database(entities = {Card.class}, version = 1, exportSchema = false)
public abstract class CardsDatabase extends RoomDatabase {


    //all the DAOs that work with the database come here;
    public abstract CardDao wordDao();

    //to make the database singleton so that there is only one instance of database.
    private static CardsDatabase INSTANCE;

    public static CardsDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CardsDatabase.class) {
                if (INSTANCE == null) {
                    // database creation goes here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CardsDatabase.class, "word_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    //to run asyncTask whenever app is opened
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    //to delete all existing entries and repopulating the database with the contents of string array
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final CardDao mDao;
        //set initial list of the cards
        String[] words = {"1. What is the supreme law of the land",
                "2. What does the\n" +
                        "Constitution do?",
                "3. What is an amendment?",
                "4. How many amendments\n" +
                        "does the Constitution have?"};

        PopulateDbAsync(CardsDatabase db) {
            mDao = db.wordDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {

            //if we have no cards, then create the initial list of cards
            if (mDao.getAnyWord().length < 1) {
                for (int i = 0; i <= words.length - 1; i++) {
                    Card card = new Card(words[i]);
                    mDao.insert(card);
                }
            }
            return null;
        }
    }
}

