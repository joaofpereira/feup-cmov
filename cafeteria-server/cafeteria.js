const pg = require('pg');

const client = new pg.Client({
    user: 'fqhoczevushmzq',
    password: 'dfza0Wcbr-TvWljhyzSUUBETbe',
    database: 'd5lpdcg8qd6e9',
    port: 5432,
    host: 'ec2-54-217-233-209.eu-west-1.compute.amazonaws.com',
    ssl: true
});


client.connect();
const query = client.query(
  'CREATE TABLE users(' +
  'id UUID PRIMARY KEY DEFAULT gen_random_uuid(),'+
  'name VARCHAR(120) not null, '+
  'username VARCHAR(120) not null,' +
  'email VARCHAR(120) not null unique, '+
  'creditCardInfo VARCHAR(16) not null, '+
  'hash_pin TEXT not null)');
query.on('end', () => { client.end(); });