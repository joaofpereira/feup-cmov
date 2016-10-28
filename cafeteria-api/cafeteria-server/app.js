var express = require('express');
var bodyParser = require('body-parser');
var dotenv = require('dotenv');

dotenv.load();

var db = require('./cafeteria.js');
var Product = require('./product.js')

var app = express();

var p = new Product('Batata Portugal', 0.6);
var p2 = new Product('Leite Agros', 1.0);

//db.startDB();
//db.insertProduct(p);
//db.insertProduct(p2);

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

/**
*   HTTP GET functions
*/
app.get('/', function (req, res) {
	res.send('Hello World!');
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

app.listen(process.env.PORT || 5000);
console.log('Server running in port ' + (process.env.PORT || 5000));
