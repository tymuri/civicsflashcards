package me.flashcards.civicsflashcards;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class CardDao_Impl implements CardDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfCard;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfCard;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public CardDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCard = new EntityInsertionAdapter<Card>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `word_table`(`word`) VALUES (?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Card value) {
        if (value.getWord() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getWord());
        }
      }
    };
    this.__deletionAdapterOfCard = new EntityDeletionOrUpdateAdapter<Card>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `word_table` WHERE `word` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Card value) {
        if (value.getWord() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getWord());
        }
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM WORD_TABLE";
        return _query;
      }
    };
  }

  @Override
  public void insert(final Card card) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCard.insert(card);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteWord(final Card card) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfCard.handle(card);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public LiveData<List<Card>> getAllWords() {
    final String _sql = "SELECT * FROM word_table ORDER BY word ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"word_table"}, false, new Callable<List<Card>>() {
      @Override
      public List<Card> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false);
        try {
          final int _cursorIndexOfMWord = CursorUtil.getColumnIndexOrThrow(_cursor, "word");
          final List<Card> _result = new ArrayList<Card>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Card _item;
            final String _tmpMWord;
            _tmpMWord = _cursor.getString(_cursorIndexOfMWord);
            _item = new Card(_tmpMWord);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Card[] getAnyWord() {
    final String _sql = "SELECT * from word_table LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfMWord = CursorUtil.getColumnIndexOrThrow(_cursor, "word");
      final Card[] _result = new Card[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final Card _item;
        final String _tmpMWord;
        _tmpMWord = _cursor.getString(_cursorIndexOfMWord);
        _item = new Card(_tmpMWord);
        _result[_index] = _item;
        _index ++;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
