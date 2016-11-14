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
		'CREATE TABLE creditcards (' +
		'id SERIAL PRIMARY KEY not null,'+
		'cardNumber VARCHAR(16) not null,' +
		'securityCode VARCHAR(3) not null,' +
		'expMonth VARCHAR(2) not null,' +
		'expYear VARCHAR(2) not null)');

	query.on('end', () => { client.end(); });
}

function dropTableCreditCards() {
	var client = initClient();

	client.connect();
	const query = client.query(
		'DROP TABLE creditcards;');

	query.on('end', () => { client.end(); });
}

function createTableUsers() {
	var client = initClient();

	client.connect();
	const query = client.query(
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

function dropTableUsers() {
	var client = initClient();

	client.connect();
	const query = client.query(
		'DROP TABLE users;');

	query.on('end', () => { client.end(); });
}

function createTableVouchers() {
	var client = initClient();

	client.connect();
	const query = client.query(
		'CREATE TABLE vouchers (' +
		'id SERIAL PRIMARY KEY not null,'+
		'type CHAR(1) not null,' +
		'serialNumber INTEGER not null,' +
		'signature VARCHAR(64) not null,'+
		'userID UUID references users(id) not null)');

	query.on('end', () => { client.end(); });
}

function dropTableVouchers() {
	var client = initClient();

	client.connect();
	const query = client.query(
		'DROP TABLE vouchers;');

	query.on('end', () => { client.end(); });
}

function createTableBlacklist() {
	var client = initClient();

	client.connect();
	const query = client.query(
		'CREATE TABLE blacklist (' +
		'id SERIAL PRIMARY KEY not null,'+
		'userID UUID references users(id) not null,' +
		'motive VARCHAR(60) not null)');

	query.on('end', () => { client.end(); });
}

function dropTableBlacklist() {
	var client = initClient();

	client.connect();
	const query = client.query(
		'DROP TABLE blacklist;');

	query.on('end', () => { client.end(); });
}

function createTableProducts() {
	var client = initClient();

	client.connect();
	const query = client.query(
		'CREATE TABLE products (' +
		'id SERIAL PRIMARY KEY not null,' +
		'name VARCHAR(120) not null,' +
		'price FLOAT not null)');

	query.on('end', () => { client.end(); });
}

function dropTableProducts() {
	var client = initClient();

	client.connect();
	const query = client.query(
		'DROP TABLE products;');

	query.on('end', () => { client.end(); });
}

function createTableTransactions() {
	var client = initClient();

	client.connect();
	const query = client.query(
		'CREATE TABLE transactions (' +
		'id SERIAL PRIMARY KEY not null,'+
		'totalValue FLOAT not null,' +
		'date timestamp not null,'+
		'userID UUID references users(id) not null)');

	query.on('end', () => { client.end(); });
}

function dropTableTransactions() {
	var client = initClient();

	client.connect();
	const query = client.query(
		'DROP TABLE transactions;');

	query.on('end', () => { client.end(); });
}

function createTableTransactionRows() {

	var client = initClient();

	client.connect();
	const query = client.query(
		'CREATE TABLE transactionrows (' +
		'id SERIAL PRIMARY KEY not null,'+
		'transactionID INTEGER references transactions(id) not null,'+
		'productID INTEGER references products(id) not null,' +
		'amount INTEGER not null)'
	);

	query.on('end', () => { client.end(); });
}

function dropTableTransactionRows() {
	var client = initClient();

	client.connect();
	const query = client.query(
		'DROP TABLE transactionrows;');

	query.on('end', () => { client.end(); });
}

exports.insertCreditCard = function insertCreditCard(req, res, callbackInsertUser){

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

exports.updateCreditCard = function updateCreditCard(req, res, callbackUpdateUserCreditCard, callback){

	var creditcard = req.body;
	var client = initClient();

	client.connect();
	const query = client.query('INSERT INTO creditcards (cardNumber, securityCode, expMonth, expYear) VALUES ($1, $2, $3, $4) RETURNING *', [creditcard.cardNumber, creditcard.securityCode, creditcard.expMonth, creditcard.expYear],
		function(err, result) {
			client.end();
			if (err) {
				console.log(err);
			} else {
				callbackUpdateUserCreditCard(req, res, result.rows[0], callback);
			}
		});
}

exports.updateUserCreditCard = function updateUserCreditCard(req, res, callbackDeleteUserFromBlackList, creditCard, callback){

	var user = req.body;
	var client = initClient();
	console.log("Entrei no update user");
	console.log(user);
	console.log("creditCard " + creditCard.id);

	client.connect();
	const query = client.query("UPDATE users SET creditcard ='"+ creditCard.id + "' WHERE users.id ='"+ user.userID +"'",
		function(err, result) {
			client.end();
			if (err) {
				console.log(err);
			} else {
				callbackDeleteUserFromBlackList(res, callback, {'userID': user.userID, 'creditCardID': creditCard.id})
			}
		});
}

exports.deleteUserFromBlackList = function deleteUserFromBlackList(res, callback, obj) {
	var client = initClient();
	client.connect();

	const query = client.query("DELETE FROM blacklist WHERE blacklist.userid='" + obj.userID + "' AND blacklist.motive='Invalid Credit Card'",
		function(err, result) {
			client.end();
			if (err) {
				callback(res, null, err);
			} else {
				console.log(callback);
				console.log("credit card: " + obj.creditCardID);
				callback(res, {'creditCardID': obj.creditCardID}, null)
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

exports.insertTransaction = function insertTransaction(req, res, callback, callbackTransactionRows, typeOfVouchers){

	var transaction = req.body;

	console.log(typeOfVouchers);

	transaction.products = JSON.parse(transaction.products);

	if(typeOfVouchers.discount)
		transaction.totalValue = transaction.totalValue * 0.95;

	var client = initClient();
	client.connect();

	const query = client.query('INSERT INTO transactions (totalValue, userID, date) VALUES ($1, $2, current_timestamp) RETURNING transactions.id', [transaction.totalValue, transaction.userID],
		function(err, result) {
		client.end();

			if (err) {
				//callback(res, null, err);
				console.log(err);
			} else {
				callbackTransactionRows(res, callback, result.rows[0].id, transaction, 0, typeOfVouchers);
			}
		});
}

exports.insertTransactionRows = function insertTransactionRows(res, callback, callbackTransactionRows, transactionID, transaction, index, typeOfVouchers){

		var client = initClient();

		client.connect();

	const query = client.query('INSERT INTO transactionrows (transactionID, productID, amount) VALUES ($1, $2 ,$3) RETURNING transactionrows.id', [transactionID, transaction.products[index]['product-id'], transaction.products[index]['product-amount']],
		function(err, result) {
		client.end();

			if (err) {
					console.log(err);
					//callback(res, null, err);
			} else {
					callbackTransactionRows(res, callback, transactionID, transaction, index + 1, typeOfVouchers);
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

exports.insertVoucher = function insertVoucher(res, callback, createDiscountVoucher, totalValue, transactionID, transaction, serialNumber, signature, voucherType, typeOfVouchers) {

	var client = initClient();

	client.connect();
	const query = client.query("INSERT INTO vouchers (serialnumber, type, signature, userid) VALUES ($1, $2, $3, $4) RETURNING *", [serialNumber, voucherType, signature, transaction.userID],
		function(err, result) {
			client.end();
			if (err) {
					console.log(err);
			} else {
					createDiscountVoucher(res, callback, transactionID, transaction, totalValue, result.rows[0], typeOfVouchers);
			}
	});
}

exports.insertVoucherDiscount = function insertVoucherDiscount(res, callback, pastResult, transactionID, transaction, serialNumber, signature, typeOfVouchers) {

	var client = initClient();

	client.connect();
	const query = client.query("INSERT INTO vouchers (serialnumber, type, signature, userid) VALUES ($1, $2, $3, $4) RETURNING *", [serialNumber, 3, signature, transaction.userID],
		function(err, result) {
			client.end();
			if (err) {
					console.log(err);
			} else {
				callback(res, {
					'simple-voucher': pastResult,
					'discount-voucher': result.rows[0],
					'vouchers-used': typeOfVouchers,
					'transaction-products': transaction.products,
					'transaction-totalValue': transaction.totalValue,
					'transaction-id': transactionID
				}, null);
			}
	});
}

exports.insertUserOnBlackList = function insertUserOnBlackList(res, callback, userID, message) {

	var client = initClient();

	client.connect();
	const query = client.query("INSERT INTO blacklist (userid, motive) VALUES ($1, $2) RETURNING *", [userID, message],
		function(err, result) {
			client.end();
			if (err) {
					console.log(err);
			} else {
				if(message == "Invalid Vouchers")
					callback(res, result.rows[0], "invalid vouchers");
				else if(message == "Invalid CreditCard")
					callback(res, result.rows[0], "invalid creditcard");
					else if(message == "Vouchers not exists")
					callback(res, result.rows[0], "vouchers not exists");
			}
	});
}

exports.getUserByID = function getUserByID(req, res, callbackGetUserByID) {
	var client = initClient();
	client.connect();

	const query = client.query("SELECT users.id FROM users WHERE users.id='" + req.body.userID +"'",
		function(err, result) {
			client.end();
			if (err) {
				callback(res, null, err);
			} else {
				callbackGetUserByID(req, res, result.rows);
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
						callbackGetUser(res, result.rows[0], pin, callback);
					else
						callback(res, null, "wrong pin");
				}
			}
	});
}

exports.getUserByUsernamePassword = function getUserByEmail(req, res, callback, callbackGetUser) {
	var client = initClient();

	var username = req.body.username;
	var password = req.body.password;

	client.connect();
	const query = client.query("SELECT * FROM users WHERE users.username='" + username +"'",
		function(err, result) {
			client.end();
			if (err) {
				callback(res, null, err);
			} else {
				if(result.rows.length == 0)
					callback(res, null, "no username found");
				else {
					console.log("vou validar o user");
					if(bcrypt.compareSync(password, result.rows[0].password)){
						console.log("Ta valido");
						callback(res,{
							'alreadyLogged':'ok'
						},null);
					}
					else
						callback(res, null, "wrong password");
				}
			}
	});
}

exports.getCreditCardByID = function getCreditCardByID(res, user, pin, callback) {
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
					'pin': pin,
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
					callback(res, null, err);
			} else {
					callback(res, {'products': result.rows}, null);
			}
	});
}

exports.getCreditCardByUserID = function getCreditCardByUserID(req, res, callbackGetCreditCard) {
	var client = initClient();
	client.connect();

	const query = client.query("SELECT creditcards.expmonth as month, creditcards.expyear as year FROM creditcards INNER JOIN users ON (creditcards.id = users.creditcard) WHERE users.id = '" + req.body.userID + "'",
	function(err, result) {
		client.end();
		if (err) {
				console.log(err);
		} else {
				var d = new Date();
				var monthCR = result.rows[0].month;
				var monthJ = d.getMonth() + 1;
				var yearCR = result.rows[0].year;
				var yearJ = ("" + d.getFullYear()).substr(2)

				var result;

				if(yearJ < yearCR)
					result = true;
				else if(yearJ == yearCR)
					if(monthJ <= monthCR)
						result = true;
					else
						result = false;
				else
					result = false;

				console.log("RESULT: " + result);

				callbackGetCreditCard(req, res, result);
		}
});
}

exports.getBlacklistedUsers = function getProducts(res, callback) {
	var client = initClient();

	client.connect();
	const query = client.query('SELECT * FROM blacklist',
		function(err, result) {
			client.end();
			if (err) {
					callback(res, null, err);
			} else {
					callback(res, {'blacklist-users': result.rows}, null);
			}
	});
}

exports.getAllTransactionsByUserID = function getAllTransactionsByUserID(req, res, callback, callbackGetTransactionRows) {
	var client = initClient();

	var userID = req.params['userID'];

	client.connect();
	const query = client.query("SELECT * FROM transactions WHERE transactions.userID = '"+ userID +"'",
		function(err, result) {
			client.end();
			if (err) {
					callback(res, null, err);
			} else {
					callbackGetTransactionRows(res, result.rows, 0, callback);
			}
	});
}

exports.getTransactionRowsByTransactionID = function getTransactionRowsByTransactionID(res, transactions, index, callback, callbackGetTransactionRows) {
	var client = initClient();

	client.connect();
	const query = client.query("SELECT transactionrows.productID, transactionrows.amount FROM transactionrows WHERE transactionrows.transactionID = '"+ transactions[index].id +"'",
		function(err, result) {
			client.end();
			if (err) {
					callback(res, null, err);
			} else {
					transactions[index]['products'] = result.rows;
					callbackGetTransactionRows(res, transactions, index + 1, callback);
			}
	});
}

exports.getVouchersByUserID = function getVouchersByUserID(req,res,callback){

	var client = initClient();
	var userID = req.params['userID'];

	client.connect();
	const query = client.query("SELECT * FROM vouchers WHERE vouchers.userID= '"+ userID +"'",
		function(err, result) {
			client.end();
			if (err) {
					callback(res, null, err);
			} else {
					callback(res, {'vouchers': result.rows}, null);
			}
	});

}

exports.getTotalValueOfTransactions = function getTotalValueOfTransactions(res, callback, createVouchersCoffeePopCorn, transactionID, transaction, typeOfVouchers) {
	var client = initClient();

	client.connect();
	const query = client.query("SELECT sum(transactions.totalvalue) as totalValue FROM transactions WHERE transactions.userid='"+ transaction.userID +"'",
		function(err, result) {
			client.end();
			if (err) {
				//callback(res, null, err);
				console.log(err);
			} else {
				createVouchersCoffeePopCorn(res, callback, transactionID, transaction, result.rows[0].totalvalue, typeOfVouchers);
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

exports.deleteVouchers = function deleteVouchers(req, res, insertTransaction, callback, callbackTransactionRows, vouchers, typeOfVouchers) {
	var client = initClient();

	var q = "DELETE FROM vouchers WHERE vouchers.id in (";

	for(var i = 0; i < vouchers.length; i++)
		if(i == vouchers.length - 1)
			q += vouchers[i].id + ")";
		else
			q += vouchers[i].id + ",";

	console.log(typeOfVouchers);

	client.connect();
	const query = client.query(q,
		function(err, result) {
			client.end();
			if (err) {
				console.log(err);
			} else {
				if(result.rowCount > 0) {
					console.log("tava tudo bem");
					insertTransaction(req, res, callback, callbackTransactionRows, typeOfVouchers);
				}
				else {
					console.log("entrei no else: vouchers not exists")
					callback(res, null, "Vouchers not exists");
				}
			}
	});
}


exports.startDB = function startDB() {
	//createTableCreditCards();
	//createTableProducts();
	//createTableUsers();
	//createTableTransactions();
	//createTableTransactionRows();
	//createTableVouchers();
	//createTableBlacklist();
}

exports.dropTables = function dropTables() {
	//dropTableBlacklist();
	//dropTableTransactionRows();
	//dropTableTransactions();
	//dropTableUsers();
	//dropTableCreditCards();
	//dropTableProducts();
	//dropTableVouchers();
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
