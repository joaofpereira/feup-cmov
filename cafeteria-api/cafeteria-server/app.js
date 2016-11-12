var express = require('express');
var bodyParser = require('body-parser');
var dotenv = require('dotenv');
var crypto = require('crypto');
var assert = require('assert');
var fs = require('fs');

dotenv.load();

var db = require('./cafeteria.js');
var Transaction = require('./transaction.js');
var Product = require('./product.js')

var app = express();

var p = new Product('Batata Portugal', 0.6);
var p2 = new Product('Leite Agros', 1.0);
var p3 = new Product('Lanche', 1.0);
var p4 = new Product('Café', 0.6);
var p5 = new Product('Mista', 1.5);
var p6 = new Product('Rebuçado', 0.1);
var p7 = new Product('Pipocas', 1.5);
var p8 = new Product('Rissol', 0.8);
var p9 = new Product('Baguete Mista', 1.0);
var p10 = new Product('Compal', 1.0);
var p11 = new Product('Kinder Bueno', 1.0);
var p12 = new Product('Choc. Dove', 1.0);
var p13 = new Product('Panike Misto', 1.1);

//db.dropTables();
//db.startDB();
/*db.insertProduct(p);
db.insertProduct(p2);
db.insertProduct(p3);
db.insertProduct(p4);
db.insertProduct(p5);
db.insertProduct(p6);
db.insertProduct(p7);
db.insertProduct(p8);
db.insertProduct(p9)
db.insertProduct(p10)
db.insertProduct(p11)
db.insertProduct(p12)
db.insertProduct(p13);*/

/** bodyParser.urlencoded(options)
 * Parses the text as URL encoded data (which is how browsers tend to send form data from regular forms set to POST)
 * and exposes the resulting object (containing the keys and values) on req.body
 */
app.use(bodyParser.urlencoded({
	extended: true
}));

/**bodyParser.json(options)
 * Parses the text as JSON and exposes the resulting object on req.body.
 */
app.use(bodyParser.json());

function callback(res, obj, err) {
	console.log("ENTREI");
	console.log(err);
	console.log(obj);

	if(err != null) {
		if(err.constraint == 'users_username_key') {
			res.json({
				code: 400,
				success: false,
				message: "Username already exists."
			});
		} else if(err.constraint == 'users_email_key') {
			res.json({
				code: 401,
				success: false,
				message: "Email already exists."
			});
		} else if (err=='no user found') {
			res.json({
				code: 402,
				success: false,
				message: "No user with that email found."
			});
		} else if (err=='wrong pin') {
			res.json({
				code: 403,
				success: false,
				message: "Wrong pin."
			});
		} else {
			res.json({
				code: 404,
				success: false,
				error: err
			});
		}
	} else if(obj == null){
		res.json({
			code: 404,
			success: false
		});
	} else {
		res.json({
			code: 200,
			success: true,
			data: obj
		});
	}
}

function callbackInsertUser(req, res, creditCard, err) {
	db.insertUser(req, res, creditCard, callback);
}

function callbackGetUser(res, user, pin, callback) {
	db.getCreditCardByID(res, user, pin, callback);
}

function callbackGetTransactionRows(res, transactions, index, callback) {
if(Object.keys(transactions).length >= index + 1) {
	db.getTransactionRowsByTransactionID(res, transactions, index, callback, callbackGetTransactionRows);
} else {
		callback(res, transactions, null);
	}
}

function callbackTransactionRows(res, callback, transactionID, transaction, index) {
	if(Object.keys(transaction.products).length >= index + 1) {
			db.insertTransactionRows(res, callback, callbackTransactionRows, transactionID, transaction, index);
		} else {
			db.getTotalValueOfTransactions(res, callback, createVouchersCoffeePopCorn, transaction);
		}
}

function callBackAllTransactions(client, req, res, callback, callBackAllTransactions, callbackAllTransactionRows, indexT) {
		if(JSON.parse(req.body.transactions).length >= indexT + 1) {
			db.insertAllTransactions(client, req, res, indexT, callback, callBackAllTransactions, callbackAllTransactionRows);
		} else {
			client.end();

			console.log("CHEGUEI AO CALLBACK");

			callback(res, {
				'mensagem': 'deu certo'
			}, null);
		}
}

function callbackAllTransactionRows(client, req, res, transactions, indexT, callback, callBackAllTransactions, callbackAllTransactionRows, transactionID, transaction, indexR) {
	if(Object.keys(transaction.products).length >= indexR + 1) {
			db.insertAllTransactionRows(client, req, res, transactions, indexT, callback, callBackAllTransactions, callbackAllTransactionRows, transactionID, transaction, indexR);
		} else {
			callBackAllTransactions(client, req, res, callback, callBackAllTransactions, callbackAllTransactionRows, indexT + 1);
		}
}

