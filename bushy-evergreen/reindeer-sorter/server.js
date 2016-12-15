var express = require('express'),
    bodyParser = require('body-parser');

var app = express();

app.use(bodyParser.json());

app.post('/sorter', function(request, response){
  console.log("Request Body: " + request.body);      // Log Req

  function compare(a,b) {
	  if (a.reindeerName < b.reindeerName) return -1;
	  if (a.reindeerName > b.reindeerName) return 1;
	  return 0;
  }
  request.body.sort(compare);
  
  response.body = JSON.stringify(request.body);
  response.setHeader('Content-Type', 'application/json');
  response.send(response.body);                       // echo the Req
  console.log("Response Body: " + response.body);    // Log Response
});

app.listen(8080);


