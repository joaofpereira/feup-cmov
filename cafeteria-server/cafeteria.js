const pg = require('pg');

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
		//done();
	});
	query.on('end', () => { client.end(); });
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

exports.startDB = function startDB() {
	createTableUsers();
	createTableProducts();
}
