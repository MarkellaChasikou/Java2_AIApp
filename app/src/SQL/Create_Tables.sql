CREATE TABLE Chatroom (
 roomId INT IDENTITY(1,1) PRIMARY KEY,
 name varchar(25) NOT NULL
 )
 CREATE TABLE AppUser (
 userId INT IDENTITY(1,1) PRIMARY KEY,
 username varchar(25) UNIQUE NOT NULL,
 pass_word varchar(25) NOT NULL,
 country varchar(15) NOT NULL
)
 CREATE TABLE ChatroomUser (
  roomId INT,
  userId INT,
  PRIMARY KEY (roomId, userId),
  FOREIGN KEY (roomId) REFERENCES Chatroom(roomId),
  FOREIGN KEY (userId) REFERENCES AppUser(userId)
)
CREATE TABLE Followers (
 followedId INT, --the user who gets followed
 followerId INT, --the user that follows 
 PRIMARY KEY (followedId, followerId),
 FOREIGN KEY (followedId) REFERENCES AppUser(userId),
 FOREIGN KEY (followerId) REFERENCES AppUser(userId)
)
CREATE TABLE Preferred_Movies (
 userId INT,
 movieId varchar(25),
 movieName varchar(25) NOT NULL,
 PRIMARY KEY (userId, movieId),
 FOREIGN KEY (userId) REFERENCES AppUser(userId),
)
CREATE TABLE Preferred_Cast (
 userId INT,
 castId varchar(25),
 castName varchar(25) NOT NULL,
 PRIMARY KEY (userId, castId),
 FOREIGN KEY (userId) REFERENCES AppUser(userId),
)
CREATE TABLE Preferred_Genres (
 userId INT,
 genreName varchar(25), --den eimai sigouros an exoun id i an mas noiazei/to xreiazomaste ston pinaka. Nomizw komple einai kai etsi
 PRIMARY KEY (userId, genreName),
 FOREIGN KEY (userId) REFERENCES AppUser(userId),
)
CREATE TABLE Review (
 reviewId INT IDENTITY(1,1) PRIMARY KEY,
 userId INT,
 movieId INT NOT NULL, --tmdb movie id
 review_text varchar(MAX) NOT NULL,
 rating FLOAT NOT NULL,
 spoiler BIT NOT NULL,
 FOREIGN KEY (userId) REFERENCES AppUser(userId)
)
CREATE TABLE Message (
 id INT IDENTITY(1,1) PRIMARY KEY,
 roomId INT,
 userId INT, --the user that sends it
 spoiler BIT NOT NULL,
 text varchar(MAX) NOT NULL,
 FOREIGN KEY (roomId) REFERENCES Chatroom(roomId),
 FOREIGN KEY (userId) REFERENCES AppUser(userId)
)
--afta den thimamai pws eixame pei na ta kanoyme alla afto skeftika twra
CREATE TABLE LastSeenMessage (
 userId INT,
 roomId INT,
 lastSeenMessageId int,
 PRIMARY KEY (userId, roomId),
 FOREIGN KEY (roomId) REFERENCES Chatroom(roomId),
 FOREIGN KEY (userId) REFERENCES AppUser(userId),
 FOREIGN KEY (lastSeenMessageId) REFERENCES Message(Id)
)
CREATE TABLE LastSeenReview (
 userId INT,
 movieId INT,
 lastSeenReviewId int,
 PRIMARY KEY (userId, movieId),
 FOREIGN KEY (userId) REFERENCES AppUser(userId),
 FOREIGN KEY (lastSeenReviewId) REFERENCES Review(reviewId)
)



CREATE TABLE List (
 listId INT IDENTITY(1,1) PRIMARY KEY,
 listType varchar(20) NOT NULL,
 name varchar(25) NOT NULL,
 userId int,
 movieName varchar(30) NOT NULL,
 movieId varchar(20) NOT NULL, --It is varchar because in tmdb database its a string. Also we need it so we can search the movies in the tmdb database from the list immediately. Without the id and with only the name we woulndt be able to do it
 FOREIGN KEY (userId) REFERENCES AppUser(userId)
)

DROP TABLE List
DROP TABLE LastSeenReview
DROP TABLE LastSeenMessage
DROP TABLE Message
DROP TABLE Review
DROP TABLE Preferred_Cast
DROP TABLE Preferred_Genres
DROP TABLE Preferred_Movies
DROP TABLE Followers
DROP TABLE ChatroomUser
DROP TABLE AppUser
DROP TABLE Chatroom





 
 