const pg = require('pg');
const crypto = require('crypto');
const hash = crypto.createHash('sha1');

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

function createTableUsers() {
	var client = initClient();

	client.connect();
	const query = client.query(
		'DROP TABLE IF EXISTS users;' +
		'CREATE TABLE users (' +
		'id UUID PRIMARY KEY DEFAULT gen_random_uuid(),'+
		'name VARCHAR(120) not null, '+
		'username VARCHAR(120) not null,' +
		'email VARCHAR(120) not null unique, '+
		'password VARCHAR(120) not null, '+
		'creditCardInfo VARCHAR(16) not null, '+
		'hash_pin TEXT not null)');

	query.on('end', () => { client.end(); });
}

function createTableProducts() {
	var client = initClient();

	client.connect();
	const query = client.query(
		'DROP TABLE IF EXISTS products;' +
		'CREATE TABLE products (' +
		'id UUID PRIMARY KEY DEFAULT gen_random_uuid(),'+
		'name VARCHAR(120) not null, '+
		'price FLOAT not null)');

	query.on('end', () => { client.end(); });
}

exports.insertUser= function insertUser(req, res, callback){

	var user = req.body;
	console.log(user);
	var client = initClient();

	client.connect();
	const query = client.query('INSERT INTO users (name, username, email, password, creditcardinfo, hash_pin) VALUES ($1, $2, $3, $4, $5, $6) RETURNING users.id', [user.name, user.username, user.email, user.password, user.creditCardInfo, 000],
		function(err, result) {
			if (err) {
					console.log(err);
			} else {
				var pin = generatePin();
				updateUserHashPin(result.rows[0].id, pin, res, callback);
			}
		});
	query.on('end', () => { client.end(); });
}

exports.insertProduct = function insertProduct(product){

	var client = initClient();

	client.connect();
	const query = client.query('INSERT INTO products (name, price) VALUES ($1, $2) RETURNING *', [product.name, product.price],
		function(err, result) {
			if (err) {
					console.log(err);
			} else {
					console.log('row inserted with id: ' + result.rows[0].id);
			}
	});
	query.on('end', () => { client.end(); });
}

exports.getUserByUsername = function getUserByUsername(req, res, callback) {
	var client = initClient();

	var username = req.params['username'];

	client.connect();
	const query = client.query("SELECT * FROM users WHERE users.username='" + username +"'",
		function(err, result) {
			if (err) {
					console.log(err);
			} else {
					callback(res, result.rows[0]);
			}
	});
	query.on('end', () => { client.end(); });

	return query;
}

exports.getProductByName = function getProductByName(req, res, callback) {
	var client = initClient();

	var name = req.params['name'];

	client.connect();
	const query = client.query("SELECT * FROM products WHERE products.name='" + name +"'",
		function(err, result) {
			if (err) {
					console.log(err);
			} else {
					callback(res, result.rows[0]);
			}
	});
	query.on('end', () => { client.end(); });

	return query;
}

exports.getProducts = function getProducts(res, callback) {
	var client = initClient();

	client.connect();
	const query = client.query('SELECT * FROM products',
		function(err, result) {
			if (err) {
					console.log(err);
			} else {
					callback(res, result.rows);
			}
	});
	query.on('end', () => { client.end(); });
}

function updateUserHashPin(userID, pin, res, callback) {
	var client = initClient();

	client.connect();
	const query = client.query("UPDATE users SET hash_pin='" + hash.update(pin).digest('hex') + "' WHERE id='" + userID + "' RETURNING *",
		function(err, result) {
			if (err) {
				console.log(err);
			} else {
				callback(res, {
					'user': result.rows[0],
					'pin': pin
				});
			}
	});
	query.on('end', () => { client.end(); });
}

exports.startDB = function startDB() {
	createTableUsers();
	createTableProducts();
}

function generatePin () {
    min = 0,
    max = 9999;
    return ("0" + Math.floor(Math.random() * (max - min + 1)) + min).substr(-4);
}