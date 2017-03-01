package com.myApp.vilrolf.discgolfputting.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Viljar on 20-Sep-16.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String LOG = "DbHelper";

    private static final int DATABASE_VERSION = 30;

    // Database Name
    private static final String DATABASE_NAME = "dgpDB";

    // Table Names
    private static final String TABLE_GAME = "game";
    private static final String TABLE_MULTIPLAYER_GAME = "multiplayerGame";
    private static final String TABLE_USER = "user";
    private static final String TABLE_THROW = "throw";
    private static final String TABLE_GAME_TYPE = "gameType";


    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    //User Column names
    private static final String USER_COLUMN_NAME = "name";

    // GAME COLUMNS
    private static final String GAME_COLUMN_TIMESTAMP = "timestamp";
    private static final String GAME_COLUMN_FOREIGN_USER_ID = "foreignUserId";
    private static final String GAME_COLUMN_SCORE = "score";
    private static final String GAME_COLUMN_GAME_TYPE = "gameType";
    private static final String GAME_COLUMN_AVG_POINT_PER_THROW = "avgPointPerThrow";
    private static final String GAME_COLUMN_FOREIGN_MULTPLAYER_GAME = "foreignMultiplayerId";

    // MULTIPLAYER COLUMNS
    private static final String MULTIPLAYER_COLUMN_AMOUNT_OF_PLAYERS = "score";

    // THROW COLUMNS
    private static final String THROW_COLUMN_SCORE = "score";
    private static final String THROW_COLUMN_HIT = "hit";
    private static final String THROW_COLUMN_DISTANCE = "distance";
    private static final String THROW_COLUMN_TYPE = "type";
    private static final String THROW_COLUMN_THROW_NR = "throwNr";
    private static final String THROW_COLUMN_ROUND_NR = "roundNr";
    private static final String THROW_COLUMN_FOREIGN_GAME_ID = "foreignGameId";
    private static final String THROW_COLUMN_FOREIGN_USER_ID = "foreignUserId";
    private static final String THROW_COLUMN_TIMESTAMP = "timestamp";


    // GAMETYPE COLUMS
    private static final String GAMETYPE_COLUMN_ROUNDS = "nrOfRounds";
    private static final String GAMETYPE_COLUMN_NR_OF_THROWS_PER_ROUND = "nrOfThrowsPerRound";
    private static final String GAMETYPE_COLUMN_GAME_MODE = "gameMode";
    private static final String GAMETYPE_COLUMN_FIRST_SHOT_MULTIPLIER = "firstShotMultiplier";
    private static final String GAMETYPE_COLUMN_LAST_SHOT_MULTIPLIER = "lastShotMultiplier";
    private static final String GAMETYPE_COLUMN_ALL_SHOT_HIT_BONUS = "allShotHitBonus";
    private static final String GAMETYPE_COLUMN_START_DISTANCE = "startDistance";
    private static final String GAMETYPE_COLUMN_INCREMENT = "increment";
    private static final String GAMETYPE_COLUMN_THRESHOLD_DOWNGRADE = "thresholdDowngrade";
    private static final String GAMETYPE_COLUMN_THRESHOLD_UPGRADE = "thresholdUpgrade";
    private static final String GAMETYPE_COLUMN_POINTS_PER_DISTANCE = "pointsPerDistance";

    private static final String GAMETYPE_COLUMN_GAME_NAME = "gameName";

    private static final String[] timeSort = {"1", "2", "3"};


    private static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS "
            + TABLE_USER + "("
            + KEY_ID + " INTEGER PRIMARY KEY, "
            + USER_COLUMN_NAME + " TEXT" + ");";


    private static final String CREATE_TABLE_GAME = "CREATE TABLE IF NOT EXISTS "
            + TABLE_GAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY, "
            + GAME_COLUMN_FOREIGN_USER_ID + " INTEGER, "
            + GAME_COLUMN_FOREIGN_MULTPLAYER_GAME + " INTEGER, "
            + GAME_COLUMN_AVG_POINT_PER_THROW + " REAL, "
            + GAME_COLUMN_SCORE + " REAL, "
            + GAME_COLUMN_GAME_TYPE + " INTEGER, "
            + GAME_COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
            + ");";


    private static final String CREATE_TABLE_MULTIPLAYER_GAME = "CREATE TABLE IF NOT EXISTS "
            + TABLE_MULTIPLAYER_GAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY, "
            + MULTIPLAYER_COLUMN_AMOUNT_OF_PLAYERS + " INTEGER"
            + ");";


    private static final String CREATE_TABLE_THROW = "CREATE TABLE IF NOT EXISTS "
            + TABLE_THROW + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + THROW_COLUMN_DISTANCE + " REAL, "
            + THROW_COLUMN_HIT + " INTEGER, "
            + THROW_COLUMN_SCORE + " REAL, "
            + THROW_COLUMN_THROW_NR + " INTEGER, "
            + THROW_COLUMN_ROUND_NR + " INTEGER, "
            + THROW_COLUMN_FOREIGN_GAME_ID + " INTEGER, "
            + THROW_COLUMN_FOREIGN_USER_ID + " INTEGER, "
            + THROW_COLUMN_TYPE + " INTEGER, "
            + THROW_COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT  CURRENT_TIMESTAMP"
            + ");";

    private static final String CREATE_TABLE_GAME_TYPE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_GAME_TYPE + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + GAMETYPE_COLUMN_ROUNDS + " INTEGER,"
            + GAMETYPE_COLUMN_GAME_MODE + " INTEGER,"
            + GAMETYPE_COLUMN_NR_OF_THROWS_PER_ROUND + " INTEGER,"
            + GAMETYPE_COLUMN_THRESHOLD_DOWNGRADE + " INTEGER,"
            + GAMETYPE_COLUMN_THRESHOLD_UPGRADE + " INTEGER,"
            + GAMETYPE_COLUMN_FIRST_SHOT_MULTIPLIER + " REAL,"
            + GAMETYPE_COLUMN_LAST_SHOT_MULTIPLIER + " REAL,"
            + GAMETYPE_COLUMN_ALL_SHOT_HIT_BONUS + " REAL,"
            + GAMETYPE_COLUMN_START_DISTANCE + " REAL,"
            + GAMETYPE_COLUMN_INCREMENT + " REAL, "
            + GAMETYPE_COLUMN_POINTS_PER_DISTANCE + " REAL,"
            + GAMETYPE_COLUMN_GAME_NAME + " TEXT"
            + ");";
    private ArrayList<Game> allGamesWithThrows;

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_GAME);
        db.execSQL(CREATE_TABLE_THROW);
        db.execSQL(CREATE_TABLE_GAME_TYPE);
        db.execSQL(CREATE_TABLE_MULTIPLAYER_GAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUND);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_THROW);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAME_TYPE);
        */
        if (newVersion > oldVersion) {
            if (oldVersion < 28) {
                db.execSQL("ALTER TABLE " + TABLE_GAME + " ADD COLUMN " + GAME_COLUMN_FOREIGN_MULTPLAYER_GAME + " INTEGER DEFAULT -1");
                db.execSQL("ALTER TABLE " + TABLE_GAME_TYPE + " ADD COLUMN " + GAMETYPE_COLUMN_GAME_MODE + " INTEGER");
                db.execSQL("UPDATE " + TABLE_GAME_TYPE + " SET " + GAMETYPE_COLUMN_GAME_MODE + " = 1");
            }
            if (oldVersion < 29) {
                db.execSQL("ALTER TABLE " + TABLE_GAME_TYPE + " ADD COLUMN " + GAMETYPE_COLUMN_THRESHOLD_DOWNGRADE + " INTEGER");
                db.execSQL("ALTER TABLE " + TABLE_GAME_TYPE + " ADD COLUMN " + GAMETYPE_COLUMN_THRESHOLD_UPGRADE + " INTEGER");
            }
            if (oldVersion < 30) {
                db.execSQL("ALTER TABLE " + TABLE_GAME + " ADD COLUMN " + GAME_COLUMN_AVG_POINT_PER_THROW + " REAL");
            }

        }


        onCreate(db);
    }

    public void calculateAVGPointPerThrowForEachGame() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Game> allGames = getAllGamesWithThrows();
        for (Game game : allGames) {
            double score = 0;
            for (Throw th : game.getDiscThrows()) {
                if (th.isHit()) {
                    score += th.getDistance();
                }
            }
            game.setAvgPointPerThrow(score / game.getDiscThrows().size());
            ContentValues cv = new ContentValues();
            cv.put(GAME_COLUMN_AVG_POINT_PER_THROW, game.getAvgPointPerThrow());
            int r =   db.update(TABLE_GAME, cv, KEY_ID + " = " + game.getId(), null);
            int a = r+1+2+3;
        }


    }

    public long createUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_COLUMN_NAME, user.getName());

        return db.insert(TABLE_USER, null, values);
    }

    public long createMultiplayerGame() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(MULTIPLAYER_COLUMN_AMOUNT_OF_PLAYERS, 2); // THIS IS JUST SO I CAN ACTUALLY CREATE THIS SHIT
        long id = db.insert(TABLE_MULTIPLAYER_GAME, null, cv);
        return id;

    }

    public void setAllGamesToUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(GAME_COLUMN_FOREIGN_USER_ID, user.getId());
        db.update(TABLE_GAME, cv, null, null);

        //db.rawQuery("UPDATE " + TABLE_GAME + " SET " + GAME_COLUMN_FOREIGN_USER_ID + " = " + user.getId(), null);

    }

    public long createGameType(GameType gameType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(GAMETYPE_COLUMN_ROUNDS, gameType.getRounds());
        cv.put(GAMETYPE_COLUMN_NR_OF_THROWS_PER_ROUND, gameType.getNrOfThrowsPerRound());
        cv.put(GAMETYPE_COLUMN_NR_OF_THROWS_PER_ROUND, gameType.getNrOfThrowsPerRound());
        cv.put(GAMETYPE_COLUMN_FIRST_SHOT_MULTIPLIER, gameType.getFirstShotMultiplier());
        cv.put(GAMETYPE_COLUMN_LAST_SHOT_MULTIPLIER, gameType.getLastShotMultiplier());
        cv.put(GAMETYPE_COLUMN_ALL_SHOT_HIT_BONUS, gameType.getAllShotHitBonus());
        cv.put(GAMETYPE_COLUMN_INCREMENT, gameType.getIncrement());
        cv.put(GAMETYPE_COLUMN_START_DISTANCE, gameType.getStart());
        cv.put(GAMETYPE_COLUMN_POINTS_PER_DISTANCE, gameType.getPointsPerDistance());
        cv.put(GAMETYPE_COLUMN_GAME_NAME, gameType.getGameName());
        cv.put(GAMETYPE_COLUMN_GAME_MODE, gameType.getGameMode());
        cv.put(GAMETYPE_COLUMN_THRESHOLD_DOWNGRADE, gameType.getThresholdDowngrade());
        cv.put(GAMETYPE_COLUMN_THRESHOLD_UPGRADE, gameType.getThresholdUpgrade());

        long id = db.insert(TABLE_GAME_TYPE, null, cv);

        return id;
    }

    public long createGame(Game game) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        if(game.getAvgPointPerThrow() == -1){
            boolean sucsess =  game.updateAvgPointPerThrow();
            if(!sucsess){
                game.setDiscThrows(getThrowsFromGame(game.getId()));
                game.updateAvgPointPerThrow();
            }
        }
        cv.put(GAME_COLUMN_AVG_POINT_PER_THROW,game.getAvgPointPerThrow());
        cv.put(GAME_COLUMN_GAME_TYPE, game.getGameType().getId());
        cv.put(GAME_COLUMN_SCORE, game.getScore());
        cv.put(GAME_COLUMN_FOREIGN_MULTPLAYER_GAME, game.getMultiplayerId());
        cv.put(GAME_COLUMN_FOREIGN_USER_ID, game.getUser().getId());
        return db.insert(TABLE_GAME, null, cv);
    }

    public long createThrow(Throw discThrow) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(THROW_COLUMN_DISTANCE, discThrow.getDistance());
        cv.put(THROW_COLUMN_HIT, discThrow.isHit() ? 1 : 0);
        cv.put(THROW_COLUMN_THROW_NR, (long) discThrow.getThrowNr());
        cv.put(THROW_COLUMN_ROUND_NR, (long) discThrow.getRoundNr());
        cv.put(THROW_COLUMN_TYPE, (long) discThrow.getType());
        cv.put(THROW_COLUMN_SCORE, discThrow.getScore());
        cv.put(THROW_COLUMN_FOREIGN_GAME_ID, discThrow.getGameId());

        long id = db.insert(TABLE_THROW, null, cv);
        return id;
    }


    public ArrayList<User> getAllUsers() {

        ArrayList<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_USER, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            User user = new User();
            user.setId(res.getLong(res.getColumnIndex(KEY_ID)));
            user.setName(res.getString(res.getColumnIndex(USER_COLUMN_NAME)));
            users.add(user);
            res.moveToNext();
        }

        res.close();
        return users;

    }

    /***
     * 0 = ALL, 1 = YEAR, 2 = MONTH, 3 = WEEK, 4 = DAY NOT REALLY DOING ANYTHING NOW I THINK
     *
     * @param timeSort
     * @return uh
     */
    public long getNrOfHits(int timeSort) {
        //TODO think this is working. Kinda boring to do without data though.
        SQLiteDatabase db = this.getWritableDatabase();
        //String day = " AND " + GAME_COLUMN_TIMESTAMP + " BETWEEN datetime('now', 'start of day') AND datetime('now', 'localtime')";

        return DatabaseUtils.queryNumEntries(db, TABLE_THROW, THROW_COLUMN_HIT + " = 1");
    }

    public ArrayList<Throw> getAllThrows() {
        //TODO its not done mate!
        ArrayList<Throw> throwArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        // SELECT * FROM statistics WHERE date BETWEEN datetime('now', 'start of day') AND datetime('now', 'localtime');
        Cursor res = db.rawQuery("select * from " + TABLE_THROW, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            throwArrayList.add(saveThrow(res));

            res.moveToNext();
        }
        res.close();
        return throwArrayList;
    }

    public long getNrOfThrows() {

        SQLiteDatabase db = this.getWritableDatabase();
        return DatabaseUtils.queryNumEntries(db, TABLE_THROW);

    }

    public long getNrOfThrowsWithType(int type) {
        SQLiteDatabase db = this.getWritableDatabase();
        return DatabaseUtils.queryNumEntries(db, TABLE_THROW, THROW_COLUMN_TYPE + " =" + type);
    }

    public long getNrOfHitsWithType(int type) {
        SQLiteDatabase db = this.getWritableDatabase();
        return DatabaseUtils.queryNumEntries(db, TABLE_THROW, THROW_COLUMN_HIT + " = 1 AND " + THROW_COLUMN_TYPE + " = " + type);
    }

    public double[] getAllDifferentDistancesUsed() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT DISTINCT(" + THROW_COLUMN_DISTANCE + ") FROM " + TABLE_THROW, null);
        res.moveToFirst();
        double distances[] = new double[res.getCount()];
        while (!res.isAfterLast()) {
            distances[res.getPosition()] = res.getDouble(res.getColumnIndex(THROW_COLUMN_DISTANCE));
            res.moveToNext();
        }
        res.close();
        return distances;
    }

    private GameType makeGameType(Cursor res) {
        GameType gameType = new GameType();
        gameType.setId(res.getLong(res.getColumnIndex(KEY_ID)));
        gameType.setRounds(res.getLong(res.getColumnIndex(GAMETYPE_COLUMN_ROUNDS)));
        gameType.setNrOfThrowsPerRound(res.getLong(res.getColumnIndex(GAMETYPE_COLUMN_NR_OF_THROWS_PER_ROUND)));
        gameType.setFirstShotMultiplier(res.getDouble(res.getColumnIndex(GAMETYPE_COLUMN_FIRST_SHOT_MULTIPLIER)));
        gameType.setLastShotMultiplier(res.getDouble(res.getColumnIndex(GAMETYPE_COLUMN_LAST_SHOT_MULTIPLIER)));
        gameType.setAllShotHitBonus(res.getDouble(res.getColumnIndex(GAMETYPE_COLUMN_ALL_SHOT_HIT_BONUS)));
        gameType.setIncrement(res.getDouble(res.getColumnIndex(GAMETYPE_COLUMN_INCREMENT)));
        gameType.setStart(res.getDouble(res.getColumnIndex(GAMETYPE_COLUMN_START_DISTANCE)));
        gameType.setPointsPerDistance(res.getDouble(res.getColumnIndex(GAMETYPE_COLUMN_POINTS_PER_DISTANCE)));
        gameType.setGameName(res.getString(res.getColumnIndex(GAMETYPE_COLUMN_GAME_NAME)));
        gameType.setGameMode(res.getLong(res.getColumnIndex(GAMETYPE_COLUMN_GAME_MODE)));
        gameType.setThresholdUpgrade(res.getLong(res.getColumnIndex(GAMETYPE_COLUMN_THRESHOLD_UPGRADE)));
        gameType.setThresholdDowngrade(res.getLong(res.getColumnIndex(GAMETYPE_COLUMN_THRESHOLD_DOWNGRADE)));
        return gameType;
    }

    public ArrayList<GameType> getAllGameTypes() {
        ArrayList<GameType> gameTypes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_GAME_TYPE, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            gameTypes.add(makeGameType(res));
            res.moveToNext();
        }
        return gameTypes;
    }


    public ArrayList<Game> getAllGamesFromUser(User user) {
        ArrayList<Game> games = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from " + TABLE_GAME + " where " + GAME_COLUMN_FOREIGN_USER_ID + " = " + user.getId(), null); //TABLE_GAME, null)
        res.moveToFirst();
        while (!res.isAfterLast()) {
            games.add(saveGame(res));
        }
        return games;

    }

    public ArrayList<Game> getAllGames() {
        ArrayList<Game> games = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT datetime(timestamp, 'localtime') AS timestamp, "
                         + GAME_COLUMN_AVG_POINT_PER_THROW  + "," + GAME_COLUMN_GAME_TYPE + "," + GAME_COLUMN_SCORE + "," + GAME_COLUMN_FOREIGN_MULTPLAYER_GAME + "," + GAME_COLUMN_FOREIGN_USER_ID + "," + KEY_ID + "," + GAME_COLUMN_FOREIGN_MULTPLAYER_GAME + " FROM " + TABLE_GAME
                , null);
        res.moveToFirst();


        while (!res.isAfterLast()) {
            games.add(saveGame(res));
        }

        return games;
    }

    public long getNrOfHitsFromDistance(double distance) {
        SQLiteDatabase db = this.getWritableDatabase();
        return DatabaseUtils.queryNumEntries(db, TABLE_THROW, THROW_COLUMN_HIT + " = 1 AND " + THROW_COLUMN_DISTANCE + " = " + distance);

    }

    public long getNrOfThrowsFromDistance(double distance) {
        SQLiteDatabase db = this.getWritableDatabase();
        return DatabaseUtils.queryNumEntries(db, TABLE_THROW, THROW_COLUMN_DISTANCE + " = " + distance);
    }

    public ArrayList<Throw> getThrowsFromGame(long id) {

        ArrayList<Throw> dThrows = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from " + TABLE_THROW + " WHERE " + THROW_COLUMN_FOREIGN_GAME_ID + " = " + id, null); //TABLE_GAME, null)
        res.moveToFirst();
        while (!res.isAfterLast()) {
            dThrows.add(saveThrow(res));
            res.moveToNext();
        }

        return dThrows;
    }

    private Game saveGame(Cursor res) {
        Game game = new Game();
        game.setId(res.getLong(res.getColumnIndex(KEY_ID)));
        game.setUserId(res.getLong(res.getColumnIndex(GAME_COLUMN_FOREIGN_USER_ID)));
        game.setScore(res.getDouble(res.getColumnIndex(GAME_COLUMN_SCORE)));
        game.setAvgPointPerThrow(res.getDouble(res.getColumnIndex(GAME_COLUMN_AVG_POINT_PER_THROW)));
        game.setGameTypeId((int) res.getLong(res.getColumnIndex(GAME_COLUMN_GAME_TYPE)));
        game.setMultiplayerId(res.getLong(res.getColumnIndex(GAME_COLUMN_FOREIGN_MULTPLAYER_GAME)));
        game.setCreated_at(res.getString(res.getColumnIndex(GAME_COLUMN_TIMESTAMP)));
        res.moveToNext();

        //TODO
        return game;
    }

    private Throw saveThrow(Cursor res) {
        Throw th = new Throw();
        th.setType((int) res.getLong(res.getColumnIndex(THROW_COLUMN_TYPE)));
        th.setThrowNr((int) res.getLong(res.getColumnIndex(THROW_COLUMN_THROW_NR)));
        th.setRoundNr((int) res.getLong(res.getColumnIndex(THROW_COLUMN_ROUND_NR)));
        th.setDistance(res.getDouble(res.getColumnIndex(THROW_COLUMN_DISTANCE)));
        th.setHit(res.getLong(res.getColumnIndex(THROW_COLUMN_HIT)));
        th.setScore(res.getDouble(res.getColumnIndex(THROW_COLUMN_SCORE)));
        th.setTimestamp(res.getString(res.getColumnIndex(THROW_COLUMN_TIMESTAMP)));

        return th;
    }

    public void deleteGame(long id, SQLiteDatabase db) {
        if (db == null) {
            db = this.getReadableDatabase();
        }
        db.delete(TABLE_THROW, THROW_COLUMN_FOREIGN_GAME_ID + "=" + id, null);
        db.delete(TABLE_GAME, KEY_ID + "=" + id, null);
    }

    public boolean hasGames() {

        //TODO HAS TO BE A BETTER WAY!?
        List<Game> games = getAllGames();
        return games.size() > 0;
    }


    public GameType createStandardGameType(int n, String distanceMarker) {
        GameType gameType = new GameType();
        gameType.setGameName("Standard");
        gameType.setNrOfThrowsPerRound(n);
        gameType.setRounds(6);
        gameType.setGameMode(1);

        if (distanceMarker.equals("m")) {
            gameType.setStart(3);
            gameType.setIncrement(1.5);
            gameType.setPointsPerDistance(3);
            gameType.setLastShotMultiplier(1.2);
            gameType.setFirstShotMultiplier(1.2);
            gameType.setAllShotHitBonus(1);

        } else {
            gameType.setStart(10);
            gameType.setIncrement(5);
            gameType.setPointsPerDistance(1);
            gameType.setLastShotMultiplier(1.2);
            gameType.setFirstShotMultiplier(1.2);
            gameType.setAllShotHitBonus(1);
        }

        gameType.setId(createGameType(gameType));
        return gameType;
    }

    public ArrayList<Game> getGamesFromIds(ArrayList<Long> gamesList) {
        SQLiteDatabase db = this.getReadableDatabase();
        String values = "";
        for (Long l : gamesList) {
            values += "" + l + ",";
        }
        values = values.substring(0, values.length() - 1);
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_GAME + " WHERE " + KEY_ID + " IN (" + values + ")", null);
        res.moveToFirst();
        ArrayList<Game> games = new ArrayList<>();
        while (!res.isAfterLast()) {
            games.add(saveGame(res));
        }
        return games;
    }

    public Game getGameFromId(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_GAME + " WHERE " + KEY_ID + " = " + id, null);
        res.moveToFirst();
        return saveGame(res);
    }

    public User getUserFromId(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + KEY_ID + " = " + userId, null);
        res.moveToFirst();
        return saveUser(res);
    }

    private User saveUser(Cursor res) {
        User user = new User();
        user.setId(res.getLong(res.getColumnIndex(KEY_ID)));
        user.setName(res.getString(res.getColumnIndex(USER_COLUMN_NAME)));
        res.moveToNext();
        return user;
    }

    public ArrayList<MultiplayerGame> getAllMultiplayerGames() {
        ArrayList<MultiplayerGame> allMultiplayerGames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_MULTIPLAYER_GAME, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            MultiplayerGame mpg = new MultiplayerGame();
            mpg.setId(res.getLong(res.getColumnIndex(KEY_ID)));
            allMultiplayerGames.add(mpg);
            res.moveToNext();
        }
        res.close();
        for (MultiplayerGame mpg : allMultiplayerGames) {
            Cursor resGame = db.rawQuery("SELECT * FROM " + TABLE_GAME + " WHERE " + GAME_COLUMN_FOREIGN_MULTPLAYER_GAME + " = " + mpg.getId(), null);
            resGame.moveToFirst();
            while (!resGame.isAfterLast()) {
                Game game = saveGame(resGame);
                mpg.addGame(game);
            }
            for (Game game : mpg.getGames()) {
                game.setUser(getUserFromId(game.getUserId()));
            }
            resGame.close();

        }

        ArrayList<MultiplayerGame> realList = new ArrayList<>(allMultiplayerGames);
        for (MultiplayerGame mpg : allMultiplayerGames) {
            if (mpg.getGames().size() == 0) {
                realList.remove(mpg);
            }
        }


        return realList;
    }

    public void deleteMultiPlayerGame(MultiplayerGame mpg) {
        SQLiteDatabase db = this.getReadableDatabase();
        for (Game game : mpg.getGames()) {
            db.delete(TABLE_THROW, THROW_COLUMN_FOREIGN_GAME_ID + "=" + game.getId(), null);
            db.delete(TABLE_GAME, KEY_ID + "=" + game.getId(), null);
        }
        db.delete(TABLE_MULTIPLAYER_GAME, KEY_ID + "=" + mpg.getId(), null);

    }

    public void deleteUser(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        //TODO SAFE DELETE PLZ
        ArrayList<Game> games = getAllGamesFromUser(user);
        for (Game game : games) {
            deleteGame(game.getId(), db);
        }
        db.delete(TABLE_USER, KEY_ID + "=" + user.getId(), null);
    }

    public void deleteGameType(GameType gameType) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_GAME_TYPE, KEY_ID + "=" + gameType.getId(), null);
    }

    public ArrayList<Throw> get7DayStatisticsFromDistance(double distance) {
        SQLiteDatabase db = this.getWritableDatabase();
        // TODAY //               SELECT * FROM statistics          WHERE date                          BETWEEN datetime('now', '-6 days') AND datetime('now', 'localtime');
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_THROW + " WHERE " + THROW_COLUMN_TIMESTAMP + " BETWEEN datetime('now', '-6 days') AND datetime('now', 'localtime')", null);
        ArrayList<Throw> week = new ArrayList<>();
        res.moveToFirst();
        while (!res.isAfterLast()) {
            week.add(saveThrow(res));
            res.moveToNext();
        }
        return week;

    }


    public GameType createStandardDynamicGameType(int n, String distanceMarker) {
        GameType gt = new GameType();
        gt.setGameName("Dynamic Standard Scored");
        gt.setAllShotHitBonus(1);
        gt.setFirstShotMultiplier(1);
        gt.setLastShotMultiplier(1.2);
        gt.setRounds(60 / n);
        gt.setNrOfThrowsPerRound(n);
        gt.setGameMode(2);
        gt.setThresholdDowngrade(n / 2);
        if (n >= 7) {
            gt.setThresholdUpgrade(n - 1);
        } else {
            gt.setThresholdUpgrade(n);
        }

        if (distanceMarker.equals("m")) {
            gt.setStart(3);
            gt.setPointsPerDistance(3);
            gt.setIncrement(1);
        } else {
            gt.setStart(10);
            gt.setPointsPerDistance(1);
            gt.setIncrement(3);
        }
        gt.setId(createGameType(gt));
        return gt;
    }

    public ArrayList<Game> getAllGamesWithThrows() {
        ArrayList<Game> games = getAllGames();
        for (Game game : games) {
            game.setDiscThrows(getThrowsFromGame(game.getId()));
        }
        return games;
    }

    public GameType createStandardStreakGame(int nrOfThrowsPerRound, String distanceMarker) {
        GameType gt = new GameType();
        gt.setGameName("Streak");
        gt.setAllShotHitBonus(1);
        gt.setFirstShotMultiplier(1);
        gt.setLastShotMultiplier(1.2);
        gt.setRounds(6);
        gt.setNrOfThrowsPerRound(nrOfThrowsPerRound);
        gt.setGameMode(3);

        if (distanceMarker.equals("m")) {
            gt.setStart(3);
            gt.setPointsPerDistance(3);
            gt.setIncrement(1);
        } else {
            gt.setStart(10);
            gt.setPointsPerDistance(1);
            gt.setIncrement(3);
        }
        createGameType(gt);
        return gt;
    }

    /**
     * 0 = all , 1 = year, 2 = month, 3 = week, 4 = day
     * @param time
     * @param user, if null Selects all users
     * @return
     */
    public ArrayList<Throw> getAllThrowsFromUserInTimePeriod(int time, User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // TODAY //               SELECT * FROM statistics          WHERE date                          BETWEEN datetime('now', '-6 days') AND datetime('now', 'localtime');


        if(user == null){
            if(time == 0){
                return getAllThrows();
            }
            else if(time == 1) {
                Cursor res = db.rawQuery("SELECT * FROM " + TABLE_THROW + " WHERE " + THROW_COLUMN_TIMESTAMP + " BETWEEN datetime('now', '-6 days') AND datetime('now', 'localtime')", null);
            }
        }
        return null;
    }

}
