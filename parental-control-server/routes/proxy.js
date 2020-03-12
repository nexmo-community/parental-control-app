const express = require('express');
const router = express.Router();

const Router = function(proxies) { 

  router.post('/', (req, res) => {
    console.log('[Proxy - Request]'); 
    console.log(JSON.stringify(req.body));
    const from = req.body.from; 
    const to = req.body.to; 
    
    const json = { 
      number: process.env.VONAGE_PROXY_NUMBER_LOCAL
    }; 

    proxies.setProxy(from, to); 

    res.status(200).json(json); 
  });

  return router; 
}

module.exports = Router;
