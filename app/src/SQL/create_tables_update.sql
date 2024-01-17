use bugsbunny;

CREATE TABLE Chatroom (
    roomId INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(25) UNIQUE NOT NULL,
    creatorId INT,
    FOREIGN KEY (creatorId) REFERENCES AppUser(userId)
);

 CREATE TABLE AppUser (
 userId INT AUTO_INCREMENT PRIMARY KEY,
 username varchar(25) UNIQUE NOT NULL,
 pass_word varchar(25) NOT NULL,
 country varchar(15) NOT NULL
);
 CREATE TABLE ChatroomUser (
  roomId INT,
  userId INT,
  PRIMARY KEY (roomId, userId),
  FOREIGN KEY (roomId) REFERENCES Chatroom(roomId),
  FOREIGN KEY (userId) REFERENCES AppUser(userId)
);
CREATE TABLE Followers (
 followedId INT,
 followerId INT,
 PRIMARY KEY (followedId, followerId),
 FOREIGN KEY (followedId) REFERENCES AppUser(userId),
 FOREIGN KEY (followerId) REFERENCES AppUser(userId)
);
CREATE TABLE Preferred_Movies (
 userId INT,
 movieId varchar(25),
 movieName varchar(25) NOT NULL,
 PRIMARY KEY (userId, movieId),
 FOREIGN KEY (userId) REFERENCES AppUser(userId)
);

CREATE TABLE Preferred_Cast (
 userId INT,
 castId varchar(25),
 castName varchar(25) NOT NULL,
 PRIMARY KEY (userId, castId),
 FOREIGN KEY (userId) REFERENCES AppUser(userId)
);
CREATE TABLE Preferred_Genres (
 userId INT,
 genreName varchar(25),
 PRIMARY KEY (userId, genreName),
 FOREIGN KEY (userId) REFERENCES AppUser(userId)
);

CREATE TABLE Review (
 reviewId INT AUTO_INCREMENT PRIMARY KEY,
 userId INT,
 username varchar(25) NOT NULL,
 movieId INT NOT NULL,
 movieName varchar(25) NOT NULL,
 review_text varchar(255) NOT NULL,
 date DATE NOT NULL,
 rating FLOAT NOT NULL,
 spoiler BIT NOT NULL,
 FOREIGN KEY (userId) REFERENCES AppUser(userId)
);

CREATE TABLE Message (
    id INT AUTO_INCREMENT PRIMARY KEY,
    roomId INT,
    userId INT,
    spoiler BIT NOT NULL,
    text VARCHAR(255) NOT NULL,
    username varchar(25) NOT NULL,
    FOREIGN KEY (roomId) REFERENCES Chatroom(roomId) ON DELETE CASCADE,
    FOREIGN KEY (userId) REFERENCES AppUser(userId) ON DELETE CASCADE,
    FOREIGN KEY (username) REFERENCES AppUser(username) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE UnSeenMessage (
    userId INT,
    roomId INT,
    UnSeenMessageId INT,
    PRIMARY KEY (userId, roomId, UnSeenMessageId),
    FOREIGN KEY (roomId) REFERENCES Chatroom(roomId) ON DELETE CASCADE ,
    FOREIGN KEY (userId) REFERENCES AppUser(userId) ON DELETE CASCADE ,
    FOREIGN KEY (UnSeenMessageId) REFERENCES Message(Id) ON DELETE CASCADE
);

CREATE TABLE LastSeenReview (
 userId INT,
 movieId INT,
 lastSeenReviewId int,
 PRIMARY KEY (userId, movieId),
 FOREIGN KEY (userId) REFERENCES AppUser(userId),
 FOREIGN KEY (lastSeenReviewId) REFERENCES Review(reviewId)
);

CREATE TABLE List (
 list_id INT AUTO_INCREMENT PRIMARY KEY
 listType varchar(20) NOT NULL,
 name varchar(25) NOT NULL,
 userId int,
 FOREIGN KEY (userId) REFERENCES AppUser(userId)
);

CREATE TABLE MoviesList (
 list_id INT,
 movieName varchar(80),
 movieId INT,
 PRIMARY KEY (list_id, movieName, movieId),
 FOREIGN KEY (list_id) REFERENCES List(list_id) ON DELETE CASCADE ON UPDATE CASCADE
);

