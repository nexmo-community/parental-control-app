const express = require('express');
const router = express.Router();

const getNCCOForAnswer = function (from, to, proxies) {

    // Outbound call 
    if (proxies.getOutboundProxy(from) !== undefined) { 
        console.error('[getNCCOForAnswer] - Found outbound Proxy');   
        const destination = proxies.getOutboundProxy(from); 
        return buildConnectNCCO(proxies.getProxyNumber(), destination); 
    }

    // Inbound call  
    else if (proxies.getInboundProxy(from) !== undefined) {
        console.error('[getNCCOForAnswer] - Found inbound Proxy');   
        const destination = proxies.getInboundProxy(from); 
        return buildConnectNCCO(proxies.getProxyNumber, destination); 
    } 

    else { 
        console.error('[getNCCOForAnswer] - Proxy not found!'); 
        return buildTTSNCCO("We are sorry, but the number you called is not connected"); 
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

const buildTTSNCCO = function (text) { 
    const ncco =
    [{
        action: 'talk',
        text
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
