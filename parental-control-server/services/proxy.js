module.exports = () => {
	return {
        // Outbound proxies - outbound calls set by 'setProxy' 
        outboundProxies: [], 
        // Inbound proxies - inbound calls, set by reversing 'setProxy' affect 
        inboundProxies: [], 
        proxyNumber: process.env.VONAGE_PROXY_NUMBER,
		getProxyNumber() {
			return this.proxyNumber; 
        },
        setProxy(from, to) { 
            console.log("Adding proxy from: " + from + ", to: " + to); 
            this.outboundProxies[from] = to; 
            this.inboundProxies[to] = from; 
        }, 
        getOutboundProxy(from) { 
            return this.outboundProxies[from]; 
        }, 
        getInboundProxy(from) { 
            return this.inboundProxies[from]; 
        }
	}
};