var express = require('express');
var bodyParser = require('body-parser');
var dotenv = require('dotenv');

dotenv.load();

var db = require('./cafeteria.js');
var Product = require('./product.js')

var app = express();

var p = new Product('batata', 0.6);
var p2 = new Product('queque', 1.0	);

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

function callback(res, obj) {
	res.json({
		success: true,
		data: obj
	});
}


/**
*   HTTP GET functions
*/
app.get('/', function (req, res) {
	res.send('Hello World!');
});

app.get('/api/user/:username', function (req, res) {
	db.getProductByUsername(req, res, callback);
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

app.post('/api/register', function(req, res) {
	db.insertUser(req, res, callback);
});

app.listen(process.env.PORT || 5000);
console.log('Server running in port ' + (process.env.PORT || 5000));
