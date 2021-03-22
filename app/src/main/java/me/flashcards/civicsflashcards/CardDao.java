package me.flashcards.civicsflashcards;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CardDao {

    //method to insert one card
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Card card);

    //method to delete all items from the table
    @Query("DELETE FROM WORD_TABLE")
    void deleteAll();

    //method to delete one card
    @Delete
    void deleteWord(Card card);

    //method to get all the cards from the table in List object
    @Query("SELECT * FROM word_table ORDER BY word ASC")
    LiveData<List<Card>> getAllWords();

    //method to get any card
    @Query("SELECT * from word_table LIMIT 1")
    Card[] getAnyWord();
}
