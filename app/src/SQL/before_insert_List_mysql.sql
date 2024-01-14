DELIMITER //

CREATE TRIGGER before_insert_List_mysql
BEFORE INSERT ON List
FOR EACH ROW
BEGIN
  DECLARE allowed_values VARCHAR(100);
  
  SET allowed_values = 'Private%|Public%|Protected%';

  IF NEW.listType NOT REGEXP allowed_values THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Invalid listType. Allowed values are Private, Public, and Protected.';
  END IF;
END//

DELIMITER ;
