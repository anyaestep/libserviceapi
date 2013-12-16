CREATE TABLE libdb.student_school ( 
	student_id           varchar( 32 ) NOT NULL,
	school_id            varchar( 32 ) NOT NULL,
	CONSTRAINT idx_student_school PRIMARY KEY ( student_id, school_id )
 );

CREATE TABLE libdb.book ( 
	book_id              varchar( 32 ) NOT NULL,
	name                 varchar( 100 ),
	content_path         varchar( 255 ) NOT NULL,
	CONSTRAINT pk_book PRIMARY KEY ( book_id )
 ); 
 
CREATE TABLE libdb.book_school ( 
	book_id              varchar( 32 ) NOT NULL,
	school_id            varchar( 32 ) NOT NULL,
	CONSTRAINT idx_book_school PRIMARY KEY ( book_id, school_id )
 );

ALTER TABLE libdb.book_school ADD CONSTRAINT fk_book_school FOREIGN KEY ( book_id ) REFERENCES libdb.book( book_id );

insert into libdb.book values ('1', '1','/tmp/audit.log');
insert into libdb.book_school values ('1', '1');
insert into libdb.student_school values ('1', '1');