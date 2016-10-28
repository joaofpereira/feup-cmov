const pg = require('pg');
const bcrypt = require('bcrypt');

function initClient() {
	const client = new pg.Client({
		user: process.env.DATABASE_USER,
		password: process.env.DATABASE_PASSWORD,
		database: process.env.DATABASE_NAME,
		port: process.env.DATABASE_PORT,
		host: process.env.DATABASE_HOST,
		ssl: true
	});
	return client;
}

function createTableCreditCards() {
	var client = initClient();

	client.connect();
	const query = client.query(
		'DROP TABLE creditcards CASCADE;' +
		'CREATE TABLE creditcards (' +
		'id SERIAL PRIMARY KEY not null,'+
		'cardNumber VARCHAR(16) not null,' +
		'securityCode VARCHAR(3) not null,' +
		'expMonth VARCHAR(2) not null,' +
		'expYear VARCHAR(2) not null)');

	query.on('end', () => { client.end(); });
}

function createTableUsers() {
	var client = initClient();

	client.connect();
	const query = client.query(
		'DROP TABLE IF EXISTS users;' +
		'CREATE TABLE users (' +
		'id UUID PRIMARY KEY DEFAULT gen_random_uuid(),'+
		'name VARCHAR(120) not null, '+
		'username VARCHAR(120) not null unique,' +
		'email VARCHAR(120) not null unique, '+
		'password VARCHAR(120) not null, '+
		'creditcard INTEGER references creditcards(id),'+
		'hash_pin TEXT not null)');

	query.on('end', () => { client.end(); });
}

function createTableProducts() {
	var client = initClient();

	client.connect();
	const query = client.query(
		'DROP TABLE IF EXISTS products;' +
		'CREATE TABLE products (' +
		'id SERIAL PRIMARY KEY not null,' +
		'name VARCHAR(120) not null,' +
		'price FLOAT not null)');

	query.on('end', () => { client.end(); });
}

exports.insertCreditCard= function insertCreditCard(req, res, callbackInsertUser){

	var creditcard = req.body;
	var client = initClient();

	client.connect();
	const query = client.query('INSERT INTO creditcards (cardNumber, securityCode, expMonth, expYear) VALUES ($1, $2, $3, $4) RETURNING *', [creditcard.cardNumber, creditcard.securityCode, creditcard.expMonth, creditcard.expYear],
		function(err, result) {
			client.end();
			if (err) {
				callbackInsertUser(res, null, err);
			} else {
				callbackInsertUser(req, res, result.rows[0], null);
			}
		});
}

exports.insertUser= function insertUser(req, res, creditCard, callback){

	var user = req.body;
	var client = initClient();

	client.connect();
	const query = client.query('INSERT INTO users (name, username, email, password, creditcard, hash_pin) VALUES ($1, $2, $3, $4, $5, $6) RETURNING users.id', [user.name, user.username, user.email, encrypt(user.password), creditCard.id, 000],
		function(err, result) {
			client.end();
			if (err) {
				callback(res, null, err);
			} else {
				var pin = generatePin();
				updateUserHashPin(result.rows[0].id, pin, creditCard, res, callback);
			}
		});
}

exports.insertProduct = function insertProduct(product){

	var client = initClient();

	client.connect();
	const query = client.query("INSERT INTO products (name, price) VALUES ($1, $2) RETURNING *", [product.name, product.price],
		function(err, result) {
			client.end();
			if (err) {
					console.log(err);
			} else {
					console.log('row inserted with id: ' + result.rows[0].id);
			}
	});
}

exports.getUserByEmail = function getUserByEmail(req, res, callback, callbackGetUser) {
	var client = initClient();

	var email = req.body.email;
	var pin = req.body.pin;

	client.connect();
	const query = client.query("SELECT * FROM users WHERE users.email='" + email +"'",
		function(err, result) {
			client.end();
			if (err) {
				callback(res, null, err);
			} else {
				if(result.rows.length == 0)
					callback(res, null, "no user found");
				else {
					if(bcrypt.compareSync(pin, result.rows[0].hash_pin))
						callbackGetUser(res, result.rows[0], callback);
					else
						callback(res, null, "wrong pin");
				}
			}
	});
}

exports.getCreditCardByID = function getCreditCardByID(res, user, callback) {
	var client = initClient();

	client.connect();
	const query = client.query("SELECT * FROM creditcards WHERE creditcards.id='" + user.creditcard +"'",
		function(err, result) {
			client.end();
			if (err) {
				callback(res, null, err);
			} else {
				callback(res, {
					'user': user,
					'creditCard': result.rows[0]
				}, null);
			}
	});
}

exports.getProductByName = function getProductByName(req, res, callback) {
	var client = initClient();

	var name = req.params['name'];

	client.connect();
	const query = client.query("SELECT * FROM products WHERE products.name='" + name +"'",
		function(err, result) {
			client.end();
			if (err) {
				console.log(err);
			} else if(result.rows.length == 0){
				callback(res, null, null);
			} else {
				callback(res, result.rows[0], null);
			}
	});
}

exports.getProducts = function getProducts(res, callback) {
	var client = initClient();

	client.connect();
	const query = client.query('SELECT * FROM products',
		function(err, result) {
			client.end();
			if (err) {
					console.log(err);
			} else {
					callback(res, result.rows, null);
			}
	});
}

function updateUserHashPin(userID, pin, creditCard, res, callback) {
	var client = initClient();

	client.connect();
	const query = client.query("UPDATE users SET hash_pin='" + encrypt(pin) + "' WHERE id='" + userID + "' RETURNING *",
		function(err, result) {
			client.end();
			if (err) {
				console.log(err);
			} else {
				callback(res, {
					'user': result.rows[0],
					'pin': pin,
					'creditCard': creditCard
				}, null);
			}
	});
}

exports.startDB = function startDB() {
	createTableCreditCards();
	createTableUsers();
	createTableProducts();
}

function generatePin () {
    min = 0,
    max = 9999;
    return ("0" + Math.floor(Math.random() * (max - min + 1)) + min).substr(-4);
}

function encrypt(elem) {
	var salt = bcrypt.genSaltSync(10);
	return bcrypt.hashSync(elem, salt);
}