function createVouchersCoffeePopCorn(res, callback, transaction, totalValue) {
	var serialNumber = generateVoucherSerialNumber();

	if(transaction.totalValue >= 20) {
		var privateKey = fs.readFileSync('privkey.pem');

    var signer = crypto.createSign('sha1');
    signer.update(serialNumber);
    var sign = signer.sign(privateKey,'base64');

		var voucherType = getRandomVoucherType();

		db.insertVoucher(res, callback, createDiscountVoucher, totalValue, transaction, serialNumber, sign, voucherType);
	} else {
			createDiscountVoucher(res, callback, transaction, totalValue, null);
		}
}

function createDiscountVoucher(res, callback, transaction, totalValue, result) {
	var serialNumber = generateVoucherSerialNumber();

	diff = totalValue - transaction.totalValue;
	qT = Math.floor(totalValue / 100);
	qD = Math.floor(diff / 100);

	if(qT != qD) {
		var privateKey = fs.readFileSync('privkey.pem');

    var signer = crypto.createSign('sha1');
    signer.update(serialNumber);
    var sign = signer.sign(privateKey,'base64');

		var voucherType = getRandomVoucherType();

		db.insertVoucherDiscount(res, callback, result, transaction, serialNumber, sign);
	} else {
			if(result != null)
				callback(res, {
					'simple-voucher': result,
					'discount-voucher': 'null'
				}, null);
			else
				callback(res, {
					'simple-voucher': 'null',
					'discount-voucher': 'null'
				}, null);
	}
}

function generateVoucherSerialNumber () {
    min = 1, max = 1000, sumMin = 100, sumMax = 500;
    var first = Math.floor(Math.random() * (max - min + 1)) + min;
		var second = Math.floor(Math.random() * (sumMax - sumMin + 1)) + sumMin;

		var result = ("0" + (first + second)).substr(-4);

		return result;
}

function getRandomVoucherType () {
		return Math.floor(Math.random() * 2) + 1;
}

function testVouchers() {
		var serial_number = generateVoucherSerialNumber();

		console.log(serial_number);

		var privateKey = fs.readFileSync('privkey.pem');
		var publicKey = fs.readFileSync('pubkey.pem');

    var crypto = require('crypto');

    var signer = crypto.createSign('sha1');
    signer.update(serial_number);
    var sign = signer.sign(privateKey);

    var verifier = crypto.createVerify('sha1');
    verifier.update(serial_number);
    var ver = verifier.verify(publicKey, sign,'base64');
    console.log(ver);//<--- always false!

    console.log(sign);
}

function testVouchersType() {
	console.log(getRandomVoucherType());
}

function testAlg(totalValue, lastTrans) {
	diff = totalValue - lastTrans;
	qT = Math.floor(totalValue / 100);
	qD = Math.floor(diff / 100);

	console.log("qT " + qT);
	console.log("qD " + qD);

	console.log(qT != qD);
}

/**
*   HTTP GET functions
*/
app.get('/', function (req, res) {
	res.send('Hello World!');
});

app.get('/vouchers', function (req, res) {
	testVouchers();
});

app.get('/type', function (req, res) {
	testVouchersType();
});

app.get('/alg', function (req, res) {
	testAlg(100, 10);
});

app.get('/api/user/:email', function (req, res) {
	db.getUserByEmail(req, res, callback);
});

app.get('/api/product/:name', function (req, res) {
	db.getProductByName(req, res, callback);
});

app.get('/api/products', function (req, res) {
	db.getProducts(res, callback);
});


app.get('/api/transactions/:userID', function (req, res) {
	db.getAllTransactionsByUserID(req, res, callback, callbackGetTransactionRows);
});

app.get('/api/vouchers/:userID', function(req,res){
		db.getVouchersByUserID(req, res, callback);
});

/**
*   HTTP POST functions
*/

app.post('/api/login', function(req, res) {
	db.getUserByEmail(req, res, callback, callbackGetUser);
});

app.post('/api/register', function(req, res) {
	db.insertCreditCard(req, res, callbackInsertUser);
});

app.post('/api/creditcard', function(req, res) {
	db.insertCreditCard(req, res, callback);
});

app.post('/api/updateTransactions', function(req, res) {
	db.insertAllTransactions(null, req, res, 0, callback, callBackAllTransactions, callbackAllTransactionRows);
});

app.post('/api/transaction', function(req, res) {
	db.insertTransaction(req, res, callback, callbackTransactionRows);
});

app.listen(process.env.PORT || 5000);
console.log('Server running in port ' + (process.env.PORT || 5000));
