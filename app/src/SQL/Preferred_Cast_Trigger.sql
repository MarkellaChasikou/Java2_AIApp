-- Trigger to limit insertions on Preferred_Cast
CREATE TRIGGER before_insert_Preferred_Cast
ON Preferred_Cast
INSTEAD OF INSERT
AS
BEGIN
  DECLARE @user_cast_count INT;
  
  SELECT @user_cast_count = COUNT(*)
  FROM Preferred_Cast
  WHERE userId IN (SELECT userId FROM inserted);
  
  IF @user_cast_count > 4
  BEGIN
    THROW  50000, 'Cannot have more than 4 preferred cast members', 1; --to vazoume kai sthn java ws exception handling
  END;
  ELSE
    INSERT INTO Preferred_Cast (userId, castId, castName)
    SELECT userId, castId, castName
    FROM inserted;
END;