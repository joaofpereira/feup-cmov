var express = require('express');
var bodyParser = require('body-parser');

var app = express();

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


app.get('/', function (req, res) {
  res.send('Hello World!');
});

app.listen(process.env.PORT || 5000);
console.log('Server running in port ' + (process.env.PORT || 5000));