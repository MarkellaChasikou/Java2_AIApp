-- Trigger to limit insertions on Preferred_Movies
CREATE TRIGGER before_insert_Preferred_Movies
ON Preferred_Movies
INSTEAD OF INSERT
AS
BEGIN
  DECLARE @user_movie_count INT;
  
  SELECT @user_movie_count = COUNT(*)
  FROM Preferred_Movies
  WHERE userId IN (SELECT userId FROM inserted);
  
  IF @user_movie_count > 4
  BEGIN
    THROW  50000, 'Cannot have more than 4 preferred movies', 1; --to vazoume kai sthn java ws exception handling
  END;
  ELSE
    INSERT INTO Preferred_Movies (userId, movieId, movieName)
    SELECT userId, movieId, movieName
    FROM inserted;
END;