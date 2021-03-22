package me.flashcards.civicsflashcards;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


// Entity class for Cards database
@Entity(tableName = "word_table")
public class Card {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "word")
    //set string as the primary key in the table and the value can never be null
    private String mWord;


    //public constructor of the class
    public Card(@NonNull String mWord) {
        this.mWord = mWord;
    }

    //method to get a string
    public String getWord() {
        return this.mWord;
    }
}
