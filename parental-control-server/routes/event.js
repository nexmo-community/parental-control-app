var express = require('express');
var router = express.Router();

router.post('/', (req, res) => {    
  console.log('[Event - Request]'); 
  console.log(JSON.stringify(req.body));
  res.status(200).end();
});

module.exports = router;
