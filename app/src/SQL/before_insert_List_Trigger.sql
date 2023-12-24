-- Trigger to ensure that the list types are only private protected or public
--boroume kai xwris trigger me to enum apla me trigger den tha einai case sensitive. Oti thelete
CREATE TRIGGER before_insert_List
ON List
INSTEAD OF INSERT
AS
BEGIN
  DECLARE @allowed_values VARCHAR(100);
  
  SET @allowed_values = 'Private%|Public%|Protected%';
  
  IF NOT EXISTS (SELECT 1 FROM inserted WHERE listType LIKE @allowed_values)
  BEGIN
    THROW  50000, 'Invalid listType. Allowed values are Private, Public, and Protected.', 1; --to vazoume kai sthn java ws exception handling
  END;
  ELSE
    INSERT INTO List (listId, listType, name, userId, movieName, movieId)
    SELECT listId, listType, name, userId, movieName, movieId
    FROM inserted;
END;