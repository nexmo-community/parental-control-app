module.exports = () => {
	return {
        proxies: [], 
        proxyNumber: process.env.VONAGE_PROXY_NUMBER,
		getProxyNumber() {
			return this.proxyNumber; 
        },
        setProxy(from, to) { 
            this.proxies[from] = to; 
        }, 
        getProxy(from) { 
            return this.proxies[from]; 
        }
	}
};