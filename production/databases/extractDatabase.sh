# extractDatabase.sh

# Extract database file, WAL file and shared memory file
adb exec-out run-as com.example.app cat databases/app.db > app.db
adb exec-out run-as com.example.app cat databases/app.db-wal > app.db-wal
adb exec-out run-as com.example.app cat databases/app.db-shm > app.db-shm

# Commit changes from WAL file to database file
sqlite3 app.db "PRAGMA wal_checkpoint"

# Delete files that are not needed anymore
rm app.db-wal
rm app.db-shm

# Open the database in a browser
open -a "DB Browser For SQLite" app.db
