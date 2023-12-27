-- Trigger to limit insertions on Preferred_Genres
CREATE TRIGGER before_insert_Preferred_Genres
ON Preferred_Genres
INSTEAD OF INSERT
AS
BEGIN
  DECLARE @user_genre_count INT;
  
  SELECT @user_genre_count = COUNT(*)
  FROM Preferred_Genres
  WHERE userId IN (SELECT userId FROM inserted);
  
  IF @user_genre_count > 4
  BEGIN
    THROW  50000, 'Cannot have more than 4 preferred genres', 1; --to vazoume kai sthn java ws exception handling
  END;
  ELSE
    INSERT INTO Preferred_Genres (userId, genreName)
    SELECT userId, genreName
    FROM inserted;
END;