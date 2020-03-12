const express = require('express');
const router = express.Router();

const getNCCOForAnswer = function (from, to, proxies) {

    // Outbound call 
    if (proxies.getProxy(from) !== undefined) { 
        const proxiedTo = proxies.getProxy(from); 
        return buildConnectNCCO(proxies.getProxyNumber(), proxiedTo); 
    }
    // Inbound call  
    else if (proxies.getProxy(to) !== undefined) {  
        const proxiedTo = proxies.getRevereseProxy(to); 
        return buildConnectNCCO(proxies.getProxyNumber, proxiedTo); 
    } 
    // Error 
    // TODO - throw error 
    else { 
        console.error('Proxy not found!'); 
        //TODO - add the feature part of this 
    }
} 

const buildConnectNCCO = function (from, to) { 
    const ncco =
    [{
        action: 'connect',
        from,
        timeout: "45",
        endpoint: [
            {
                type: 'phone',
                number: to
            }
        ]
    }];
    return ncco; 
}

const Router = function(proxies) { 

    router.get('/', (req, res) => {
        console.log('[Answer - Request]'); 
        console.log(JSON.stringify(req.query));

        const ncco = getNCCOForAnswer(req.query.from, req.query.to, proxies);

        console.log(JSON.stringify('NCCO returned: ' + JSON.stringify(ncco)));
        
        res.status(200).json(ncco);
    });

    return router; 
}

module.exports = Router;
